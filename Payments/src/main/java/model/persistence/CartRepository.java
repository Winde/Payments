package model.persistence;

import java.util.List;

import model.dataobjects.Cart;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart,Long>{

	public List<Cart> findAll();
	
	public Cart findOne(Long id);
	
	@Query("select distinct c.shop from Cart c where c.shop is not null ")
	public List<String> findShops();
	
}
