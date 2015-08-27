package model.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
			if (tag.getUsage()==null || tag.getUsage()<0){
				tag.setUsage(new Long(0));
			}			
			tag.setUsage(tag.getUsage()+1);
			entityManager.persist(tag);			
		}						
		return tags;
	}

	@Transactional
	public Tag saveAndDecreaseUsage(Tag tag) {		
		entityManager.merge(tag);		
		if (tag.getUsage()==null || tag.getUsage()<=0){
			tag.setUsage(new Long(0));
		} else {
			tag.setUsage(tag.getUsage()-1);
		}			
		repository.save(tag);			
		return tag;
	}

	
}
