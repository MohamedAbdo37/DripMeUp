package edu.alexu.cse.dripmeup;
import edu.alexu.cse.dripmeup.enumeration.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.exception.HandlerException;
import edu.alexu.cse.dripmeup.repository.AdminRepository;
import edu.alexu.cse.dripmeup.service.AdminService;
import edu.alexu.cse.dripmeup.service.handler.CreatorIsAdminHandler;

class AdminCreationServiceTest {

    @Mock
    private Person mockAdmin;

    @Mock
    private AdminEntity mockNewAdmin;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAdminWithValidInput() {
        // Arrange
        when(mockAdmin.getRole()).thenReturn(Role.ADMIN);

        // Act
        Person result = adminService.createAdmin(mockNewAdmin);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testCreateAdminWithNonAdminCreator() {
        // Arrange
        when(mockAdmin.getRole()).thenReturn(Role.USER);

        // Act
        Person result = adminService.createAdmin(mockNewAdmin);

        // Assert
        assertNull(result);
    }

    @Test
    void testCreateAdminWithNullCreator() {
        // Act & Assert

        Person person = adminService.createAdmin(mockNewAdmin);

        assertNull(person);
    }

    @Test
    void testCreateAdminWithHandlerException() {
        // Arrange
        doThrow(new HandlerException("Test Exception"))
                .when(mockAdmin).getRole();

        // Act
        Person result = adminService.createAdmin(mockNewAdmin);

        // Assert
        assertNull(result);
    }
}

