package Adapter;

import Entities.Item;
import Entities.ItemInventory;
import Entities.Trade;
import Entities.UserAccount;
import Usecase.UserManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

public class UserMenuController {
    private UserAccount currentUser;
    private ItemInventory itemInventory;
    private UserManager userManager;
    private final UserMenuPresenter userMP;
    private final ReadWriteData readerWriter;
    private final UpdateDeleteData editor;

    /**
     * Creates a UserMenuController associated with the current user.
     * @param currentUser the current user's UserAccount
     * @param itemInventory the ItemInventory object which stores all Items in the system
     * @param userManager the UserManager object which stores all UserAccounts in the system
     */
    public UserMenuController(UserAccount currentUser, ItemInventory itemInventory, UserManager userManager){
        this.userMP = new UserMenuPresenter();
        this.readerWriter = new ReadWriteData();
        this.editor = new UpdateDeleteData();
        this.currentUser = currentUser;
        this.itemInventory = itemInventory;
        this.userManager = userManager;
    }

    /**
     * Adds a new item with the name and description to the user's inventory.
     * Updates the storage files.
     * @param description the description of the item requesting to be added
     * @param name the name of the item requesting to be added
     */
    private void requestAddToMyInventory(String description, String name) {
        try {
            Integer id = readerWriter.insertItem(name, currentUser.getUserID(), description, false);
            Item newItem = new Item(currentUser.getUserID(), name, description);
            newItem.setItemID(id);
            List<Item> itemList = new ArrayList<>();
            itemList.add(newItem);
            readerWriter.insertInventory(currentUser.getUserID(), itemList);
            newItem.setItemID(id);
            itemInventory.addToPending(newItem);
            currentUser.addToInventory(newItem);

        } catch (IOException e) {
            userMP.requestAddError();
        }

    }

    /**
     * Allows the user to add an item to their inventory.
     * Prompts the user to enter a name and description, then creates
     * the item and adds it to the user's inventory and the system item inventory.
     */
    private void requestAddToInvRun(){
        Scanner scanner = new Scanner(System.in);
        userMP.presentAddItemPrompt();
        try {
            String input = scanner.nextLine();
            String[] nameDes = input.split(", ");
            requestAddToMyInventory(nameDes[1], nameDes[0]);
            userMP.itemAddedMessage();
        } catch(PatternSyntaxException | ArrayIndexOutOfBoundsException e){
            userMP.itemInfoFormatError();
        }
    }

    /**
     * Removes the Item with id idToRemove from the current user's inventory.
     * Removes the item from the system's item
     * inventory and any other users' wish lists. Updates the item storage files.
     * @param idToRemove the id of the item to be removed from it's inventory
     */
    private void removeFromUserInv(Integer idToRemove){
        try {
            Item toRemove = itemInventory.getItemWithID(idToRemove);
            currentUser.removeFromInventory(toRemove);
            if (toRemove.isApproved()) {
                itemInventory.removeFromApproved(toRemove);
            } else {
                itemInventory.removeFromPending(toRemove);
            }
            Integer userID = currentUser.getUserID();
            editor.removeItem(idToRemove, 1, userID); // remove from Inventory
            editor.removeItem(idToRemove, 2, userID); // remove from Items
            List<UserAccount> allUsers = userManager.getAllUsers();
            for(UserAccount userAccount: allUsers){
                if(userAccount.wantsItem(toRemove)){
                    userAccount.removeFromWishlist(toRemove);
                    editor.removeItem(idToRemove, 0, userAccount.getUserID());
                }
            }
        } catch (IOException | NullPointerException e) {
            userMP.removeItemError();
        }
    }

    /**
     * Allows the user to remove an item from their inventory. Prompts the user to enter the ID
     * of the item to remove, then removes it.
     */
    private void removeFromUserInvRun(){
        Scanner scanner = new Scanner(System.in);
        userMP.presentDeleteItemPrompt();
        try {
            String input = scanner.nextLine();
            Integer itemID = Integer.parseInt(input);
            removeFromUserInv(itemID);
        } catch (NumberFormatException f){
            userMP.itemIDFormatError();
        }
    }

    /**
     * Displays the user's inventory. Prompts the user to add or remove items if desired.
     */
    private void userInvMenuRun(){
        Scanner scanner = new Scanner(System.in);
        userMP.presentUserInventory(userManager, currentUser);
        userMP.presentUserInventoryMenu();
        String input = scanner.nextLine();
        while(!(input.equals("exit"))) {
            if (input.equals("1")) {
                requestAddToInvRun();
            } else if (input.equals("2")) {
                removeFromUserInvRun();
            }
            userMP.presentUserInventory(userManager, currentUser);
            userMP.presentUserInventoryMenu();
            input = scanner.nextLine();
        }

    }

    /**
     * Adds the item with id itemID to the current user's wishlist. Updates the storage file.
     * @param itemID the ID of the item being added to the user's wish list
     */
    private void addToMyWishList(Integer itemID){
        Item toWish;
        try {
            toWish = itemInventory.getItemWithID(itemID);
            if (toWish == null) {
                userMP.addWishListError();
                return;
            }
            currentUser.addToWishlist(toWish);
            editor.addItemToUserItems(itemID, currentUser.getUserID(), true);
        } catch (NullPointerException | IOException e1){
            userMP.addWishListError();
        }
    }

