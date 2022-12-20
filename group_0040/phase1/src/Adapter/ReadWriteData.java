package Adapter;

import Entities.*;
import Usecase.Authenticator;
import com.opencsv.CSVWriter;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


public class ReadWriteData {
    /**
     * This is a helper method that writes data to files
     * @param filePath String value that represents the path of the file
     * @param data Array of type String containing the data itself
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     */
    private void writeData(String filePath, String[] data) throws IOException {
        File file = new File(filePath);
        FileWriter outputFile = new FileWriter(file, true);
        CSVWriter fileWriter = new CSVWriter(outputFile);
        fileWriter.writeNext(data);
        fileWriter.close();
        outputFile.close();
    }


    /**
     * Helper method that stores a password in the 'Passwords.csv' CSV file for a given userID
     * @param userID ID of the user
     * @param password the password associated with the userID
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    private void storePassword(int userID, String password) throws IOException, NullPointerException {
        Authenticator a = new Authenticator();
        String [] data = {Integer.toString(userID), a.passwordHash(password)};
        writeData("./Passwords.csv", data);
    }


    /**
     * Stores the itemID's in the 'Inventory.csv' CSV file for a certain userID
     * @param userID ID of the user for which the list of Items is being added
     * @param inventory List consisting of Item entities
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void insertInventory(int userID, List<Item> inventory) throws IOException, NullPointerException{
        String[] data;
        for (Item item : inventory) {
            data = new String[]{Integer.toString(item.getItemID()), Integer.toString(userID)};
            writeData("./Inventory.csv", data);
        }
    }


    /**
     * Stores the itemID's in the 'Wishlist.csv' CSV file for a certain userID
     * @param userID userID ID of the user for which the list of Items is being added
     * @param wishlist List consisting of Item entities
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void insertWishlist(int userID, List<Item> wishlist) throws IOException, NullPointerException{
        String[] data;
        for (Item item : wishlist) {
            data = new String[]{Integer.toString(item.getItemID()), Integer.toString(userID)};
            writeData("./Wishlist.csv", data);
        }
    }


    /**
     * Helper method that stores all users in the 'Accounts.csv' CSV file which includes UserAccount Entities and
     * AdminAccount Entities
     * @param isAdmin Represents if the account is a UserAccount or an AdminAccount (Value '1' represents Admin,
     *                Value '0' represents User)
     * @return Return the appropriate ID of the Account based on the data that exist in the file
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    private int insertAccount(int isAdmin) throws IOException, NullPointerException{
        String USER_ACCOUNT_FILE_PATH = "./Accounts.csv";
        int returnedID = findNextKey(USER_ACCOUNT_FILE_PATH);
        String[] data = {Integer.toString(returnedID), Integer.toString(isAdmin)};
        writeData(USER_ACCOUNT_FILE_PATH, data);
        return returnedID;
    }


    /**
     * Stores a given UserAccount Entity with its information in the 'Users.csv' CSV
     * @param password Represents the password of the User
     * @param overBorrowed Represents the number of over borrowed items the user has
     * @param numTrades Represents the number of complete trades the user was involved in
     * @param incompleteTrades Represents the number of incomplete trades the user was involved in
     * @param isFrozen Represents if the UserAccount entity is a frozen account or not (Value '1' represents frozen,
     *                 Value '0' represents unfrozen)
     * @param wishList User's wishlist which is a list of items that will be stored in the 'Wishlist.csv' file
     * @param inventory User's wishlist which is a List of items that will be stored in the 'Inventory.csv' file
     * @return The appropriate user ID of the given information
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected int insertUser(String password, int overBorrowed, int numTrades, int incompleteTrades,
                             int isFrozen, List<Item> wishList, List<Item> inventory) throws IOException,
            NullPointerException{
        int userID = insertAccount(0);
        String[] data = {Integer.toString(userID), Integer.toString(overBorrowed), Integer.toString(numTrades),
                Integer.toString(incompleteTrades), Integer.toString(isFrozen)};
        insertInventory(userID, inventory);
        insertWishlist(userID, wishList);
        writeData("./Users.csv", data);
        storePassword(userID, password);
        return userID;
    }

    /**
     * Stores a given AdminAccount Entity with its information in the 'Administrators.csv' CSV
     * @param password Represents the password of the administrator
     * @return the ID of the admin with the given password
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected int insertAdmin(String password) throws IOException, NullPointerException{
        int userID = insertAccount(1);
        String[] data = {Integer.toString(userID)};
        writeData("./Administrators.csv", data);
        storePassword(userID, password);
        return userID;
    }

    /**
     * Stores a given Item Entity with its information in the 'Items.csv' CSV file
     * @param itemName Represents the name of the item
     * @param ownerID the ID of the user who owns the item
     * @param description Represents the description of the item
     * @param approved represents if the item is approved or not
     * @return the itemID for the Item entity with the given specification
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected int insertItem(String itemName, int ownerID, String description, boolean approved) throws IOException,
            NullPointerException{
        String ITEMS_FILE_PATH = "./Items.csv";
        int approvedInteger = 0;
        if (approved){
            approvedInteger = 1;
        }
        String[] data;
        int itemID = findNextKey(ITEMS_FILE_PATH);
        data = new String[]{Integer.toString(itemID), Integer.toString(ownerID), itemName, description,
                Integer.toString(approvedInteger)};
        writeData(ITEMS_FILE_PATH, data);
        return itemID;
    }

    /**
     * Returns a list of items with the status approved
     * @return The list containing itemIDs of approved items
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     */
    protected List<Integer> findApprovedItems() throws IOException{
        List<Integer> approvedItems = new ArrayList<>();
        String[][] itemData= readDataToMatrix("./Items.csv");
        for(int x=1; x<itemData.length; x++){
            if (Integer.parseInt(removeChar(itemData[x][3])) == 1){
                approvedItems.add(Integer.parseInt(removeChar(itemData[x][0])));
            }
        }
        return approvedItems;
    }

