package model.persistence;

import java.util.Date;
import java.util.List;
import java.util.Set;

import model.dataobjects.Payment;
import model.dataobjects.Tag;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment,Long> {
		
	public List<Payment> findAll();
	
	@Query("select p from Payment p " +
	         "where p.date between ?1 and ?2")
	public List<Payment> findLazyByDateBetween(Date beginning, Date end);
	
	@Query("select p from Payment p LEFT JOIN FETCH p.tags t " +
	         "where p.date between ?1 and ?2")
	public Set<Payment> findEagerByDateBetween(Date beginning, Date end);

	@Query("select p from Payment p " +
	         "where p.date > ?1")
	public List<Payment> findAfter(Date date);
	
	@Query("select p from Payment p LEFT JOIN FETCH p.tags t WHERE t = ?1")	
	public List<Payment> findByTag(Tag tag);
}

