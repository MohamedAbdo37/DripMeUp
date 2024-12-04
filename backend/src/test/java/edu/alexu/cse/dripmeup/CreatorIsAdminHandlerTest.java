package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Enumeration.Role;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.Handler.CreatorIsAdminHandler;
import edu.alexu.cse.dripmeup.Service.Handler.HandlerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatorIsAdminHandlerTest {

    private Person mockPerson;
    private AdminEntity mockAdminEntity;

    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        mockPerson = mock(Person.class);
        mockAdminEntity = mock(AdminEntity.class);
    }

    @Test
    void testHandleWithNonAdminCreator() {
        // Arrange
        when(mockPerson.getRole()).thenReturn(Role.USER);
        when(mockPerson.getUsername()).thenReturn("non_admin");

        // Act & Assert
        HandlerException exception = assertThrows(HandlerException.class, () ->
                new CreatorIsAdminHandler(mockPerson, mockAdminEntity, adminRepository).handle()
        );
        assertEquals(
                "CreatorIsAdminHandler:User non_admin can't create admin account.",
                exception.getMessage()
        );
    }

    @Test
    void testHandleWithNullCreator() {
        // Act & Assert
        HandlerException exception = assertThrows(HandlerException.class, () ->
                new CreatorIsAdminHandler(null, mockAdminEntity,adminRepository).handle()
        );
        assertEquals("CreatorIsAdminHandler:Invalid input.", exception.getMessage());
    }

}
