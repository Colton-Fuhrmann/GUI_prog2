/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author 7106215
 */
public class computation {
    
    double ra = 1.770; //Sirius from star file
    double dec = -0.292; //Sirius from star file
    double lat = 0.769; //Computed from user entry 44.08
    double lon = -1.802; //Computed from user entry -103.23
    double azi = 5.074; //Computed from star, 290.709 degrees
    double alt = -0.749; //-Computed from star, 42.932 degrees
    double azi0 = Math.toRadians(45); //Supplied by user as azi
    double alt0 = Math.toRadians(45); //Supplied by user as alt
    
    //Star (x, y) for 9Alp (Sirius) to be (-0.667,-0.269)
    
    ElementLister stars;
    ElementLister constellations;
    String[] constellation_abbr;
    constellation[] constellation_objects;
    
    public computation(String[] args)
    {
        //Set defualt filenames
        String stars_filename = "../stars.xml";
        String constellations_filename = "../constellations.xml";
        
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
                                      double user_azi0, double user_alt0)
    {
        lat = Math.toRadians(user_lat);
        lon = Math.toRadians(user_lon);
        azi0 = Math.toRadians(user_azi0);
        alt0 = Math.toRadians(user_alt0);
        
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
            x_y = compute_x_y(current_star);
            
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
                        
                        /*for(int z = 0; z < constellation_objects[j].line_list.length; z++)
                        {
                            System.out.println(constellation_objects[j].line_list[z]);
                        }

                        System.out.print("\n\n");*/
                        
                    }
                }
                
             if(j != -1) //If the constellation is not in the constellations.xml
             {
                //System.out.println(current_star.constellation);
                //System.exit(123);
            
                /*if(current_star.constellation.equals(" CMa "))
                {
                for(int z = 0; z < constellation_objects[j].name_table.length; z++)
                {
                    System.out.println(constellation_objects[j].name_table[z]);
                }
                }*/
                
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
            /*if(current_star.constellation.equals(" CMa "))
            {
                System.out.println("\n\n");
            for(int z = 0; z < constellation_objects[0].name_table.length; z++)
            {
                System.out.println(constellation_objects[j].name_table[z]);
            }
            System.out.println("\n\n");
            }*/
            

            }

            stars.current_star_positions.add(x_y);
            
            /*System.out.print(i);
            System.out.print( " star (x, y) for ");
            System.out.print(current_star.name);
            System.out.printf( " = %.3f %.3f\n", x_y.x, x_y.y);*/
            
            
            //////////////////
            //Draw star with current_star. variables for information and x y for position
            /////////////////
            
            i++;
            current_i.next();
        }
        
        
        for(i = 0; i < constellation_objects[0].line_list.length; i++)
        {
            System.out.println(constellation_objects[0].line_list[i]);
        }
        
        System.out.print("\n\n");
        
        for(i = 0; i < constellation_objects[0].name_table.length; i++)
        {
            System.out.println(constellation_objects[0].name_table[i]);
        }
        
        System.out.print("\n\n");
        
        for(i = 0; i < constellation_objects[0].value_table.length; i++)
        {
            System.out.print(constellation_objects[0].value_table[i].x);
            System.out.print(", ");
            System.out.println(constellation_objects[0].value_table[i].y);
            
        }
         
    }
    
    //From StarPos
    public double elapsed_days( )
    {
        // double date_now, time_now;
        // System.out.print( "Enter date and time: " );
        // cin >> date_now >> time_now;

        // e.g., suppose current time is Oct 29, 2012 11:00:00 MST
        GregorianCalendar now_cal = new GregorianCalendar();
        GregorianCalendar then_cal = new GregorianCalendar();
        now_cal.set( 2012, 10, 29, 11, 0, 0 );
        then_cal.set( 2005, 5, 10, 6, 45, 14 );

        // need current time in GMT (MST + 6 hours, or MST 7 hours if not daylight savings time)
        long now_msec = now_cal.getTimeInMillis() + 6 * 3600 * 1000;
        long then_msec = then_cal.getTimeInMillis();
        double diff_days = ( now_msec - then_msec ) / 1000.0 / ( 24.0 * 3600.0 );
        // System.out.println( "Diff in days = " + diff_days );
        return diff_days;
    }
    
    //From StarPos
    public point compute_x_y(star_contents current)
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
        double R = 1.0;		// distance to star: assume all stars are located on sphere of radius 1
        double x = R * Math.cos( alt ) * Math.sin( azi - azi0 );
        double y = R * ( Math.cos( alt0 ) * Math.sin( alt ) - Math.sin( alt0 ) * Math.cos( alt ) * Math.cos( azi - azi0 ) );
        double clip = Math.sin( alt0 ) * Math.sin ( alt ) + Math.cos( alt0 ) * Math.cos( alt ) * Math.cos( azi - azi0 );
        //System.out.printf( "\nstar position: (x,y) = (%.3f,%.3f)", x, y );
        //if ( clip < 0.0 )
            //System.out.print( "    <= bad point, should be clipped!" );
        //System.out.printf( "\n\n" );
        
        point x_y = new point();
        x_y.x = x;
        x_y.y = y;
        
        return x_y;     
    }
    
    
}
