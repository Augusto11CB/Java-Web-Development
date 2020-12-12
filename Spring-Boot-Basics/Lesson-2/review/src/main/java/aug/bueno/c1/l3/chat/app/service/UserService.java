package aug.bueno.c1.l3.chat.app.service;

import aug.bueno.c1.l3.chat.app.model.User;
import aug.bueno.c1.l3.chat.app.repository.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUserByName(username) == null;
    }

    public int createUser(User user) {
        final SecureRandom random = new SecureRandom();
        final byte[] salt = new byte[16];
        random.nextBytes(salt);
        final String encodedSalt = Base64.getEncoder().encodeToString(salt);
        final String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insert(new User(0, user.getUsername(), encodedSalt, hashedPassword, user.getFirstname(), user.getLastname()));
    }

    public User getUser(String username) {
        return userMapper.getUserByName(username);
    }
}
