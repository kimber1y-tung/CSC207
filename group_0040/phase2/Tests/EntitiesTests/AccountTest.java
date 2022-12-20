package EntitiesTests;

import accounts.Account;
import org.junit.Test;

/**
 * @author Rutwa Engineer
 */
public class AccountTest {

    //the Account constructor
    @Test(timeout = 50)
    public void testUserAccount() {
        Account u = new Account("hi");

    }

}
