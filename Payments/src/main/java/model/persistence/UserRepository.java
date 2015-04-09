package model.persistence;

import java.util.List;

import model.dataobjects.Payment;
import model.dataobjects.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,String> {
		
	public List<User> findAll();
	public User findOne(String name);
}

