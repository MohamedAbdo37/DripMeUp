package edu.alexu.cse.dripmeup;
import edu.alexu.cse.dripmeup.enumeration.Role;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        Person person = new AdminService(adminRepository).createAdmin(mockNewAdmin);

        assertNull(person);
    }

    @Test
    void testCreateAdminWithHandlerException() {
        // Arrange
        doThrow(new HandlerException("Test Exception"))
                .when(mockAdmin).getRole();

        // Act
        Person result = new AdminService(adminRepository).createAdmin(mockNewAdmin);

        // Assert
        assertNull(result);
    }
}

