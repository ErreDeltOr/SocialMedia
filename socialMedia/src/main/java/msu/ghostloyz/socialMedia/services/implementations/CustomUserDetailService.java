package msu.ghostloyz.socialMedia.services.implementations;

import lombok.RequiredArgsConstructor;
import msu.ghostloyz.socialMedia.repo.interfaces.UserDAO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserDAO userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.getUserByEmail(email);
    }
}
