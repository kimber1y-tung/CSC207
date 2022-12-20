package Adapter;

import Entities.Item;
import Entities.ItemInventory;
import Entities.UserAccount;
import Usecase.UserEnforcer;
import Usecase.UserManager;

import java.util.List;
import java.util.ArrayList;

public class AdminMenuPresenter {

//    private List<String> adminMenuMsg = new ArrayList<>();
    private ItemInventory itemInventory;
    private UserManager userManager;
    private UserEnforcer userEnforcer;

    /**
     * The constructor.
     * @param itemInventory ItemInventory object
     * @param userManager UserManager object
     * @param userEnforcer UserEnforcer object.
     */
    public AdminMenuPresenter(ItemInventory itemInventory, UserManager userManager, UserEnforcer userEnforcer) {
        this.itemInventory = itemInventory;
        this.userManager = userManager;
        this.userEnforcer = userEnforcer;
    }

    /**
     * Print the text prompts for the admin main menu.
     */
    protected void presentMainMenu() {
        String mainMenu =
                "Main Menu: \n" +
                        "  Select one of the following options or type 'exit' to return to the former menu:\n" +
                        "\t 1. View user requested items pending approval. \n" +
                        "\t 2. View users flagged by the system to be frozen.\n " +
                        "\t 3. Unfreeze a user.\n" +
                        "\t 4. Edit trading limits.\n" +
                        "\t 5. Reset weekly trade count.\n" +
                        "\t 6. Add a new administrator account.";
        System.out.println(mainMenu);
    }


    /**
     * Print a list of user items pending approval with their IDs. Returns true iff there is at least one item
     * pending approval.
     * @return true iff there is at least one item pending approval
     */
    protected boolean presentPendingItems(){
        List<Item> pendingItems = itemInventory.getPendingItems();
        if (pendingItems.size() == 0) {
            System.out.println("There are no items waiting for approval. Exiting now.\n");
            return false;
        }
        System.out.println("The following items are waiting for approval:");
        for (Item item : pendingItems) {
            System.out.println(item.getItemName() + ": " + item.getDescription() +
                        " (ID: " + item.getItemID() + ")");
            }
        System.out.println("Enter the ID of an item to approve or deny or type 'exit'.");
        return true;
    }


    /**
     * Presents a menu for the admin to approve or deny the item
     */
    protected void presentApprovalMenu() {
        String approvalMenu = "To approve this request, select 1. To deny this request, select 2. To go back, type " +
                "'exit'.";
        System.out.print(approvalMenu);
    }


    /**
     * Print a list of users that the system has flagged to be frozen with their IDs. Returns true iff there is at least
     * one flagged user.
     * @return true iff there is at least one flagged user.
     */
    protected boolean presentFlaggedUsers() {
        boolean containsFlagged = false;
        List<Integer> flaggedUsers = userEnforcer.getFlaggedUsers();
        if (flaggedUsers.size() == 0) {
            System.out.println("There are no flagged users. Exiting now.");
        } else {
            containsFlagged = true;
            System.out.println("The following users have been flagged by the system:\n");
            for (Integer userID : flaggedUsers) {
                UserAccount user = userManager.getUser(userID);
                System.out.println("UserID: " + userID + "\nTrades this week: " + user.getNumTrade() + "\nOverborrowed: " +
                        user.getOverBorrowed() + "\nIncomplete trades: " + user.getIncompleteTrade());
            }
            System.out.println("Enter a userID to freeze the user, or type 'exit'.");
        }
        return containsFlagged;
    }

    /**
     * Shows a message to admin that the user's account has become frozen
     */
    protected void userFrozenSuccess() {
        System.out.println("User successfully frozen.");
    }

    /**
     * Check if there are frozen users, and print text prompt options.
     * @return true iff there are frozen users.
     */
    protected boolean presentFrozenUsers() {
        boolean containsFrozen = false;
        List<UserAccount> frozenUsers = new ArrayList<>();
        for (UserAccount user : userManager.getAllUsers()) {
            if (user.isFrozen()) {
                frozenUsers.add(user);
            }
        }
        if (frozenUsers.size() == 0) {
            System.out.println("There are no frozen users. Exiting now.");
        } else {
            containsFrozen = true;
            System.out.println("The following users are currently frozen:");
            for (UserAccount user : frozenUsers) {
                System.out.println(user.getUserID());
            }
            System.out.println("Enter a userID to unfreeze or type 'exit'.");
        }
        return containsFrozen;

    }

    /***
     * Shows a message to the admin that the user's account is unfrozen
     */
    protected void userUnfrozenSuccess() {
        System.out.println("User successfully unfrozen.");
    }


    /**
     * Presents a menu of different limits set for every user. There is number of meetings,
     * overborrow value, maximum weekly transactions, and number of incomplete trades
     */
    protected void presentLimitList() {
        System.out.println("Select a limit to edit:\n" +
                "\t1. Number of times a meeting may be edited\n" +
                "\t2. How many more items a user may borrow than lend\n" +
                "\t3. Maximum weekly transactions per user\n" +
                "\t4. Maximum incomplete trades per user");
    }

    /**
     * Prompt to edit the limit values
     * @return a string of limit values
     */
    protected String[] editLimitPrompts(){
        return new String[] {"Enter the meeting edit limit: ", "Enter the overborrow limit: ", "Enter the weekly trade limit: ",
                "Enter the incomplete trades limit: "};
    }

    /**
     * New admin account create a password
     */
    protected void newAdminPrompt() {
        System.out.println("Please enter a password for the new administrator account: ");
    }

    /**
     * Presents the autogenerated admin id
     * @param userID the admin's id
     */
    protected void newAdminIDMessage(Integer userID) {
        System.out.println("The new administrator's userID is " + userID);
    }


    /**
     * Shows error message if there was an issue writing to  file
     */
    protected void fileError() {
        System.out.println("Something went wrong while writing to the file.");
    }

    /**
     * Shows a error message that the input was invalid
     */
    protected void invalidInputError() {
        System.out.println("The input was invalid.");
    }

    /**
     * Shows a error message if the admin tries to freeze an user account
     * that has already been frozen
     * @param userID userId of the frozen account
     */
    protected void userAlreadyFrozenError(int userID) {System.out.println("The user" + userID + "is already frozen."); }

    /**
     * Shoes an error message if the admin tries to unfreeze an account that is already unfrozen
     * @param userID userId of the unfrozen account
     */
    protected void userAlreadyUnfrozenError(int userID) {System.out.println("The user" + userID + "is already frozen.");
    }

    /***
     * Shoes an error when there is an error with updating the data
     */
    public void updateDataError(){
        System.out.println("There is some error when updating the data of the program");
    }



}
