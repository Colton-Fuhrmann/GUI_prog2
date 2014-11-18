/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.ToolTipManager;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.*;

/**
 *
 * @author 7106215
 */
public class star_panel extends JPanel implements Runnable
{
    computation compute;
    boolean displaying_tooltip;
    int count;
    ui ui;
    int usr_click_x;
    int usr_click_y;
    double scale_factor = 1;
    
    public void run()
    {
        System.out.println("Started thread");
    }
    
    public star_panel(computation passed_compute, ui passed_ui)
    {
        super();

        //Save computation object so paintComponent() can call its draw_stars() method
        compute = passed_compute; 
        ui = passed_ui;
        
        ToolTipManager.sharedInstance().setDismissDelay(1000000);
        
        //Add listener for when the mouse moves to see if its current position
        //is containted by any of the onscreen stars
        addMouseMotionListener(new MouseAdapter() 
        {     
            @Override  
            public void mouseMoved(MouseEvent e) 
            {
                
                star_contents current_star;
                boolean inside_star = false;
                for(int i = 0; i < compute.onscreen_stars.size() && inside_star == false; i++)
                {
                    current_star = compute.onscreen_stars.get(i);
                    if(current_star.tooltip_area.contains(e.getPoint()))
                    {   
                        setToolTipText("<html>HRnumber: " + current_star.HRnumber + "<br>" +
                                       "Name: " + current_star.name + "<br>" +
                                       "Constellation: " + current_star.constellation + "<br>" +
                                       "RA: " + current_star.ra + "<br>" +
                                       "Dec: " + current_star.dec + "<br>" +
                                       "Vmag: " + current_star.vmag + "<br>" +
                                       "Class: " + current_star.star_class + "<br>" +
                                       "Common name: " + current_star.common_name + "<br></html>");
                        inside_star = true;
                    }
                }
                
                //If not inside a star, don't show a tooltip
                if(inside_star == false)
                {
                    setToolTipText(null);
                }
                
                //ui.dispatchEvent(e);
                
                //Check for tooltip again
                ToolTipManager.sharedInstance().mouseMoved(e);
            }  
                        
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                  // get current x & y values of where user is dragging cursor
                  int x = e.getX();
                  int y = e.getY();
                
                  // clip x if it goes beyond the drawArea
                  if( x < 0)
                      x = 0;
                  else if ( x > ui.drawArea.getWidth() )
                      x = ui.drawArea.getWidth();
                
                  // clip y if it goes beyond the drawArea
                  if( y < 0)
                      y = 0;
                  else if ( y > ui.drawArea.getHeight() )
                      y = ui.drawArea.getHeight();
                 
                  // compute difference of where user clicked and where they
                  // are currently dragging their cursor at
                  int x_diff = usr_click_x - x;
                  int y_diff = usr_click_y - y;
               
                  // store the azi and alt values of the drag iteration
                  double old_azi = ui.toolbar.azi.input_value;
                  double old_alt = ui.toolbar.alt.input_value;
               
                  // allow user to go left or right with azi depending on if they
                  // drag their mouse left or right from where they first clicked
                  if( x_diff < 0 )
                  {
                    ui.toolbar.azi.input_value = old_azi - 1;
                  }
                  else
                    ui.toolbar.azi.input_value = old_azi + 1;

                  // allow user to move up or down depending on where they've
                  // dragged their cursor from initial click location
                  if( y_diff < 0 )
                    ui.toolbar.alt.input_value = old_alt - 1;
                  else
                    ui.toolbar.alt.input_value = old_alt + 1;
               
               
                  // don't allow user to go past altitude of 0 or 90
                  if( ui.toolbar.alt.input_value > 90 )
                      ui.toolbar.alt.input_value = 90;
                  else if( ui.toolbar.alt.input_value < 0 )
                      ui.toolbar.alt.input_value = 0;

                  // allow azimuth gaze to rollover
                  if( ui.toolbar.azi.input_value > 360 )
                      ui.toolbar.azi.input_value -= 360;
                  else if( ui.toolbar.azi.input_value < 0 )
                      ui.toolbar.azi.input_value += 360;
                
                //Update text in fields
                ui.toolbar.alt.input.setText(String.format("%.2f", ui.toolbar.alt.input_value));
                ui.toolbar.azi.input.setText(String.format("%.2f", ui.toolbar.azi.input_value));
                
                

                compute.user_changes_position(ui.toolbar.lat.input_value, ui.toolbar.lon.input_value,
                ui.toolbar.azi.input_value , ui.toolbar.alt.input_value, ui.toolbar.year.input_value,
                ui.toolbar.month.input_value, ui.toolbar.day.input_value,
                ui.toolbar.hour.input_value, ui.toolbar.min.input_value, ui.toolbar.sec.input_value,
                scale_factor);
                ui.drawArea.repaint();
               

                }
            }
                        
                        
        });
        
        this.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                  usr_click_x = e.getX();
                  usr_click_y = e.getY();
                }
            }
        });
        
        this.addMouseWheelListener(new MouseAdapter(){
            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                int value = e.getWheelRotation();
                
                //Zoomed in
                if(value < 0)
                {
                    value = -value; 
                    scale_factor += (value * .1);
                }
                else //Zoomed out
                {
                    scale_factor -= (value * .1);
                }
                
                //Keep within the bounds of 1 and 3
                if(scale_factor < .5)
                {
                    scale_factor = .5;
                }
                else if(scale_factor > 3)
                {
                    scale_factor = 3;
                }
                
                compute.user_changes_position(ui.toolbar.lat.input_value, ui.toolbar.lon.input_value,
                ui.toolbar.azi.input_value , ui.toolbar.alt.input_value, ui.toolbar.year.input_value,
                ui.toolbar.month.input_value, ui.toolbar.day.input_value,
                ui.toolbar.hour.input_value, ui.toolbar.min.input_value, ui.toolbar.sec.input_value,
                scale_factor);
                ui.drawArea.repaint();
            }
        });
        
        
    }
    
    @Override
    public void paintComponent(Graphics graphic) 
    {  
        Graphics2D g = (Graphics2D) graphic;
        int width = this.getWidth();
        int height = this.getHeight();
        
        //g.translate(width / 2, height / 2);
        //g.scale(4, 4);
        //g.translate(-width / 2, -height / 2);
        
        Font font = new Font("Serif", Font.PLAIN, 12);
        g.setFont(font);
        
        //Draw the background as black
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        
        //Draw all the stars in the panel
        g.setColor(Color.white);
        compute.draw_stars(g, width, height);
    }
}
