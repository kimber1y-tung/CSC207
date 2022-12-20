package Usecase;

import Entities.*;

import java.time.LocalDateTime;

public class TradeManager {

    private UserManager userManager;
    private ItemInventory itemInventory;
    private TradeLogManager tradeLogManager;
    private TradeValidator tradeValidator;
    private int meetingEditLimit;

    /***
     * The constructor
     * @param userManager UserManager object
     * @param itemInventory ItemInventory object
     * @param tradeLogManager TradeLogManager object
     * @param tradeValidator TradeValidator object
     * @param meetingEditLimit integer
     */
    public TradeManager(UserManager userManager, ItemInventory itemInventory, TradeLogManager tradeLogManager,
                        TradeValidator tradeValidator, int meetingEditLimit) {
        this.userManager = userManager;
        this.itemInventory = itemInventory;
        this.tradeLogManager = tradeLogManager;
        this.tradeValidator = tradeValidator;
        this.meetingEditLimit = meetingEditLimit;
    }


    /**
     * Changes the trade's status and moves it to the correct place in the TradeLogManager's statusDictionary.
     * @param trade the trade to be updated
     * @param status the new status for the trade
     */
    public void updateStatus(Trade trade, String status) {
        tradeLogManager.updateStatus(trade, status);
        trade.setStatus(status);
    }

    /**
     * Returns the trade's status.
     * @param trade The trade
     * @return The trade's status
     */
    public String getStatus(Trade trade) {
        return trade.getStatus();
    }


    /**
     * Creates a OneWayTrade with status "requested" and adds it to the TradeLogManager.
     * @param itemID the ID of the requested Item
     * @param lenderID the ID of the user who owns the requested Item
     * @param borrowerID the ID of the user requesting the Trade
     * @param isPermanent whether the Trade is permanent or not
     * @return true iff the trade was successfully created
     */
    public Trade requestTrade(int itemID, int lenderID, int borrowerID, boolean isPermanent) {
        OneWayTrade newTrade = new OneWayTrade(itemID, lenderID, borrowerID);
        newTrade.setIsPermanent(isPermanent);
        if (tradeValidator.validateTrade(newTrade)) {
            newTrade.setStatus("requested");
            return newTrade;
        } else {
            newTrade.setStatus("invalid");
            return newTrade;
        }
    }

    /**
     * Creates a TwoWayTrade with status "requested" and adds it to the TradeLogManager.
     * @param item1 the ID of the requested item
     * @param item2 the ID of the item being offered
     * @param user1ID the ID of the user who owns the requested item
     * @param user2ID the ID of the user requesting the trade
     * @param isPermanent whether the trade is permanent or not
     * @return true iff the trade was successfully created
     */
    public Trade requestTrade(int item1, int item2, int user1ID, int user2ID, boolean isPermanent) {
        TwoWayTrade newTrade = new TwoWayTrade(item1, item2, user1ID, user2ID);
        newTrade.setIsPermanent(isPermanent);
        if (tradeValidator.validateTrade(newTrade)) {
            newTrade.setStatus("requested");
        } else {
            newTrade.setStatus("invalid");
        }
        return newTrade;

    }

    /**
     * Accepts a requested Trade by updating its status to "pending".
     * @param trade the trade to be accepted
     */
    public void acceptTrade(Trade trade) {
        updateStatus(trade, "pending");
    }


    /**
     * Declines a requested trade by updating its status to "declined".
     * @param trade the trade to be declined
     */
    public void declineTrade(Trade trade) {
        updateStatus(trade, "declined");
    }


    /** Confirms a trade by updating its status to "open". It should be called when the Trade's meeting
     * time and place have been confirmed.
     * @param trade the trade to be confirmed
     */
    public void confirmTrade(Trade trade) {
        updateStatus(trade, "open");
        trade.setLastEditorID(0);
    }


    /** Completes a trade by updating its status to "complete". Updates the users' inventory and
     * wishlist. Increments each users number of trades. If the trade is one way, increments overBorrowed for each
     * user.
     * @param trade the trade to be completed
     */
    public void completeTrade(Trade trade) {
        updateStatus(trade, "complete");
        incrementUserVariables(trade);
        if (!trade.getIsPermanent()) {
            transferItems(trade);
            updateItemOwners(trade);
        } else {
            deleteItems(trade);
        }
    }

    /* Helper method for completeTrade that transfers each item from its old owner to its new owner. */
    private void transferItems(Trade trade){
        UserAccount user1 = userManager.getUser(trade.getUser1ID());
        UserAccount user2 = userManager.getUser(trade.getUser2ID());
        if (trade instanceof OneWayTrade) {
            Item item = itemInventory.getItemWithID(((OneWayTrade) trade).getItemID());
            userManager.transferItem(item, user1, user2);
        } else {
            Item item1 = itemInventory.getItemWithID(((TwoWayTrade) trade).getItem1ID());
            Item item2 = itemInventory.getItemWithID(((TwoWayTrade) trade).getItem2ID());
            userManager.transferItem(item1, user1, user2);
            userManager.transferItem(item2, user2, user1);
        }
    }

