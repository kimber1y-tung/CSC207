package UsecaseTests;

import Entities.*;
import Usecase.TradeLogManager;
import Usecase.TradeValidator;
import Usecase.UserManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TradeValidatorTest {
    UserAccount user1;
    UserAccount user2;
    Item item1;
    Item item2;
    UserManager userManager;
    ItemInventory itemInventory;
    TradeValidator tradeValidator;
    OneWayTrade trade1;
    TwoWayTrade trade2;


    @Before
    public void setUp() {
        user1 = new UserAccount("password");
        user1.setUserID(1);
        user2 = new UserAccount("password");
        user2.setUserID(2);

        TradeLog tradeLog = new TradeLog(new HashMap<Integer, Trade>());
        TradeLogManager tradeLogManager = new TradeLogManager(tradeLog);

        Map<Integer, UserAccount> allUsers = new HashMap<Integer, UserAccount>();
        userManager = new UserManager(allUsers, tradeLogManager);
        itemInventory = new ItemInventory(new ArrayList<Item>());
        tradeValidator = new TradeValidator(userManager, itemInventory);

        item1 = new Item(1, "Rock", "a cool rock");
        item1.setItemID(0);
        item1.approve();
        item2 = new Item(2, "Shell", "a cool shell");
        item2.setItemID(1);
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
    public void TestValidateOneWayTrade() {
        assertTrue(tradeValidator.validateTrade(trade1));
    }

    @Test
    public void TestValidateTwoWayTrade() {
        assertTrue(tradeValidator.validateTrade(trade2));
    }
}

