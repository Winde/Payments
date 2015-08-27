package model.persistence;

import java.util.List;

import model.dataobjects.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag,String>, TagRepositoryCustom {

	@Query("select t from Tag t where t.usage > 0")
	public List<Tag> findAllUsed();

}

