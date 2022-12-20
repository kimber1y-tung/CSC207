package Adapter;


import Entities.Item;
import Entities.ItemInventory;
import Entities.UserAccount;
import Usecase.UserEnforcer;
import Usecase.UserManager;

import java.io.IOException;
import java.util.*;


public class AdminController {
    private AdminMenuPresenter adminMenuPresenter;
    private UserManager userManager;
    private ItemInventory itemInventory;
    private UserEnforcer userEnforcer;
    private final UpdateDeleteData dataUpdater = new UpdateDeleteData();

    /***
     *
     * @param userManager stores all users and maps them to user id
     * @param itemInventory list of all items
     * @param userEnforcer tracks borrowing limit of user, and freezes, flag, unfreezes, or unflag the account
     */
    public AdminController(UserManager userManager, ItemInventory itemInventory, UserEnforcer userEnforcer) {
        this.userManager = userManager;
        this.itemInventory = itemInventory;
        this.userEnforcer = userEnforcer;
        this.adminMenuPresenter = new AdminMenuPresenter(itemInventory, userManager, userEnforcer);
    }

    /***
     * Admin can approves the item
     * @param item the item waiting to be approved
     */
    private void approveItem(Item item){
        try {
            item.approve();
            itemInventory.addToApproved(item);
            itemInventory.removeFromPending(item);
            dataUpdater.approveItem(item.getItemID());
        } catch (IOException e) {
            adminMenuPresenter.updateDataError();
        }
    }

    /***
     * Admin can deny the item
     * @param item the item waiting to be approved
     */
    private void denyItem(Item item) {
        itemInventory.removeFromPending(item);
        UserAccount owner = userManager.getUser(item.getOwnerID());
        owner.removeFromInventory(item);
        try {
            dataUpdater.removeItem(item.getItemID(), 1, item.getOwnerID());
            dataUpdater.removeItem(item.getItemID(), 2, item.getOwnerID());
        } catch (IOException e) {
            adminMenuPresenter.updateDataError();
        }

    }

