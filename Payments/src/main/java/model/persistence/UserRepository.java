package model.persistence;

import java.util.List;

import model.dataobjects.Transaction;
import model.dataobjects.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
		
	List<User> findAll();
	List<User> findByName(String name);
}

