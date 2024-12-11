package edu.alexu.cse.dripmeup.handler;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Excpetion.HandlerException;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.Handler.ValidAdminUserNameHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidAdminUserNameHandlerTest {

    @Mock
    private AdminRepository mockAdminRepository;

    @BeforeEach
    void setUp() {
        mockAdminRepository = mock(AdminRepository.class);
    }

    @Test
    void testValidUserName() {
        // Arrange
        String validUserName = "new_admin";
        when(mockAdminRepository.findByUserName(validUserName)).thenReturn(null);

        // Act & Assert
        assertDoesNotThrow(() -> new ValidAdminUserNameHandler(validUserName, mockAdminRepository).handle()); 
        verify(mockAdminRepository, times(1)).findByUserName(validUserName);
    }

    @Test
    void testNullUserName() {
        // Arrange

        // Act & Assert
        HandlerException exception = assertThrows(HandlerException.class, () ->
                new ValidAdminUserNameHandler(null,mockAdminRepository).handle()
        );
        assertEquals("Invalid admin user name.", exception.getMessage());
    }

    @Test
    void testEmptyUserName() {
        // Arrange
        String emptyUserName = "";

        // Act & Assert
        HandlerException exception = assertThrows(HandlerException.class, () ->
                new ValidAdminUserNameHandler(emptyUserName, mockAdminRepository).handle()
        );
        assertEquals("Invalid admin user name.", exception.getMessage());
    }

    @Test
    void testExistingUserName() {
        // Arrange
        String existingUserName = "existing_admin";
        when(mockAdminRepository.findByUserName(existingUserName)).thenReturn(new AdminEntity());

        // Act & Assert
        HandlerException exception = assertThrows(HandlerException.class, () ->
                new ValidAdminUserNameHandler(existingUserName, mockAdminRepository).handle()
        );
        assertEquals("User name already exist.", exception.getMessage());
    }
}

