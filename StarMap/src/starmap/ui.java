/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *
 * @author 7106215
 */
public class ui extends JFrame
{
    private Container contents;
    public int x;
    star_panel drawArea;

    public ui(final computation compute)      
    {
        super("StarMap"); //Call JFrame constructor
        x = 10;
       
        contents = getContentPane();
       
        Dimension content_size = new Dimension(600,600);
        drawArea = new star_panel(compute);
        drawArea.setPreferredSize(content_size);
        drawArea.setBackground(Color.black);

        final toolbar toolbar;
        toolbar = new toolbar(compute, drawArea);
        contents.add(toolbar, BorderLayout.NORTH);
        contents.add(drawArea, BorderLayout.CENTER);
       
        setLocationRelativeTo( null );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
  
        //drawArea.repaint();
       
        setVisible(true);     
        pack();
       
  
       
        this.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                System.out.println(e.getX());
                System.out.println(e.getY());
                }
            }
        });
        this.addMouseMotionListener(new MouseAdapter(){
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                  int x = e.getX();
                  int y = e.getY();
                 
                  if( x < 0)
                      x = 0;
                  else if ( x > drawArea.getWidth() )
                      x = drawArea.getWidth();
                 
                  if( y < 100)
                      y = 100;
                  else if ( y > drawArea.getHeight() )
                      y = drawArea.getHeight();
                 
//                System.out.println(e.getX());
//                System.out.println(e.getY());
                System.out.println(x);
                System.out.println(y);
               
                toolbar.alt.input_value = y/2 * 0.1f;
                toolbar.azi.input_value = x/2 * 0.1f;
               
                compute.user_changes_position(toolbar.lat.input_value, toolbar.lon.input_value,
                x/2 * 0.1f, y/2 * 0.1f, toolbar.year.input_value,
                toolbar.month.input_value, toolbar.day.input_value,
                toolbar.min.input_value, toolbar.hour.input_value, toolbar.sec.input_value);
                drawArea.repaint();
                }
               

            }
        });

    }
}