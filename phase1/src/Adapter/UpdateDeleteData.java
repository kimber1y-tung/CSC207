package Adapter;

import com.opencsv.CSVWriter;
import java.io.*;
import java.time.LocalDateTime;

public class UpdateDeleteData {

    /**
     * This is a helper method that writes data to files
     * @param filePath String value that represents the path of the file
     * @param data Multi-dimensional array of type String containing the data itself
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     */
    private void writeData(String filePath, String[][] data) throws IOException {
        File file = new File(filePath);
        FileWriter outputFile = new FileWriter(file);
        CSVWriter fileWriter = new CSVWriter(outputFile);
        for (String[] datum : data) {
            fileWriter.writeNext(datum);
        }
        fileWriter.close();
        outputFile.close();
    }

    /**
     * This method allows an adminUser to modify the trade count of a specified UserAccount
     * @param userID ID of the user whose trade count will be modified
     * @param newNumTrade new trade count for the user
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void updateNumTrades(int userID, int newNumTrade) throws IOException, NullPointerException {
        String USERS_FILE_PATH = "./Users.csv";
        String[][] usersData = readDataToMatrix(USERS_FILE_PATH);
        String[][] newUserData = new String[usersData.length][usersData[0].length];
        for(int u=0; u<usersData[0].length; u++){
            newUserData[0][u] = removeChar(usersData[0][u]);
        }
        for(int x=1; x<usersData.length; x++){
            for(int j=0; j<usersData[x].length; j++){
                if(Integer.parseInt(removeChar(usersData[x][0])) != userID){
                    newUserData[x][j] = removeChar(usersData[x][j]);
                }else{
                    if(j!=2){
                        newUserData[x][j] = removeChar(usersData[x][j]);
                    }else{
                        newUserData[x][j] = Integer.toString(newNumTrade);
                    }
                }
            }
        }
        writeData(USERS_FILE_PATH, newUserData);
    }

    /**
     * Helper method that reads the data from the given type of file and returns the data in it
     * @param filePath String representing the path of the file the method will access
     * @return Return a 2D array containing all the data found in the file
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    private String[][] readDataToMatrix(String filePath) throws IOException, NullPointerException{
        FileReader fileReader = new FileReader(filePath);
        String row;
        int c=0;
        int counter = 0;
        BufferedReader csvReader = new BufferedReader(fileReader);
        while (csvReader.readLine() != null) {
            counter++;
        }
        FileReader fileReader2 = new FileReader(filePath);
        BufferedReader csvReader2 = new BufferedReader(fileReader2);
        String[][] data = new String[counter][];
        while ((row = csvReader2.readLine()) != null) {
            data[c] = row.split(",");
            c++;
        }
        fileReader.close();
        fileReader2.close();
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
     * This method checks if an itemID exists for a specified userID in either the 'Wishlist' or 'Inventory' CSV files
     * @param itemID  ID of the item
     * @param userID ID of the user for which the item belongs to
     * @param isWishlist If this value is true, it will add the itemID in the 'Wishlist' CSV file
     *                   If this value is false, it will add the itemID in the 'Inventory' CSV file
     * @return true if there exists already an itemID for the userID passed in the parameter, otherwise, it returns
     *         false
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     */
    private boolean checkIfItemExits (int itemID, int userID, boolean isWishlist) throws IOException{
        boolean exists = false;
        String filePath;
        if (isWishlist){
            filePath =  "./Wishlist.csv";
        }else{
            filePath = "./Inventory.csv";
        }
        String[][] data = readDataToMatrix(filePath);
        for (int x=1; x<data.length; x++){
            if(Integer.parseInt(removeChar(data[x][0])) == itemID &&
                    Integer.parseInt(removeChar(data[x][1])) == userID){
                exists = true;
            }
        }
        return exists;
    }

