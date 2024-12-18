package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.entity.Profile;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.enumeration.Gender;
import edu.alexu.cse.dripmeup.exception.BadInputException;
import edu.alexu.cse.dripmeup.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageUploader imageUploader;

    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userProfileService = new UserProfileService(userRepository, imageUploader);
    }

    @Test
    void testUpdateUserInfo() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserID(1L);
        user.setUserName("oldUsername");
        user.setEmail("oldEmail@example.com");
        user.setPassword("oldPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("newEmail@example.com")).thenReturn(null);

        HashMap<String, String> body = new HashMap<>();
        body.put("username", "newUsername");
        body.put("email", "newEmail@example.com");
        body.put("gender", "MALE");
        body.put("oldPassword", "oldPassword");
        body.put("newPassword", "newPassword");

        userProfileService.updateUserInfo(1L, body);

        assertEquals("newUsername", user.getUserName());
        assertEquals("newEmail@example.com", user.getEmail());
        assertEquals(Gender.MALE, user.getGender());
        assertEquals("newPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testGetUserProfile() {
        UserEntity user = new UserEntity();
        user.setUserID(1L);
        user.setUserName("username");
        user.setEmail("email@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Profile profile = userProfileService.getUserProfile(1L);

        assertNotNull(profile);
        assertEquals("username", profile.getUsername());
        assertEquals("email@example.com", profile.getEmail());
    }

    @Test
    void testUpdateInvalidUserPhoto() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserID(1L);
        user.setPhoto("oldPhotoUrl");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(imageUploader.uploadImage(any())).thenReturn("newPhotoUrl");

        byte[] photo = new byte[]{1, 2, 3};

        assertThrows(BadInputException.class, () -> userProfileService.updateUserPhoto(1L, null));

    }

    @Test
    void testDeleteUserPhoto() {
        UserEntity user = new UserEntity();
        user.setUserID(1L);
        user.setPhoto("photoUrl");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userProfileService.deleteUserPhoto(1L);

        assertNull(user.getPhoto());
        verify(userRepository).save(user);
    }

}