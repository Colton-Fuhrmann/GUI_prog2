/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.jdom2.Element;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 *
 * @author 7106215
 */
public class computation {
    
    double ra;
    double dec;
    double lat;
    double lon;
    double azi;
    double alt;
    double azi0;
    double alt0;
    int year;
    int month;
    int day;
    int hour;
    int min;
    int sec;
    ui ui;
    boolean constellations_on = false;
    double minimum_vmag = 3;
    double vmag_range_low;
    double vmag_range_high;
    
    //Star (x, y) for 9Alp (Sirius) to be (-0.667,-0.269)
    
    ElementLister stars;
    ElementLister constellations;
    String[] constellation_abbr;
    constellation[] constellation_objects;
    List<star_contents> onscreen_stars;
    
    public computation(String[] args)
    {
        //Set defualt filenames
        String stars_filename = "../stars.xml";
        String constellations_filename = "../constellations.xml";
        onscreen_stars = new ArrayList<>();
        
        //Check for command line arguments
        if(args.length == 1)
        {
            stars_filename = args[0]; //Stars file was provided
            
            if(args.length == 2)
            {
                constellations_filename = args[1]; //Constellations file was provided
            }
        }
        
        //Read in stars and constellations xml using the ElementLister class
        stars = new ElementLister(stars_filename);
        constellations = new ElementLister(constellations_filename);
        //stars.listChildren(stars.original_root, 0);
        
        //Create an array that has the constellation abbreviations in alphabetical abbr order
        constellation_abbr = new String[(constellations.original_root.getContentSize() - 1) / 2];
        //Create array of constellation objects that will eventually be in alphabetical abbr order
        constellation_objects = new constellation[(constellations.original_root.getContentSize() - 1) / 2];
        
        constellation_abbr = constellations.abbr_in_order();
        
        for(int i = 0; i < constellation_abbr.length; i++)
        {
            Element constellation_node = (Element)
                constellations.get_node_from_abbr(constellations.original_root, 
                                                  constellation_abbr[i]);
            if( constellation_node == null )
            {
                System.out.print("it was null.\n");
            }
            
            constellation_objects[i] = new constellation(constellation_node);
        }
        
        //Get a list of all the star nodes
        List children = stars.original_root.getChildren();
        Iterator iterator = children.iterator();
        int i = 0;
        
        //Process all stars
        while(iterator.hasNext())
        {
            //Populate stars.star_array with the information
            //So we don't have to keep processing the xml
            stars.put_star_in_array( (Element) children.get(i));
            
            i++;
            iterator.next();
        }   
    }
    
    public void user_changes_position(double user_lat, double user_lon,
                                      double user_azi0, double user_alt0,
                                      double user_year, double user_month,
                                      double user_day, double user_hour,
                                      double user_min, double user_sec,
                                      double scale_factor)
    {
        lat = Math.toRadians(user_lat);
        lon = Math.toRadians(user_lon);
        azi0 = Math.toRadians(user_azi0);
        alt0 = Math.toRadians(user_alt0);
        year = (int)user_year;
        month = (int)user_month;
        day = (int)user_day;
        hour = (int)user_hour;
        min = (int)user_min;
        sec = (int)user_sec;
        
        vmag_range_low = 50;
        vmag_range_high = -50;
        
        Iterator<star_contents> current_i = stars.star_array.iterator();
        int i = 0;
        int j = -1;
        point x_y = new point();
        
        for(i = 0; i < constellation_objects.length; i++)
        {
            constellation_objects[i].reset_hash_table();
        }
        
        i = 0;
        
        stars.current_star_positions.clear();
        
        //Process all stars
        while(current_i.hasNext())
        {
            j = -1; //Reset j
            
            //Set current star and compute the x and y values given user position
            star_contents current_star = stars.star_array.get(i);
            x_y = compute_x_y(current_star, ui.drawArea.scale_factor);
            
            
            //If the star is in a constellation
            if(current_star.constellation != null)
            {
                //Find the index of the star's constellation in the array of abbr
                for(int k = 0; k < constellation_abbr.length && j == -1; k++)
                {
                    if(constellation_abbr[k].equals(current_star.constellation))
                    {
                        //Set j to the index of the constellation abbr, 
                        //which causes it to drop out next time thorugh the loop
                        j = k;                  
                    }
                }
                
             if(j != -1) //If the constellation is not in the constellations.xml
             {
                int in_list = 0;
                int common_in_list = 0;
                for(int l = 0; l < constellation_objects[j].line_list.length && in_list == 0; l++)
                {
                    //If the star is in the current constellation's line list
                    if(current_star.common_name != null)
                    {
                        if(current_star.common_name.equals(constellation_objects[j].line_list[l]))
                        {
                            in_list = 1;
                            common_in_list = 1;
                        }
                    }
                    if(current_star.name.equals(constellation_objects[j].line_list[l]))
                    {
                        in_list = 1;
                    }
                }

                if(in_list == 1)
                {
                    //If the star has a common name, the constellation uses that
                    //Else, the constellation uses just the regular names
                    if(current_star.common_name != null && common_in_list == 1)
                    {
                        constellation_objects[j].set_star_hash_value(current_star.common_name,
                                                          x_y.x, x_y.y);
                        
                    }
                    else
                    {
                        constellation_objects[j].set_star_hash_value(current_star.name,
                                                          x_y.x, x_y.y);
                    }
                }
            
            }
            }

            stars.current_star_positions.add(x_y); 
            i++;
            current_i.next();
        }
    }
    
