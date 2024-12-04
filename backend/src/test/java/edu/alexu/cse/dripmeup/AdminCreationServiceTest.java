package edu.alexu.cse.dripmeup;
import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Enumeration.Role;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.AdminCreationService;
import edu.alexu.cse.dripmeup.Service.Handler.CreatorIsAdminHandler;
import edu.alexu.cse.dripmeup.Service.Handler.HandlerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminCreationServiceTest {

    @Mock
    private Person mockAdmin;

    @Mock
    private AdminEntity mockNewAdmin;

    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAdminWithValidInput() {
        // Arrange
        when(mockAdmin.getRole()).thenReturn(Role.ADMIN);

        // Act
        Person result = AdminCreationService.createAdmin(mockAdmin, mockNewAdmin);

        // Assert
//        assertNotNull(result);
        verify(mockAdmin, times(1)).getRole();
    }

    @Test
    void testCreateAdminWithNonAdminCreator() {
        // Arrange
        when(mockAdmin.getRole()).thenReturn(Role.USER);

        // Act & Assert
        assertThrows(HandlerException.class, () ->
                new CreatorIsAdminHandler(mockAdmin, mockNewAdmin, adminRepository).handle()
        );
    }

    @Test
    void testCreateAdminWithNullCreator() {
        // Act & Assert

        Person person = AdminCreationService.createAdmin(null, mockNewAdmin);

        assertNull(person);
    }

    @Test
    void testCreateAdminWithHandlerException() {
        // Arrange
        doThrow(new HandlerException("Test Exception"))
                .when(mockAdmin).getRole();

        // Act
        Person result = AdminCreationService.createAdmin(mockAdmin, mockNewAdmin);

        // Assert
        assertNull(result);
    }
}

