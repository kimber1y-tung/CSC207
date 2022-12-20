package Usecase;

import Entities.*;

// import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import java.util.*;
import java.util.Map.Entry;



public class UserManager {
    public Map<Integer, UserAccount> allUsers;
    public TradeLogManager tradeLogManager;


    /***
     * The constructor.
     * @param map Map of userid to useraccount
     * @param tradeLogManager tracks all trades
     */
    public UserManager(Map<Integer, UserAccount> map, TradeLogManager tradeLogManager) {
        allUsers = map;
        this.tradeLogManager = tradeLogManager;
    }


    /**
     * Returns a list of all the users in the system.
     * @return a list of all the users in the system
     */
    public List<UserAccount> getAllUsers() {
        return new ArrayList<>(allUsers.values());
    }


    /**
     * Adds a user to the allUsers list.
     * @param user the user to be added
     */
    public void addUser(UserAccount user) {
        allUsers.put(user.getUserID(), user);
    }


    /**
     * Removes a user from the allUsers list.
     * @param user the user to be removed
     */
    public void removeUser(UserAccount user) {
        allUsers.remove(user.getUserID());
    }


    /**
     * Returns the user associated with a given userID
     * @param userID the ID of the user
     * @return the user
     */
    public UserAccount getUser(Integer userID) {
        return allUsers.get(userID);
    }


    /**
     * Increases the user's number of trades by one.
     * @param userID the ID of the user
     */
    public void incrementNumTrade(Integer userID) {
        UserAccount user = getUser(userID);
        user.setNumTrade(user.getNumTrade() + 1);
    }

    /**
     * Increases or decreases the user's number of items overborrowed by the amount given. Can be negative if the
     * overborrowed variable is to decrease.
     * @param userID the ID of the user
     * @param amount the amount to increase or decrease the overborrowed variable by
     */
    public void changeOverborrowed(Integer userID, int amount) {
        UserAccount user = getUser(userID);
        user.setOverBorrowed(user.getOverBorrowed() + amount);
    }

    /**
     * Transfers an item from user1's inventory to user2's inventory.
     *
     * @param item the item to transfer
     * @param user1 the user giving the item
     * @param user2 the user receiving the item*/
    public void transferItem(Item item, UserAccount user1, UserAccount user2) {

        if (user1.getInventory().contains(item)) {
            user2.addToInventory(item);
            user1.removeFromInventory(item);
            user2.removeFromWishlist(item);
        }
    }


    /**
     * Return all trades associated with a given user.
     * @param user the user
     * @return the List of the user's trades
     */
    public List<Trade> getTrades(UserAccount user) {
        return tradeLogManager.getUserTrades(user.getUserID());
    }


    /**
     * Return a list of up to three most recent completed trades associated with a given user. If user has completed less
     * than three trades, returns all of them.
     @param user the user
     @return the list of the user's three most recent trades
     **/
    public List<Trade> getRecentTrades(UserAccount user) {
        Integer userID = user.getUserID();
        List<Trade> uTrades = tradeLogManager.getUserTrades(userID);
        List<Trade> finished = new ArrayList<>();
        List<Trade> recent = new ArrayList<>();

        for (Trade uTrade : uTrades) {
            if (uTrade.getStatus().equals("complete")) {
                finished.add(uTrade);
            }
        }

        int count = 0;
        while (count < 3 && finished.size() > 0)
        {
            recent.add(finished.remove(finished.size() - 1));
            count++;
        }
        return recent;

        }


    /**
     * Return the userIDs of the three most common trading partners of a given user.
     * @param user the user
     * @return the list of userIDs of the three most common trading partners of the user
     */
    public List<Integer> getTopTradingPartners(UserAccount user) {
        Integer userID = user.getUserID();
        List<Trade> trades = tradeLogManager.getUserTrades(userID);
        Map<Integer, Integer> partners = new HashMap<>();

        // create a map with keys as partner IDs and values as their number of occurrences
        for (Trade trade : trades) {
            if (trade.getUser2ID().equals((user.getUserID()))) {
                if (partners.containsKey(trade.getUser1ID())) {
                    partners.put(trade.getUser1ID(),
                            partners.get(trade.getUser1ID()) + 1);
                }
                else {
                    partners.put(trade.getUser1ID(), 1);
                }
            }
            else {
                if (partners.containsKey(trade.getUser2ID())) {
                    partners.put(trade.getUser2ID(),
                            partners.get(trade.getUser2ID()) + 1);
                }
                else {
                    partners.put(trade.getUser2ID(), 1);
                }
            }
        }

        List<Integer> common = new ArrayList<>();
        // iterate through hash map 3 times to find 3 most common trading partners.

        for (int j = 0; j < 3; j ++) {
            int max = 0;
            int i = -1;
            for(Entry<Integer, Integer> val : partners.entrySet()) {
                if (max < val.getValue()) {
                    i = val.getKey();
                    max = val.getValue();
                }
            }
            common.add(partners.get(i));
            partners.remove(partners.get(i));
        }


        return common;
        }


    /**
     * Return a list of trades requested of a given user.
     * @param user the user
     * @return the list of requested trades
     */
    public List<Trade> checkTradeRequests(UserAccount user) {
        Integer userID = user.getUserID();
        List<Trade> aTrades = tradeLogManager.getUserTrades(userID);
        if (aTrades.size() == 0) {
            return new ArrayList<>();
        }
        List<Trade> requests = new ArrayList<>();
        for (Trade aTrade : aTrades) {
            if (aTrade.getStatus().equals("requested") && aTrade.getUser1ID().equals(userID)) {
                requests.add(aTrade);
            }
        }
        return requests;
    }

}




