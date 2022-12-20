package gui.util;

import accounts.UserAccount;
import items.Item;
import trades.Meeting;
import trades.Trade;

public class DisplayUtil {

    /**
     * displays the item with its details
     * @param item the Item
     * @return a string displaying the item with its details
     */
    public static String getItemDisplay(Item item) {
        return "Item ID: " + item.getItemID() + " | " + "Item: " + item.getItemName() + " | " + "Description: " +item.getDescription()+ " | " + "Price: " +item.getPrice() + " | " + "Owner ID: " + item.getOwnerID();
    }

    /**
     * displays the trade with its details
     * @param trade the Trade
     * @return a string displaying the trade with its details
     */
    public static String getTradeDisplay (Trade trade) {
       return "Trade ID: " + trade.getTradeID() + " | " + "Items: " + trade.getAllItems()+ " | " + "Requester ID: " +trade.getRequesterID()
               +" | " + "Is permanent: " +trade.getIsPermanent() + " | " + "status: " + trade.getStatus() + " | " + "Meeting Details: " + trade.getMeeting();
    }

    /**
     * displays the meeting with its details
     * @param meeting the Meeting
     * @return a string displaying the meeting with its details
     */
    public static String getMeetingDisplay(Meeting meeting) {
        return "Meeting Location " + meeting.getLocation() + " | " + "Meeting Time: " + meeting.getTime()+ " | " + "Meeting Status: " +meeting.getStatus()+ " | " + "Meeting Last Editor ID: " +meeting.getLastEditorID();
    }

    /**
     * displays the user with its details
     * @param user the user
     * @return a string displaying the user with its details
     */
    public static String getUserDisplay(UserAccount user) {
        return "User ID: " + user.getUserID() + " | " + "City: " + user.getCity()+ " | " + "Vacation Status: " +user.isOnVacation();


    }
}
