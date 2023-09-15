package llipport.mc_questions.auth;

import llipport.mc_questions.database.entities.AppUser;
import llipport.mc_questions.database.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        AppUser user = userRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException(name);
        }
        return new CustomUserPrincipal(user);
    }
}
