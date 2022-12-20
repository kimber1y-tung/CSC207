package UsecaseTests;

import Entities.*;
import Usecase.TradeLogManager;
import Usecase.TradeManager;
import Usecase.TradeValidator;
import Usecase.UserManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TradeManagerTest {


    UserAccount user1;
    UserAccount user2;
    Item item1;
    Item item2;
    UserManager userManager;
    ItemInventory itemInventory;
    TradeLog tradeLog;
    TradeLogManager tradeLogManager;
    TradeValidator tradeValidator;
    TradeManager tradeManager;
    OneWayTrade trade1;
    TwoWayTrade trade2;

    @Before
    public void setUp() {
        user1 = new UserAccount("password");
        user1.setUserID(1);
        user2 = new UserAccount("password");
        user2.setUserID(2);

        tradeLog = new TradeLog(new HashMap<Integer, Trade>());
        tradeLogManager = new TradeLogManager(tradeLog);

        Map<Integer, UserAccount> allUsers = new HashMap<Integer, UserAccount>();
        userManager = new UserManager(allUsers, tradeLogManager);

        itemInventory = new ItemInventory(new ArrayList<Item>());

        tradeValidator = new TradeValidator(userManager, itemInventory);
        tradeManager = new TradeManager(userManager, itemInventory, tradeLogManager, tradeValidator, 6);

        item1 = new Item(1, "Rock", "a cool rock");
        item1.setItemID(1);
        item1.approve();
        item2 = new Item(2, "Shell", "a cool shell");
        item2.setItemID(2);
        item2.approve();

        allUsers.put(user1.getUserID(), user1);
        allUsers.put(user2.getUserID(), user2);
        user1.addToInventory(item1);
        user2.addToInventory(item2);
        user1.addToWishlist(item2);
        user2.addToWishlist(item1);

        trade1 = new OneWayTrade(item1.getItemID(), user1.getUserID(), user2.getUserID());
        trade1.setIsPermanent(false);
        trade1.setLocation("Toronto");
        trade1.setTime(LocalDateTime.of(2020, 07, 01, 2, 0));

        trade2 = new TwoWayTrade(item1.getItemID(), item2.getItemID(), user1.getUserID(), user2.getUserID());
        trade2.setIsPermanent(false);
        trade2.setLocation("Toronto");
        trade2.setTime(LocalDateTime.of(2020, 07, 01, 2, 0));

        itemInventory.addToApproved(item1);
        itemInventory.addToApproved(item2);
    }





    @Test
    public void TestUpdateStatus() {
        tradeManager.updateStatus(trade1, "requested");
        assertTrue("trade status not updated correctly", tradeLogManager.getStatusTrades("requested").contains(trade1));
        tradeManager.updateStatus(trade1, "open");
        assertEquals("trade status not updated", "open", trade1.getStatus());
        assertFalse("trade not removed from requested list", tradeLogManager.getStatusTrades("requested").contains(trade1));
        assertTrue("trade not added to open list", tradeLogManager.getStatusTrades("open").contains(trade1));
    }

    @Test
    public void TestAcceptTrade() {
        tradeManager.updateStatus(trade1, "requested");
        tradeManager.acceptTrade(trade1);
        assertEquals("trade's status is not pending", "pending", trade1.getStatus());
        assertFalse("trade not removed from requested list", tradeLogManager.getStatusTrades("requested").contains(trade1));
    }

    @Test
    public void TestDeclineTrade() {
        tradeManager.updateStatus(trade1, "requested");
        tradeManager.declineTrade(trade1);
        assertEquals("trade not set to declined", "declined", trade1.getStatus());
        assertTrue("trade not added to declined list", tradeLogManager.getStatusTrades("declined").contains(trade1));
        assertFalse("trade not removed from requested list", tradeLogManager.getStatusTrades("requested").contains(trade1));
    }

    @Test
    public void TestConfirmTrade() {
        tradeManager.updateStatus(trade1, "pending");
        tradeManager.confirmTrade(trade1);
        assertEquals("trade's status is not open", "open", trade1.getStatus());
        assertEquals("trade not removed from pending list", 0, tradeLogManager.getStatusTrades("pending").size());
    }


    @Test
    public void TestCompleteOneWayTrade() {
        tradeManager.updateStatus(trade1, "open");
        tradeManager.completeTrade(trade1);
        assertFalse("item not removed from user1 inventory", user1.getInventory().contains(item1));
        assertFalse("item not removed from user2 wishlist", user2.getWishlist().contains(item1));
        assertEquals("user1 overBorrowed not decreased", -1, user1.getOverBorrowed());
        assertEquals("user2 overBorrowed not increased", 1, user2.getOverBorrowed());
        assertFalse("first trade not removed from open list", tradeLogManager.getStatusTrades("open").contains(trade1));
        assertEquals("second trade not added to open list", 1, tradeLogManager.getStatusTrades("open").size());
    }

    @Test
    public void TestCompleteTwoWayTrade() {
        tradeManager.updateStatus(trade2, "open");
        tradeManager.completeTrade(trade2);
        assertFalse("item1 not removed from user1 inventory", user1.getInventory().contains(item1));
        assertFalse("item1 not removed from user2 wishlist", user2.getWishlist().contains(item1));
        assertFalse("item2 not removed from user2 inventory", user2.getInventory().contains(item2));
        assertFalse("item2 not removed from user1 wishlist", user1.getWishlist().contains(item2));
        assertEquals("user1 overBorrowed should not change", 0, user1.getOverBorrowed());
        assertEquals("user2 overBorrowed should not change", 0, user2.getOverBorrowed());
        assertFalse("first trade not removed from open list", tradeLogManager.getStatusTrades("open").contains(trade1));
        assertEquals("second trade not added to open list", 1, tradeLogManager.getStatusTrades("open").size());
    }

    public void TestIncompleteTrade() {
        tradeManager.updateStatus(trade2, "open");
        tradeLogManager.getUserTrades(user1.getUserID()).add(trade2);
        tradeLogManager.getUserTrades(user2.getUserID()).add(trade2);
        tradeManager.incompleteTrade(trade2);
        assertFalse("trade not removed from tradelog", tradeLog.getTradeHistory().containsValue(trade2));
        assertFalse("trade not removed from user1 trades", tradeLogManager.getUserTrades(user1.getUserID()).contains(trade2));
        assertFalse("trade not removed from user2 trades", tradeLogManager.getUserTrades(user2.getUserID()).contains(trade2));
        assertFalse("trade not removed from open trades", tradeLogManager.getStatusTrades("open").contains(trade2));
        assertEquals("user1 incomplete counter not increased", 1, user1.getIncompleteTrade());
        assertEquals("user2 incomplete counter not increased", 1, user2.getIncompleteTrade());

    }

}
