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
    
    public ui()
    {
        super("StarMap"); //Call JFrame constructor
        
        contents = getContentPane();       
        toolbar toolbar = new toolbar();
    
        contents.add(toolbar, BorderLayout.LINE_START);
        setSize( 600, 400 );
        setLocationRelativeTo( null );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        
        setVisible(true);      
        //pack();
    }
    
}
