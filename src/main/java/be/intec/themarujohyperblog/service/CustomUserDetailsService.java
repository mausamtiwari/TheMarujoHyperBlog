package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username or Password not found");
        }
        return new CustomUserDetail(
                user.get().getUsername(),
                user.get().getPassword(),
                authorities(),
                user.get().getFirstName(),
                user.get().getLastName());
    }

    public Collection<? extends GrantedAuthority> authorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }
}
