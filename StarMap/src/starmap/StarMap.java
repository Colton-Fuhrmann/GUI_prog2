/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import java.io.IOException;
import java.util.*;

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
        String[] constellation_abbr = new String[constellations.original_root.getContentSize()];
        //Create array of constellation objects that will eventually be in alphabetical abbr order
        constellation[] constellation_objects = new constellation[constellations.original_root.getContentSize()];
        
        for(int i = 0; i < constellation_abbr.length; i++)
        {
            Element constellation_node = (Element)
                constellations.get_node_from_abbr(constellations.original_root, 
                                                  constellation_abbr[i]);
            constellation_objects[i] = new constellation(constellation_node);
        }
    }
}