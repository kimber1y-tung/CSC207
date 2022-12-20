package Adapter;

import Entities.*;
import Usecase.TradeLogManager;
import Usecase.TradeManager;
import Usecase.UserManager;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TradeController {
    private UserAccount currentUser;
    private TradeManager tradeManager;
    private TradeLogManager tradeLogManager;
    private ItemInventory itemInventory;
    private UserManager userManager;
    private TradeMenuPresenter tradeMenuPresenter;
    private ReadWriteData readWriter;
    private UpdateDeleteData updaterDeleter;


    /**
     *
     * @param currentUser the current user's UserAccount
     * @param tradeManager the tradeManager object which contains methods related to creating and modifying trades
     * @param tradeLogManager the tradeLogManager object which stores all Trades
     * @param itemInventory the itemInventory object which stores all Items
     * @param userManager the userManager object which stores all UserAccounts
     */
    public TradeController(UserAccount currentUser, TradeManager tradeManager, TradeLogManager tradeLogManager,
                           ItemInventory itemInventory, UserManager userManager) {
        this.currentUser = currentUser;
        this.tradeManager = tradeManager;
        this.tradeLogManager = tradeLogManager;
        this.itemInventory = itemInventory;
        this.userManager = userManager;
        this.tradeMenuPresenter = new TradeMenuPresenter(currentUser, userManager, itemInventory);
        this.readWriter = new ReadWriteData();
        this.updaterDeleter = new UpdateDeleteData();


    }

    /*  Writes the trade's new status to the storage file. */
    private boolean updateTradeStatusFile(Trade trade, String status) {
        try {
            updaterDeleter.updateTrade(currentUser.getUserID(), trade.getTradeID(), 5, status);
            return true;
        } catch (NullPointerException | IOException e) {
            tradeMenuPresenter.fileError();
            return false;
        }
    }



    /*
    Runs when the user selects the option to offer an item as part of a trade. Prompts the user to select permanent or
    temporary, then creates the TwoWayTrade. The user may type 'exit' to exit.
    */
    private void offerItem(Integer itemRequestedID, Integer ownerID) {
        boolean tradeComplete = false;
        Scanner tradeScanner = new Scanner(System.in);
        tradeMenuPresenter.enterItemOfferPrompt();
        String input = tradeScanner.nextLine();
        try {
            int myItem = Integer.parseInt(input);
            Item item = itemInventory.getItemWithID(myItem);
            if (!currentUser.getInventory().contains(item)) {
                tradeMenuPresenter.invalidIDError();
                return;
            }

            while (!input.equals("exit") && !tradeComplete) {
                tradeMenuPresenter.selectPermanentPrompt();
                input = tradeScanner.nextLine();
                while (!input.equals("exit") && !tradeComplete) {
                    switch (input) {
                        case "1": //trade is permanent
                            tradeComplete = true;
                            if (requestTrade(itemRequestedID, myItem, ownerID, currentUser.getUserID(), true)) {
                                tradeMenuPresenter.requestedMessage();
                            }
                            break;
                        case "2": // trade is temporary
                            tradeComplete = true;
                            if (requestTrade(itemRequestedID, myItem, ownerID, currentUser.getUserID(), false)) {
                                tradeMenuPresenter.requestedMessage();
                            }
                            break;
                        }
                    }
                }
            }
            catch (NumberFormatException e) {
                tradeMenuPresenter.invalidIDError();
            }
    }

    /*
    Runs when the user selects the option to request a one way trade. Prompts the user to select permanent or
    temporary, then creates the OneWayTrade. The user may type 'exit' to exit.
     */
    private void requestOneWay(Integer itemRequestedID, Integer ownerID) {
        boolean tradeComplete = false;
        Scanner tradeScanner = new Scanner(System.in);
        tradeMenuPresenter.selectPermanentPrompt();
        String input = tradeScanner.nextLine();
        while (!input.equals("exit") && !tradeComplete) {
            switch (input) {
                case "1": //trade is permanent
                    if (requestTrade(itemRequestedID, ownerID, currentUser.getUserID(), true)) {
                        tradeMenuPresenter.requestedMessage();
                    }
                        tradeComplete = true;
                        break;

                case "2": // trade is temporary
                    if (requestTrade(itemRequestedID, ownerID, currentUser.getUserID(), false)) {
                        tradeMenuPresenter.requestedMessage();
                    }
                        tradeComplete = true;
                        break;

            }
        }
    }

    /*
    Creates a OneWayTrade with status 'requested', adds it to the TradeLogManager, and adds it to the storage file.
    Returns true iff this is successful.
     */
    private boolean requestTrade(int itemID, int lenderID, int borrowerID,
                                 boolean isPermanent) {
        try {
            Trade newTrade = tradeManager.requestTrade(itemID, lenderID, borrowerID, isPermanent);
            if (tradeManager.getStatus(newTrade).equals("invalid")) {
                System.out.println("Trade is invalid.");
                return false;
            }

            int permanentInt = 0;
            if (newTrade.getIsPermanent()) {
                permanentInt = 1;
            }
            Integer tradeID = readWriter.insertTrade(lenderID, borrowerID, itemID, 0, permanentInt, newTrade.getStatus(),
                    "location not set", 0, 0);
            newTrade.setTradeID(tradeID);

            tradeLogManager.addTrade(tradeID, newTrade);
        } catch (IOException | NullPointerException e) {
            System.out.println("Trade failed due to file error; try again.");
            return false;
        }

        return true;
    }

    /*
    Creates a TwoWayTrade with status 'requested', adds it to the TradeLogManager, and adds it to the storage file.
    Returns true iff this is successful.
     */
    private boolean requestTrade(int item1, int item2, int user1ID, int user2ID,
                                 boolean isPermanent) {
        try {
            Trade newTrade = tradeManager.requestTrade(item1, item2, user1ID, user2ID, isPermanent);
            if (tradeManager.getStatus(newTrade).equals("invalid")) {
                return false;
            }
            int permanentInt = 0;
            if (newTrade.getIsPermanent()) {
                permanentInt = 1;
            }
            Integer tradeID = readWriter.insertTrade(user1ID, user2ID, item1, item2, permanentInt,
                    newTrade.getStatus(), "location not set", 0, 0);
            newTrade.setTradeID(tradeID);
            tradeLogManager.addTrade(tradeID, newTrade);
        } catch (IOException | NullPointerException e) {
            System.out.println("Trade failed due to file error; try again.");
            return false;
        }
        return true;
    }

    /*
    Runs when the user selects the option to request an item from the available items list. Displays a list of items, if
    any, that the current user owns which their potential trading partner wants. Prompts the user to offer one of these
    items or request a one way trade.
    */
    private void requestItemMenu(Integer ownerID, Integer itemRequestedID) {
        UserAccount owner = userManager.getUser(ownerID);
        Scanner tradeScanner = new Scanner(System.in);
        boolean taskComplete = false;
        if (tradeMenuPresenter.viewItemsToOffer(owner)) {
            String input = tradeScanner.nextLine();
            while (!input.equals("exit") && !taskComplete) {
                switch (input) {
                    case "1": //Offer an item
                        offerItem(itemRequestedID, ownerID);
                        taskComplete = true;
                        break;
                    case "2": //Request a one way trade
                        requestOneWay(itemRequestedID, ownerID);
                        taskComplete = true;
                        break;
                }
            }
        } else {
            String input = tradeScanner.nextLine();
            while (!input.equals("exit") && !taskComplete) {
                if (input.equals("1")) {
                    requestOneWay(itemRequestedID, ownerID);
                    taskComplete = true;
                }
            }
        }
    }


    /**
     * Runs when the user selects the option to view all available items. Displays each item with its ID. The user is
     * then prompted to select an item ID, and adds the item to their wishlist if it is not already there. Then
     * presents the item request menu. The user may type 'exit' to exit.
     */
    private void viewAvailableItemsMenu() {
        if (!tradeMenuPresenter.printItemInventory(itemInventory)) {
            return;
        }
        boolean taskComplete = false;
        Scanner tradeScanner = new Scanner(System.in);
        String input = tradeScanner.nextLine();
        while (!input.equals("exit") && !taskComplete) {
            try {
                Integer itemRequestedID = Integer.parseInt(input);
                Item item = itemInventory.getItemWithID(itemRequestedID);
                if (!currentUser.wantsItem(item)) {
                    currentUser.addToWishlist(item);
                    updaterDeleter.addItemToUserItems(itemRequestedID, currentUser.getUserID(), true);
                }
                Integer ownerID = item.getOwnerID();
                requestItemMenu(ownerID, itemRequestedID);
                taskComplete = true;
            } catch (NumberFormatException e) {
                tradeMenuPresenter.invalidOptionError();
            } catch (NullPointerException e) {
                tradeMenuPresenter.invalidIDError();
            } catch (IOException e) {
                tradeMenuPresenter.fileError();
            }
        if (!tradeMenuPresenter.printItemInventory(itemInventory)) {
            return;
        }
        input = tradeScanner.nextLine();

        }
    }



    /* Changes a trade's status from 'requested' to 'pending'. Updates the new status in the storage file. */
    private void acceptTrade(Trade trade) {
        tradeManager.acceptTrade(trade);
        if (updateTradeStatusFile(trade, "pending")) {
            tradeMenuPresenter.tradeAcceptedMessage();
        }
    }

    /* Changes a trade's status from 'requested' to 'declined'. Updates the new status in the storage file. */
    private void declineTrade(Trade trade) {
        tradeManager.declineTrade(trade);
        if (updateTradeStatusFile(trade, "declined")) {
            tradeMenuPresenter.tradeDeclinedMessage();
        }
    }

    /* Runs when the user selects the option to respond to a requested trade. Prompts to user to either accept or
    decline, then accepts or declines the trade. The user may type 'exit' to exit. */
    private void acceptDeclineTradeRun(Trade trade) {
        Scanner tradeScanner = new Scanner(System.in);
        tradeMenuPresenter.acceptDeclinePrompt();
        String input = tradeScanner.nextLine();
        boolean taskComplete = false;
        while (!input.equals("exit") && !taskComplete) {
            switch (input) {
                case "1":
                    acceptTrade(trade);
                    taskComplete = true;
                    break;
                case "2":
                    declineTrade(trade);
                    taskComplete = true;
                    break;
                default:
                    tradeMenuPresenter.invalidOptionError();
                    input = tradeScanner.nextLine();
            }
        }
    }

    /* Returns true iff the trade has status 'requested' and was requested of this user by another user. */
    private boolean isMyTradeRequest(Trade trade) {
        return trade != null && trade.getUser1ID().equals(currentUser.getUserID()) &&
                trade.getStatus().equals("requested");
    }

    /* Runs when the user selects the option to view trade requests. Prints each trade request associated with this
    * user and its tradeID, then prompts to user to select an ID. The user may type 'exit' to exit. */
    private void viewTradeRequestsMenu() {
        if (!tradeMenuPresenter.printTradeRequests()){
            return;
        }
        Scanner tradeScanner = new Scanner(System.in);
        String input = tradeScanner.nextLine(); //get trade ID
        boolean taskComplete = false;
        while (!input.equals("exit") && !taskComplete) {
            try {
                int myTradeID = Integer.parseInt(input);
                Trade trade = tradeLogManager.getTradeWithID(myTradeID);
                while (!isMyTradeRequest(trade)) {
                    tradeMenuPresenter.invalidIDError();
                    input = tradeScanner.nextLine();
                    myTradeID = Integer.parseInt(input);
                    trade = tradeLogManager.getTradeWithID(myTradeID);
                }
                acceptDeclineTradeRun(trade);
                taskComplete = true;
            } catch (NumberFormatException e){
                tradeMenuPresenter.invalidIDError();
                input = tradeScanner.nextLine();
            }
        }
    }

    /* Returns a list of the current user's pending and open trades. */
    private List<Trade> getCurrentTrades() {
        List<Trade> myTrades = userManager.getTrades(currentUser);
        List<Trade> currentTrades = new ArrayList<>();
        for (Trade trade: myTrades) {
            if (trade.getStatus().equals("pending") || trade.getStatus().equals("open")) {
                currentTrades.add(trade);
            }
        }
        return currentTrades;
    }

    /*
Prompts the user to enter a year, month, day, hour, and minute, then returns a corresponding LocalDateTime object.
*/
    private LocalDateTime getMeetingTime() throws NumberFormatException, DateTimeException {
        Scanner timeScanner = new Scanner(System.in);
        String[] prompts = tradeMenuPresenter.meetingTimePrompts();
        List<Integer> timeList = new ArrayList<>();
        for (String prompt : prompts) {
            System.out.println(prompt);
            timeList.add(Integer.parseInt(timeScanner.nextLine()));
        }
        return LocalDateTime.of(timeList.get(0), timeList.get(1), timeList.get(2), timeList.get(3), timeList.get(4));
    }

    /*
    Runs when the user selects the option to modify the meeting location and time for the trade. Prompts the user to
    enter a new location and time, then updates the trade and writes the new data to the storage file. The user may
    type 'exit' to exit.
     */
    private void editMeeting(Trade trade) {
        if (trade.getLastEditorID().equals(currentUser.getUserID())) {
            tradeMenuPresenter.wrongEditorError();
        } else {
            try {
                Scanner tradeScanner = new Scanner(System.in);
                LocalDateTime time = getMeetingTime();
                tradeMenuPresenter.meetingLocationPrompt();
                String location = tradeScanner.nextLine();
                Integer editResult = tradeManager.editMeeting(location, time, trade, currentUser.getUserID());
                if(editResult.equals(2)){
                    updaterDeleter.updateTrade(currentUser.getUserID(), trade.getTradeID(), 8, time.toString());
                    updaterDeleter.updateTrade(currentUser.getUserID(), trade.getTradeID(), 6, location);
                    updaterDeleter.updateTrade(currentUser.getUserID(), trade.getTradeID(), 12,
                            String.valueOf(trade.getNumEdits()));
                    tradeMenuPresenter.meetingEditSuccess();
                } else if(editResult.equals(1)){
                    tradeMenuPresenter.notEditMeetingTurn();
                } else if(editResult.equals(0)){
                    tradeMenuPresenter.exceedingEditLimit();
                    updaterDeleter.removeItem(trade.getTradeID(), 3, currentUser.getUserID());
                }
            } catch (DateTimeException | NumberFormatException e) {
                tradeMenuPresenter.genericError();
            } catch (IOException e2) {
                tradeMenuPresenter.fileError();
            }
        }


    }
    /*
    Changes a trade's status to open. Will not run if the meeting has not been edited at least once or
    if the current user was the most recent editor of the meeting. Updates the trade's new status in the storage file.
    */
    private void confirmTrade(Trade trade) {
        if (trade.getLastEditorID().equals(currentUser.getUserID())) {
            tradeMenuPresenter.wrongEditorError();
        } else if (trade.getLocation().equals("location not set")) {
            tradeMenuPresenter.noMeetingError();
        } else if (!trade.getStatus().equals("pending")){
            tradeMenuPresenter.tradeConfirmError();
        } else {
            tradeManager.confirmTrade(trade);
            if (updateTradeStatusFile(trade, "open")) {
                tradeMenuPresenter.confirmTradeSuccess();
            }
        }
    }


    /*
    Updates the file storage of each item in a completed trade. Removes items from the wishlist of each user. If the
    trade is permanent, removes items from the inventory of each user and the item inventory. If the trade is temporary,
    transfers items from the user giving item to the user receiving it.
     */
    private void updateTradeItems(Trade trade) {
        try {
            if (trade instanceof OneWayTrade) {
                try {
                    updaterDeleter.removeItem(((OneWayTrade) trade).getItemID(), 0, trade.getUser2ID());
                } catch (IOException | IndexOutOfBoundsException ignored) {
                    // if the item is not in the user's wishlist, it doesn't need to be removed
                }
                if (trade.getIsPermanent()) {
                    updaterDeleter.removeItem(((OneWayTrade) trade).getItemID(), 1, trade.getUser1ID());
                } else {
                    updaterDeleter.updateItem(((OneWayTrade) trade).getItemID(), trade.getUser2ID(), 1);
                }
            } else if (trade instanceof TwoWayTrade) {
                try {
                    updaterDeleter.removeItem(((TwoWayTrade) trade).getItem1ID(), 0, trade.getUser2ID());
                    updaterDeleter.removeItem(((TwoWayTrade) trade).getItem2ID(), 0, trade.getUser1ID());
                } catch (IOException | IndexOutOfBoundsException ignored){
                    // if the item is not in the user's wishlist, it doesn't need to be removed
                }
                if (trade.getIsPermanent()) {
                    updaterDeleter.removeItem(((TwoWayTrade) trade).getItem1ID(), 1, trade.getUser1ID());
                    updaterDeleter.removeItem(((TwoWayTrade) trade).getItem2ID(), 1, trade.getUser2ID());
                } else {
                    updaterDeleter.updateItem(((TwoWayTrade) trade).getItem1ID(), trade.getUser2ID(), 1);
                    updaterDeleter.updateItem(((TwoWayTrade) trade).getItem2ID(), trade.getUser1ID(), 1);
                }
            }
        } catch (IOException e) {
            tradeMenuPresenter.fileError();
        }
    }

    private void createReturnTrade(Trade trade) throws IOException {
        int item1ID;
        int item2ID;
        Trade secondTrade = tradeManager.createReturnTrade(trade);
        if (secondTrade instanceof OneWayTrade) {
            item1ID = ((OneWayTrade) secondTrade).getItemID();
            item2ID = 0;
        } else {
            item1ID = ((TwoWayTrade) secondTrade).getItem1ID();
            item2ID = ((TwoWayTrade) secondTrade).getItem2ID();

        }
        Integer tradeID = readWriter.insertTrade(secondTrade.getUser1ID(), secondTrade.getUser2ID(), item1ID, item2ID,
                1, "open", secondTrade.getLocation(), 0, 0);
        secondTrade.setTradeID(tradeID);
        updaterDeleter.updateTrade(currentUser.getUserID(), tradeID, 8, secondTrade.getTime().toString());
        tradeLogManager.addTrade(tradeID, trade);

    }

    /*
    Completes a trade. If the current user is the first to mark the trade complete, changes the trade's lastEditor to
    the current user's ID. If the other user has already marked the trade complete, then this method changes its status
    to "complete" and updates the trade in the file storage. Will not run if the meeting was never confirmed, this user
    already marked it complete, or the other user marked the trade incomplete.
     */
    private void completeTrade(Trade trade) {
        try {
            if (!trade.getStatus().equals("open")) {
                tradeMenuPresenter.tradeUnconfirmedError();
            } else if (trade.getLastEditorID() == 0) { //trade has not been completed by either partner
                trade.setLastEditorID(currentUser.getUserID());
                updaterDeleter.updateTrade(currentUser.getUserID(), trade.getTradeID(), 11 ,
                        Integer.toString(currentUser.getUserID()));
                tradeMenuPresenter.firstTraderCompleteMessage();
            } else if (trade.getLastEditorID().equals(currentUser.getUserID())) { //trade already completed by current user
                tradeMenuPresenter.wrongEditorError();
            } else if (trade.getStatus().equals("incomplete")) { //other user marked incomplete
                tradeMenuPresenter.incompleteTradeError();
            } else { //trade already completed by other user - can now complete
                tradeManager.completeTrade(trade);
                updateTradeStatusFile(trade, "complete");
                updateTradeItems(trade);
                if (!trade.getIsPermanent()) {
                    createReturnTrade(trade);
                }
                tradeMenuPresenter.completeTradeSuccess();
                // numTrades and overBorrowed should be updated automatically by the gateway method
            }
        } catch (IOException e) {
            tradeMenuPresenter.fileError();
        }
    }

    /* Changes the trade's status to 'incomplete' and updates the trade in the storage file. This is used when either
    * user did not show up for the meeting. */
    private void incompleteTrade(Trade trade) {
        tradeManager.incompleteTrade(trade);
        //incompleteTrades should be updated automatically by the gateway method
        if (updateTradeStatusFile(trade, "incomplete")) {
            tradeMenuPresenter.incompleteTradeSuccess();
        }
    }

    /*
    Runs when the user selects the option to modify a trade. Prompts the user to select what to do to the trade. User
    may time 'exit' to exit.
    */
    private void modifyTradeMenu(Trade trade) {
        tradeMenuPresenter.modifyTradeMenu();
        Scanner tradeScanner = new Scanner(System.in);
        String input = tradeScanner.nextLine();
        boolean taskComplete = false;
        while (!input.equals("exit") && !taskComplete){
            switch (input) {
            case "1": // Edit meeting location and time
                editMeeting(trade);
                taskComplete = true;
                break;
            case "2": // Accept meeting location and time
                confirmTrade(trade);
                taskComplete = true;
                break;
            case "3": //
                completeTrade(trade);
                taskComplete = true;
                break;
            case "4":
                incompleteTrade(trade);
                taskComplete = true;
                break;
            default:
                tradeMenuPresenter.invalidOptionError();
                input = tradeScanner.nextLine();
        }
        }
    }

    /*
    Runs when the user selects the option to view their current trades. Prints each trade with its ID, then prompts
    the user to enter an ID. The user may type 'exit' to exit.
     */
    private void viewCurrentTradesMenu() {
        List<Trade> myTrades = getCurrentTrades();
        if (!tradeMenuPresenter.printCurrentTrades(myTrades)) {
            return;
        }
        Scanner tradeScanner = new Scanner(System.in);
        String input = tradeScanner.nextLine();
        boolean taskComplete = false;

        while (!input.equals("exit") && !taskComplete) {
            Trade trade = null;
            try {
                Integer myTradeID = Integer.parseInt(input);
                trade = tradeLogManager.getTradeWithID(myTradeID);
            } catch (NumberFormatException e) {
                tradeMenuPresenter.invalidIDError();
                input = tradeScanner.nextLine();
            }
                if (trade == null || (!trade.getUser1ID().equals(currentUser.getUserID()) && !trade.getUser2ID().equals(currentUser.getUserID())) ||
                        (!trade.getStatus().equals("pending") && !trade.getStatus().equals("open"))) {
                    tradeMenuPresenter.invalidIDError();
                    input = tradeScanner.nextLine();
                } else {
                    modifyTradeMenu(trade);
                    taskComplete = true;
                }



        }

    }

    /**
     * Presents user menu options relating to trades and prompts the user to select an option. From this menu, the user
     * can:
     * 1. view all the items available in the system, then select an item to request a trade
     * 2. view trades that others have requested of them, and accept or decline these trades
     * 3. view their trades that have been accepted but not yet completed, edit or accept a meeting time and location
     * for these trades, and mark them as complete or incomplete.
     * The user may type 'exit' to return to the previous menu.
     */
    public void tradeRun() {
        Scanner tradeScanner = new Scanner(System.in);
        tradeMenuPresenter.tradeOptions();
        String input = tradeScanner.nextLine();
        while (!input.equals("exit")) {
            switch(input) {
                case "1":
                    viewAvailableItemsMenu();
                    break;
                case "2":
                    viewTradeRequestsMenu();
                    break;
                case "3":
                    viewCurrentTradesMenu();
                    break;
                default:
                    tradeMenuPresenter.invalidOptionError();
            }
            tradeMenuPresenter.tradeOptions();
            input = tradeScanner.nextLine();
        }
    }

}