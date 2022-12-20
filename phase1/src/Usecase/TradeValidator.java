package Usecase;

import Entities.*;


public class TradeValidator {
    UserManager userManager;
    ItemInventory itemInventory;

    /**
     * The constructor.
     * @param userManager UserManager object
     * @param itemInventory ItemInventory object.
     */
    public TradeValidator(UserManager userManager, ItemInventory itemInventory) {
        this.userManager = userManager;
        this.itemInventory = itemInventory;
    }


    /**
     * Returns true iff a OneWayTrade is valid. A trade is valid if neither user is Frozen, the item is approved, and
     * the item appears in the correct inventory and wishlist.
     * @param trade the trade to be validated
     * @return true iff the trade is valid
     */
    public boolean validateTrade(OneWayTrade trade) {
        UserAccount lender = userManager.getUser(trade.getUser1ID());
        UserAccount borrower = userManager.getUser(trade.getUser2ID());
        Item item = itemInventory.getItemWithID(trade.getItemID());

        if (lender.isFrozen() || borrower.isFrozen()) {
            return false;
        }

        if (!item.isApproved() || !(lender.ownsItem(item))) {
            return false;
        }

        return true;
    }


    /**
     * Returns true iff a TwoWayTrade is valid. A trade is valid if neither user is Frozen, both items are approved, and
     * each item appears in the correct inventory and wishlist. Returns true iff the trade is balid.
     * @param trade the trade to be validated
     * @return true iff the trade is valid
     * */
    public boolean validateTrade(TwoWayTrade trade) {
        UserAccount user1 = userManager.getUser(trade.getUser1ID());
        UserAccount user2 = userManager.getUser(trade.getUser2ID());
        Item item1 = itemInventory.getItemWithID(trade.getItem1ID());
        Item item2 = itemInventory.getItemWithID(trade.getItem2ID());

        if (user1.isFrozen() || user2.isFrozen()) {
            return false;
        }

        if (!item1.isApproved() || !user1.ownsItem(item1)) { //not checking user2 wishlist because they are requesting this trade
            return false;
        }

        if (!item2.isApproved() || !user1.wantsItem(item2) || !user2.ownsItem(item2)) {
            return false;
        }

        return true;
    }
}
