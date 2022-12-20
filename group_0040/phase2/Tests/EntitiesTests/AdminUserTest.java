package EntitiesTests;

import accounts.AdminAccount;
import org.junit.*;

/**
 * @author Rutwa Engineer
 */
public class AdminUserTest {

    //the AdminUser constructor
    @Test(timeout = 50)
    public void testAdminUser() {
        AdminAccount au = new AdminAccount("blerg");
    }

}
