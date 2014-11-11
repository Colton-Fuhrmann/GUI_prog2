/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import org.jdom2.*;
import java.util.*;

/**
 *
 * @author 7106215
 */
public class constellation {
    
    static int table_size = 97; //Size of hash and  line_list tables
    public String[] name_table; //Hash table for the names of the stars
    public point[] value_table; //Values that correspond to the names in name_table[]
    public String[] line_list; //List of star names of line segments in constellation
    
    /////////////////////////////////////////////////
    //TODO pass in the xml node for the constellation that has the list of stars
    //and then put those in line_list
    public constellation(Element node)
    {
        name_table = new String[table_size];
        value_table = new point[table_size];
        line_list = new String[table_size];
        
        for(int i = 0; i < table_size; i++)
        {
            value_table[i] = new point();
        }
        
        //Initialize the required arrays with invalid values
        for(int i = 0; i < table_size; i++)
        {
            name_table[i] = "invalid_star";
            line_list[i] = "invalid_star";
        }
        
        List children = node.getChildren();
        int size = (node.getContentSize() - 1) / 2;
        int j = 0;
        
        //Go through all tags in node
        for(int i = 0; i < size; i++)
        {
            // If it's a line tag
            if (((Element)children.get(i)).getName() == "line")
            {
                //Break into two strings, each holding a star name, 
                //since the line tag is "star1 to star2"
                String text = ((Element)children.get(i)).getValue();
                String[] star_names = text.split("to");
                
                line_list[j] = star_names[0];
                j++;
                line_list[j] = star_names[1];
                j++;
            }
        }
    }
    
    public void reset_hash_table()
    {
        for(int i = 0; i < table_size; i++)
        {
            name_table[i] = "invalid_star";
        }
    }
    
    public void draw()
    {
        point point1 = new point();
        point point2 = new point();
        
        for(int i = 0; i < table_size; i++)
        {
            System.out.println(line_list[i]);
        }
        
        for(int i = 0; i < table_size && line_list[i] != "invalid_star"; i++)
        {
          //line_list[i] is a star name
          //get_star_hash_value returns the (x, y) values of that star in a point
          point1 = get_star_hash_value(line_list[i]);
          i++;
          point2 = get_star_hash_value(line_list[i]);
          
          ///////////////////////////////////////////////
          //TODO draw a line from point1 to point 2 in UI
        }
    }
    
    public int hash_star_name(String name)
    {
        int index = 0;
        int i;

        //Hash by adding all the characters together and modding by table_size
        for(i = 0; i < name.length(); i++)
        {
            index += name.charAt(i);
        }
        
        index = index % table_size;
        
        //Use quadratic hashing to find available index for name
        for(i = 1; i < table_size; i++)
        {
            if(name_table[index].equals("invalid_star")) //Spot is available
            {
                return index; //name hashes here
            }
            else
            {
                index = (index + (i * i)) % table_size; //Compute new hash value
            }
        }
        
        //System.out.print("Here with name = ");
        //System.out.println(name);
        return index; //Shuts up NetBeans error for no return at end of function
    }
    
    //Puts the star name in name_table and its computed x and y in the value_table
    public void set_star_hash_value(String name, double x, double y)
    {
        int index = hash_star_name(name); //Get hash value for star name
        
        //System.out.print("index = ");
        //System.out.print(index);
        //System.out.print("\n");
        
        //System.out.print("x = ");
        //System.out.print(x);
        //System.out.print("\n");
        
        //System.out.print("value_table[index].x = ");
        //System.out.print(value_table[index].x);
        //System.out.print("\n");
        
        //Set the passed values in the appropriate tables
        name_table[index] = name;
        value_table[index].x = x;
        value_table[index].y = y;
        
        /*if(name.equals(" 31Del "))
        {
            System.out.println(name_table[index]);
            System.out.println(value_table[index].x);
            System.out.println(value_table[index].y);
        }*/
        
        return;
    }
    
    public point get_star_hash_value(String name)
    {
        int index = hash_star_name(name); //Get hash value for star name
        
        //Get star's x and y from value_table
        point values = new point();
        values.x = value_table[index].x;
        values.y = value_table[index].y;
        
        return values;
    }
}
