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
}
