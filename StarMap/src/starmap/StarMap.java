/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

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

        //compute.user_changes_position(44.08, -103.23, 45, 45, 2010, 10, 6, 5, 2, 1);
        
        ui = new ui(compute);
        compute.set_ui(ui);

        }
        
    }
    
    
