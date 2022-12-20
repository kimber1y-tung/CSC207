package EntitiesTests;

import Entities.Account;
import org.junit.Test;

public class AccountTest {

    //the Account constructor
    @Test(timeout = 50)
    public void testUserAccount() {
        Account u = new Account("hi");

    }

}
