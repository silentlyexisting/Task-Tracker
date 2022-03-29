package hexlet.code.service.impl;

import hexlet.code.dto.UserDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static hexlet.code.config.security.SecurityConfig.AUTHORITIES;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createNewUser(final UserDto userDto) {
        return userRepository.save(new User(
                userDto.getEmail(),
                userDto.getFirstName(),
                userDto.getLastName(),
                passwordEncoder.encode(userDto.getPassword())
        ));
    }

    @Override
    public User updateExistingUser(final long id, final UserDto dto) {
        final User userToUpdate = userRepository.findById(id)
                        .orElseThrow(() -> new CustomNotFoundException("User"));
        userToUpdate.setEmail(dto.getEmail());
        userToUpdate.setFirstName(dto.getFirstName());
        userToUpdate.setLastName(dto.getLastName());
        userToUpdate.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(userToUpdate);
    }

    @Override
    public User getCurrentUser() {
        final var username = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(username.toString()).get();
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(this::convertToSpringUser)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + username));
    }

    private UserDetails convertToSpringUser(final User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                AUTHORITIES
        );
    }
}
