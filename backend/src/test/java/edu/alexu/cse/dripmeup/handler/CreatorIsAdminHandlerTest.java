package edu.alexu.cse.dripmeup.handler;

import edu.alexu.cse.dripmeup.enumeration.Role;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.exception.HandlerException;
import edu.alexu.cse.dripmeup.repository.AdminRepository;
import edu.alexu.cse.dripmeup.service.handler.CreatorIsAdminHandler;

class CreatorIsAdminHandlerTest {

    @Mock
    private Person mockPerson;
    @Mock
    private AdminEntity mockAdminEntity;

    @Mock
    private AdminRepository mockAdminRepository;


    @BeforeEach
    void setUp() {
        mockPerson = mock(Person.class);
        mockAdminEntity = mock(AdminEntity.class);
        mockAdminRepository = mock(AdminRepository.class);
    }

    @Test
    void testHandleNullPerson() {
        @SuppressWarnings("static-access")
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
