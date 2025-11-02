package in.splitgen.service;

import in.splitgen.entity.User;
import in.splitgen.model.UserInfo;
import in.splitgen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // New helper to update user info based on authenticated email
    public UserInfo updateUserInfo(UserInfo updates, String email) {
        Optional<User> optionalUser = findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

        User user = optionalUser.get();

        if (updates.getName() != null) {
            user.setName(updates.getName());
        }
        if (updates.getSex() != null) {
            user.setSex(updates.getSex());
        }
        if (updates.getMobileNo() != null) {
            user.setMobileNo(updates.getMobileNo());
        }
        if (updates.getUpiId() != null) {
            user.setUpiId(updates.getUpiId());
        }
        if (updates.getDob() != null) {
            try {
                LocalDate dob = LocalDate.parse(updates.getDob());
                user.setDob(dob);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format for dob. Use yyyy-MM-dd");
            }
        }

        User saved = updateUser(user);

        UserInfo result = new UserInfo();
        result.setName(saved.getName());
        result.setSex(saved.getSex());
        result.setMobileNo(saved.getMobileNo());
        result.setUpiId(saved.getUpiId());
        result.setDob(saved.getDob() != null ? saved.getDob().toString() : null);

        return result;
    }
}