    /**
     * This method adds an itemID in either the 'Wishlist' or 'Inventory' CSV files
     * @param itemID  ID of the new item
     * @param userID ID of the user for which the item will belong
     * @param isWishlist If this value is true, it will add the itemID in the 'Wishlist' CSV file
     *                   If this value is false, it will add the itemID in the 'Inventory' CSV file
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void addItemToUserItems(int itemID, int userID, boolean isWishlist) throws IOException,
            NullPointerException{
        String filePath;
        if (isWishlist){
            filePath =  "./Wishlist.csv";
        }else{
            filePath = "./Inventory.csv";
        }
        String [][] data = readDataToMatrix(filePath);
        String [][] updatedData = new String[data.length][data[0].length];
        if(!checkIfItemExits(itemID, userID, isWishlist)){
            updatedData = new String [data.length+1][data[0].length];
            for(int y=0; y<data.length;y++){
                for (int x=0; x<data[y].length; x++){
                    updatedData[y][x]= removeChar(data[y][x]);
                }
            }
            updatedData[data.length][0] = String.valueOf(itemID);
            updatedData[data.length][1] = String.valueOf(userID);
        }else{
            for(int y=0; y<data.length;y++){
                for (int x=0; x<data[y].length; x++){
                    updatedData[y][x]= removeChar(data[y][x]);
                }
            }
        }
        writeData(filePath, updatedData);
    }

    /**
     * Helper method that modifies the IS_Frozen value in the row passed as a parameter
     * @param row Array of type string which represents the row containing the IS_FROZEN value
     * @param freezeColumnNumber The Index of the IS_FROZEN value exists in the value passed for the 'row' parameter
     * @return Return the modified row with the new IS_FROZEN value
     */
    private String[] changeFreeze(String [] row, int freezeColumnNumber){
        int freezeValue = Integer.parseInt(removeChar(row[freezeColumnNumber]));
        int newFreezeValue =0;
        String [] newRow = new String[row.length];
        if (freezeValue == 0){
            newFreezeValue = 1;
        }
        for (int x=0; x<row.length; x++){
            if(x!=freezeColumnNumber){
                newRow[x]= removeChar(row[x]);
            }else{
                newRow[x]= String.valueOf(newFreezeValue);
            }
        }
        return newRow;
    }

    /**
     * Changes the freeze status of a user in the 'Users' CSV file
     * @param userID ID of the user
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void changeFreezeStatus(int userID) throws IOException, NullPointerException{
        String[][] userData = readDataToMatrix("./Users.csv");
        String[][] modifiedUserData = new String[userData.length][userData[0].length];
        int isFreezeColumn = 0;
        for(int y=0; y< userData.length;y++){
            if(y==0){
                for(int u=0; u<userData[0].length; u++){
                    modifiedUserData[y][u] = removeChar(userData[y][u]);
                    if(removeChar(userData[y][u]).equals("IS_FROZEN")){
                        isFreezeColumn = u;
                    }
                }
            }else{
                if(Integer.parseInt(removeChar(userData[y][0])) == userID){
                    modifiedUserData[y] = changeFreeze(userData[y], isFreezeColumn);
                }else{
                    for(int x=0; x<userData[y].length; x++){
                        modifiedUserData[y][x] = removeChar(userData[y][x]);
                    }
                }
            }
        }
        writeData("./Users.csv", modifiedUserData);
    }

    /**
     * Removes row from a multi-dimensional array from data that is passed as parameters
     * @param rowToBeIgnored This is the row to be removed
     * @param originalTable The original set of data
     * @return The updated set of data with the specified row removed
     */
    protected String[][] removeRow(int rowToBeIgnored, String[][] originalTable){
        String[][] modifiedTable = new String[originalTable.length-1][originalTable[0].length];
        for(int x=0; x<originalTable.length; x++){
            if(x<rowToBeIgnored){
                modifiedTable[x] = originalTable[x];
            }else if(x > rowToBeIgnored){
                modifiedTable[x-1] = originalTable[x];
            }
        }
        return modifiedTable;
    }

