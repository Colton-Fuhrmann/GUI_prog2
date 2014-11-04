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
    public static void main(String[] args) {
        System.out.println("test");
        ElementLister stars = new ElementLister(args[0]);
        stars.listChildren(stars.original_root, 0);
    }
}