    //From StarPos
    public double elapsed_days()
    {
        // e.g., suppose current time is Oct 29, 2012 11:00:00 MST
        GregorianCalendar now_cal = new GregorianCalendar();
        GregorianCalendar then_cal = new GregorianCalendar();
        now_cal.set( year, month, day, hour, min, sec );
        then_cal.set( 2005, 5, 10, 6, 45, 14 );

        // need current time in GMT (MST + 6 hours, or MST 7 hours if not daylight savings time)
        long now_msec = now_cal.getTimeInMillis() + 6 * 3600 * 1000;
        long then_msec = then_cal.getTimeInMillis();
        double diff_days = ( now_msec - then_msec ) / 1000.0 / ( 24.0 * 3600.0 );
        // System.out.println( "Diff in days = " + diff_days );
        return diff_days;
    }
    
    //From StarPos
    public point compute_x_y(star_contents current, double scale_factor)
    {
        // # days since June 10, 2005 6:45:14 GMT = 1957.093588
        double t = elapsed_days();
        // System.out.printf( "t = %.3f days elapsed\n", t );
        double tG = Math.IEEEremainder( 360.0 * 1.0027379093 * t, 360.0 );
        double thetaG = Math.toRadians( tG );
        // System.out.printf( "thetaG = %.3f = %.3f\n", tG, thetaG );
        double psi = tG + Math.toDegrees( lon ) + 90;
        // System.out.printf( "psi = %.3f = %.3f\n", psi, Math.toRadians( psi ) );

        // rename ala formulas in Don's paper
        double alpha = current.ra;
        double beta  = lat;
        double delta = current.dec;
        psi = Math.toRadians( psi );

        double X =  Math.cos( psi ) * Math.cos( delta ) * Math.cos( alpha )
                  + Math.sin( psi ) * Math.cos( delta ) * Math.sin( alpha );
        double Y = -Math.sin( beta ) * Math.sin( psi ) * Math.cos( delta ) * Math.cos( alpha )
                  + Math.sin( beta ) * Math.cos( psi ) * Math.cos( delta ) * Math.sin( alpha )
                  + Math.cos( beta ) * Math.sin( delta );
        double Z =  Math.cos( beta ) * Math.sin( psi ) * Math.cos( delta ) * Math.cos( alpha )
                  - Math.cos( beta ) * Math.cos( psi ) * Math.cos( delta ) * Math.sin( alpha )
                  + Math.sin( beta ) * Math.sin( delta );
        // System.out.printf( "(X,Y,Z) = (%.3f,%.3f,%3f)\n\n", X, Y, Z );

        // finally compute alt/azi values
        double alt = Math.atan( Z / Math.sqrt( X * X + Y * Y ) );
        double azi = Math.acos( Y / Math.sqrt( X * X + Y * Y ) );
        if ( X < 0.0 ) azi = 2.0 * Math.PI - azi;

        // project star's (alt,azi) position on sphere to (x,y) coordinate on viewing window
        double R = 1;		// distance to star: assume all stars are located on sphere of radius 1
        double x = .5 + (scale_factor * R * Math.cos( alt ) * Math.sin( azi - azi0 ));
        double y = .5 + (scale_factor * R * ( Math.cos( alt0 ) * Math.sin( alt ) - Math.sin( alt0 ) * Math.cos( alt ) * Math.cos( azi - azi0 ) ) );
        double clip = Math.sin( alt0 ) * Math.sin ( alt ) + Math.cos( alt0 ) * Math.cos( alt ) * Math.cos( azi - azi0 );
        //System.out.printf( "\nstar position: (x,y) = (%.3f,%.3f)", x, y );
        
        point x_y = new point();
        
        //If the star has positive coordinates and should be displayed
        if (clip >= 0.0)
        {
            //Save this view's min and max vmag range to be used for scaling later
            if(current.vmag > vmag_range_high)
            {
                vmag_range_high = current.vmag;
            }
            else if(current.vmag < vmag_range_low)
            {
                vmag_range_low = current.vmag;
            }  
        }
        
        x_y.x = x;
        x_y.y = y;
            
        return x_y;     
    }
    
