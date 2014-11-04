/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

/**
 *
 * @author 7106215
 */
public class constellation {
    
    static int table_size = 53; //Size of hash and  line_list tables
    
    //Stores an (x, y) point of a star in the constellation
    public class point
    {
        float x, y;
        
        //Construct origin point
        public point()
        {
            x = 0;
            y = 0;
        }
    }
    
    public String[] name_table; //Hash table for the names of the stars
    public point[] value_table; //Values that correspond to the names in name_table[]
    public String[] line_list; //List of line segments in constellation
    
    /////////////////////////////////////////////////
    //TODO pass in the xml node for the constellation that has the list of stars
    //and then put those in line_list
    public constellation()
    {
        name_table = new String[table_size];
        value_table = new point[table_size];
        line_list = new String[table_size];
        
        //Initialize the required arrays with invalid values
        for(int i = 0; i < table_size; i++)
        {
            name_table[i] = "invalid_star";
            line_list[i] = "invalid_star";
        }
    }
    
    public void draw()
    {
        point point1 = new point();
        point point2 = new point();
        
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
            if(name_table[index] == "invalid_star") //Spot is available
            {
                return index; //name hashes here
            }
            else
            {
                index = (index + (i * i)) % table_size; //Compute new hash value
            }
        }
        
        return index; //Shuts up NetBeans error for no return at end of function
    }
    
    //Puts the star name in name_table and its computed x and y in the value_table
    public void set_star_hash_value(String name, float x, float y)
    {
        int index = hash_star_name(name); //Get hash value for star name
        
        //Set the passed values in the appropriate tables
        name_table[index] = name;
        value_table[index].x = x;
        value_table[index].y = y;
        
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
