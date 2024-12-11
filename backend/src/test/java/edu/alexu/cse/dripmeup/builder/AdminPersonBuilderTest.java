package edu.alexu.cse.dripmeup.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.Builder.AdminPersonBuilder;

public class AdminPersonBuilderTest {

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
    void testBuild() {
        // Arrange

        AdminPersonBuilder adminPersonBuilder = new AdminPersonBuilder(mockAdminEntity, mockAdminRepository);

        // Act
        Person person = adminPersonBuilder.build();

        // Assert
        assertEquals(mockPerson, person);
    }

}
