package model.persistence;

import java.util.Date;
import java.util.List;

import model.dataobjects.IncomeEntry;
import model.dataobjects.Payment;
import model.dataobjects.Saving;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends CrudRepository<Saving,Long> {
		
	public List<Saving> findAll();
	
	@Query("select s from Saving s where s.date > ?1 order by s.date ASC)")
	public List<Saving> findFirstAfterDate(Date from);
}

