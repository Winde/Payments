package model.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.metamodel.binding.CascadeType;

@Entity
@Table(
	uniqueConstraints=
		@UniqueConstraint(columnNames="productName")	
)

public class CartLineTypeAlias implements Comparable<CartLineTypeAlias>{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true, nullable=true )
	private String productName;
	
	@Column(nullable=false)
	private String alias;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public int compareTo(CartLineTypeAlias o) {
		if (productName==null){
			return -1;
		} else {
			return this.productName.compareTo(o.productName);			
		}
	}
	
	
}
