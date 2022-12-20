package Adapter;

import Entities.*;
import Usecase.UserManager;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TradeMenuPresenter {
    UserAccount currentUser;
    UserManager userManager;
    ItemInventory itemInventory;



    public TradeMenuPresenter(UserAccount currentUser, UserManager userManager, ItemInventory itemInventory) {
        this.userManager = userManager;
        this.currentUser = currentUser;
        this.itemInventory = itemInventory;
    }

    /**
     * Presents the options related to creating and modifying trades.
     */
    protected void tradeOptions() {
        String tradeMenu =
                "Please select an option or type 'exit':\n" +
                        "\t 1. View available items\n" +
                        "\t 2. View trade requests\n" +
                        "\t 3. View my trades\n";
        System.out.println(tradeMenu);
    }

    /**
     * Prints each item in the system along with its ID. Returns true iff at least one item exists in the system.
     * @param itemInventory the ItemInventory object which stores all items in the system.
     * @return true iff at least one item exists in the system.
     */
    protected boolean printItemInventory(ItemInventory itemInventory){
        List<Item> availableItems = itemInventory.getApprovedItems();
        if (availableItems.size() == 0) {
            System.out.println("There are no items available. Exiting now.\n");
            return false;
        }
        System.out.println("The following items are available for trade:");
        for (Item item : availableItems) {
            if (!item.getOwnerID().equals(currentUser.getUserID())){
                System.out.println(item.getItemName() + ": " + item.getDescription() +
                        " (ID: " + item.getItemID() + ")");
            }
        }
        System.out.println("Enter the ID of an item to request. This will add the item to your wishlist if it is not " +
                "already there. Type 'exit' to exit.");
        return true;
    }

    /**
     * Prints all items that appear in both the current user's inventory and the otherUser's wishlist. Prompts the user
     * to offer one of these items or request a one way trade. If there are no items in common, prompts the user
     * to request a one way trade. Returns true iff there is at least one item in common.
     * @param otherUser the UserAccount whose wishlist will be checked
     * @return true iff there is at least one item that appears in both the current user's inventory and the otherUser's
     * wishlist.
     */
    protected boolean viewItemsToOffer(UserAccount otherUser) {
        List<Item> desiredItems = new ArrayList<>();
        for (Item myItem : currentUser.getInventory()) {
            if (otherUser.wantsItem(myItem)) {
                desiredItems.add(myItem);
            }
        }
        if (desiredItems.size() == 0) {
            System.out.println("You have no items the owner is interested in. Type '1' to request a one way trade or " +
                    "type 'exit'.\n");
            return false;
        }
        System.out.println("The owner of this item may be interested in these items you have:");
        for (Item myItem : desiredItems) {
            System.out.println(myItem.getItemName() + ": " + myItem.getDescription() +
                    " (ID: " + myItem.getItemID() + ")");
        }
        System.out.println("\nPlease select an option or type 'exit':\n" +
                "1. Offer an item\n" +
                "2. Request a one way trade\n");
        return true;
    }

    /**
     * Presents a message prompting the user to enter the ID of an item to offer.
     */
    protected void enterItemOfferPrompt() {
        String IDPrompt =
                "Please enter the ID of the item you want to offer: ";
        System.out.println(IDPrompt);
    }

    /**
     * Presents a menu asking the user to select either permanent or temporary for a new trade.
     */
    protected void selectPermanentPrompt() {
        String permPrompt = "" +
                "Would you like to request a permanent or temporary trade?\n" +
                "1. Permanent\n" +
                "2. Temporary\n" +
                "Type 'exit' to cancel\n";
        System.out.println(permPrompt);
    }

    /**
     * Presents a message confirming that a trade has been successfull requested.
     */
    protected void requestedMessage() {
        System.out.println("Trade has been requested.");
    }

    /**
     * Presents a list of all trade requests associated with a user along with each trade's ID. Prompts the user to
     * enter the ID of a trade to respond to. Returns true iff there is at least one trade request in the list.
     * @return true iff there is at least one trade request in the list.
     */
    protected boolean printTradeRequests() {
        List<Trade> tradeRequests = userManager.checkTradeRequests(currentUser);
        if (tradeRequests.size() == 0) {
            System.out.println("You have no trade requests. Exiting now.");
            return false;
        }
        for (Trade trade : tradeRequests) {
            printTrade(trade);
        }
        System.out.println("Enter the ID of a trade to respond to or type 'exit'.");
        return true;
    }

    /**
     * Presents a menu asking the user to either accept or decline a trade.
     */
    protected void acceptDeclinePrompt() {
        String acceptDeclinePrompt = "1. Accept this trade \n" +
                "2. Decline this trade";
        System.out.println(acceptDeclinePrompt);
    }

    /**
     * Presents a message confirming that a trade was successfully accepted.
     */
    protected void tradeAcceptedMessage() {
        String tradeAcceptedMessage = "The trade was successfully accepted.";
        System.out.println(tradeAcceptedMessage);
    }

    /**
     * Presents a message confirming that a trade was successfully declined.
     */
    protected void tradeDeclinedMessage() {
        String tradeDeclinedMessage = "The trade was successfully declined.";
        System.out.println(tradeDeclinedMessage);

    }

    /**
     * Presents a list of trades along with their ID, meeting time and location, and a message stating what actions
     * can be taken to advance the trade. Returns true iff there is at least one trade in the list.
     * @param myTrades the list of trades to print
     * @return true iff there is at least one trade in the list.
     */
    protected boolean printCurrentTrades(List<Trade> myTrades) {
        if (myTrades.size() == 0) {
            System.out.println("You have no current trades. Exiting now.");
            return false;
        }
        System.out.println("Your current trades: ");
        for (Trade trade: myTrades) {
            String timeString;
            LocalDateTime time = trade.getTime();
            if (time == null) {
                timeString = "not set";
            } else {
                timeString = time.toString();
            }
            String locationString;
            String location = trade.getLocation();
            if (location == null) {
                locationString = "not set";
            }
            else {
                locationString = location;
            }
            printTrade(trade);
            System.out.println("Meeting time: " + timeString + "\nMeeting location: " + locationString);
            if (trade.getStatus().equals("pending")) {
                System.out.println("Accept or edit the meeting to advance this trade.");
            } else if (trade.getStatus().equals("open") && !trade.getLastEditorID().equals(currentUser.getUserID())) {
                System.out.println("This trade can be marked complete or incomplete.");
            } else if (trade.getStatus().equals("open") && trade.getLastEditorID().equals(currentUser.getUserID())) {
                System.out.println("Waiting for partner to confirm this trade is complete.");
            }
        }
        System.out.println("Enter the ID of a trade to modify or type 'exit'.");
        return true;
    }

    /**
     * Prints a trade, including its ID, a brief description, and whether it is permanent or temporary.
     * @param trade the trade to print
     */
    protected void printTrade(Trade trade) {
        if (trade instanceof OneWayTrade) {
            Item item = itemInventory.getItemWithID(((OneWayTrade) trade).getItemID());
            System.out.println("Trade ID " + trade.getTradeID() + ": " +
                    "User " + trade.getUser2ID() + " would like to borrow " + item.getItemName() + " from user " +
                    trade.getUser1ID() + ". Permanent: " + trade.getIsPermanent());
        } else if (trade instanceof TwoWayTrade) {
            Item item1 = itemInventory.getItemWithID(((TwoWayTrade) trade).getItem1ID());
            Item item2 = itemInventory.getItemWithID(((TwoWayTrade) trade).getItem2ID());
            System.out.println("Trade ID " + trade.getTradeID() + ": " +
                    "User " + trade.getUser2ID() + " would like to trade their " + item2.getItemName() +
                    " for User " + trade.getUser1ID() + "'s " + item1.getItemName() + ". " +
                    "Permanent: " + trade.getIsPermanent());
        }
    }

    /**
     * Presents a list of options for how a trade may be modified.
     */
    protected void modifyTradeMenu() {
        System.out.println("What would you like to do to this trade??\n" +
                "1. Edit the meeting location and time\n" +
                "2. Accept the meeting location and time\n" +
                "3. Mark the trade as complete\n" +
                "4. Mark the trade as incomplete\n");
    }

    /**
     * Presents a message confirming that a trade's meeting time and location have been successfully confirmed.
     */
    protected void confirmTradeSuccess() {
        System.out.println("The trade was successfully confirmed - meet your trading partner at the agreed time " +
                "and location.");
    }

    /**
     * Returns an array of prompts to present to the user to get a year, month, day, hour, and minute.
     * @return an array of prompts to present to the user to get a year, month, day, hour, and minute.
     */
    protected String[] meetingTimePrompts(){
        String[] prompts = new String[] {"Enter the year (YYYY): ", "Enter the month (MM): ", "Enter the day (DD): ",
                "Enter the hour (HH): ", "Enter the minute (mm): "};
        return prompts;
    }

    /**
     * Presents a message asking the user to enter a new meeting location.
     */
    protected void meetingLocationPrompt() {
        System.out.println("Enter the new meeting location: ");
    }

    /**
     * Presents a message confirming that the meeting was edited succcessfully.
     */
    protected void meetingEditSuccess() {
        System.out.println("The meeting was edited successfully - please wait for your partner to accept or edit.");
    }

    /**
     * Present a message to inform the user that it's not his/her turn to edit the meeting.
     */
    protected void notEditMeetingTurn(){
        System.out.println("The meeting edit failed because it is not your turn to edit it.");
    }

    /**
     * Present a message to inform the user that the trade is automatically cancelled because
     * it has been edited too many times.
     */
    protected void exceedingEditLimit(){
        System.out.println("Your trade is automatically cancelled " +
                "because you and your partner changed the meeting location/time too many times.");
    }

    /**
     * Presents a message confirming that the user has marked a trade completed and must wait for their partner to
     * confirm.
     */
    protected void firstTraderCompleteMessage() {
        System.out.println("You have marked the trade complete; now wait for your partner to confirm.");
    }

    /**
     * Presents a message confirming that the trade was successfully completed.
     */
    protected void completeTradeSuccess(){
        System.out.println("Trade has successfully been completed.");
    }

    /**
     * Presents a message confirming that the trade was successfully marked incomplete.
     */
    protected void incompleteTradeSuccess() {
        System.out.println("The trade was successfully marked incomplete.");
    }

    /**
     * Presents a generic error message.
     */
    protected void genericError() {
        System.out.println("Something went wrong.");
    }

    /**
     * Presents an error message indicating a file error.
     */
    protected void fileError() {
        System.out.println("Something went wrong while saving to file.");
    }

    /**
     * Presents an error message for when the user enters an invalid option.
     */
    protected void invalidOptionError() {
        System.out.println("Please enter a valid option.");
    }

    /**
     * Presents an error message for when the user marks a trade complete or incomplete that is not eligible.
     */
    protected void tradeUnconfirmedError() {
        System.out.println("This trade is not yet eligible to be marked complete or incomplete.");
    }

    /**
     * Presents an error message for when the user attempts to confirm a trade that is not eligible.
     */
    protected void tradeConfirmError() {
        System.out.println("This trade is not eligible to be confirmed.");
    }

    /**
     * Presents an error message for when the user enters an invalid ID.
     */
    protected void invalidIDError() {
        String invalidIDMessage = "The ID is invalid; please enter a valid ID.";
        System.out.println(invalidIDMessage);
    }

    /**
     * Presents an error message for when the user attempts to edit a trade when it is not their turn.
     */
    protected void wrongEditorError() {
        System.out.println("Please wait until your trading partner confirms.");
    }

    /**
     * Presents an error message for when the user attempts to modify a trade that has been marked incomplete.
     */
    protected void incompleteTradeError() {
        System.out.println("This trade has already been marked incomplete.");
    }

    /**
     * Presents an error message for when the user attempts to accept a meeting that has not been suggested.
     */
    protected void noMeetingError() {
        System.out.println("Please suggest a meeting time and location first.");
    }

    /**
     * Presents an error message saying the input was invalid, and prompts the user to try again.
     */
    protected void invalidInputError() {
        System.out.println("Invalid input, try again: ");
    }
}
