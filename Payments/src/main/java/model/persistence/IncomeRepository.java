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
		
	List<IncomeEntry> findAll();
	
	@Query("select t from IncomeEntry t " +
	         "where t.date between ?1 and ?2")
	List<IncomeEntry> findByDateBetween(Date beginning, Date end);
}
