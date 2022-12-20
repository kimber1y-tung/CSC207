package UsecaseTests;
import Entities.*;
import Usecase.TradeLogManager;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TradeLogManagerTest {

    Trade trade1;
    Trade trade2;
    Trade trade3;
    Trade trade4;
    Trade trade5;
    TradeLog tLog;
    TradeLogManager tLM;

    @Before
    public void setUp() {
        trade1 = new Trade(new Integer(1), new Integer(2));
        trade1.setStatus("requested");
        trade1.setIsPermanent(false);
        trade1.setLocation("Toronto");
        trade1.setNumEdits(3);
        trade1.setTime(LocalDateTime.of(2020, 07, 01, 2, 0));trade1.setLastEditorID(1);

        trade2 = new Trade(new Integer(3), new Integer(4));
        trade2.setStatus("pending");
        trade2.setIsPermanent(false);
        trade2.setLocation("Mississauga");
        trade2.setNumEdits(1);
        trade2.setTime(LocalDateTime.of(2020, 07, 01, 2, 0));
        trade2.setLastEditorID(3);

        trade3 = new Trade(new Integer(5), new Integer(6));
        trade3.setStatus("open");
        trade3.setIsPermanent(false);
        trade3.setLocation("Brampton");
        trade3.setNumEdits(4);
        trade3.setTime(LocalDateTime.of(2020, 07, 01, 2, 0));
        trade3.setLastEditorID(5);

        trade4 = new Trade(new Integer(1), new Integer(6));
        trade4.setStatus("complete");
        trade4.setIsPermanent(false);
        trade4.setLocation("Scarborough");
        trade4.setNumEdits(3);
        trade4.setTime(LocalDateTime.of(2020, 07, 01, 2, 0));
        trade4.setLastEditorID(6);

        trade5 = new Trade(new Integer(2), new Integer(7));
        trade5.setStatus("complete");
        trade5.setIsPermanent(false);
        trade5.setLocation("North York");
        trade5.setNumEdits(3);
        trade5.setTime(LocalDateTime.of(2020, 07, 01, 2, 0));
        trade5.setLastEditorID(7);

        tLog = new TradeLog(new HashMap<Integer, Trade>());
        tLog.addTrade(new Integer (1), trade1);
        tLog.addTrade(new Integer (2), trade2);
        tLog.addTrade(new Integer (3), trade3);
        tLog.addTrade(new Integer (4), trade4);
        tLM = new TradeLogManager(tLog);
    }




    @Test
    public void TestGetStatusDictionary() {
        assertEquals("getStatusDictionary()", tLM.getStatusDictionary().get("requested").get(0).getStatus(), "requested");
        assertEquals("getStatusDictionary()", tLM.getStatusDictionary().get("pending").get(0).getStatus(), "pending");
        assertEquals("getStatusDictionary()", tLM.getStatusDictionary().get("open").get(0).getStatus(), "open");
        assertEquals("getStatusDictionary()", tLM.getStatusDictionary().get("complete").get(0).getStatus(), "complete");
    }

    @Test
    public void TestGetUserDictionary() {
        assertEquals("getUserDictionary()", tLM.getUserDictionary().get(new Integer (2)).isEmpty(), false);
    }

    @Test
    public void TestGetUserTrades() {
        assertEquals("getUserTrades()", tLM.getUserTrades(new Integer (3)).isEmpty(), false);
       }

    @Test
    public void TestGetStatusTrades() {
        assertEquals("getStatusTrades()", tLM.getStatusTrades("complete").get(0).getLastEditorID(), new Integer(6));
        assertEquals("getStatusTrades()", tLM.getStatusTrades("pending").get(0).getUser1ID(), new Integer(3));
        assertEquals("getStatusTrades()", tLM.getStatusTrades("open").get(0).getNumEdits(), 4);
        assertEquals("getStatusTrades()", tLM.getStatusTrades("requested").get(0).getLastEditorID(), new Integer(1));
    }

    @Test
    public void TestAddTrade() {
        tLM.addTrade(new Integer (1), trade5);
        assertEquals("addTrade()", tLM.getUserDictionary().get(new Integer (7)).get(0).getLocation(), "North York");
     }

    @Test
    public void TestRemoveTrade() {

        tLM.removeTrade(trade4);
        assertEquals("removeTrade()", tLM.getStatusDictionary().get("complete").isEmpty(), true);
        assertEquals("removeTrade()", tLM.getStatusDictionary().get("open").isEmpty(), false);
        assertEquals("removeTrade()", tLM.getStatusDictionary().get("requested").isEmpty(), false);
        assertEquals("removeTrade()", tLM.getStatusDictionary().get("pending").isEmpty(), false);
    }



    @Test
    public void TestUpdateStatus() {
        tLM.updateStatus(trade3, "open");
        assertEquals("upgradeStatus(trade3, open)", tLM.getStatusDictionary().get("open").get(0).getLocation(), "Brampton");
        assertEquals("upgradeStatus(trade2, complete)", tLM.getStatusDictionary().get("complete").get(0).getLocation(), "Scarborough");
        assertEquals("upgradeStatus(trade1, open)", tLM.getStatusDictionary().get("pending").get(0).getLocation(), "Mississauga");
        assertEquals("upgradeStatus(trade4, open)", tLM.getStatusDictionary().get("requested").get(0).getLocation(), "Toronto");

    }
}