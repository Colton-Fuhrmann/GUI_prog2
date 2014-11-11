/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author 7106215
 */
public class toolbar_item 
{
    public JLabel label;
    public JTextField input;
    public String label_text;
    public String input_default;
    public double input_value;
    public int min_value;
    public int max_value;
    
    public toolbar_item(String passed_label_text, String passed_default_string, 
                        double passed_default_value, int min, int max)
    {
        label = new JLabel(passed_label_text);
        input = new JTextField(passed_default_string, 7);
        
        label_text = passed_label_text;
        input_default = passed_default_string;
        input_value = passed_default_value;
        min_value = min;
        max_value = max;
        
        input.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent event )
            {
                System.out.println( "Input was " + input.getText());
                try
                {
                    input_value = Double.parseDouble(input.getText());
                }
                catch(NumberFormatException exception)
                {
                    input.setText(input_default);
                }
                
                if(input_value > max_value || input_value < min_value)
                {
                    input.setText(input_default);
                }               
            }
        } );
    }
    
    
}