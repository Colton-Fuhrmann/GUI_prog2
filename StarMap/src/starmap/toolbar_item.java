////////////////////////////////////////////////////////////////////////////////
//Authors: Colton Fuhrmann, Kevin Hilt
//Date: November 24, 2014
//Course: CSC421
//Instructor: Dr. Weiss
//
//Description: The toolbar_item class defines a basic toolbar input widget,
//giving it a container for its default values, range bounds, input elements,
//and event handlers.
////////////////////////////////////////////////////////////////////////////////
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
    
    public toolbar_item(String passed_label_text, String passed_default_string, 
                        final double passed_default_value, int min, int max)
    {
        label = new JLabel(passed_label_text);
        input = new JTextField(passed_default_string, 7);
        slider = new JSlider(min, max, (int)passed_default_value);
        
        // set tooltip to show user correct input for each toolbar_item
        label.setToolTipText("<html>" + passed_label_text + 
                              "<br>" + min/100 + 
                              " to " + max/100 + "</html>");
        
        label_text = passed_label_text;
        input_default = passed_default_string;
        input_value = passed_default_value;
        min_value = min;
        max_value = max;
        
        // check for valid input. If not valid, set to default value
        input.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent event )
            {
                try
                {
                    input_value = Double.parseDouble(input.getText());
                    // have to multiple slider value by 100 to convert from an
                    // int to a double with two decimal point precision
                    slider.setValue((int)input_value * 100);
                }
                catch(NumberFormatException exception)
                {
                    input.setText(Double.toString(passed_default_value/100));
                    slider.setValue((int)(passed_default_value));
                }
                
                if(input_value > max_value || input_value < min_value)
                {
                    input.setText(Double.toString(passed_default_value/100));
                    slider.setValue((int)(passed_default_value));

                }               
            }
        } );
        
        // check for valid input, if not valid, set to default
        input.addFocusListener( new FocusListener()
        {
            @Override
            public void focusLost( FocusEvent event )
            {
                try
                {
                    input_value = Double.parseDouble(input.getText());
                    // have to multiple slider value by 100 to convert from an
                    // int to a double with two decimal point precision
                    slider.setValue((int)input_value*100);
                }
                catch(NumberFormatException exception)
                {
                    input.setText(Double.toString((double)passed_default_value/100));
                    input_value = passed_default_value;
                    slider.setValue((int)input_value);
                }

                if(input_value > max_value || input_value < min_value)
                {
                    input.setText(Double.toString((double)passed_default_value/100));
                    input_value = passed_default_value;          
                    slider.setValue((int)input_value);
                }               
            }
            
            @Override
            public void focusGained( FocusEvent event )
            {

            }
        } );

        
        // link up slider with textbox so both values change accordingly
        slider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged( ChangeEvent e) {
                input.setText(String.valueOf((double)slider.getValue()/100));
                input_value = (double)slider.getValue()/100;
            }
        });
        
    }//end toolbar_item constructor   
    
}
