/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author 7106215
 */
public class toolbar_item
{
    public JLabel label;
    public JTextField input;
    public JSlider slider;
    public String label_text;
    public String input_default;
    public double input_value;
    public int min_value;
    public int max_value;
    public double last_input_value;
    
    public toolbar_item(String passed_label_text, String passed_default_string, 
                        double passed_default_value, int min, int max)
    {
        label = new JLabel(passed_label_text);
        input = new JTextField(passed_default_string, 7);
        slider = new JSlider(min, max, (int)passed_default_value);
        
        label.setToolTipText("<html>" + passed_label_text + "<br>" + min + 
                              " to " + max + "</html>");
        
        label_text = passed_label_text;
        input_default = passed_default_string;
        input_value = passed_default_value;
        last_input_value = passed_default_value;
        min_value = min;
        max_value = max;
        
        input.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent event )
            {
                try
                {
                    input_value = Double.parseDouble(input.getText()) * 100;
                    slider.setValue((int)input_value);
                }
                catch(NumberFormatException exception)
                {
                    input.setText(Double.toString(last_input_value));
                    slider.setValue((int)(last_input_value));
                }
                
                if(input_value > max_value || input_value < min_value)
                {
                    input.setText(Double.toString(last_input_value));
                    slider.setValue((int)(last_input_value));

                }               
            }
        } );
        
        input.addFocusListener( new FocusListener()
        {
            @Override
            public void focusLost( FocusEvent event )
            {
                try
                {
                    input_value = Double.parseDouble(input.getText()) * 100;
                    slider.setValue((int)input_value);
                }
                catch(NumberFormatException exception)
                {
                    input.setText(Double.toString((double)last_input_value));
                    input_value = last_input_value;
                    slider.setValue((int)input_value);
                }

                if(input_value > max_value || input_value < min_value)
                {
                    input.setText(Double.toString((double)last_input_value));
                    input_value = last_input_value;          
                    slider.setValue((int)input_value);
                }               
            }
            
            @Override
            public void focusGained( FocusEvent event )
            {

            }
        } );

        
        slider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged( ChangeEvent e) {
                input.setText(String.valueOf((double)slider.getValue()/100));
                input_value = (double)slider.getValue()/100;
            }
        });
        
    }//end toolbar_item constructor
    
    
}
