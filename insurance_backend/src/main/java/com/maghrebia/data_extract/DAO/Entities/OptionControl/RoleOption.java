package com.maghrebia.data_extract.DAO.Entities.OptionControl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleOption {

    Admin(Set.of(PrivilegeOption.READ_PRIVILEGE,
            PrivilegeOption.WRITE_PRIVILEGE,
            PrivilegeOption.UPDATE_PRIVILEGE,
            PrivilegeOption.DELETE_PRIVILEGE)),
    Agent(Set.of(PrivilegeOption.READ_PRIVILEGE,
            PrivilegeOption.WRITE_PRIVILEGE,
            PrivilegeOption.UPDATE_PRIVILEGE));

    @Getter
    private final Set<PrivilegeOption> privileges;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPrivileges()
                .stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name())); // ROLE_ prefix
        return authorities;
    }
}
