package com.example.security.security;

import com.example.security.model.UserEntity;
import com.example.security.repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component // obligatory implementation UserDetailService!
public class DemoUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public DemoUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override // at Login send username&pass, to find such user in db
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepo.findByName(username).orElseThrow(
                () -> new UsernameNotFoundException("User with name " + username + " was not found."));
        return mapToUserDetails(userEntity);
    }

    private UserDetails mapToUserDetails(UserEntity userEntity) {
        List<SimpleGrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(userRoleEntity -> new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getRole().name()))
                .collect(Collectors.toList());
        return new User(userEntity.getName(), userEntity.getPassword(), authorities);
    } // return User(org.springframework.security)
}
