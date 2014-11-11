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
   
    // constructor
    public toolbar()
    {    
        // add buttons to toolbar
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout( new FlowLayout(FlowLayout.LEFT) );
        
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

        toolbar.add(lat.label);
        toolbar.add(lat.input);
        toolbar.add(lon.label);
        toolbar.add(lon.input);
        toolbar.add(azi.label);
        toolbar.add(azi.input);
        toolbar.add(alt.label);
        toolbar.add(alt.input);
        toolbar.add(year.label);
        toolbar.add(year.input);
        toolbar.add(month.label);
        toolbar.add(month.input);
        toolbar.add(day.label);
        toolbar.add(day.input);
        toolbar.add(hour.label);
        toolbar.add(hour.input);
        toolbar.add(min.label);
        toolbar.add(min.input);
        toolbar.add(sec.label);
        toolbar.add(sec.input);
        
        
        this.add(toolbar);

        // misc
        //setSize( 360, 250 );
        //setLocationRelativeTo( null );
        //setDefaultCloseOperation( EXIT_ON_CLOSE );
    }

}

