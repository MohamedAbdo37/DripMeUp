package edu.alexu.cse.dripmeup.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.repository.AdminRepository;
import edu.alexu.cse.dripmeup.service.builder.AdminPersonBuilder;

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
        // Person person = adminPersonBuilder.build();

        // Assert
        // assertEquals(mockPerson, person);
    }

}
