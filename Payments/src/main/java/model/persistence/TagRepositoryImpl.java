package model.persistence;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import model.dataobjects.Tag;

public class TagRepositoryImpl implements TagRepositoryCustom {

	@Autowired
	TagRepository repository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Transactional
	public Iterable<Tag> saveAndAddUsage(Iterable<Tag> tags) {			
		for (Tag tag: tags) {						
			tag = entityManager.merge(tag);
			entityManager.persist(tag);			
		}						
		return tags;
	}

	@Transactional
	public Tag saveAndDecreaseUsage(Tag tag) {		
		entityManager.merge(tag);		
		repository.save(tag);			
		return tag;
	}

	public List<Tag> findAllWithUsage() {
		String sql = "SELECT tags_name name,COUNT(payment_id) instances_used "+
						"FROM payment_tags "+
						"GROUP BY tags_name "+
						"HAVING COUNT(payment_id) > 0";
		
		Query query = entityManager.createNativeQuery(sql, Tag.class);
		
		
		List<Tag> result = query.getResultList();
		
		
		Collections.sort(result,Collections.reverseOrder());
		return result;
		
	}

}
