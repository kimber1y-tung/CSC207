package Adapter;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FileInitializer{
    /**
     * Initializes the file with the specified headers at the specified location
     * @param type the type of CSV file the method will initialize
     * @throws IOException thrown if there is failure or interruption in the I/O operations
     * @throws NullPointerException thrown when null is passed as an argument
     */
    public void initializeFile(String type) throws IOException, NullPointerException {
        Map<String, String[]> fileHeaders = new HashMap<String, String[]>() {
            {
                put("User", new String[]{"USER_ID", "OVER_BORROWED", "NUM_TRADES", "INCOMPLETE_TRADES", "IS_FROZEN"});
                put("Admin", new String[]{"USER_ID"});
                put("Password", new String[]{"USER_ID", "PASSWORD"});
                put("Item", new String[]{"ITEM_ID", "OWNER_ID", "ITEM_NAME", "DESCRIPTION", "IS_APPROVED"});
                put("Accounts", new String[]{"USER_ID", "IS_ADMINISTRATOR"});
                put("Trade", new String[]{"TRADE_ID", "USER1_ID", "USER2_ID", "ITEM_ID_USER1_GETS",
                        "ITEM_ID_USER2_GETS", "TRADE_STATUS", "LOCATION", "IS_PERMANENT", "TRADE_DATE",
                        "EXPECTED_RETURN_DATE", "IS_REVERSED", "LAST_EDITED_BY_ID", "NUMBER_OF_EDITS"});
                put("Inventory", new String[]{"ITEM_ID", "BELONGS_TO_USER_ID"});
                put("Wishlist", new String[]{"ITEM_ID", "BELONGS_TO_USER_ID"});
            }
        };
        Map<String, String> filePaths = new HashMap<String, String>(){
            {
                put("User", "./Users.csv");
                put("Admin", "./Administrators.csv");
                put("Password", "./Passwords.csv");
                put("Trade", "./Trades.csv");
                put("Item", "./Items.csv");
                put("Accounts", "./Accounts.csv");
                put("Inventory", "./Inventory.csv");
                put("Wishlist", "./Wishlist.csv");
            }
        };

        File file = new File(filePaths.get(type));
        FileWriter outputFile = new FileWriter(file);
        CSVWriter fileWriter = new CSVWriter(outputFile);
        fileWriter.writeNext(fileHeaders.get(type));
        fileWriter.close();
        outputFile.close();
    }

}
