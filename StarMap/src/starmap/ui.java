////////////////////////////////////////////////////////////////////////////////
//Authors: Colton Fuhrmann, Kevin Hilt
//Date: November 24, 2014
//Course: CSC421
//Instructor: Dr. Weiss
//
//Description: The ui class provides the main frame for the application. It
//contains a toolbar at the north position for user input and a central
//drawing area where the stars will be drawn.
////////////////////////////////////////////////////////////////////////////////
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
    
    final toolbar toolbar;

    public ui(final computation compute)      
    {
        super("StarMap"); //Call JFrame constructor
        x = 10;
       
        contents = getContentPane();
       
        toolbar = new toolbar(compute, this);
        contents.add(toolbar, BorderLayout.NORTH);
        
        Dimension content_size = new Dimension(800,800);
        drawArea = new star_panel(compute, this);
        drawArea.setPreferredSize(content_size);
        drawArea.setBackground(Color.black); 
        contents.add(drawArea, BorderLayout.CENTER);
       
        setLocationRelativeTo( null );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
  
        //drawArea.repaint();
       
        setVisible(true);
        setSize(toolbar.getWidth() , toolbar.getWidth() + toolbar.getHeight());
        pack();
    }
}