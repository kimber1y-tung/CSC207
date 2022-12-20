package EntitiesTests;

import Entities.AdminAccount;
import org.junit.*;

public class AdminUserTest {

    //the AdminUser constructor
    @Test(timeout = 50)
    public void testAdminUser() {
        AdminAccount au = new AdminAccount("admin");
    }

}
