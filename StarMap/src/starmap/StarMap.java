/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import java.io.IOException;
import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author 7106215
 */
public class StarMap {

    /**
     * @param args the command line arguments
     */
    
    double ra = 1.770; //Sirius from star file
    double dec = -0.292; //Sirius from star file
    double lat = 0.769; //Computed from user entry 44.08
    double lon = -1.802; //Computed from user entry -103.23
    double azi = 5.074; //Computed from star, 290.709 degrees
    double alt = -0.749; //-Computed from star, 42.932 degrees
    double azi0 = Math.toRadians(45); //Supplied by user as azi
    double alt0 = Math.toRadians(45); //Supplied by user as alt
    
    //Main takes two command line arguments
    //args[0] will be the stars xml file (default "../stars.xml")
    //args[1] will be the constellations xml file (default "../constellations.xml")
    public static void main(String[] args) {
        
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
        ElementLister stars = new ElementLister(stars_filename);
        ElementLister constellations = new ElementLister(constellations_filename);
        //stars.listChildren(stars.original_root, 0);
        
        //Create an array that has the constellation abbreviations in alphabetical abbr order
        String[] constellation_abbr = new String[(constellations.original_root.getContentSize() - 1) / 2];
        //Create array of constellation objects that will eventually be in alphabetical abbr order
        constellation[] constellation_objects = new constellation[(constellations.original_root.getContentSize() - 1) / 2];
        
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
            
            //stars.listChildren(constellation_node, 0);
            constellation_objects[i] = new constellation(constellation_node);
        }
        
        //Get a list of all the star nodes
        List children = stars.original_root.getChildren();
        Iterator iterator = children.iterator();
        int i = 0;
        
        //Process all stars
        while(iterator.hasNext())
        {
            //System.out.print(i);
            //System.out.print("\n");
            //Populate stars.star_array with the information
            //So we don't have to keep processing the xml
            stars.put_star_in_array( (Element) children.get(i));
            
            //stars.listChildren((Element) children.get(i), 0);
            ///System.exit(123);
            
            i++;
            iterator.next();
        }
        
        //get the user positions, times, etc        
        
        //Star (x, y) for Sirius to be (-0.667,-0.269)
        
        //////////////////////////////////////////////////////////////
        //Will be done each time the user changes position
        Iterator<star_contents> current_i = stars.star_array.iterator();
        i = 0;
        int j = -1;
        point x_y = new point();
        
        double lat = 0.769; //Computed from user entry 44.08
        double lon = -1.802; //Computed from user entry -103.23
        double azi0 = Math.toRadians(45); //Supplied by user as azi
        double alt0 = Math.toRadians(45); //Supplied by user as alt
        
        for(i = 0; i < constellation_objects.length; i++)
        {
            constellation_objects[i].reset_hash_table();
        }
        
        i = 0;
        
        stars.current_star_positions.clear();
        
        //Process all stars
        while(current_i.hasNext())
        {
            //Set current star and compute the x and y values given user position
            star_contents current_star = stars.star_array.get(i);
            x_y = compute_x_y(current_star, lat, lon, alt0, azi0);
            
            //If the star is in a constellation
            if(current_star.constellation != null)
            {
                //Find the index of the star's constellation in the array of abbr
                for(int k = 0; k < constellation_abbr.length && j == -1; k++)
                {
                    if(constellation_abbr[k].equals(current_star.constellation))
                    {
                        j = k;
                    }
                }
                
                constellation current_constellation = constellation_objects[j];
            
                current_constellation.set_star_hash_value(current_star.constellation,
                                                      x_y.x, x_y.y);
            }

            stars.current_star_positions.add(x_y);
            System.out.print( "(x, y) for ");
            System.out.print(current_star.name);
            System.out.printf( " = %e %e\n", x_y.x, x_y.y);
            //////////////////
            //Draw star with current_star. variables for information and x y for position
            /////////////////
            
            i++;
            current_i.next();
            System.exit(123);
        }
        
    }
    
    public static point compute_x_y(star_contents current, double lat, double lon, double alt0, double azi0)
    {
        // # days since June 10, 2005 6:45:14 GMT = 1957.093588
        double t = StarPos.elapsed_days();
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