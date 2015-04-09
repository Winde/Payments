package model.persistence;

import java.util.Date;
import java.util.List;

import model.dataobjects.CartLine;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
	
	@Query("SELECT new web.assisting.Statistic(l.name, sum(l.amount)) " +
		       "from CartLine l "
		       + "where l.cart in (SELECT id from Payment p where p.date between ?1 and ?2)"
		       + "GROUP BY l.name")
	public List<Statistic> findStatistic(Date beginning, Date end);
}

