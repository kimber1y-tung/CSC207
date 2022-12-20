package Adapter;

import Entities.*;
import Usecase.*;

import java.io.IOException;
import java.util.*;

public class MainController {
    private final TradeManager tradeManager;
    private final UserManager userManager;
    private TradeLogManager tradeLogManager;
    private final UserEnforcer userEnforcer;
    private final ItemInventory itemInventory;
    private final ReadWriteData dataReaderWriter = new ReadWriteData();
    private final MainMenuPresenter mainMP = new MainMenuPresenter();
    private String firstRun;

    /**
     * Creates an instance MainController, which allows logging in, signing up, and accessing menus for users
     * and administrators.
     * @param firstRun a string representing whether the program is running for the first time or not. 0 indicates
     *                 it is running for the first time and 1 indicates it is not.
     */
    public MainController(String firstRun) {
        this.firstRun = firstRun;
        this.tradeLogManager = createTradeLogManager();
        this.userManager = createUserManager();
        this.itemInventory = createItemInventory(userManager);
        this.tradeManager = createTradeManager(userManager, itemInventory, tradeLogManager);
        this.userEnforcer = createUserEnforcer(userManager);
    }

    /*
    Reads all trades from the storage file and returns a new TradeLogManager to store them. Returns an empty
    TradeLogManager if there is no file or the file read fails.
     */
    private TradeLogManager createTradeLogManager() {
        try {
            TradeLog tradeLog = dataReaderWriter.getTradeLog();
            tradeLogManager = new TradeLogManager(tradeLog);
        } catch (IOException e) {
            if (!firstRun.equals("0")) {
                mainMP.readDataError(); // error message if file expected
            }
            tradeLogManager = new TradeLogManager(new TradeLog(new HashMap<>())); //creates empty trade log if file read fails or no file exists
        }
        return tradeLogManager;
    }

    /*
    Reads all users from the storage file and returns a new a UserManager to store them. Returns an empty UserManager if
    there is no file or the file read fails.
     */
    private UserManager createUserManager() {
        HashMap<Integer, UserAccount> idToUsers = new HashMap<>();
        UserManager um;
        try {
            List<Integer> allUserIDs = dataReaderWriter.getAllUserIDs();
            for (Integer id : allUserIDs) {
                idToUsers.put(id, dataReaderWriter.getUser(id));
            }
        } catch (IOException e) {
            if (!firstRun.equals("0")) {
                mainMP.readDataError(); // error message if file expected
            }
        }catch (NullPointerException e){
            mainMP.readDataError();
        }
        return new UserManager(idToUsers, tradeLogManager);
    }

    /*
    Collects all items from all users' inventories in the UserManager and returns a new ItemInventory to store them.
    Returns an empty ItemInventory if there are no items.
     */
    private ItemInventory createItemInventory(UserManager userManager) {
        ArrayList<Item> allItems = new ArrayList<>();
        for (UserAccount user : userManager.getAllUsers()) {
            allItems.addAll(user.getInventory());
        }
        return new ItemInventory(allItems);
    }

    /*
    Reads the meeting edit limit from the configuration file and returns a new TradeManager to manage trades.
     */
    private TradeManager createTradeManager(UserManager um, ItemInventory inv, TradeLogManager tlm) {
        TradeValidator tv = new TradeValidator(um, inv);
        Integer meetingEditLimit = 0;
       try {
           meetingEditLimit = dataReaderWriter.getTradeThresholds().get("meetingEditLimit");
       } catch (IOException e) {
           mainMP.readDataError();
       }
       return new TradeManager(um, inv, tlm, tv, meetingEditLimit);
    }

    /*
    Reads the overborrow limit, trade limit, and incomplete limit from the configuration file and returns a new
    UserEnforcer to enforce these limits.
     */
    private UserEnforcer createUserEnforcer(UserManager um){
        Integer overBorrowLimit = 0;
        Integer tradeLimit = 0;
        Integer incompleteLimit = 0;
        try{
            overBorrowLimit = dataReaderWriter.getTradeThresholds().get("overBorrowLimit");
            tradeLimit = dataReaderWriter.getTradeThresholds().get("tradeLimit");
            incompleteLimit = dataReaderWriter.getTradeThresholds().get("incompleteLimit");
        } catch(IOException e){
            mainMP.readDataError();
        }
        return new UserEnforcer(um, tradeLimit, incompleteLimit, overBorrowLimit);

    }

    /*
    Allows users to take various actions relating to trades and their accounts. Runs when a user logs in.
     */
    private void userMainMenuRun(MainMenuPresenter mainMenuPresenter, TradeController tradeController,
                                 UserMenuController userMenuController) {
        Scanner scanner = new Scanner(System.in);
        mainMenuPresenter.mainMenuPrompts();
        String input = scanner.nextLine();
        while (!input.equals("exit")) {
            switch (input) {
                case "1":
                    tradeController.tradeRun();
                    break;
                case "2":
                    userMenuController.userMenuRun();
                    break;
            }
            mainMenuPresenter.mainMenuPrompts();
            input = scanner.nextLine();
        }
    }

    /*
    Runs when the program is run for the first time. Initializes all storage files and creates the initial
    administrator account.
     */
    private void firstTimeRun() {
        FileInitializer fileInitializer = new FileInitializer();
        String[] fileNames = new String[] {"User", "Admin", "Password", "Trade", "Item", "Accounts", "Inventory",
                "Wishlist"};

        try {
            for (String fileName : fileNames) {
                fileInitializer.initializeFile(fileName);
            }
            ReadWriteData dataWriter = new ReadWriteData();
            dataWriter.insertAdmin("blerg"); // create initial admin account
        } catch (IOException e) {
            System.out.println("Something went wrong while initializing the files.");
        }
    }

    /**
     * Runs the trading system program, allowing users and administrators to log in and take any action that is allowed.
     * If the program is being run for the first time, initializes all storage files and creates the initial
     * administrator account.
     * If the program is not being run for the first time, reads all data from the storage files.
     * Prompts the user to login with a username and password and verifies their account type.
     * If the user is an administrator, creates an AdminController and directs the user to the administrator menu. If
     * the user is a regular user, creates a UserMenuController and a TradeController and directs the user to the user
     * menu.
     */
    public void mainRun() {

        if (firstRun.equals("0")) {
            firstTimeRun();
        }

        LoginSignUpController loginSignUpController = new LoginSignUpController();
        Integer userID = loginSignUpController.loginRun(userManager);
        if (userID == 0) {
            mainMP.loginError();
            return;
        }

        try {
            if (dataReaderWriter.getIsAdmin(userID)) { //user is an admin - create AdminController
                AdminController adminController = new AdminController(userManager, itemInventory, userEnforcer);
                adminController.adminRun();
            } else { //user is a regular user - create TradeController and UserMenuController
                UserAccount currentUser = userManager.getUser(userID);
                TradeController tradeController = new TradeController(currentUser, tradeManager, tradeLogManager,
                        itemInventory, userManager);
                UserMenuController userMenuController = new UserMenuController(currentUser, itemInventory, userManager);
                userMainMenuRun(mainMP, tradeController, userMenuController);
            }
        } catch (IOException e) {
            mainMP.readDataError();
        } catch (NullPointerException e){
            mainMP.signUpError();
        }
    }

}
