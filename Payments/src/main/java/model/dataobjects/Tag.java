package model.dataobjects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import model.persistence.TagRepository;

@Entity
public class Tag {
	
	@Id
	@Column(unique=true)
	private String name;
	
	@OneToMany(fetch=FetchType.LAZY)
	private List<Payment> payments;

	@Column(name="instances")
	private Long usage;
	
	public Tag(){
		usage = new Long(0);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public boolean isValid(){
		return name!=null && name.length()>0;
	}

	public Tag getVerySimilar(List<Tag> tags){		
		Tag candidate = null;
		
		//TODO check similar tags and present a candidate	
				
		return candidate;
	}

	public Long getUsage() {
		return usage;
	}

	public void setUsage(Long usage) {
		this.usage = usage;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Tag) {
			if (name == null) {
				return false;
			} else {
				return name.equals(((Tag) o).getName());
			}
		} else {
			return false;
		}
		
	}
	
	public String toString(){
		return "[" + this.getName() + "(" + this.getUsage() + ") ]";		
	}
	

	public static List<Tag> createTags(String tagString, List<Tag> existingTags) {
		
		List<Tag> newTags = new ArrayList<>();
		List<Tag> applyTags = new ArrayList<>();
		if (tagString!=null){
			String[] candidateTags = tagString.split(",");
			for (String candidateTag : candidateTags) {
				String cleanedTag = candidateTag.trim();
				Tag tag = new Tag();
				tag.setName(cleanedTag);
				if (tag.isValid()){
					int index = existingTags.indexOf(tag);
					if (index>=0){						
						applyTags.add(existingTags.get(index));
					} else {											
						newTags.add(tag);
					}
				}
			}																				
			applyTags.addAll(newTags);						
		}
		
		return applyTags;
		
	}
}
