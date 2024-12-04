package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.Handler.HandlerException;
import edu.alexu.cse.dripmeup.Service.Handler.ValidAdminUserNameHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidAdminUserNameHandlerTest {

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidUserName() {
        // Arrange
        String validUserName = "new_admin";
        when(adminRepository.findByUserName(validUserName)).thenReturn(List.of());

        // Act & Assert
        assertDoesNotThrow(() -> new ValidAdminUserNameHandler(validUserName, adminRepository) {
        });
    }

    @Test
    void testNullUserName() {
        // Arrange

        // Act & Assert
        HandlerException exception = assertThrows(HandlerException.class, () ->
                new ValidAdminUserNameHandler(null,adminRepository) {
                }
        );
        assertEquals("Invalid admin user name.", exception.getMessage());
    }

    @Test
    void testEmptyUserName() {
        // Arrange
        String emptyUserName = "";

        // Act & Assert
        HandlerException exception = assertThrows(HandlerException.class, () ->
                new ValidAdminUserNameHandler(emptyUserName, adminRepository)
        );
        assertEquals("Invalid admin user name.", exception.getMessage());
    }

    @Test
    void testExistingUserName() {
        // Arrange
        String existingUserName = "existing_admin";
        when(adminRepository.findByUserName(existingUserName)).thenReturn(List.of(new AdminEntity()));

        // Act & Assert
        HandlerException exception = assertThrows(HandlerException.class, () ->
                new ValidAdminUserNameHandler(existingUserName,adminRepository)
        );
        assertEquals("User name already exist.", exception.getMessage());
    }
}

