package Adapter;

import Entities.Item;
import Entities.ItemInventory;
import Entities.Trade;
import Entities.UserAccount;
import Usecase.UserManager;

import java.util.ArrayList;
import java.util.List;


public class UserMenuPresenter {

    private List<String> addRemoveItemsMsg = new ArrayList<>();

    /**
     * Constructor for UserMenuPresenter
     */
    public UserMenuPresenter(){
    }

    /**
     * Check if has next message
     * @return true iff it has next message
     */
    protected boolean hasNext(){
        return addRemoveItemsMsg.iterator().hasNext();
    }

    /**
     * Get the next message
     * @return next available message
     */
    protected String next(){
        return addRemoveItemsMsg.iterator().next();
    }

    /**
     * Print an error message to the screen when removing an Item goes wrong.
     */
    protected void removeItemError(){
        System.out.println("Fail to remove the item.\nPlease check to make sure the item id is entered correctly");
    }

    /**
     * Print an error message to the screen when requesting to add an Item to the user inventory goes wrong.
     */
    protected void requestAddError(){
        System.out.println("Fail to request adding the item to your inventory.\nPlease make sure the item name and description is entered correctly");
    }

    /**
     * Print an error message to the screen when requesting to add an to the wish list goes wrong.
     */
    protected void addWishListError(){
        System.out.println("Fail to request adding the item to your wish list. \nPlease make sure the item id is entered correctly");
    }

    /**
     * Print the wish list of the UserAccount with userID.
     * @param um the UserManager of all user accounts in the program, can look up the UserAccount with userID.
     * @param userID the id of the UserAccount whose wish list is to be presented.
     */
    protected void presentWishList(UserManager um, Integer userID){
        System.out.println("Wish List:" + um.getUser(userID).getWishlist() + "\n ");
    }

    /**
     * Prints the user inventory
     * @param um the UserManager of all user accounts in the program, can look up the UserAccount with userID.
     * @param currentUser the user account
     */
    protected void presentUserInventory(UserManager um, UserAccount currentUser){
        System.out.println("User inventory:" + currentUser.getInventory());
    }

    /**
     * Prints the system inventory
     * @param itemInventory list of all items
     * @param currentUser the user account
     * @return boolean if the system contains the item
     */
    protected boolean presentSystemInventory(ItemInventory itemInventory, UserAccount currentUser){
        boolean containsItems = false;
        List<Item> availableItems = itemInventory.getApprovedItems();
        if (availableItems.size() == 0) {
            System.out.println("There are no items available.\n");
        } else {
            containsItems = true;
            System.out.println("The following items are available for trade:");
            for (Item item : availableItems) {
                if (!item.getOwnerID().equals(currentUser.getUserID())) {
                    System.out.println(item.getItemName() + ": " + item.getDescription() +
                            " (ID: " + item.getItemID() + ")");
                }
            }
        }
        return containsItems;
    }

    /**
     * Present the ids of the 3 most frequent traders.
     * @param traderIDs the IDs of the top 3 most frequent traders.
     */
    protected void presentFrequentTraders(List<Integer> traderIDs){
        System.out.println(traderIDs);
    }

    /**
     * Present the 3 most recent trades.
     * @param recentTrades a list of the 3 most recent trades.
     */
    protected void presentRecentTrades(List<Trade> recentTrades){
        System.out.println(recentTrades);
    }

    /**
     * Print Main Menu
     */
    protected void presentMainMenu() {
        String mainMenu =
                "Main Menu: \n" +
                    "  Select one of the following options or type 'exit' to return to the former menu:\n" +
                        "\t 1. Proceed to your inventory\n" +
                        "\t 2. Proceed to the system Inventory\n" +
                        "\t 3. Proceed to Wish List\n" +
                        "\t 4. Get recent traded items\n" +
                        "\t 5. Get top 3 frequent traders\n";
        System.out.println(mainMenu);
        // 1: enter User inventory menu, 2: enter System inventory menu, 3: enter wish list menu, 4: print recent traded items on screen, 5: print the top 3 frequent traders on screen.
    }

    /**
     * Print User Inventory Menu
     */
    protected void presentUserInventoryMenu() {
        String userInventoryMenu =
                "User Inventory Menu: \n"+
                        "  Select one of the following options or type 'exit' to return to the former menu: \n" +
                        "\t 1. Request an item to be approved.\n" +
                        "\t 2. Remove an item from your inventory.";
        System.out.println(userInventoryMenu);
        //if choose 1, print: please enter the following information of the requested item: \n name: \n description:"
        // if choose 2, print: please enter the id of the item
    }

    /**
     * Print User Inventory Menu Selection 1
     */
    protected void presentAddItemPrompt() {
        String userInvMenuSelect1 =
                "Please enter the following information of the requested item: \n" +
                        "\t name, description: ";
        System.out.println(userInvMenuSelect1);
    }

    /**
     * Print User Inventory Menu Selection 2
     */
    protected void presentDeleteItemPrompt() {
        System.out.println("Please enter the id of the item");
    }

    protected void itemAddedMessage() {
        System.out.println("Item was added to your inventory.");
    }

    /**
     * Print Wish List Menu
     */
    protected void presentWishListMenu(){
        String wishListMenu =
                "Wish List Menu: \n" +
                        "\t Select 1 to remove an item from your wish list " +
                        "\n \t Select 2 to add an item to your wish list" +
                        "\n \t type 'exit' to return to the former menu.";
        System.out.println(wishListMenu);
    }

    /**
     * Response to wish list menu select 1
     */
    protected void presentRemoveWishlistID(){
        System.out.println("Please enter the id of the item");
    }

    // after press 2, print: please enter the id of the item
    /**
     * Response to wish list menu select 2
     */
    protected void presentWishListMenuSelect2(){
        System.out.println("Please enter the id of the item");
    }

    /**
     * Print System Inventory Menu
     */
    protected void presentSystemInventoryMenu(){
        String systemInventoryMenu =
                "System Inventory Menu: \n" +
                        "\t Select 1 to add an item to your wish list or type 'exit' to return to the former menu.";
        System.out.println(systemInventoryMenu);
        //after select 1, print: please enter the id of the item
    }

    /**
     * Response to system inventory menu select 1
     */
    protected void presentAddWishlistID(){
        System.out.println("Please enter the id of the item");
    }

    /**
     * Print an error message when reading input goes wrong.
     */
    protected void readInputError(){
        System.out.println("Something went wrong while reading in your input.");
    }

    /**
     * Print an error message when the entered itemID cannot be parsed as an integer.
     */
    protected void itemIDFormatError(){
        System.out.println("Item id's should only include numbers, please enter it correctly.");
    }

    /**
     * Print an error message when the user doesn't enter the name and description of an item in the correct format.
     */
    protected void itemInfoFormatError(){
        System.out.println("Please enter the item information in the 'name, description' format");
    }

    /**
     * Print an confirmation message when an item is successfully added to it's own wishlist
     */
    protected void itemSuccessfullyAddedToWishlist(){
        System.out.println("The item is successfully added to your wish list!");
    }

    /**
     * Print a confirmation message when an item is successfully removed from it's own wishlist
     */
    protected void itemSuccessfullyRemovedFromWishlist(){
        System.out.println("The item is successfully removed from your wish list!");
    }

    /**
     * Print a message denying to add to wish list when the user already owns the item
     */
    protected void addToWishlistDenied(){
        System.out.println("Cannot add item to wish list because you own the item!");
    }

    /**
     * Print an error message when the item doesnt exist
     */
    protected void itemError(){
        System.out.println("The entered item ID doesn't exist!");
    }

}