    /**
     * Allows the user to add an item to their wishlist. Prompts the user to enter the ID of the
     * item, then adds it to the user's wishlist and updates the storage file.
     * Does not add the item if it's already in the user's wishlist.
     */
    private void addToWishListRun(){
        userMP.presentAddWishlistID();
        try{
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("exit")){
                userMP.presentWishListMenu();
            }
            else {
                Integer idToAdd = Integer.parseInt(input);

                // check if the item ID exists
                if (itemInventory.getItemWithID(idToAdd) == null){
                    userMP.itemError();
                    userMP.presentWishListMenu();
                    return;
                }

                // check if the user already owns the item
                if (currentUser.ownsItem(itemInventory.getItemWithID(idToAdd))){
                    userMP.addToWishlistDenied();
                    userMP.presentWishListMenu();
                }

                // check if the user already has this item on their wishlist
                if (!currentUser.wantsItem(itemInventory.getItemWithID(idToAdd))) {
                    addToMyWishList(idToAdd);
                    userMP.itemSuccessfullyAddedToWishlist();
                    userMP.presentWishListMenu();
                }
            }
        } catch(NumberFormatException f){
            userMP.itemIDFormatError();
            userMP.presentWishListMenu();
        }
    }


    /**
     * Displays the system inventory. Prompts the user to add items to their wishlist if desired.
     */
    private void systemInvMenuRun(){
        Scanner scanner = new Scanner(System.in);
        if (userMP.presentSystemInventory(itemInventory, currentUser)) {
            userMP.presentSystemInventoryMenu();
            String input = scanner.nextLine();
            while(!(input.equals("exit"))){
                if(input.equals("1")){
                    addToWishListRun();
                }
                userMP.presentSystemInventory(itemInventory, currentUser);
                userMP.presentSystemInventoryMenu();
                input = scanner.nextLine();
            }
        }
    }

    /**
     * Removes the Item with ID idToRemove from the current user's wishlist. Updates the storage file.
     * @param idToRemove the ID of the item to be removed from the user's wish list
     */
    private void removeFromWishList(Integer idToRemove) {
        try {
            Item toRemove = itemInventory.getItemWithID(idToRemove);
            currentUser.removeFromWishlist(toRemove);
            editor.removeItem(idToRemove, 0, currentUser.getUserID());
        } catch (IOException | NullPointerException e){
            userMP.removeItemError();
        }
    }

    /**
     * Allows the user to remove an item from their wishlist. Prompts the user to enter the ID
     * of the item to remove, then removes it from their wishlist.
     */
    private void removeFromWishListRun(){
        userMP.presentRemoveWishlistID();
        try{
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (!input.equals("exit")) {
                Integer idToRemove = Integer.parseInt(input);

                // check if the item ID exists
                if (itemInventory.getItemWithID(idToRemove) == null){
                    userMP.itemError();
                    userMP.presentWishListMenu();
                    return;
                }

                removeFromWishList(idToRemove);
                userMP.itemSuccessfullyRemovedFromWishlist();
            }
            userMP.presentWishListMenu();
        } catch(NumberFormatException f){
            userMP.itemIDFormatError();
            userMP.presentWishListMenu();
        }
    }

    /**
     * Displays the current user's wishlist.
     * Prompts the user to remove or add an item from their wishlist if desired.
     */
    private void wishListMenuRun(){
        Scanner scanner = new Scanner(System.in);
        userMP.presentWishList(userManager, currentUser.getUserID());
        userMP.presentWishListMenu();
        String input = scanner.nextLine();
        while(!(input.equals("exit"))){
            if(input.equals("1")){
                removeFromWishListRun();
            }
            if(input.equals("2")){
                addToWishListRun();
            }
            input = scanner.nextLine();
        }
    }

    /**
     * Returns a list of the current user's three most recent trades, or fewer if they have not participated in three
     * trades.
     */
    private List<Trade> getRecentTrades(){
        return userManager.getRecentTrades(currentUser);
    }

    /**
     * Returns a list of the IDs of the top three most frequent trading partners of the current user, or fewer if they
     * have not traded with three users.
     */
    private List<Integer> getFrequentTraders(){
        return userManager.getTopTradingPartners(currentUser);
    }

    /**
     * Presents user menu options relating to the user's account and prompts the user to select an option. From this
     * menu, the user can:
     * 1. View their inventory
     * 2. View the system inventory
     * 3. View their wishlist
     * 4. View their most recent trades
     * 5. View their most frequent trading partners
     * The user may type 'exit' to return to the previous menu.
     */
    public void userMenuRun(){
        Scanner scanner = new Scanner(System.in);
        userMP.presentMainMenu();
        String input = scanner.nextLine();
        while (!(input.equals("exit"))){
            switch (input) {
                case "1":
                    userInvMenuRun();
                    break;
                case "2":
                    systemInvMenuRun();
                    break;
                case "3":
                    wishListMenuRun();
                    break;
                case "4":
                    userMP.presentRecentTrades(getRecentTrades());
                    break;
                case "5":
                    userMP.presentFrequentTraders(getFrequentTraders());
                    break;
            }
            userMP.presentMainMenu();
            input = scanner.nextLine();
        }
    }


}
