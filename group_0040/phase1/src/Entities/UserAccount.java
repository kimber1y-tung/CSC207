package Entities;

import java.util.ArrayList;
import java.util.List;

public class UserAccount extends Account {
    private List<Item> wishlist = new ArrayList<>();
    private List<Item> inventory = new ArrayList<>();
    private int overBorrowed = 0; //borrowed items - lent items > 0
    private int numTrade = 0;  //# of times the User has traded
    private int incompleteTrade = 0;
    private boolean isFrozen = false; //account

    /**
     * User constructor
     * @param password the user's password
     */
    public UserAccount(String password) {
        super(password);
    }

    /**
     * Gets the wishlist
     * @return a list of items inside the wishlist
     */
    public List<Item> getWishlist() {
        return wishlist;
    }

    /**
     * Sets the wishlist
     * @param wishlist wishlist
     */
    public void setWishlist(List<Item> wishlist) {
        this.wishlist = wishlist;
    }

    /**
     * Add an item to the wishlist
     * @param i Item
     */
    public void addToWishlist (Item i){
        wishlist.add(i);
    }

    /**
     * Removes item from the wishlist
     * @param i Item
     */
    public void removeFromWishlist(Item i){
        wishlist.remove(i);
    }

    /**
     * Add an item to the inventory
     * @param i Item
     */
    public void addToInventory(Item i){
        inventory.add(i);
    }

    /**
     * Remove an item from the inventory
     * @param i Item
     */
    public void removeFromInventory(Item i){
        inventory.remove(i);
    }

    /**
     * Gets the inventory
     * @return the list of items in inventory
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Sets the inventory
     * @param inventory inventory
     */
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    /**
     * Returns true iff the user has the item in their inventory.
     * @param item The item to check
     * @return true iff the user owns the item
     */
    public boolean ownsItem(Item item) {
        boolean owns = false;
        for (Item myItem : inventory) {
            if (item.getItemID().equals(myItem.getItemID())) {
                owns = true;
                break;
            }
        }
        return owns;
    }

    /**
     * Returns true iff the user has the item in their wishlist
     * @param item The item to check
     * @return true iff the user wants the item
     */
    public boolean wantsItem(Item item) {
        boolean wants = false;
        for (Item myItem : wishlist) {
            if (item.getItemID().equals(myItem.getItemID())) {
                wants = true;
                break;
            }
        }
        return wants;
    }

    /**
     * Gets the difference between the number of borrowed items and lent items
     * @return the number of item that were over borrowed
     */
    public int getOverBorrowed() {
        return overBorrowed;
    }

    /**
     * Sets the number of over borrowed items
     * @param overBorrowed number of over borrowed items
     */
    public void setOverBorrowed(int overBorrowed) {
        this.overBorrowed = overBorrowed;
    }

    /**
     * Gets the number of trades the user made
     * @return the number of trades
     */
    public int getNumTrade() {
        return numTrade;
    }

    /**
     * Sets the number of trades
     * @param numTrade number of trades
     */
    public void setNumTrade(int numTrade) {
        this.numTrade = numTrade;
    }

    /**
     * Gets the number of incomplete trades
     * @return the number of incomplete trades
     */
    public int getIncompleteTrade() {
        return incompleteTrade;
    }

    /**
     * Sets the number of incomplete trades
     * @param incompleteTrade number of incomplete trades
     */
    public void setIncompleteTrade(int incompleteTrade) {
        this.incompleteTrade = incompleteTrade;
    }

    /**
     * Gets if the account is frozen
     * @return true if account is frozen, else false
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * Freeze or unfreeze an account
     * @param freeze account frozen status
     */
    public void setFrozen(Boolean freeze)
    {
        this.isFrozen = freeze;
    }
}


