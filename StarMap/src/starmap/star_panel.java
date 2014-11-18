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

/**
 *
 * @author 7106215
 */
public class star_panel extends JPanel
{
    computation compute;
    boolean displaying_tooltip;
    int count;
    
    public star_panel(computation passed_compute)
    {
        super();

        //Save computation object so paintComponent() can call its draw_stars() method
        compute = passed_compute;
    }
    
    @Override
    public void paintComponent(Graphics graphic) 
    {  
        Graphics2D g = (Graphics2D) graphic;
        int width = this.getWidth();
        int height = this.getHeight();
        
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
