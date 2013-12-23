package es.upm.emse.enteridea.persistence.entity;

import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name = "Idea")
/**
 * Represent an idea and all its attributtes
 * @author ottoabreu
 *
 */
public class Idea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
	@Column(name = "idea_id")
	private long ideaiId;
	private String title;
	private String description;
//	@JsonBackReference
	// For JSON serialization
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TOPIC_ID", nullable = false)
	private Topic topic;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	private Date creationDate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modification_date")
	private Date modificationDate;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "idea", cascade = CascadeType.REMOVE)
	@ElementCollection(targetClass = IdeaComment.class)
	private Set<IdeaComment> ideaComments;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User ideaOwner;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "idea", cascade = CascadeType.REMOVE)
	@ElementCollection(targetClass = Vote.class)
	private Set<Vote> votes;
	// creation date formated to be shown
	@Transient
	private String dateToShow;
	// modification date formated to be shown
	@Transient
	private String modDateToShow;

	private static final int DATE_FORMAT = DateFormat.SHORT;

	/**
	 * Generates a String representation of the creation date
	 * 
	 * @return String
	 */
	public String getDateToShow() {

		if (this.creationDate != null) {
			this.dateToShow = DateFormat.getDateInstance(DATE_FORMAT).format(
					this.creationDate);
		}
		return this.dateToShow;
	}

	/**
	 * Generates a String representation of the modification date
	 * 
	 * @return String
	 */
	public String getModDateToShow() {
		if (this.modificationDate != null) {
			this.modDateToShow = DateFormat.getDateInstance(DATE_FORMAT)
					.format(this.modificationDate);
		}
		return this.modDateToShow;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getIdeaiId() {
		return ideaiId;
	}

	public void setIdeaiId(long ideaiId) {
		this.ideaiId = ideaiId;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Set<IdeaComment> getIdeaComments() {
		return ideaComments;
	}

	public void setIdeaComments(Set<IdeaComment> ideaComments) {
		this.ideaComments = ideaComments;
	}

	public User getIdeaOwner() {
		return ideaOwner;
	}

	public void setIdeaOwner(User ideaOwner) {
		this.ideaOwner = ideaOwner;
	}

	public Set<Vote> getVotes() {
		return votes;
	}

	public void setVotes(Set<Vote> votes) {
		this.votes = votes;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Idea() {
		
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("IDEA[");
		sb.append(this.ideaiId);
		sb.append(", title: ");
		sb.append(this.title);
		sb.append(", topic: ");
		sb.append(this.topic);
		sb.append(", Creation date: ");
		sb.append(this.creationDate);
		sb.append(", Modification date: ");
		sb.append(this.modificationDate);
		sb.append(",  votes: ");
		sb.append(this.votes);
		sb.append(", owner : ");
		sb.append(this.ideaOwner);
		sb.append(", comments : ");
		sb.append(this.ideaComments);
		sb.append("]  ");
		return sb.toString();
	}

}
