package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.entity.Profile;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.ImageUploader;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.enumeration.Role;
import edu.alexu.cse.dripmeup.exception.BadInputException;
import edu.alexu.cse.dripmeup.enumeration.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserProfileService {
    UserRepository userRepository;
    ImageUploader imageUploader;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserProfileService(UserRepository userRepository,
            @Qualifier("cloudinaryUploader") ImageUploader imageUploader) {
        this.userRepository = userRepository;
        this.imageUploader = imageUploader;
    }

    public void updateUserInfo(Long userId, HashMap<String, String> body) throws Exception {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new Exception("User not found");

        if (!body.containsKey("username") || body.get("username") == null) {
            throw new BadInputException("Username is required");
        }
        user.setUserName(body.get("username"));

        if (!body.containsKey("email") || body.get("email") == null) {
            throw new BadInputException("Email is required");
        }
        user.setEmail(body.get("email"));
        UserEntity userWithSameEmail = userRepository.findByEmail(user.getEmail());
        if (userWithSameEmail != null && !userWithSameEmail.getUserID().equals(user.getUserID()))
            throw new BadInputException("Email already exists");

        if (!body.containsKey("gender") || body.get("gender") == null) {
            throw new BadInputException("gender is required");
        }
        try {
            user.setGender(Gender.valueOf(body.get("gender")));
        } catch (Exception e) {
            throw new BadInputException("gender is malformed");
        }

        if (body.containsKey("phoneNumber") && body.get("phoneNumber") != null) {
            user.setPhone(body.get("phoneNumber"));
        } else {
            user.setPhone(user.getPhone());
        }

        userRepository.save(user);

    }

    public Profile getUserProfile(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        return getTransferObject(user);
    }

    public static Profile getTransferObject(UserEntity user) {
        if (user == null)
            return null;
        Profile profile = new Profile();
        profile.setRole(Role.USER);
        profile.setUsername(user.getUserName());
        profile.setEmail(user.getEmail());
        profile.setGender(user.getGender());
        profile.setPhoneNumber(user.getPhone());
        profile.setProfilePhoto(user.getPhoto());
        return profile;
    }

    public String changeUserProfilePhoto(String id, byte[] photo) {
        return "POST request received with body: ";
    }

    public String changeUsername(String id, String name) {
        return "POST request received with body: ";
    }

    public void updateUserPhoto(Long userId, byte[] body) throws Exception {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new Exception("User not found");
        if (!ImageUploader.isValidImage(body))
            throw new BadInputException("Invalid image");
        if (user.getPhoto() == null) {
            user.setPhoto(imageUploader.uploadImage(body));
        } else {
            user.setPhoto(imageUploader.updateImage(user.getPhoto(), body));
        }
        userRepository.save(user);
    }

    public void deleteUserPhoto(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return;
        if (user.getPhoto() == null)
            return;
        try {
            imageUploader.deleteImage(user.getPhoto());
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setPhoto(null);
        userRepository.save(user);
    }

    public void updateUserPassword(Long userId, HashMap<String, String> body) throws BadInputException {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return;
        if (!body.containsKey("oldPassword") || body.get("oldPassword") == null) {
            throw new BadInputException("oldPassword is required");
        }
        if (!body.containsKey("newPassword") || body.get("newPassword") == null) {
            throw new BadInputException("newPassword is required");
        }
        if (!passwordEncoder.matches(body.get("oldPassword"), user.getPassword())) {
            throw new BadInputException("oldPassword is incorrect");
        }
        user.setPassword(passwordEncoder.encode(body.get("newPassword")));
        userRepository.save(user);
    }
}
