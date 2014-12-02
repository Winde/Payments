package model.persistence;

import java.util.List;

import model.dataobjects.Transaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Transaction,Long> {
		
	List<Transaction> findAll();
}

