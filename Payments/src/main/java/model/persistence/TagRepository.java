package model.persistence;

import java.util.List;

import model.dataobjects.Tag;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag,String> {
		
	public List<Tag> findAll();

	@Query("select t from Tag t " +
	         "where t.usage > 0")
	public List<Tag> findAllUsed();
}