    /**
     * Removes an item with all of its' information in either the 'Wishlist' or 'Inventory' or 'Items' CSV files
     * @param itemID ID of the item
     * @param typeOfRemoval if this value is 0, then it updates the 'wishlist' CSV.
     *                      if this value is 1, then the method updates the 'Inventory' CSV file
     *                      if this value is 2, then the method updates the 'Item' CSV file
     *                      if this value is 3, then the method updates the 'Trades' CSV file
     * @param userID ID of the user for which the item will be removed (Only used if the removal is from
     *               the 'Wishlist' CSV file)
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void removeItem(int itemID, int typeOfRemoval, int userID) throws IOException, NullPointerException {
        String filePath = null;
        if (typeOfRemoval == 0){
            filePath = "./Wishlist.csv";
        }else if(typeOfRemoval == 1){
            filePath = "./Inventory.csv";
        }else if(typeOfRemoval == 2){
            filePath = "./Items.csv";
        }else if(typeOfRemoval == 3){
            filePath = "./Trades.csv";
        }
        String [][] data = readDataToMatrix(filePath);
        int indexOfIgnorance = -5;
        String [][] newData = new String[data.length][data[0].length];
        for(int j=0; j<data[0].length; j++){
            newData[0][j] = removeChar(data[0][j]);
        }
        for (int x=1; x<data.length; x++){
            if(typeOfRemoval == 0){
                if(Integer.parseInt(removeChar(data[x][0]))==itemID &&
                        Integer.parseInt(removeChar(data[x][1]))==userID){
                    indexOfIgnorance = x;
                }
            }else{
                if(Integer.parseInt(removeChar(data[x][0]))==itemID){
                    indexOfIgnorance = x;
                }
            }
            for(int y=0; y<data[x].length; y++){
                newData[x][y] = removeChar(data[x][y]);
            }
        }
        String[][] newData2 = new String[0][];
        if(indexOfIgnorance < 0){
            newData2 = newData;
        }else if(indexOfIgnorance > 0){
            newData2 = removeRow(indexOfIgnorance, newData);
        }
        writeData(filePath, newData2);
    }

    /**
     * Updates the user ID of an item in either the 'Wishlist' or 'Inventory' CSV files
     * @param itemID ID of the item
     * @param newUserID ID of the new User
     * @param typeOfUpdate if this value is 0, then it updates the 'wishlist' CSV. Otherwise, if this value is 1,
     *                     then the method updates the 'Inventory' CSV file
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void updateItem(int itemID, int newUserID, int typeOfUpdate) throws IOException, NullPointerException{
        String filePath = null;
        if (typeOfUpdate == 0){
            filePath= "./Wishlist.csv";
        }else if(typeOfUpdate == 1){
            filePath = "./Inventory.csv";
        }else if(typeOfUpdate == 2){
            filePath = "./Items.csv";
        }
        String [][] itemData = readDataToMatrix(filePath);
        String [][] updatedItemData = new String[itemData.length][itemData[0].length];
        for(int h=0; h<itemData[0].length; h++){
            updatedItemData[0][h] = removeChar(itemData[0][h]);
        }
        for (int x=1; x<itemData.length; x++){
            for (int y=0; y<itemData[x].length; y++){
                updatedItemData[x][y] = removeChar(itemData[x][y]);
                if(Integer.parseInt(removeChar(itemData[x][0])) == itemID){
                    updatedItemData[x][0] = removeChar(itemData[x][0]);
                    updatedItemData[x][1] = String.valueOf(newUserID);
                }
            }
        }
        writeData(filePath, updatedItemData);
    }

    /**
     * Helper method to updates the 'Inventory' and 'Items' CSV files using the trade data provided
     * @param data Data provided about the trade
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     */
    private void updateInventory(String[] data) throws IOException {
        if(Integer.parseInt(removeChar(data[3])) != 0 && Integer.parseInt(removeChar(data[4])) == 0){
            updateItem(Integer.parseInt(removeChar(data[3])), Integer.parseInt(removeChar(data[1])), 1);
            updateItem(Integer.parseInt(removeChar(data[3])), Integer.parseInt(removeChar(data[1])), 2);
        }else if(Integer.parseInt(removeChar(data[3])) == 0 && Integer.parseInt(removeChar(data[4])) != 0){
            updateItem(Integer.parseInt(removeChar(data[4])), Integer.parseInt(removeChar(data[2])), 1);
            updateItem(Integer.parseInt(removeChar(data[4])), Integer.parseInt(removeChar(data[2])), 2);
        }else if(Integer.parseInt(removeChar(data[3])) != 0 && Integer.parseInt(removeChar(data[4])) != 0){
            updateItem(Integer.parseInt(removeChar(data[3])), Integer.parseInt(removeChar(data[1])), 1);
            updateItem(Integer.parseInt(removeChar(data[3])), Integer.parseInt(removeChar(data[1])), 2);
            updateItem(Integer.parseInt(removeChar(data[4])), Integer.parseInt(removeChar(data[2])), 1);
            updateItem(Integer.parseInt(removeChar(data[4])), Integer.parseInt(removeChar(data[2])), 2);
        }
    }

