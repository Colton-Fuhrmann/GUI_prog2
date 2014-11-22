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
import java.util.Calendar;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Date;

// toolbar demo class
public class toolbar extends JPanel
{
   public toolbar_item lat, lon, azi, alt, vmag, year, month, day, hour, min, sec;
   computation compute;
   ui ui;
   
    // constructor
    public toolbar(computation passed_compute, ui passed_ui)
    {    
        ui = passed_ui;
       // add buttons to toolbar
        JToolBar toolbar = new JToolBar();
        JPanel top_row = new JPanel(new GridLayout(1, 6));
        JPanel middle_row = new JPanel(new GridLayout(1, 6));
        JPanel bottom_row = new JPanel(new GridLayout(1, 6));
        JPanel second_row = new JPanel(new GridLayout(1,6));
        JPanel fourth_row = new JPanel(new GridLayout(1,6));
        toolbar.setLayout( new GridLayout(5, 1) );
       
        compute = passed_compute;
        


        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.YEAR, -8);
        Date startDate = cal.getTime();
        cal.add(Calendar.YEAR, 3000);
        Date endDate = cal.getTime();
        SpinnerModel model = new SpinnerDateModel(now, startDate, endDate, Calendar.YEAR);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner,"dd/MM/yyyy hh/mm/ss"));
        
        
        lat = new toolbar_item(" Latitude: ", "44.08", 44.08, -90, 90);
        lon = new toolbar_item(" Longitude: ", "-103.23", -103.23, -180, 180);
        azi = new toolbar_item(" Azimuth: ", "45", 45, 0, 360);
        alt = new toolbar_item(" Altitude: ", "45", 45, 0, 90);
        vmag = new toolbar_item(" Minimum Vmag: ", "3", 3, -2, 50);
        year = new toolbar_item(" Year: ", "2014", 2014, 2006, 5000);
        month = new toolbar_item(" Month: ", "11", 11, 1, 12);
        day = new toolbar_item(" Day: ", "10", 10, 1, 31);
        hour = new toolbar_item(" Hours: ", "18", 18, 0, 23);
        min = new toolbar_item(" Minutes: ", "20", 20, 0, 60);
        sec = new toolbar_item(" Seconds: ", "00", 0, 0, 60);

        
        Color time_color = new Color(.9f, 1f, 1f);
        Color lat_lon_color = new Color(1f, .9f, 1f);
        Color azi_alt_color = new Color(1f, 1f, .9f);
        Color vmag_color = new Color(.9f, 1f, .9f);
        
        year.input.setBackground(time_color);
        month.input.setBackground(time_color);
        day.input.setBackground(time_color);
        hour.input.setBackground(time_color);
        min.input.setBackground(time_color);
        sec.input.setBackground(time_color);
        
        lat.input.setBackground(lat_lon_color);
        lon.input.setBackground(lat_lon_color);
        
        azi.input.setBackground(azi_alt_color);
        alt.input.setBackground(azi_alt_color);
        
        vmag.input.setBackground(vmag_color);
        
        // create a button for users to click to apply their changes
        JButton applyInput = new JButton( "Apply");
        applyInput.addActionListener( new applyButtonHandler());
        applyInput.setToolTipText("Update StarMap view");
        JButton toggle_constellations = new JButton("Toggle");
        toggle_constellations.addActionListener(new toggle_button_handler());
        toggle_constellations.setToolTipText("Toggle constellations on/off");
        
        top_row.add(azi.label);
        top_row.add(azi.input);
        top_row.add(alt.label);
        top_row.add(alt.input);
        top_row.add(lat.label);
        top_row.add(lat.input);
        top_row.add(lon.label);
        top_row.add(lon.input);
        
        second_row.add(azi.slider);
        second_row.add(alt.slider);
        second_row.add(lat.slider);
        second_row.add(lon.slider);

        middle_row.add(year.label);
        middle_row.add(year.input);
        middle_row.add(month.label);
        middle_row.add(month.input);
        middle_row.add(day.label);
        middle_row.add(day.input);
        middle_row.add(hour.label);
        middle_row.add(hour.input);
        middle_row.add(min.label);
        middle_row.add(min.input);
        middle_row.add(sec.label);
        middle_row.add(sec.input);

        fourth_row.add(year.slider);
        fourth_row.add(month.slider);
        fourth_row.add(day.slider);
        fourth_row.add(hour.slider);
        fourth_row.add(min.slider);
        fourth_row.add(sec.slider);
        
        bottom_row.add(vmag.label);
        bottom_row.add(vmag.input);
        bottom_row.add(spinner);
        bottom_row.add(toggle_constellations);
        bottom_row.add(applyInput);
       
        toolbar.add(top_row);
        toolbar.add(second_row);
        toolbar.add(middle_row);
        toolbar.add(fourth_row);
        toolbar.add(bottom_row);

        this.add(toolbar);
        
        lat.slider.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged( ChangeEvent e) {
            lat.input.setText(String.valueOf(lat.slider.getValue()));
            lat.input_value = lat.slider.getValue();

            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, year.input_value,
                  month.input_value, day.input_value, 
                  hour.input_value, min.input_value, sec.input_value, 
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            }
        });
        lon.slider.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged( ChangeEvent e) {
            lon.input.setText(String.valueOf(lon.slider.getValue()));
            lon.input_value = lon.slider.getValue();

            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, year.input_value,
                  month.input_value, day.input_value, 
                  hour.input_value, min.input_value, sec.input_value, 
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            }
        });
        azi.slider.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged( ChangeEvent e) {
            azi.input.setText(String.valueOf(azi.slider.getValue()));
            azi.input_value = azi.slider.getValue();

            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, year.input_value,
                  month.input_value, day.input_value, 
                  hour.input_value, min.input_value, sec.input_value, 
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            }
        });
        alt.slider.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged( ChangeEvent e) {
            alt.input.setText(String.valueOf(alt.slider.getValue()));
            alt.input_value = alt.slider.getValue();

            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, year.input_value,
                  month.input_value, day.input_value, 
                  hour.input_value, min.input_value, sec.input_value, 
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            }
        });
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
            compute.minimum_vmag = vmag.input_value;
                  
            {
                compute.user_changes_position(lat.input_value, lon.input_value,
                      azi.input_value, alt.input_value, year.input_value,
                      month.input_value, day.input_value, 
                      hour.input_value, min.input_value, sec.input_value, 
                      ui.drawArea.scale_factor);
                
                lat.last_input_value = lat.input_value;
                lon.last_input_value = lon.input_value;
                azi.last_input_value = azi.input_value;
                alt.last_input_value = alt.input_value;
                year.last_input_value = year.input_value;
                month.last_input_value = month.input_value;
                day.last_input_value = day.input_value;
                hour.last_input_value = hour.input_value;
                min.last_input_value = min.input_value;
                sec.last_input_value = sec.input_value;
            }
            
            ui.drawArea.repaint();
        }
    }
    
    //Event handler for apply button
    private class toggle_button_handler implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent event )
        {
            compute.constellations_on = !compute.constellations_on;
            ui.drawArea.repaint();
        }
    }
    
    
}

