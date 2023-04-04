package com.gzella.coinMarketSupervisor.persistence.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long userId;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String role;
    @Column
    private BigDecimal externalFunds;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<WalletEntry> walletEntries;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Transaction> transactions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this::getRole);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", externalFunds=" + externalFunds +
                ", walletEntries=" + walletEntries +
                ", transactions=" + transactions +
                '}';
    }
}