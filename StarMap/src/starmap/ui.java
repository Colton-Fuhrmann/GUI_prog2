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
    JPanel drawArea;
    
    public ui(computation compute)       
    {
        super("StarMap"); //Call JFrame constructor
        x = 10;
        
        contents = getContentPane();       

        toolbar toolbar = new toolbar(compute);
        contents.add(toolbar, BorderLayout.NORTH);
        setLocationRelativeTo( null );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        
        Dimension content_size = new Dimension(600,600);
        drawArea = new JPanel();
        drawArea.setPreferredSize(content_size);
        drawArea.setBackground(Color.black);
        contents.add(drawArea, BorderLayout.CENTER);
   
        setVisible(true);      
        pack();
    }
    
}