    /**
     * Helper method to update the NumTrades and IncompleteTrades files in the 'Users' CSV file
     * @param data The data of the trade the this method will work with
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     */
    private void updateTradeQuantities(String[] data, String isComplete) throws IOException {
        String USERS_FILE_PATH = "./Users.csv";
        String [][] usersData = readDataToMatrix(USERS_FILE_PATH);
        String [][] newUsersData = new String[usersData.length][usersData[0].length];
        for(int j=0; j<usersData[0].length; j++){
            newUsersData[0][j] = removeChar(usersData[0][j]);
        }
        for(int x=1; x<usersData.length; x++){
            for(int y=0; y<usersData[x].length; y++){
                newUsersData[x][y] = removeChar(usersData[x][y]);
            }
            if(isComplete.equalsIgnoreCase("incomplete")){
                newUsersData[x][3] = String.valueOf(Integer.parseInt(removeChar(usersData[x][3])) + 1);
            }else{
                if(Integer.parseInt(removeChar(usersData[x][0])) == Integer.parseInt(removeChar(data[1]))){
                    if(Integer.parseInt(removeChar(data[3])) != 0 && Integer.parseInt(removeChar(data[4])) == 0){
                        newUsersData[x][1] = String.valueOf(Integer.parseInt(removeChar(usersData[x][1])) + 1);
                    }else if(Integer.parseInt(removeChar(data[3])) == 0 && Integer.parseInt(removeChar(data[4])) != 0){
                        newUsersData[x][1] = String.valueOf(Integer.parseInt(removeChar(usersData[x][1])) - 1);
                    }else if(Integer.parseInt(removeChar(data[3])) != 0 && Integer.parseInt(removeChar(data[4])) != 0){
                        newUsersData[x][1] = String.valueOf(Integer.parseInt(removeChar(usersData[x][1])));
                    }
                    newUsersData[x][2] = String.valueOf(Integer.parseInt(removeChar(usersData[x][2])) + 1);
                }
                if(Integer.parseInt(removeChar(usersData[x][0])) == Integer.parseInt(removeChar(data[2]))) {
                    if (Integer.parseInt(removeChar(data[3])) != 0 && Integer.parseInt(removeChar(data[4])) == 0) {
                        newUsersData[x][1] = String.valueOf(Integer.parseInt(removeChar(usersData[x][1])) - 1);
                    } else if (Integer.parseInt(removeChar(data[3])) == 0 && Integer.parseInt(removeChar(data[4])) != 0) {
                        newUsersData[x][1] = String.valueOf(Integer.parseInt(removeChar(usersData[x][1])) + 1);
                    } else if (Integer.parseInt(removeChar(data[3])) != 0 && Integer.parseInt(removeChar(data[4])) != 0) {
                        newUsersData[x][1] = String.valueOf(Integer.parseInt(removeChar(usersData[x][1])));
                    }
                    newUsersData[x][2] = String.valueOf(Integer.parseInt(removeChar(usersData[x][2])) + 1);
                }
            }
        }
        writeData(USERS_FILE_PATH, newUsersData);
    }