    /***
     * Runs the menu of approving or denying the item
     * @param item the item waiting to be approved
     */
    private void approveDenyRun(Item item) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        boolean taskComplete = false;
        while (!input.equals("exit") && !taskComplete) {
            switch (input) {
                case "1":
                    approveItem(item);
                    taskComplete = true;
                    break;
                case "2":
                    denyItem(item);
                    taskComplete = true;
                    break;
                default:
                    adminMenuPresenter.invalidInputError();
                    input = scanner.nextLine();
                    break;
            }
        }
    }

    /**
     * Admin will review items in the pending items list
     */
    private void reviewPendingItems(){
        if (!adminMenuPresenter.presentPendingItems()) {
            return;
        }
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("exit")) {
            try {
                Integer itemID = Integer.parseInt(input);
                Item item = itemInventory.getItemWithID(itemID);
                adminMenuPresenter.presentApprovalMenu();
                approveDenyRun(item);
                if (!adminMenuPresenter.presentPendingItems()) {
                    return;
                } else {
                    input = scanner.nextLine();
                }
            } catch (NumberFormatException | NullPointerException e) {
                adminMenuPresenter.invalidInputError();
                input = scanner.nextLine();
            }
        }

    }


    /**
     * Admin goes through all flagged user and can freeze user
     * @param userAccount the user's account
     */
    private void freezeUser(UserAccount userAccount){
        if (userAccount.isFrozen() || !userEnforcer.isFlagged(userAccount)) {
            adminMenuPresenter.invalidInputError();
            if (userAccount.isFrozen()) {
                adminMenuPresenter.userAlreadyFrozenError(userAccount.getUserID());
            }
            return;
        }

        try {
            userEnforcer.freezeUser(userAccount);
            userEnforcer.unFlagUser(userAccount);
            dataUpdater.changeFreezeStatus(userAccount.getUserID());
            adminMenuPresenter.userFrozenSuccess();
        } catch (IOException e) {
            adminMenuPresenter.updateDataError();
        }
    }

    /**
     * Admin can unfreeze a user account
     * @param userAccount  the user's account
     */
    private void unfreezeUser(UserAccount userAccount){
        if (!userAccount.isFrozen()) {
            adminMenuPresenter.invalidInputError();
            adminMenuPresenter.userAlreadyUnfrozenError(userAccount.getUserID());
            return;
        }
        try {
            userEnforcer.unfreezeUser(userAccount);
            dataUpdater.changeFreezeStatus(userAccount.getUserID());
            adminMenuPresenter.userUnfrozenSuccess();
        } catch (IOException e) {
            adminMenuPresenter.updateDataError();
        }

    }


    /***
     * Admin reviews all the flagged users
     */
    private void reviewFlaggedUsers() {
        if (!adminMenuPresenter.presentFlaggedUsers()) {
            return;
        }
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("exit")) {
            try {
                Integer userID = Integer.parseInt(input);
                UserAccount user = userManager.getUser(userID);
                freezeUser(user);
            } catch (NumberFormatException | NullPointerException e) {
                adminMenuPresenter.invalidInputError();
            }
            if (!adminMenuPresenter.presentFlaggedUsers()) {
                return;
            } else {
                input = scanner.nextLine();
            }
        }

    }

    /***
     * Admin revies all the frozen user accounts
     */
    private void reviewFrozenUsers() {
        if (!adminMenuPresenter.presentFrozenUsers()) {
            return;
        }
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("exit")) {
            try {
                Integer userID = Integer.parseInt(input);
                UserAccount user = userManager.getUser(userID);
                unfreezeUser(user);
            } catch (NumberFormatException | NullPointerException e) {
                adminMenuPresenter.invalidInputError();
            }
            if (!adminMenuPresenter.presentFrozenUsers()) {
                return;
            } else {
                input = scanner.nextLine();
            }
        }
    }

    /**
     * From a config file admin reviews the number of time a meeting can be edited,
     * items the user can overborrow, trade limit, and incomplete trades
     */
    private void reviewLimits() {
        try {
            Scanner timeScanner = new Scanner(System.in);
            String[] prompts = adminMenuPresenter.editLimitPrompts();
            List<Integer> limitList = new ArrayList<>();
            for (String prompt : prompts) {
                System.out.println(prompt);
                limitList.add(Integer.parseInt(timeScanner.nextLine()));
            }
            ReadWriteData readWriter = new ReadWriteData();
            Map<String, Integer> limits = new HashMap<>();
            limits.put("meetingEditLimit", limitList.get(0));
            limits.put("overBorrowLimit", limitList.get(1));
            limits.put("tradeLimit", limitList.get(2));
            limits.put("incompleteLimit", limitList.get(3));
            readWriter.writeTradeThresholds(limits);
        } catch (IOException e) {
            adminMenuPresenter.fileError();
        }

    }


    /**
     * Admin resets user's number of trade back to zero each week
     */
    private void resetWeeklyTrades() {
        userEnforcer.resetNumTrades(userManager);
        try {
            for (UserAccount user : userManager.getAllUsers()) {
                dataUpdater.updateNumTrades(user.getUserID(), 0);
            }
        } catch (IOException e) {
            adminMenuPresenter.fileError();
        }
    }


    /**
     * Admin can add another admin account
     */
    private void addNewAdmin() {
        adminMenuPresenter.newAdminPrompt();
        Scanner scanner = new Scanner(System.in);
        String newPassword = scanner.nextLine();
        while (newPassword.length() == 0) {
            adminMenuPresenter.newAdminPrompt();
            newPassword = scanner.nextLine();
        }
        int userID = 0;
        ReadWriteData dataWriter = new ReadWriteData();
        try {
            userID = dataWriter.insertAdmin(newPassword);
            adminMenuPresenter.newAdminIDMessage(userID);
        } catch (IOException | NullPointerException e) {
            adminMenuPresenter.fileError();
        }
    }

    /**
     * Runs the Admin menu where they can approve items, freeze, and unfreeze users
     */
    public void adminRun() {
        Scanner adminScanner = new Scanner(System.in);
        adminMenuPresenter.presentMainMenu();
        String input = adminScanner.nextLine();
        while (!input.equals("exit")) {
            switch (input) {
                case "1":
                    reviewPendingItems();
                    break;
                case "2":
                    reviewFlaggedUsers();
                    break;
                case "3":
                    reviewFrozenUsers();
                    break;
                case "4":
                    reviewLimits();
                    break;
                case "5":
                    resetWeeklyTrades();
                    break;
                case "6":
                    addNewAdmin();
                    break;

            }
            adminMenuPresenter.presentMainMenu();
            input = adminScanner.nextLine();
        }
    }


}
