package model.dataobjects;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
public class User implements UserDetails {
	
	@Transient
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Id
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;
	
	@OneToMany(fetch = FetchType.EAGER)
	private List<Role> roles;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		// 
		return roles;
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = passwordEncoder.encode(password);
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String toString(){
		return this.getUsername();
	}
	
}