    /**
     * Stores a given Trade Entity with its information in the 'Trades.csv' CSV file
     * Note: Every trade this method inserts is inserted with a trade_date which represents the time the
     * method is called, it is being done this way to match the way the code works
     * @param userID1 represents the ID of the first user
     * @param userID2 represents the ID of the second user
     * @param itemID1 represents the id of the the traded item that user with userID1 will trade to the user with
     *                userID2
     * @param itemID2 represents the id of the the traded item that user with userID2 will trade to the user with
     *                userID1 - 0 if this is a OneWayTrade
     * @param isPermanent represents whether the trade is permanent
     * @param status represents the status of the trade
     * @param location represents the location where the trade took place
     * @param lastEditedByID represents the ID of the last user who made an edit
     * @param numEdits represents the total number of edits made
     * @return the ID of the trade with the given trade specifications that are passed as parameters
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected int insertTrade(int userID1, int userID2, int itemID1, int itemID2, int isPermanent, String status,
                              String location, int lastEditedByID, int numEdits) throws IOException,
            NullPointerException{
        String TRADES_FILE_PATH = "./Trades.csv";
        Date date = new Date();
        int tradeID = findNextKey(TRADES_FILE_PATH);
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String[] data = {Integer.toString(tradeID), Integer.toString(userID1),
                Integer.toString(userID2), Integer.toString(itemID2), Integer.toString(itemID1),
                status, location, Integer.toString(isPermanent), String.valueOf(localDateTime),
                "", "0", String.valueOf(lastEditedByID), String.valueOf(numEdits)};
        writeData(TRADES_FILE_PATH, data);
        return tradeID;
    }


    /**
     * Helper method that reads the data from the given type of file and returns the data in it
     * @param file String representing the type of file the method will access
     * @return Return a 2D array containing all the data found in the file
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    private String[][] readDataToMatrix(String file) throws IOException, NullPointerException{
        FileReader fileReader = new FileReader(file);
        String row;
        int c=0;
        int counter = 0;
        BufferedReader csvReader = new BufferedReader(fileReader);
        while (csvReader.readLine() != null) {
            counter++;
        }
        FileReader fileReader2 = new FileReader(file);
        BufferedReader csvReader2 = new BufferedReader(fileReader2);
        String[][] data = new String[counter][];
        while ((row = csvReader2.readLine()) != null) {
            data[c] = row.split(",");
            c++;
        }
        csvReader.close();
        csvReader2.close();
        return data;
    }

    /**
     * Helper method that manipulates the string it receives as a parameter and removes the character '"' from the it
     * @param str The string which the method receives
     * @return Returns the string after removing the '"' character
     */
    private String removeChar(String str){
        return str.replace("\"","");
    }

