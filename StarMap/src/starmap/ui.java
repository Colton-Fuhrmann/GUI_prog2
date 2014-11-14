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
    
    public ui(computation compute)       
    {
        super("StarMap"); //Call JFrame constructor
        x = 10;
        
        contents = getContentPane();
        
        Dimension content_size = new Dimension(600,600);
        drawArea = new star_panel(compute);
        drawArea.setPreferredSize(content_size);
        drawArea.setBackground(Color.black);

        toolbar toolbar = new toolbar(compute, drawArea);
        contents.add(toolbar, BorderLayout.NORTH);
        contents.add(drawArea, BorderLayout.CENTER);
        
        setLocationRelativeTo( null );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
   
        //drawArea.repaint();
        
        setVisible(true);      
        pack();
    }
    
}
