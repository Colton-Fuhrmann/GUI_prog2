////////////////////////////////////////////////////////////////////////////////
//Authors: Colton Fuhrmann, Kevin Hilt
//Date: November 24, 2014
//Course: CSC421
//Instructor: Dr. Weiss
//
//Description: The star_contents class stores all the information from an XML
//node in an object for that star.
////////////////////////////////////////////////////////////////////////////////
package starmap;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author 7106215
 */
public class star_contents
{
    String HRnumber;
    String name;
    String constellation;
    double ra;
    double dec;
    double vmag;
    String star_class;
    String common_name;
    double sin_alpha;
    double cos_alpha;
    double sin_delta;
    double cos_delta;
    
    Ellipse2D.Double ellipse;
    Ellipse2D.Double tooltip_area;

    //Construct origin point
    public star_contents(String xml_HRnumber, String xml_name, 
                         String xml_constellation, double xml_ra,
                         double xml_dec, double xml_vmag, String xml_star_class,
                         String xml_common_name)
    {
        HRnumber = xml_HRnumber;
        name = xml_name;
        constellation = xml_constellation;
        ra = xml_ra;
        dec = xml_dec;
        vmag = xml_vmag;
        star_class = xml_star_class;
        common_name = xml_common_name;
        sin_alpha = Math.sin(ra);
        cos_alpha = Math.cos(ra);
        sin_delta = Math.sin(dec);
        cos_delta = Math.cos(dec);
    }
}
