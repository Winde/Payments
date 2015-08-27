package model.persistence;

import model.dataobjects.Tag;

public interface TagRepositoryCustom {
	
	public Iterable<Tag> saveAndAddUsage(Iterable<Tag> tags);
	
	public Tag saveAndDecreaseUsage(Tag tag);
	
}
