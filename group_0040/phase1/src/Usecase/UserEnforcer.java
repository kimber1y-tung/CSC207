package Usecase;

import Entities.UserAccount;

import java.util.ArrayList;
import java.util.List;


public class UserEnforcer {
    private int tradeLimit;
    private int incompleteLimit;
    private int overBorrowLimit;
    private List<Integer> flaggedUsers;


    /**
     * The constructor.
     * @param userManager UserManager object
     * @param tradeLimit maximum number of trades allowed
     * @param incompleteLimit maximum number of incomplete trades allowed
     * @param overBorrowLimit number of items borrowed more than lent.
     */
    public UserEnforcer(UserManager userManager, int tradeLimit, int incompleteLimit, int overBorrowLimit){
        this.flaggedUsers = new ArrayList<>();
        this.tradeLimit = tradeLimit;
        this.incompleteLimit = incompleteLimit;
        this.overBorrowLimit = overBorrowLimit;
        flagAllUsers(userManager);
    }

    /**
     * Flags all users who have borrowed more items than the limit, participated in the maximum number of incomplete
     * trades, or participated in the maximum number of weekly trades.
     * @param userManager the userManager object
     */
    public void flagAllUsers(UserManager userManager) {
        for (UserAccount user : userManager.getAllUsers()) {
            if (!user.isFrozen() && (user.getOverBorrowed() > overBorrowLimit || user.getNumTrade() >= tradeLimit ||
                    user.getIncompleteTrade() >= incompleteLimit)) {
                flagUser(user);
            }
        }
    }

    /**
     * Add a user to the list of flagged users.
     * @param user the user to add
     */
    public void flagUser(UserAccount user){
        flaggedUsers.add(user.getUserID());
    }


    /**
     * Remove a user from the list of flagged users.
     * @param user the user to remove
     */
    public void unFlagUser(UserAccount user){
        flaggedUsers.remove(user.getUserID());
    }


    /**
     * Return the list of flagged users.
     * @return the list of flagged users
     */
    public List<Integer> getFlaggedUsers(){
        return flaggedUsers;

    }

    /**
     * Freeze a user's account.
     * @param user the user to freeze
     */
    public void freezeUser (UserAccount user){
       user.setFrozen(true);
    }


    /**
     * Unfreeze a user's account.
     * @param user the user to unfreeze
     */
    public void unfreezeUser (UserAccount user){
        user.setFrozen(false);
    }


    /**
     * Reset all users number of trades to zero.
     * @param userManager the userManager object
     */
    public void resetNumTrades(UserManager userManager){
        for (UserAccount user : userManager.getAllUsers()) {
            user.setNumTrade(0);
        }
    }

    /**
     * Returns true iff a user has been flagged by the system
     * @param user The UserAccount to check
     * @return true iff the user is flagged
     */
    public boolean isFlagged(UserAccount user) {
        return flaggedUsers.contains(user.getUserID());
    }

}

