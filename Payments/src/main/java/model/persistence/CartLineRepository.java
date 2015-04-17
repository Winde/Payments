package model.persistence;

import java.util.Date;
import java.util.List;

import model.dataobjects.CartLine;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import web.assisting.ShopProductEstimate;
import web.assisting.Statistic;

@Repository
public interface CartLineRepository extends CrudRepository<CartLine,Long>{

	public List<CartLine> findAll();
	
	public CartLine findOne(Long id);
	
	@Query("select distinct l.name from CartLine l where l.name is not null ")
	public List<String> findNames();
	
	@Query("SELECT new web.assisting.Statistic(l.name, sum(l.amount)) " +
		       "from CartLine l GROUP BY l.name")
	public List<Statistic> findStatistic();
	
	@Query("SELECT new web.assisting.Statistic(l.name, sum(l.amount*l.quantity)) " +
		       "from CartLine l "
		       + "where l.cart in (SELECT id from Payment p where p.date between ?1 and ?2)"
		       + "GROUP BY l.name")
	public List<Statistic> findStatistic(Date beginning, Date end);
	
	@Query("SELECT new web.assisting.Statistic(t.alias, sum(l.amount*l.quantity)) " +
		       "FROM CartLine as l,  CartLineTypeAlias as t "
		       + "WHERE l.name = t.productName "		       
		       + "GROUP BY t.alias")
	public List<Statistic> findStatisticByCategory();
		
	
	@Query("SELECT new web.assisting.Statistic(t.alias, sum(l.amount*l.quantity)) " +
		       "FROM CartLine as l,  CartLineTypeAlias as t "
		       + "WHERE l.name = t.productName and l.cart in (SELECT id from Payment p where p.date between ?1 and ?2) "		       
		       + "GROUP BY t.alias")
	public List<Statistic> findStatisticByCategory(Date beginning, Date end);
	
	@Query("SELECT new web.assisting.ShopProductEstimate(cart.shop,l.name,avg(l.amount)) "
			+ "FROM CartLine l "
			+ "INNER JOIN l.cart as cart "
			+ "WHERE cart.shop IS NOT NULL and cart.shop <> ''"
			+ "GROUP BY l.name,cart.shop "
			+ "ORDER BY l.name")
	public List<ShopProductEstimate> getComparison();
	
	
	

	

}

