////////////////////////////////////////////////////////////////////////////////
//Authors: Colton Fuhrmann, Kevin Hilt
//Date: November 24, 2014
//Course: CSC421
//Instructor: Dr. Weiss
//
//Description: The toolbar class handles populating an area with the input
//widgets that will later be inserted into the top of the ui. A part of this
//population includes defining the slider event handlers.
//
//The basic toolbar initialization is from Dr. Weiss' code:
//http://www.mcs.sdsmt.edu/csc421/Code/Java/Swing/ToolBarDemo.java
////////////////////////////////////////////////////////////////////////////////
package starmap;

/**
 *
 * @author 7106215
 */

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Date;

// toolbar demo class
public class toolbar extends JPanel
{
   public toolbar_item lat, lon, azi, alt, vmag;
   computation compute;
   ui ui;
   
   public SpinnerModel date_spinner;
   public JSpinner j_spinner;
   Date date_time_now, startDate, endDate;

    // constructor
    public toolbar(computation passed_compute, ui passed_ui)
    {    
        ui = passed_ui;
       // create toolbar and JPanel rows that will be added to toolbar
        JToolBar toolbar = new JToolBar();
        JPanel top_row = new JPanel(new GridLayout(1, 6));
        JPanel middle_row = new JPanel(new GridLayout(1, 6));
        JPanel bottom_row = new JPanel(new GridLayout(1, 6));
        JPanel second_row = new JPanel(new GridLayout(1,6));
        toolbar.setLayout( new GridLayout(4, 1) );
       
        //set compute object so compute_user_changes() can be called when a user
        //changes inputs via toolbar
        compute = passed_compute;
        
        // set the calendar to go from year 2006 to 5000
        Calendar cal = Calendar.getInstance();
        date_time_now = cal.getTime();
        cal.add(Calendar.YEAR, -8);
        startDate = cal.getTime();
        cal.add(Calendar.YEAR, 3000);
        endDate = cal.getTime();
        date_spinner = new SpinnerDateModel(date_time_now, startDate, endDate, 
                       Calendar.YEAR);
        j_spinner = new JSpinner(date_spinner);
        // set the format of how j_spinner will look & set a user tooltip
        j_spinner.setEditor(new JSpinner.DateEditor(j_spinner,"yyyy/MM/dd HH:mm:ss"));
        j_spinner.setToolTipText("<html>Format: yyyy/MM/dd hh:mm:ss <br>" + 
                "select time value and use spinner or enter<br> a valid date/time"
                + " with the format above</html>");
        
        // create all the toolbar_items the user will interact with/change
        lat = new toolbar_item(" Latitude: ", "44.08", 4408, -9000, 9000);
        lon = new toolbar_item(" Longitude: ", "-103.23", -10323, -18000, 18000);
        azi = new toolbar_item(" Azimuth: ", "45", 4500, 0, 36000);
        alt = new toolbar_item(" Altitude: ", "45", 4500, 0, 9000);
        vmag = new toolbar_item(" Minimum Vmag: ", "3", 300, -200, 5000);

        // create different colors for JTextFields so user can differentiate
        // between related inputs, & set backgrounds
        Color lat_lon_color = new Color(1f, .9f, 1f);
        Color azi_alt_color = new Color(1f, 1f, .9f);
        Color vmag_color = new Color(.9f, 1f, .9f);
        lat.input.setBackground(lat_lon_color);
        lon.input.setBackground(lat_lon_color);
        azi.input.setBackground(azi_alt_color);
        alt.input.setBackground(azi_alt_color);
        vmag.input.setBackground(vmag_color);
        
        // create a button for users to click to apply their changes
//        JButton applyInput = new JButton( "Apply");
//        applyInput.addActionListener( new applyButtonHandler());
//        applyInput.setToolTipText("Update StarMap view");
        // create button to toggle constellations
        JButton toggle_constellations = new JButton("Toggle constellations");
        toggle_constellations.addActionListener(new toggle_button_handler());
        toggle_constellations.setToolTipText("Toggle constellations on/off");
        
        // populate JPanels and then add them to the toolbar below
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

        middle_row.add(vmag.label);
        middle_row.add(vmag.input);
        middle_row.add(vmag.slider);
        
        bottom_row.add(j_spinner);
        bottom_row.add(toggle_constellations);
        //bottom_row.add(applyInput);
       
        toolbar.add(top_row);
        toolbar.add(second_row);
        toolbar.add(middle_row);
        toolbar.add(bottom_row);

        this.add(toolbar);
        
        
        // whenever the date spinner is changed by the user, the star positions
        // are calculated with the new time values and the drawArea is repainted
        j_spinner.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged( ChangeEvent e) {
            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, 
                  Integer.parseInt(new SimpleDateFormat("yyyy").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("MM").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("dd").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("HH").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("mm").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("ss").format(date_spinner.getValue())), 
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            
            }
        });
        
        // updates the star view when latitude slider is changed
        lat.slider.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged( ChangeEvent e) {
            lat.input.setText(String.valueOf((double)lat.slider.getValue()/100));
            lat.input_value = (double)lat.slider.getValue()/100;

            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, 
                  Integer.parseInt(new SimpleDateFormat("yyyy").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("MM").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("dd").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("HH").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("mm").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("ss").format(date_spinner.getValue())), 
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            }
        });
        
        // updates the star view when longitude slider is changed
        lon.slider.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged( ChangeEvent e) {
            lon.input.setText(String.valueOf((double)lon.slider.getValue()/100));
            lon.input_value = (double)lon.slider.getValue()/100;

            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, 
                  Integer.parseInt(new SimpleDateFormat("yyyy").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("MM").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("dd").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("HH").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("mm").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("ss").format(date_spinner.getValue())), 
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            }
        });
        
        // updates the star view when azimuth slider is changed
        azi.slider.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged( ChangeEvent e) {
            azi.input.setText(String.valueOf((double)azi.slider.getValue()/100));
            azi.input_value = (double)azi.slider.getValue()/100;

            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, 
                  Integer.parseInt(new SimpleDateFormat("yyyy").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("MM").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("dd").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("HH").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("mm").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("ss").format(date_spinner.getValue())), 
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            }
        });
        
        // updates the star view when altitude slider is changed
        alt.slider.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged( ChangeEvent e) {
            alt.input.setText(String.valueOf((double)alt.slider.getValue()/100));
            alt.input_value = (double)alt.slider.getValue()/100;

            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, 
                  Integer.parseInt(new SimpleDateFormat("yyyy").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("MM").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("dd").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("HH").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("mm").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("ss").format(date_spinner.getValue())),
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            }
        
        });
        
        // updates the star view when vmag slider is changed
        vmag.slider.addChangeListener(new ChangeListener(){
        @Override
        public void stateChanged(ChangeEvent e) {
            vmag.input.setText(String.valueOf(((double)vmag.slider.getValue()/100)));
            vmag.input_value = ((double)vmag.slider.getValue()/100);
            
            
            // slider changed vmag, set vmag and recompute to show more/less
            // stars
            compute.minimum_vmag = vmag.input_value;
            
            compute.user_changes_position(lat.input_value, lon.input_value,
                  azi.input_value, alt.input_value, 
                  Integer.parseInt(new SimpleDateFormat("yyyy").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("MM").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("dd").format(date_spinner.getValue())),
                  Integer.parseInt(new SimpleDateFormat("HH").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("mm").format(date_spinner.getValue())), 
                  Integer.parseInt(new SimpleDateFormat("ss").format(date_spinner.getValue())), 
                  ui.drawArea.scale_factor);
            ui.drawArea.repaint();
            }
        });

        // misc
        //setSize( 360, 250 );
        //setLocationRelativeTo( null );
        //setDefaultCloseOperation( EXIT_ON_CLOSE );
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

