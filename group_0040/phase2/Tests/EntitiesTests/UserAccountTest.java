package EntitiesTests;

import accounts.UserAccount;
import items.Item;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Rutwa Engineer
 */
public class UserAccountTest {
    //the User constructor
    @Test(timeout = 50)
    public void testUser() {
        UserAccount u = new UserAccount("enter");
    }

    //add an item to wishlist
    @Test(timeout = 50)
    public void testaddToWishlist() {
        UserAccount u = new UserAccount("enter");
        Item i = new Item(1, "Book", "The pragmatic programmer book");
        u.addToWishlist(i);
        assertTrue("addToWishlist() fails\n", u.getWishlist().contains(i));
    }

    //add an item to inventory
    @Test(timeout = 50)
    public void testaddToInventory() {
        UserAccount u = new UserAccount("enter");
        Item i = new Item (1, "book", "The pragmatic programmer book");
        u.addToInventory(i);
        assertTrue("addToInventory() fails\n", u.getInventory().contains(i));
    }

    //remove an item from wishlist
    @Test(timeout = 50)
    public void testremoveFromWishlist() {
        UserAccount u = new UserAccount("enter");
        Item i = new Item (1, "book", "The pragmatic programmer book");
        u.addToWishlist(i);
        assertTrue("addToWishlist() fails\n", u.getWishlist().contains(i));
        u.removeFromWishlist(i);
        assertTrue("removeFromWishlist() fails\n", u.getWishlist().isEmpty());
    }

    //remove an item from inventory
    @Test(timeout = 50)
    public void testRemoveFromInventory() {
        UserAccount u = new UserAccount("enter");
        Item i = new Item(1, "book", "The pragmatic programmer book");
        u.addToInventory(i);
        assertTrue("addToInventory() fails\n", u.getInventory().contains(i));
        u.removeFromInventory(i);
        assertTrue("removeFromInventory() fails\n", u.getInventory().isEmpty());
    }
}
