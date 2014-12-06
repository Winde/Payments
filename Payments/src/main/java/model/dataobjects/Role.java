package model.dataobjects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role implements GrantedAuthority{

	@Id
	private String id;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return id;
	}

	public String toString(){
		return id;
	}
	
	public int compareTo(GrantedAuthority ga) {
        if (ga != null) {
            String rhsRole = ga.getAuthority();

            if (rhsRole == null) {
                return -1;
            }

            return id.compareTo(rhsRole);
        }
        return -1;
    }
	
	public boolean equals(Object obj) {
        if (obj instanceof String) {
            return obj.equals(this.id);
        }

        if (obj instanceof GrantedAuthority) {
            GrantedAuthority attr = (GrantedAuthority) obj;

            return this.id.equals(attr.getAuthority());
        }

        return false;
    }
}
