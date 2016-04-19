package com.restaurante.persistence;

import com.restaurante.persistence.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alex on 07.05.15.
 */
@Service
@Qualifier("customUserDetailsService")
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        com.restaurante.persistence.User user = userDao.findByUsername(username);
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());

        return buildUserForAuthentication(user, authorities);

    }

    private User buildUserForAuthentication(com.restaurante.persistence.User user,
                                            List<GrantedAuthority> authorities) {
        return new User(user.getLogin(), user.getPassword(), authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        // Build user's authorities
        for (UserRole userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        }

        return new ArrayList<GrantedAuthority>(setAuths);
    }
}