    /**
     * Helper method that finds the appropriate object key based on the info that already exist in the CSV file
     * @param type Type of file the method will access
     * @return The appropriate object key (e.g. itemID, userID, adminUserID...etc)
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    private int findNextKey(String type) throws IOException, NullPointerException{
        String [][] fileData = readDataToMatrix(type);
        ArrayList<Integer> keys = new ArrayList<>();
        for (int x=1; x < fileData.length; x++){
            keys.add(Integer.parseInt(removeChar(fileData[x][0])));
        }
        int key=1;
        if(keys.size() > 0){
            key = keys.get(keys.size()-1) + 1;
        }
        return key;
    }


    /**
     * Iterates through the 'Wishlist' CSV file and returns a List that consists of entities of type Item based on the
     * given userID
     * @param userID The ID of the UserAccent
     * @return List of items that are part of the user's wishlist
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected List<Item> getWishlist(int userID) throws IOException, NullPointerException{
        String [][] wishlistData = readDataToMatrix("./Wishlist.csv");
        List<Item> returnWishlist = new ArrayList<>();
        for(int x=1; x<wishlistData.length; x++){
            if (Integer.parseInt(removeChar(wishlistData[x][1])) == userID){
                returnWishlist.add(getItem(Integer.parseInt(removeChar(wishlistData[x][0]))));
            }
        }
        return returnWishlist;
    }

    /**
     * Iterates through the 'Inventory' CSV file and returns a List that consists of entities of type Item based on the
     * given userID
     * @param userID The ID of the User
     * @return List of items that belong to a User
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected List<Item> getInventory(int userID) throws IOException, NullPointerException{
        String [][] inventoryData = readDataToMatrix("./Inventory.csv");
        List<Item> returnInventory = new ArrayList<>();
        for(int x=1; x<inventoryData.length; x++){
            if (Integer.parseInt(removeChar(inventoryData[x][1])) == userID){
                returnInventory.add(getItem(Integer.parseInt(removeChar(inventoryData[x][0]))));
            }
        }
        return returnInventory;
    }

    /**
     * Iterates through the 'Administrator' CSV file and returns a AdminAccount entity with its' appropriate information
     * using the given adminID
     * @param adminID The ID of the administrator
     * @return Entity of type AdminAccount with its appropriate information
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument or when the user is not found or
     *                              does not exist in the CSV file
     */
    protected AdminAccount getAdminAccount(int adminID) throws IOException, NullPointerException, ArrayIndexOutOfBoundsException{
        String [][] passwordData = readDataToMatrix("./Passwords.csv");
        String [][] userData = readDataToMatrix("./Administrators.csv");
        String password = null;
        AdminAccount adminToBeReturned = null;
        for (int x=1; x<passwordData.length; x++){
            if(Integer.parseInt(removeChar(passwordData[x][0])) == adminID){
                password = removeChar(passwordData[x][1]);
            }
        }
        for (int index=1; index<userData.length; index++){
            if(Integer.parseInt(removeChar(userData[index][0])) == adminID){
                adminToBeReturned = new AdminAccount(password);
                adminToBeReturned.setUserID(Integer.parseInt(removeChar(userData[index][0])));
            }
        }
        if (adminToBeReturned == null){
            throw new NullPointerException("Administrator User ID "+ adminID+ " Was Not Found");
        }
        return adminToBeReturned;
    }

