package com.docmanager.security;

import com.docmanager.constants.ErrorCode;
import com.docmanager.model.entity.Users;
import com.docmanager.repository.users.UsersRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadUserDetailsService implements UserDetailsService {
  private final UsersRepository usersRepository;
  @Override

  public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
    Users user = usersRepository.findByAccount(account)
        .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.message()));

    List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
        .toList();

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        user.getEnabled(),
        true,
        true,
        true,
        authorities);
  }
}
