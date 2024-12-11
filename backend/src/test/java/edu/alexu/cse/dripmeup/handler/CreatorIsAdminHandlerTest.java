package edu.alexu.cse.dripmeup.handler;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Excpetion.HandlerException;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.Handler.CreatorIsAdminHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.alexu.cse.dripmeup.Enumeration.Role;

class CreatorIsAdminHandlerTest {

    private Person mockPerson;
    private AdminEntity mockAdminEntity;

    @Autowired
    private AdminRepository mockAdminRepository;


    @BeforeEach
    void setUp() {
        mockPerson = mock(Person.class);
        mockAdminEntity = mock(AdminEntity.class);
        mockAdminRepository = mock(AdminRepository.class);
    }

    @Test
    void testHandleNullPerson() {
        HandlerException exception = assertThrows(HandlerException.class, () ->
            new CreatorIsAdminHandler(null, mockAdminEntity, mockAdminRepository).handle()
        );
        assertEquals("Invalid input.", exception.getMessage());
    }

    @Test
    void testHandleNullAdmin() {
        HandlerException exception = assertThrows(HandlerException.class, () -> 
            new CreatorIsAdminHandler(mockPerson, null, mockAdminRepository).handle()
        );

        assertEquals("Invalid input.", exception.getMessage());
    }


    @Test
    void testHandleNonAdminPerson() {
        // Arrange
        when(mockPerson.getRole()).thenReturn(Role.USER);
        when(mockPerson.getUsername()).thenReturn("non_admin");

        // Act & Assert
        HandlerException exception = assertThrows(HandlerException.class, () ->
                new CreatorIsAdminHandler(mockPerson, mockAdminEntity, mockAdminRepository).handle()
        );
        assertEquals(
                "User non_admin can't create admin account.",
                exception.getMessage()
        );
    }

    @Test
    void testHandleRightState() {
        // Arrange
        when(mockPerson.getRole()).thenReturn(Role.ADMIN);
        when(mockAdminEntity.getUserName()).thenReturn("admin");
        when(mockAdminRepository.findByUserName("admin")).thenReturn(null);

        // Act & Assert

        assertDoesNotThrow(() ->
                new CreatorIsAdminHandler(mockPerson, mockAdminEntity, mockAdminRepository).handle()
        );
        verify(mockAdminRepository, times(1)).findByUserName("admin");
    }

}
