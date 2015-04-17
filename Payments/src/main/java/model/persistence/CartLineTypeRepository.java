package model.persistence;

import java.util.List;

import model.dataobjects.CartLineTypeAlias;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartLineTypeRepository  extends CrudRepository<CartLineTypeAlias,Long> {

	
	public List<CartLineTypeAlias> findAll();
	
	public CartLineTypeAlias findOne(Long id);	
	
}
