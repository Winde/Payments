package model.persistence;

import java.util.Date;
import java.util.List;

import model.dataobjects.Payment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment,Long> {
		
	public List<Payment> findAll();
	
	@Query("select t from Payment t " +
	         "where t.date between ?1 and ?2")
	public List<Payment> findByDateBetween(Date beginning, Date end);

	@Query("select t from Payment t " +
	         "where t.date > ?1")
	public List<Payment> findAfter(Date date);
}

