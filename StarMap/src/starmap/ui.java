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
    
    final toolbar toolbar;

    public ui(final computation compute)      
    {
        super("StarMap"); //Call JFrame constructor
        x = 10;
       
        contents = getContentPane();
       
        Dimension content_size = new Dimension(800,800);
        drawArea = new star_panel(compute, this);
        drawArea.setPreferredSize(content_size);
        drawArea.setBackground(Color.black); 

        toolbar = new toolbar(compute, this);
        contents.add(toolbar, BorderLayout.NORTH);
        contents.add(drawArea, BorderLayout.CENTER);
       
        setLocationRelativeTo( null );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
  
        //drawArea.repaint();
       
        setVisible(true);
        setSize(toolbar.getWidth() , toolbar.getWidth() + toolbar.getHeight());
        pack();
        
        System.out.println(toolbar.getHeight());
        
            
        this.addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e) 
            {   
                Dimension size = contents.getSize();
                
                if(size.width < size.height - toolbar.getHeight())
                {
                    //size.width = size.height - toolbar.getHeight();
                }
                else
                {
                    //size.height = size.width + toolbar.getHeight();
                }
                
            }
        });
    }
}