    /**
     * Iterates through the 'User' CSV file and returns a UserAccount entity with its' appropriate information
     * using the given adminID
     * @param userID The ID of the User
     * @return Entity of type UserAccount with its appropriate information
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument or when the user is not found or
     *                              does not exist in the CSV file
     */
    protected UserAccount getUser(int userID)throws IOException, NullPointerException {
        String [][] userData = readDataToMatrix("./Users.csv");
        String [][] passwordData = readDataToMatrix("./Passwords.csv");
        String password = null;
        for (int x=1; x<passwordData.length; x++){
            if(Integer.parseInt(removeChar(passwordData[x][0])) == userID){
                password = removeChar(passwordData[x][1]);
            }
        }
        UserAccount userToBeReturned = null;
        for (int index=1; index<userData.length; index++){
            if(Integer.parseInt(removeChar(userData[index][0])) == userID){
                userToBeReturned = new UserAccount(password);
                userToBeReturned.setUserID(Integer.parseInt(removeChar(userData[index][0])));
                userToBeReturned.setOverBorrowed(Integer.parseInt(removeChar(userData[index][1])));
                userToBeReturned.setNumTrade(Integer.parseInt(removeChar(userData[index][2])));
                userToBeReturned.setIncompleteTrade(Integer.parseInt(removeChar(userData[index][3])));
                boolean isFrozen = true;
                if (Integer.parseInt(removeChar(userData[index][4])) == 0){
                    isFrozen = false;
                }
                userToBeReturned.setFrozen(isFrozen);
                userToBeReturned.setInventory(getInventory(userID));
                userToBeReturned.setWishlist(getWishlist(userID));
            }
        }
        if (userToBeReturned == null){
            throw new NullPointerException("User ID "+ userID + " Was Not Found");
        }
        return userToBeReturned;
    }

