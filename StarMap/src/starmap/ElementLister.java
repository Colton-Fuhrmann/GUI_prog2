// ElementLister.java
// Use JDOM to parse an XML file
// Based on Java example in Processing XML with Java (Elliotte Harold).
// JMW Fall 2014

package starmap;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import java.io.IOException;
import java.util.*;

public class ElementLister
{
    public Document original_list;
    public Element original_root;
    
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
    
        public Element get_node_from_abbr( Element current, String abbr_value)
    {
	// get children of current node
        List children = current.getChildren();
        Iterator iterator = children.iterator();

        // If the node is a leaf node and tag name is abbr
        if ( !iterator.hasNext() && current.getName() == "abbr" 
              && current.getValue() == abbr_value)
        {
            Element node =  (Element) children.get(0); //Return the entire xml element
            return (Element) node.getParent();
        }

        // recursively process each child node
        while ( iterator.hasNext() )
        {
            Element child = ( Element ) iterator.next();
            get_node_from_abbr( child, abbr_value );
        }
        
        return (Element) current.getParent();
    }
    
    //Returns an array of strings that has the abbr tags for constellations
    //in alphabetical order
    public String[] abbr_in_order()
    {
        int number_of_nodes = original_root.getContentSize();
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
