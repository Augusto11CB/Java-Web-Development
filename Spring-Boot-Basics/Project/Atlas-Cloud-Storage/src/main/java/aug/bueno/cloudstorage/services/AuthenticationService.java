package aug.bueno.cloudstorage.services;

import aug.bueno.cloudstorage.model.User;
import aug.bueno.cloudstorage.repository.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {

    private UserMapper userMapper;
    private HashService hashService;

    public AuthenticationService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        // TODO - Implement authentication method

//        final User user = userMapper.getUserByName(username);
//
//        if (user != null) {
//            final String encodedSalt = user.getSalt();
//            final String hashedPassword = hashService.getHashedValue(password, encodedSalt);
//            if (user.getPassword().equals(hashedPassword)) {
//                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
//            }
//        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
