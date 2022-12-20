package EntitiesTests;


import items.Item;
import org.junit.*;
import org.junit.Test;

import static org.junit.Assert.*;

/***
 * @author Rutwa Engineer
 */
public class ItemTest {



    // the item constructor
    @Test(timeout = 50)
    public void testItemC2(){
        Item i2 = new Item(2, "The Starry Night painting", "By Van Gough. It's beautiful!");
    }


    //Test item id for one item
    @Test(timeout = 50)
    public void testIdOneItem(){
        Item i3 = new Item(1, "Notebook", "a notebook");
        i3.setItemID(1);
        assertEquals((Integer) 1, i3.getItemID());
    }

}