    /**
     * Iterates through the 'Items' CSV file and returns a Item entity with its' appropriate information
     * using the given itemID
     * @param itemID The ID of the item
     * @return Entity of type Item with its appropriate information
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected Item getItem(int itemID) throws IOException, NullPointerException{
        Item returnItem = null;
        String [][] itemData = readDataToMatrix("./Items.csv");
        for(int x=1; x<itemData.length; x++){
            if (Integer.parseInt(removeChar(itemData[x][0])) == itemID){
                returnItem = new Item(
                        Integer.parseInt(removeChar(itemData[x][1])),
                        removeChar(itemData[x][2]),
                        removeChar(itemData[x][3]));
                returnItem.setItemID(itemID);
                returnItem.setApproved(true);
                if(Integer.parseInt(removeChar(itemData[x][4])) == 0){
                    returnItem.setApproved(false);
                }
//                returnItem.setOwnerID(Integer.parseInt(removeChar(itemData[x][3])));
            }
        }
        if (returnItem == null){
            throw new NullPointerException("Item ID "+ itemID+ " Was Not Found");
        }
        return returnItem;
    }


    /**
     * Returns a list of all user IDs that are stored in the 'User' CSV file
     * @return The list of all the user IDs
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected List<Integer> getAllUserIDs() throws IOException{
        String [][] userData = readDataToMatrix("./Users.csv");
        List<Integer> userIDs= new ArrayList<>();
        for (int x=1; x<userData.length; x++){
            userIDs.add(Integer.parseInt(removeChar(userData[x][0])));
        }
        return userIDs;
    }

    /**
     * Returns a list of all administrator user IDs that are stored in the 'Administrator' CSV file
     * @return The list of all the administrator IDs
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected List<Integer> getAllAdminIDs() throws IOException{
        String [][] userData = readDataToMatrix("./Administrators.csv");
        List<Integer> userIDs= new ArrayList<>();
        for (int x=1; x<userData.length; x++){
            userIDs.add(Integer.parseInt(removeChar(userData[x][0])));
        }
        return userIDs;
    }


    /**
     * Returns a list of all trade IDs that are stored in the 'Trade' CSV file
     * @return The list of all the trade IDs
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected List<Integer> getAllTradeIDs() throws IOException{
        String [][] userData = readDataToMatrix("./Trades.csv");
        List<Integer> userIDs= new ArrayList<>();
        for (int x=1; x<userData.length; x++){
            userIDs.add(Integer.parseInt(removeChar(userData[x][0])));
        }
        return userIDs;
    }


    /**
     * Returns a list of all item IDs that are stored in the 'Item' CSV file
     * @return The list of all the item IDs
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected List<Integer> getAllItemIDs() throws IOException{
        String [][] userData = readDataToMatrix("./Items.csv");
        List<Integer> userIDs= new ArrayList<>();
        for (int x=1; x<userData.length; x++){
            userIDs.add(Integer.parseInt(removeChar(userData[x][0])));
        }
        return userIDs;
    }


    /**
     * Returns a list of all frozen user IDs that are stored in the 'User' CSV file
     * @return The list of all the user IDs
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected List<Integer> getAllFrozenAccounts() throws IOException{
        String [][] userData = readDataToMatrix("./Users.csv");
        List<Integer> userIDs= new ArrayList<>();
        for (int x=1; x<userData.length; x++){
            if (Integer.parseInt(removeChar(userData[x][4])) == 1){
                userIDs.add(Integer.parseInt(removeChar(userData[x][0])));
            }
        }
        return userIDs;
    }


    /**
     * Returns a list of all over borrowing user IDs that are stored in the 'User' CSV file
     * @return The list of all the user IDs that are over borrowing
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected List<Integer> getAllOverBorrowingUsers() throws IOException{
        String [][] userData = readDataToMatrix("./Users.csv");
        List<Integer> userIDs= new ArrayList<>();
        for (int x=1; x<userData.length; x++){
            if (Integer.parseInt(removeChar(userData[x][1])) > 0){
                userIDs.add(Integer.parseInt(removeChar(userData[x][0])));
            }
        }
        return userIDs;
    }


    /**
     * Iterates through the Returns a list of all item IDs that are due to be returned for their original owners
     * @return The list of all item IDs that are due to be returned for their original owners
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected List<Integer> getDueItemIDs() throws IOException{
        String [][] tradeData = readDataToMatrix("./Trades.csv");
        List<Integer> itemIDs= new ArrayList<>();
        Date date = new Date();
        LocalDateTime localDateTimeNow = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expectedData;
        for (int x=1; x<tradeData.length; x++){
            expectedData = LocalDateTime.parse(removeChar(tradeData[x][10]));
            if(localDateTimeNow.isAfter(expectedData)){
                if(!removeChar(tradeData[x][5]).equals("None")){
                    itemIDs.add(Integer.parseInt(removeChar(tradeData[x][5])));
                }
                itemIDs.add(Integer.parseInt(removeChar(tradeData[x][4])));
            }
        }
        return itemIDs;
    }


    /**
     * Looks through the 'Accounts' to find the user and determine whether it is an AdminAccount or not
     * @param userID The ID of the User
     * @return A boolean value, if it is true, then the user is an AdminAccount. Otherwise, if it is false,
     *         then the user is a UserAccount
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument or when the given UserID
     *                              does not exist in the CSV file
     */
    protected boolean getIsAdmin(int userID) throws IOException, NullPointerException{
        String [][] usersData = readDataToMatrix("./Accounts.csv");
        boolean isAdmin = true;
        int x=1;
        boolean found = false;
        while(x <= usersData.length && !found){
            if(x==usersData.length){
                throw new NullPointerException("User ID "+ userID + " Was Not Found");
            }
            if (Integer.parseInt(removeChar(usersData[x][0])) == userID){
                found = true;
                if(Integer.parseInt(removeChar(usersData[x][1])) == 0){
                    isAdmin = false;
                }
            }
            x++;
        }
        return isAdmin;
    }

    /**
     * Looks through the 'Password' to find the password with the given userID
     * @param userID The ID of the User
     * @return The password of the user
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument or when the given UserID
     *                              does not exist in the CSV file
     */
    protected String findPassword(int userID) throws IOException, NullPointerException{
        String[][] passwordData = readDataToMatrix("./Passwords.csv");
        String password = null;
        for (int x=1; x<passwordData.length; x++){
            if(Integer.parseInt(removeChar(passwordData[x][0])) == userID){
                password = removeChar(passwordData[x][1]);
            }
        }
        return password;
    }

