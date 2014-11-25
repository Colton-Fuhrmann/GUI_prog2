////////////////////////////////////////////////////////////////////////////////
//Authors: Colton Fuhrmann, Kevin Hilt
//Date: November 24, 2014
//Course: CSC421
//Instructor: Dr. Weiss
//
//Description: This is the main class for the application. It takes the command
//line arguments for the stars and constellations XML and then initializes
//the importand computation and ui objects, which will be used for computing
//star positions and having an interface to draw on.
//
//Basic runtime of the entire StarMap application features an overhead view of
//the sky from a specified position on Earth at a specified time. Using the
//input fields on sliders for each option, the user can specify and latitude,
//longitude, azimuth, altitude, viewing time, minimum apparent magnitude,
//and constellations to show on the screen.

//The mouse both interacts with these input widgets and the drawn application 
//area itself. Clicking with the left mouse button and dragging will
//update and azimuth and altitude, dynamically updating both the input widgets
//and the view on the screen. Scrolling up or down will zoom in and out.
//Hovering over a star will bring up a tooltip with that star's information,
//which will stay as long as the cursor over the star.
////////////////////////////////////////////////////////////////////////////////
package starmap;

import java.text.SimpleDateFormat;

/**
 *
 * @author 7106215
 */
public class StarMap {

    /**
     * @param args the command line arguments
     */
    
    //Main takes two command line arguments
    //args[0] will be the stars xml file (default "../stars.xml")
    //args[1] will be the constellations xml file (default "../constellations.xml")
    public static void main(String[] args) {
        
        //Initiallize computation class, which will handle
        //conputing star and constellation positions
        ui ui = null;
        computation compute = new computation(args);

        ui = new ui(compute);
        compute.set_ui(ui);
        
        //Set default values
        ui.toolbar.lat.input.setText(String.valueOf((double)ui.toolbar.lat.slider.getValue()/100));
        ui.toolbar.lat.input_value = (double)ui.toolbar.lat.slider.getValue()/100;
        
        ui.toolbar.lon.input.setText(String.valueOf((double)ui.toolbar.lon.slider.getValue()/100));
        ui.toolbar.lon.input_value = (double)ui.toolbar.lon.slider.getValue()/100;
        
        ui.toolbar.azi.input.setText(String.valueOf((double)ui.toolbar.azi.slider.getValue()/100));
        ui.toolbar.azi.input_value = (double)ui.toolbar.azi.slider.getValue()/100;
        
        ui.toolbar.alt.input.setText(String.valueOf((double)ui.toolbar.alt.slider.getValue()/100));
        ui.toolbar.alt.input_value = (double)ui.toolbar.alt.slider.getValue()/100;
        
        //Star out at default position
        compute.user_changes_position(ui.toolbar.lat.input_value, 
                                      ui.toolbar.lon.input_value,
                                      ui.toolbar.azi.input_value, 
                                      ui.toolbar.alt.input_value, 
                                      Integer.parseInt(new SimpleDateFormat("yyyy").format(ui.toolbar.date_spinner.getValue())),
                                      Integer.parseInt(new SimpleDateFormat("MM").format(ui.toolbar.date_spinner.getValue())), 
                                      Integer.parseInt(new SimpleDateFormat("dd").format(ui.toolbar.date_spinner.getValue())),
                                      Integer.parseInt(new SimpleDateFormat("HH").format(ui.toolbar.date_spinner.getValue())), 
                                      Integer.parseInt(new SimpleDateFormat("mm").format(ui.toolbar.date_spinner.getValue())), 
                                      Integer.parseInt(new SimpleDateFormat("ss").format(ui.toolbar.date_spinner.getValue())), 
                                      ui.drawArea.scale_factor);

        ui.drawArea.repaint();

        }
        
    }
    
    
