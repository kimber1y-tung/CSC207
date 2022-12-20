package Usecase;

import Entities.*;
import java.util.*;


/**
 * The class TradeLogManager manage TradeLog.
 */
public class TradeLogManager {

        private TradeLog tradeLog;
        private Map<String, List<Trade>> statusDictionary;
        private Map<Integer, List<Trade>> userDictionary;


    /**
     * Constructor
     * @param tLog a TradeLog
     */
        public TradeLogManager(TradeLog tLog){
            tradeLog = tLog;
            statusDictionary = buildStatusDictionary(tLog);
            userDictionary = buildUserDictionary(tLog);
        }


    /**
     * Build a Status Dictionary from TradeLog tLog
     * @param tLog a TradeLog to build a StatusDictionary
     * @return a StatusDictionary
     */
        private Map<String, List<Trade>> buildStatusDictionary(TradeLog tLog) {
            Map<String, List<Trade>> statusDictionary = new HashMap<>();
            statusDictionary.put("requested", new ArrayList<>());
            statusDictionary.put("pending", new ArrayList<>());
            statusDictionary.put("open", new ArrayList<>());
            statusDictionary.put("complete", new ArrayList<>());
            statusDictionary.put("incomplete", new ArrayList<>());
            statusDictionary.put("declined", new ArrayList<>());

            if (tLog.getTradeHistory().size() != 0) {
                for (Trade trade : tLog.getTradeHistory().values()) {
                    String status = trade.getStatus();
                    statusDictionary.get(status).add(trade);
                }
            }
            return statusDictionary;
        }


    /**
     * Build a user dictionary from TradeLog tLog
     * @param tLog a TradeLog to build a UserDictionary
     * @return a User Dictionary
     */
        private Map<Integer, List<Trade>> buildUserDictionary(TradeLog tLog) {
            Map<Integer, List<Trade>> userDictionary = new HashMap<>();

            if (!(tLog.getTradeHistory().size() == 0)) {
                for (Trade trade : tLog.getTradeHistory().values()) {
                    Integer user1 = trade.getUser1ID();
                    Integer user2 = trade.getUser2ID();

                    // add users to dictionary if they aren't in it
                    if (!userDictionary.containsKey(user1)) {
                        userDictionary.put(user1, new ArrayList<>());
                    }

                    if (!userDictionary.containsKey(user2)) {
                        userDictionary.put(user2, new ArrayList<>());
                    }

                    // add trades to users' lists
                    userDictionary.get(user1).add(trade);
                    userDictionary.get(user2).add(trade);
                }
            }
            return userDictionary;
        }


    /**
     * Get the Status Dictionary
     * @return a StatusDictionary
     */
    public Map<String, List<Trade>> getStatusDictionary(){
            return statusDictionary;
        }


    /**
     *  Get the User Dictionary
     * @return a UserDictionary
     */
        public Map<Integer, List<Trade>> getUserDictionary(){
            return userDictionary;
        }


    /**
     * Get a user's list of trades
     * @param userID the user ID to get Trades of the user from UserDictionary.
     * @return a list of Trades
     */
    public List<Trade> getUserTrades(Integer userID) {
        if (!userDictionary.containsKey(userID)) {
            return new ArrayList<>();
        }
        return userDictionary.get(userID);
        }


    /**
     * Get a list of trades for a given status
     * @param status the String key to get a list of Trade from StatusDictionary.
     * @return a list of Trades.
     */
    public List<Trade> getStatusTrades(String status) {
            return statusDictionary.get(status);
        }


    /**
     * Add trade to TradeLog, StatusDictionary, and UserDictionary.
     * @param tradeID the unique ID of the trade to be added
     * @param trade a Trade to be added to UserDictionary and StatusDictionary
     */
    public void addTrade(Integer tradeID, Trade trade) {
            //add to tradeLog
            tradeLog.addTrade(tradeID, trade);

            // add to statusDictionary
            statusDictionary.get(trade.getStatus()).add(trade);

            // add to userDictionary
            Integer user1 = trade.getUser1ID();
            Integer user2 = trade.getUser2ID();
            if (!userDictionary.containsKey(user1)) {
                userDictionary.put(user1, new ArrayList<>());
            }
            userDictionary.get(trade.getUser1ID()).add(trade);

            if (!userDictionary.containsKey(user2)) {
                userDictionary.put(user2, new ArrayList<>());
            }
            userDictionary.get(trade.getUser2ID()).add(trade);

        }

    /**
     * Remove the trade from TradeLog, StatusDictionary, and UserDictionary.
     * @param trade a Trade to be removed
     */
    public void removeTrade(Trade trade) {
            // remove from tradeLog
            tradeLog.removeTrade(trade);

            // remove from statusDictionary
            if (statusDictionary.get(trade.getStatus()) != null){
                statusDictionary.get(trade.getStatus()).remove(trade);
            }

            // remove from userDictionary
            if(userDictionary.get(trade.getUser1ID()) != null){
                userDictionary.get(trade.getUser1ID()).remove(trade);
            }
            if(userDictionary.get(trade.getUser2ID()) != null){
                userDictionary.get(trade.getUser2ID()).remove(trade);
            }
        }


    /**
     * Remove Trade from current position in statusDictionary and move
     * it to desired position, update trade Requests if necessary
     * @param trade a Trade to be updated
     * @param status the status of a Trade to be updated
     */
        public void updateStatus(Trade trade, String status) {
            String currentStatus = trade.getStatus();
            if (currentStatus != null) {
                statusDictionary.get(currentStatus).remove(trade);
                }
            statusDictionary.get(status).add(trade);
        }

    /**
     * Get Trade with the Trade ID tradeID
     * @param tradeID Trade ID
     * @return Trade with trade ID tradeID
     */
    public Trade getTradeWithID(Integer tradeID) {
        return tradeLog.getTradeHistory().get(tradeID);
    }
}

