package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
        import edu.alexu.cse.dripmeup.Repository.AdminRepository;
        import edu.alexu.cse.dripmeup.Service.AdminService;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.MockitoAnnotations;

        import static org.junit.jupiter.api.Assertions.assertFalse;
        import static org.junit.jupiter.api.Assertions.assertTrue;
        import static org.mockito.Mockito.when;

public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAdminLogin_Success() {
        String userName = "admin";
        String password = "password";
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(password);

        when(adminRepository.findByUserName(userName)).thenReturn(admin);

        boolean result = adminService.adminLogin(userName, password);
        assertTrue(result);
    }

    @Test
    public void testAdminLogin_WrongUsername() {
        String userName = "admin";
        String password = "password";
        String wrongUsername = "admon";
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(password);

        when(adminRepository.findByUserName(userName)).thenReturn(admin);

        boolean result = adminService.adminLogin(wrongUsername, password);
        assertFalse(result);
    }

    @Test
    public void testAdminLogin_WrongPassword() {
        String userName = "admin";
        String password = "password";
        String wrongPassword = "pasword";
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(password);

        when(adminRepository.findByUserName(userName)).thenReturn(admin);

        boolean result = adminService.adminLogin(userName, wrongPassword);
        assertFalse(result);
    }

    @Test
    public void testAdminLogin_UserNotAdmin() {
        String userName = "nonexistent";
        String password = "password";

        when(adminRepository.findByUserName(userName)).thenReturn(null);

        boolean result = adminService.adminLogin(userName, password);
        assertFalse(result);
    }
}