    /**
     * This method finds the ID of the owner of the given itemID from either the 'Inventory' or 'Wishlist' CSV files
     * @param itemID ID of the item
     * @param fromWishlist boolean value, if it's true, then the method will search the 'Wishlist' CSV file, if it is
     *                     false, then it will search in the 'Inventory' CSV file
     * @return ID of the owner
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument or when the given UserID
     *                              does not exist in the CSV file
     */
    protected int getOwnerIDOfItem(int itemID, boolean fromWishlist) throws IOException, NullPointerException{
        String filePath;
        if (fromWishlist){
            filePath= "./Wishlist.csv";
        }else{
            filePath = "./Inventory.csv";
        }
        String [][] itemData = readDataToMatrix(filePath);
        int userID = 0;
        for(int x=1; x<itemData.length; x++){
            if(Integer.parseInt(removeChar(itemData[x][0])) == itemID){
                userID = Integer.parseInt(removeChar(itemData[x][1]));
            }
        }
        return userID;
    }

    /**
     * This method checks if the tradeID given is an ID for a oneWay or a TwoWayTrade
     * @param tradeID ID of the trade
     * @return true if the trade is OneWay or false if the trade is TwoWay
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument or when the given UserID
     *                              does not exist in the CSV file
     */
    protected boolean isOneWayTrade(int tradeID) throws IOException, NullPointerException {
        String [][] tradeData = readDataToMatrix("./Trades.csv");
        boolean isOneWay = false;
        for(int x=1; x<tradeData.length; x++){
            if(Integer.parseInt(removeChar(tradeData[x][0])) == tradeID &&
                    (Integer.parseInt(removeChar(tradeData[x][3])) == 0 ||
                            Integer.parseInt(removeChar(tradeData[x][4])) == 0)){
                    isOneWay = true;
            }
        }
        return isOneWay;
    }

    /**
     * This method looks through the 'Trades' CSV file and returns the oneWayTrade entity with the given tradeID
     * @param tradeID ID of the trade
     * @return the OneWayTrade entity with the given tradeID
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument or when the given UserID
     *                              does not exist in the CSV file
     */
    protected OneWayTrade getOneWayTrade(int tradeID) throws IOException, NullPointerException {
        String TRADES_FILE_PATH = "./Trades.csv";
        String[][] tradeData = readDataToMatrix(TRADES_FILE_PATH);
        OneWayTrade tradeToBeReturned = null;
        int zeroIndex = 3;
        for (int x = 1; x < tradeData.length; x++) {
            if (Integer.parseInt(removeChar(tradeData[x][0])) == tradeID) {
                if(Integer.parseInt(removeChar(tradeData[x][zeroIndex])) == 0){
                    zeroIndex += 1;
                }
                tradeToBeReturned = new OneWayTrade(Integer.parseInt(removeChar(tradeData[x][zeroIndex])),
                        Integer.parseInt(removeChar(tradeData[x][1])),
                        Integer.parseInt(removeChar(tradeData[x][2])));
                tradeToBeReturned.setLocation(removeChar(tradeData[x][6]));
                tradeToBeReturned.setTime(LocalDateTime.parse(removeChar(tradeData[x][8])));
                tradeToBeReturned.setTradeID(Integer.parseInt(removeChar(tradeData[x][0])));
                tradeToBeReturned.setStatus(removeChar(tradeData[x][5]));
                tradeToBeReturned.setIsPermanent(Integer.parseInt(removeChar(tradeData[x][7])) == 1);
                tradeToBeReturned.setLastEditorID(Integer.parseInt(removeChar(tradeData[x][11])));
                tradeToBeReturned.setNumEdits(Integer.parseInt(removeChar(tradeData[x][12])));
            }
        }
        return tradeToBeReturned;
    }