    public void set_ui(ui passed_ui)
    {
        ui = passed_ui;
    }
    
    public void draw_stars(Graphics2D g, int panel_width, int panel_height)
    {
        int x, y; //Position of star
        double vmag_range = vmag_range_high - vmag_range_low;
        int opacity_scale = (int) Math.abs((vmag_range - vmag_range_low));
        int radius; //Size of the star on the screen. Brighter stars are bigger
        float opacity; //Brighter stars have a higher opacity
        String name;
        star_contents current_star;
        
        onscreen_stars.clear(); //There are no stars onscreen currently
        
        if(constellations_on == true)
        {
            for(int i = 0; i < constellation_abbr.length; i++)
            {
                constellation_objects[i].reset_hash_table();
                constellation_objects[i].draw(g, panel_width, panel_height);
            }     
        }
        
        for(int i = 0; i < stars.current_star_positions.size(); i++)
        {
            current_star = stars.star_array.get(i);
            name = "No Name";
            
            //Get the x and y of the star's current position
            //Scale them by the panel_width and panel_height
            x = (int) (stars.current_star_positions.get(i).x * panel_width);
            y = (int) (stars.current_star_positions.get(i).y * panel_height);
            
            //Draw the star if it is at least the minimum vmag value
            //(negative vmags are brighter, so <=)
            if(current_star.vmag <= minimum_vmag)
            {         
                //Scale radius
                //vmag_range - current_star.vmag makes all values negative
                //Smallest values are brightest, therefore will need the largest radius
                //Multiply by vmag_range to make the radius reasonably sized
                //Absolute value to make them positive
                radius = (int) Math.abs((vmag_range - current_star.vmag) + 1 * vmag_range);
                
                if(radius < 1)
                {
                    radius = 1;
                }
                else if(radius > 20)
                {
                    radius = 20;
                }
                
                opacity = (float) Math.abs((vmag_range - current_star.vmag) + 1) / opacity_scale;
                
                if(opacity < .1)
                {
                    opacity = 0.1f;
                }
                else if(opacity > 1)
                {
                    opacity = 1;
                }
                else if(Float.isNaN(opacity))
                {
                    opacity = 0;
                }

                //Draw a star at the correct position with its radius and opacity
                g.setColor(Color.white);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                
                //Set the current star's ellipse to have the computed radius
                current_star.ellipse = new Ellipse2D.Double(x, y, radius, radius);
                current_star.tooltip_area = new Ellipse2D.Double(x, y, radius + 2, radius + 2);
                onscreen_stars.add(current_star); //Current star is onscreen
                g.fill(current_star.ellipse); //Draw current star
                
                //Only display name if brighter than 2.5
                if(current_star.vmag < 2.5)
                {
                    //Get common name or name of the star if they aren't null
                    if(stars.star_array.get(i).common_name != null)
                    {
                        name = stars.star_array.get(i).common_name;
                    }
                    else if(stars.star_array.get(i).name != null)
                    {
                        name = stars.star_array.get(i).name;
                    }
                    g.setColor(Color.yellow);
                    g.drawString(name, (x + radius + 1), (y + radius + 1));
                }
            }
        }
    }
    
}
