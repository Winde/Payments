package model.persistence;

import java.util.Date;
import java.util.List;

import model.dataobjects.IncomeEntry;
import model.dataobjects.Payment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends CrudRepository<IncomeEntry,Long> {
		
	public List<IncomeEntry> findAll();
	
	@Query("select t from IncomeEntry t " +
	         "where t.date between ?1 and ?2")
	public List<IncomeEntry> findByDateBetween(Date beginning, Date end);

	@Query("select i from IncomeEntry i " +
	         "where i.date > ?1")
	public List<IncomeEntry> findAfter(Date date);
}