    /**
     * Helper method that returns a new date which is exactly 30 days from the date in the given row
     * @param row Original data in the row
     * @return The new expected date which is 30 days from the trade date in the given row
     */
    private String setExpectedReturnData(String[] row){
        LocalDateTime expectedData;
        expectedData = LocalDateTime.parse(removeChar(row[8])).plusDays(30);
        return String.valueOf(expectedData);
    }

    /**
     * Updates any column in the 'Trades' CSV file given the new data
     * @param userIDOfEditor ID of the user who requested to make the update
     * @param tradeID ID of the Trade
     * @param columnIndex Index of the column that has the data that needs to be updated
     * @param newData The new data that will be inserted in the specified column index
     * @throws IOException thrown if there is failure or interruption in the I/O operations or if columnIndex is 0
     *                     which the method is being asked to update the value of the tradeID
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void updateTrade(int userIDOfEditor, int tradeID, int columnIndex, String newData) throws IOException,
            NullPointerException {
        if (columnIndex == 0){
            throw new IOException("Updating The Value Of The Trade ID Is Forbidden. Please Try Again With A Different" +
                    " Value To Update!");
        }
        String TRADES_FILE_PATH = "./Trades.csv";
        String [][] tradeData = readDataToMatrix(TRADES_FILE_PATH);
        String [][] updatedTradeData = new String[tradeData.length][tradeData[0].length];
        for(int j=0; j<tradeData[0].length; j++){
            updatedTradeData[0][j] = removeChar(tradeData[0][j]);
        }
        for (int x=1; x < tradeData.length; x++){
            for(int y=0; y < tradeData[x].length; y++){
                updatedTradeData[x][y] = removeChar(tradeData[x][y]);
                if(Integer.parseInt(removeChar(tradeData[x][0])) == tradeID){
                    if(y == columnIndex){
                        updatedTradeData[x][y] = newData;
                    }
                    updatedTradeData[x][updatedTradeData[x].length-2] = Integer.toString(userIDOfEditor);
                }
            }
            if(Integer.parseInt(removeChar(tradeData[x][0])) == tradeID &&
                    columnIndex == 5 && (newData.toLowerCase().equals("complete") ||
                    newData.toLowerCase().equals("incomplete"))){
                updateInventory(tradeData[x]);
                updateTradeQuantities(tradeData[x], newData.toLowerCase());
                if(Integer.parseInt(removeChar(tradeData[x][7])) == 0){
                    updatedTradeData[x][9] = setExpectedReturnData(tradeData[x]);
                }else if (Integer.parseInt(removeChar(tradeData[x][7])) == 1){
                    updatedTradeData[x][9] = "";
                }
            }
        }
        writeData(TRADES_FILE_PATH, updatedTradeData);
    }

    /**
     * This method changes the status of an item from unapproved to approved in the 'Items' CSV file given the itemID
     * @param itemID ID of the item
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    protected void approveItem(int itemID) throws IOException, NullPointerException{
        String ITEMS_FILE_PATH = "./Items.csv";
        String[][] itemData = readDataToMatrix(ITEMS_FILE_PATH);
        String[][] updatedItemData = new String[itemData.length][itemData[0].length];
        for(int j=0; j<itemData[0].length; j++){
            updatedItemData[0][j] = removeChar(itemData[0][j]);
        }
        for(int x=1; x<itemData.length; x++){
            for(int y=0; y<itemData[x].length; y++){
                updatedItemData[x][y] = removeChar(itemData[x][y]);
                if(Integer.parseInt(removeChar(itemData[x][0])) == itemID){
                    updatedItemData[x][4] = String.valueOf(1);
                }
            }
        }
        writeData(ITEMS_FILE_PATH, updatedItemData);
    }
}
