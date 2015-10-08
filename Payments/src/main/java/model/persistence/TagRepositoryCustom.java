package model.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import model.dataobjects.Tag;

public interface TagRepositoryCustom {
	
	public Iterable<Tag> saveAndAddUsage(Iterable<Tag> tags);
	
	public Tag saveAndDecreaseUsage(Tag tag);
		
	public List<Tag> findAllWithUsage();
}