    /*
    Helper method for completeTrade that deletes items from the system inventory and users' inventories and wishlists.
     */
    private void deleteItems(Trade trade){
        UserAccount user1 = userManager.getUser(trade.getUser1ID());
        UserAccount user2 = userManager.getUser(trade.getUser2ID());
        if (trade instanceof OneWayTrade) {
            Item item = itemInventory.getItemWithID(((OneWayTrade) trade).getItemID());
            user1.removeFromInventory(item);
            user2.removeFromWishlist(item);
            itemInventory.removeFromApproved(item);
        } else {
            Item item1 = itemInventory.getItemWithID(((TwoWayTrade) trade).getItem1ID());
            Item item2 = itemInventory.getItemWithID(((TwoWayTrade) trade).getItem2ID());
            user1.removeFromInventory(item1);
            user2.removeFromInventory(item2);
            user1.removeFromWishlist(item2);
            user2.removeFromWishlist(item1);
            itemInventory.removeFromApproved(item1);
            itemInventory.removeFromApproved(item2);

        }
    }

    /* Helper method for completeTrade that updates each item in the trade with its new owner's ID. */
    private void updateItemOwners(Trade trade) {
        if (trade instanceof OneWayTrade) {
            Item item = itemInventory.getItemWithID(((OneWayTrade) trade).getItemID());
            item.setOwnerID(trade.getUser2ID());
        } else {
            Item item1 = itemInventory.getItemWithID(((TwoWayTrade) trade).getItem1ID());
            Item item2 = itemInventory.getItemWithID(((TwoWayTrade) trade).getItem2ID());
            item1.setOwnerID(trade.getUser2ID());
            item2.setOwnerID(trade.getUser1ID());
        }

    }

    /*
    Helper method for completeTrade which updates the variables in the UserAccounts. Increases the number of trades
    * for each user. If the trade is one way, increases overborrowed for the borrower and decreases it for the lender.
    */
    private void incrementUserVariables(Trade trade) {
        userManager.incrementNumTrade(trade.getUser1ID());
        userManager.incrementNumTrade(trade.getUser2ID());

        if (trade instanceof OneWayTrade) {
            userManager.changeOverborrowed(trade.getUser1ID(), -1);
            userManager.changeOverborrowed(trade.getUser1ID(), 1);
        }

    }

    public Trade createReturnHelper(Trade trade) {
        if (trade instanceof OneWayTrade) {
            return new OneWayTrade(((OneWayTrade) trade).getItemID(), trade.getUser2ID(), trade.getUser1ID());
        } else {
            return new TwoWayTrade(((TwoWayTrade) trade).getItem1ID(), ((TwoWayTrade) trade).getItem2ID(), trade.getUser2ID(),
                    trade.getUser1ID());
        }
    }

    /**
    Creates a return trade for one month later than the original trade. Reverses the users that took
    part in the initial trade, so they should receive the items that they originally gave. Keeps the same location.
     @param trade the original trade
     @return the return trade
     */
    public Trade createReturnTrade(Trade trade) {
        Trade secondTrade = createReturnHelper(trade);

        secondTrade.setStatus("open");

        // keep same location
        secondTrade.setLocation(trade.getLocation());

        // set new time for the old time plus the duration
        secondTrade.setTime(trade.getTime().plusMonths(1));

        return secondTrade;
    }


    /**
     * Marks a Trade as incomplete by updating its status to incomplete. Increments each users incomplete trades.
     * @param trade the trade being marked as incomplete
     */
    public void incompleteTrade(Trade trade) {
        UserAccount user1 = userManager.getUser(trade.getUser1ID());
        UserAccount user2 = userManager.getUser(trade.getUser2ID());
        user1.setIncompleteTrade(user1.getIncompleteTrade() + 1);
        user2.setIncompleteTrade(user2.getIncompleteTrade() + 1);
        updateStatus(trade, "incomplete");
    }

    /**
     * Edit the location and time for a meeting provided that the person who edits it is not the last editor.
     * returns an integer to indicate the result of the meeting.
     * @param location the meeting location to perform the trade
     * @param time the time to perform the trade
     * @param tradeToEdit the Trade to edit
     * @param editorID integer the ID of the editor
     * @return 2 if the edit is successful.
     *         1 if the edit failed because it's not the current editor's turn.
     *         0 if the number of edits is already greater than or equal to the limit and the trade is still pending.
     */
    public Integer editMeeting(String location, LocalDateTime time, Trade tradeToEdit, int editorID) {
        if (tradeToEdit.getNumEdits() < meetingEditLimit && !(tradeToEdit.getLastEditorID().equals(editorID)) &&
                tradeToEdit.getStatus().equals("pending")) {
            tradeToEdit.setLocation(location);
            tradeToEdit.setTime(time);
            tradeToEdit.setLastEditorID(editorID);
            tradeToEdit.setNumEdits(tradeToEdit.getNumEdits() + 1);
            return 2;
        } else if (tradeToEdit.getNumEdits() >= meetingEditLimit && tradeToEdit.getStatus().equals("pending")) {
            tradeLogManager.removeTrade(tradeToEdit);
            return 0;
        } else {
            return 1;
        }
    }

    /**
     *
     * @param trade the trade to be confirmed
     * @return item item id of item being traded or requested
     */
    public Item getItem(OneWayTrade trade) {
        return itemInventory.getItemWithID(trade.getItemID());
    }

    /**
     *
     * @param trade the trade to be confirmed
     * @return item item id of item being requested
     */
    public Item getItem1(TwoWayTrade trade){
        return itemInventory.getItemWithID(trade.getItem1ID());
    }

    /***
     *
     * @param trade Trade object
     * @return item id of item being offered
     */
    public Item getItem2(TwoWayTrade trade){
        return itemInventory.getItemWithID(trade.getItem2ID());
    }
}