    /**
     * This method looks through the 'Trades' CSV file and returns the TwoWayTrade entity with the given tradeID
     * @param tradeID ID of the trade
     * @return the TwoWayTrade entity with the given tradeID
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument or when the given UserID
     *                              does not exist in the CSV file
     */
    protected TwoWayTrade getTwoWayTrade(int tradeID) throws IOException, NullPointerException{
        String TRADES_FILE_PATH = "./Trades.csv";
        String[][] tradeData = readDataToMatrix(TRADES_FILE_PATH);
        TwoWayTrade tradeToBeReturned = null;
        for (int x = 1; x < tradeData.length; x++) {
            if (Integer.parseInt(removeChar(tradeData[x][0])) == tradeID) {
                tradeToBeReturned = new TwoWayTrade(Integer.parseInt(removeChar(tradeData[x][4])),
                        Integer.parseInt(removeChar(tradeData[x][3])),
                        Integer.parseInt(removeChar(tradeData[x][1])),
                        Integer.parseInt(removeChar(tradeData[x][2])));
                tradeToBeReturned.setLocation(removeChar(tradeData[x][6]));
                tradeToBeReturned.setTime(LocalDateTime.parse(removeChar(tradeData[x][8])));
                tradeToBeReturned.setTradeID(Integer.parseInt(removeChar(tradeData[x][0])));
                tradeToBeReturned.setStatus(removeChar(tradeData[x][5]));
                tradeToBeReturned.setIsPermanent(Integer.parseInt(removeChar(tradeData[x][7])) == 1);
                tradeToBeReturned.setLastEditorID(Integer.parseInt(removeChar(tradeData[x][11])));
                tradeToBeReturned.setNumEdits(Integer.parseInt(removeChar(tradeData[x][12])));
            }
        }
        return tradeToBeReturned;
    }

    /**
     *  Get TradeLog from Trades.csv file
     * @return Get TradeLog from Trades.csv
     * @throws IOException input/output exception
     * @throws NullPointerException null pointer exception
     */
    protected TradeLog getTradeLog() throws IOException, NullPointerException {
        String[][] tradeData = readDataToMatrix("./Trades.csv");
        Map<Integer, Trade> allTrades = new HashMap<Integer, Trade>();
        for (int i=1; i < tradeData.length; i++){

            Integer tradeID = Integer.parseInt(removeChar(tradeData[i][0]));
            Integer user1ID = Integer.parseInt(removeChar(tradeData[i][1]));
            Integer user2ID = Integer.parseInt(removeChar(tradeData[i][2]));
            Integer item1ID = Integer.parseInt(removeChar(tradeData[i][4]));
            Integer item2ID = Integer.parseInt(removeChar(tradeData[i][3]));
            String status = removeChar(tradeData[i][5]);
            String location = removeChar(tradeData[i][6]);
            boolean isPermanent = (Integer.parseInt(removeChar(tradeData[i][7])) == 1);
            LocalDateTime time = LocalDateTime.parse(removeChar(tradeData[i][8]));
            Integer lastEditorID = new Integer(removeChar(tradeData[i][11]));
            int numEdits = Integer.parseInt(removeChar(tradeData[i][12]));

            Trade trade;
            if (item2ID.equals(0)) {
                trade = new OneWayTrade(item1ID, user1ID, user2ID);
            } else {
                trade = new TwoWayTrade(item1ID, item2ID, user1ID, user2ID);
            }

            trade.setNumEdits(numEdits);
            trade.setLocation(location);
            trade.setTime(time);
            trade.setIsPermanent(isPermanent);
            trade.setLastEditorID(lastEditorID);
            trade.setTradeID(tradeID);
            trade.setStatus(status);
            allTrades.put(tradeID, trade);
        }
//        TradeLog tLog = new TradeLog(allTrades);

        return new TradeLog(allTrades);
    }

    /**
     * This method writes the data of all tradeThresholds in a txt file called 'Config.txt'
     * @param tradeThresholds Map containing the type of Threshold as key and the data as values under the key
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void writeTradeThresholds(Map<String, Integer> tradeThresholds) throws IOException,
            NullPointerException {
        FileWriter writer = new FileWriter("./phase1/Config.txt");
        for(String x : tradeThresholds.keySet()){
            writer.write(x+"="+tradeThresholds.get(x) + "\n");
        }
        writer.close();
    }

    /**
     * This method reads the data of all tradeThresholds from a txt file called 'Config.txt'
     * @return Map containing the type of Threshold as key and the data as values under the key
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     */
    protected Map<String, Integer> getTradeThresholds() throws IOException {
        FileReader fileReader = new FileReader("./phase1/Config.txt");
        Properties properties = new Properties();
        properties.load(fileReader);
        Map<String, Integer> tradeThresholds = new HashMap<>();
        for (String x : properties.stringPropertyNames()) {
            tradeThresholds.put(x, Integer.parseInt(properties.getProperty(x)));
        }
        return tradeThresholds;
    }
}