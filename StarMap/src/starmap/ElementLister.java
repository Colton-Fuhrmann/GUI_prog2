// ElementLister.java
// Use JDOM to parse an XML file
// Based on Java example in Processing XML with Java (Elliotte Harold).
// JMW Fall 2014

package starmap;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import java.io.IOException;
import java.util.*;
import java.lang.Math;

public class ElementLister
{   
    public Document original_list; //Xml document
    public Element original_root; //Reference to root of xml
    //Array that will hold the star contents of the xml
    List<star_contents> star_array = new ArrayList<>();
    List<point> current_star_positions = new ArrayList<>();
    
    
    //Updated main function to be constructor
    public ElementLister( String filename )
    {
	// read and parse XML document
        SAXBuilder builder = new SAXBuilder();
        try
        {
            original_list = builder.build( filename );	// parse XML tags
            original_root = original_list.getRootElement();	// get root of XML tree
            //listChildren( root, 0 );			// print info in XML tree
        }
        // JDOMException indicates a well-formedness error
        catch ( JDOMException e )
        {
            System.out.println( filename + " is not well-formed." );
            System.out.println( e.getMessage() );
        }
        catch ( IOException e )
        {
            System.out.println( e );
        }
    }

    // print XML tags and leaf node values
    public void listChildren( Element current, int depth )
    {
	// get children of current node
        List children = current.getChildren();
        Iterator iterator = children.iterator();

        // print node name and leaf node value, indented one space per level in XML tree
        printSpaces( depth );
        System.out.print( current.getName() );
        if ( !iterator.hasNext() )
            System.out.print( " = " + current.getValue() );
        System.out.println();

        // recursively process each child node
        while ( iterator.hasNext() )
        {
            Element child = ( Element ) iterator.next();
            listChildren( child, depth + 1 );
        }
    }

    // indent to show hierarchical structure of XML tree
    private void printSpaces( int n )
    {
        for ( int i = 0; i < n; i++ )
        {
            System.out.print( "  " );
        }
    }
    
    public void put_star_in_array(Element star)
    {
	// get children of current node
        List children = star.getChildren();
        Iterator iterator = children.iterator();
        int i = 0; //Current spot in children
        
        //Variables to take out of star xml
        String HRnumber = null;
        String name = null;
        String constellation = null;
        double ra = 0;
        double dec = 0;
        double vmag = 0;
        String star_class = null;
        String common_name = null;
        
        while ( iterator.hasNext() )
        {
            Element current = (Element) children.get(i);
            
            String current_value = current.getValue();
            String current_name = current.getName();
            if(current_value.equals(" ??? "))
            {
                current_value = null;
            }
            
            if(current_name.equals("HRnumber"))
            {
//                System.out.print("Set HRnumber to ");
//                System.out.print(current_value);
//                System.out.print("\n");
                
                
                HRnumber = current_value;
            }
            else if(current_name.equals("name"))
            {
                //System.out.print("Set name to ");
                //System.out.print(current_value);
                //System.out.print("\n");
                
                name = current_value;
            }
            else if(current_name.equals("constellation"))
            {
                //System.out.print("Set constellation to ");
                //System.out.print(current_value);
                //System.out.print("\n");
                
                constellation = current_value;
            }
            else if(current_name.equals("ra") && !current_name.isEmpty())
            {
                current_value = current_value.trim();
                String delimiter = "[ ]";
                String[] result = current_value.split(delimiter);
                
                //Hours in results[0], minutes in results[1], seconds in results[2]
                ra = Math.toRadians( ( Double.parseDouble(result[0]) 
                                    + Double.parseDouble(result[1]) / 60 
                                    + Double.parseDouble(result[2]) / 3600 ) * 15 );
                
                //System.out.print("Set ra to ");
                //System.out.print(ra);
                //System.out.print("\n");
                
            }
            else if(current_name.equals("dec") && !current_name.isEmpty())
            {
                current_value = current_value.trim();
                String delimiter = "[ ]";
                String[] result = current_value.split(delimiter);
                
                //Degrees in results[0], minutes in results[1], seconds in results[2]
                dec = Math.toRadians( Math.abs( Double.parseDouble(result[0]) ) 
                                      + Double.parseDouble(result[1]) / 60 
                                      + Double.parseDouble(result[2]) / 3600 );
                if ( Double.parseDouble(result[0]) < 0 ) dec = -dec;
                
                //System.out.print("Set dec to ");
                //System.out.print(dec);
                //System.out.print("\n");
                
            }
            else if(current_name.equals("vmag"))
            {
                //System.out.print("Set vmag to ");
                //System.out.print(current_value);
                //System.out.print("\n");
                
                vmag = Double.parseDouble(current_value);
            }
            else if(current_name.equals("class"))
            {
                
                //System.out.print("Set star_class to ");
                //System.out.print(current_value);
                //System.out.print("\n");
                
                star_class = current_value;
            }
            else
            {
                //System.out.print("Set common_name to ");
                //System.out.print(current_value);
                //System.out.print("\n");
                
                common_name = current_value;
            }         

            i++;
            iterator.next();
        }
        
        //Create a new star_contents object
        star_contents current_contents = 
                new star_contents(HRnumber, name, constellation, ra, dec, vmag,
                                  star_class, common_name);
        //Push in into the array
        star_array.add(current_contents);
    }
    
    public Element get_node_from_abbr( Element current, String abbr_value)
    {
	// get children of current node
        List children = current.getChildren();
        Iterator iterator = children.iterator();
        Element to_return = null;
        Element check = null;

        // If the node is a leaf node and tag name is abbr
        if ( !iterator.hasNext() && current.getName().equals("abbr") 
              && current.getValue().equals(abbr_value))
        {
            //Element node =  (Element) children.get(0); //Return the entire xml element
            //return (Element) node.getParent();
            return current.getParentElement();
        }

        // recursively process each child node
        while ( iterator.hasNext() )
        {
            Element child = ( Element ) iterator.next();
            check = get_node_from_abbr( child, abbr_value );
            
            if(check != null)
            {
                to_return = check;
            }
        }
        
        //listChildren(current.getParentElement(), 0);
        //System.exit(123);
        return to_return; //Could be wrong, used to tbe getParent(), but threw errors
    }
    
    //Returns an array of strings that has the abbr tags for constellations
    //in alphabetical order
    public String[] abbr_in_order()
    {
        int number_of_nodes = (original_root.getContentSize() - 1) / 2;
        String[] abbr = new String[number_of_nodes];
        String child_text;
        Element current;
        
        List children = original_root.getChildren();
        
        for(int i = 0; i < number_of_nodes; i++)
        {
            current = (Element) children.get(i);
            child_text = current.getChildText("abbr");
            abbr[i] = child_text;
        }
        
        Arrays.sort(abbr);
        
        return abbr;
        
    }
}
