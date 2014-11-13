// ToolBarDemo.java
// Author: John M. Weiss, Ph.D.
// Class: CSC421/521 GUI-OOP Fall 2014

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

/**
 *
 * @author 7106215
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// toolbar demo class
public class toolbar extends JPanel
{
   public toolbar_item lat, lon, azi, alt, year, month, day, hour, min, sec;
   computation compute;
   
    // constructor
    public toolbar(computation passed_compute)
    {    
       // add buttons to toolbar
        JToolBar toolbar = new JToolBar();
        JPanel top_row = new JPanel(new GridLayout(1, 6));
        JPanel bottom_row = new JPanel(new GridLayout(1, 4));
        toolbar.setLayout( new GridLayout(2, 1) );
       
        lat = new toolbar_item(" Latitude: ", "44.08", 44.08, -90, 90);
        lon = new toolbar_item(" Longitude: ", "-103.23", -103.23, -180, 180);
        azi = new toolbar_item(" Azimuth: ", "45", 45, 0, 360);
        alt = new toolbar_item(" Altitude: ", "45", 45, 0, 90);
        year = new toolbar_item(" Year: ", "2014", 2014, 2006, 5000);
        month = new toolbar_item(" Month: ", "11", 11, 1, 12);
        day = new toolbar_item(" Day: ", "10", 10, 1, 31);
        hour = new toolbar_item(" Hours: ", "18", 18, 0, 23);
        min = new toolbar_item(" Minutes: ", "20", 20, 0, 60);
        sec = new toolbar_item(" Seconds: ", "00", 0, 0, 60);
        compute = passed_compute;

        
        // create a button for users to click to apply their changes
        JButton applyInput = new JButton( "Apply");
        applyInput.addActionListener( new applyButtonHandler());
 
        top_row.add(lat.label);
        top_row.add(lat.input);
        top_row.add(lon.label);
        top_row.add(lon.input);
        top_row.add(azi.label);
        top_row.add(azi.input);
        top_row.add(alt.label);
        top_row.add(alt.input);
        top_row.add(applyInput);
       
        bottom_row.add(year.label);
        bottom_row.add(year.input);
        bottom_row.add(month.label);
        bottom_row.add(month.input);
        bottom_row.add(day.label);
        bottom_row.add(day.input);
        bottom_row.add(hour.label);
        bottom_row.add(hour.input);
        bottom_row.add(min.label);
        bottom_row.add(min.input);
        bottom_row.add(sec.label);
        bottom_row.add(sec.input);
       
        toolbar.add(top_row);
        toolbar.add(bottom_row);

        this.add(toolbar);

        // misc
        //setSize( 360, 250 );
        //setLocationRelativeTo( null );
        //setDefaultCloseOperation( EXIT_ON_CLOSE );
    }
    
    // event handler for apply button
    private class applyButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent event )
        {

        compute.user_changes_position(lat.input_value, lon.input_value,
                      azi.input_value, alt.input_value, year.input_value,
                      month.input_value, day.input_value, 
                      min.input_value, hour.input_value, sec.input_value);

        }
    }
    
//        public void test(int x)
//    {
//        System.out.println("test");
//        System.out.println(x);  
//    }

}

