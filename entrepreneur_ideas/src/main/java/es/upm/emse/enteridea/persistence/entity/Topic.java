package es.upm.emse.enteridea.persistence.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Topic")
/**
 * Represent a topic
 * @author ottoabreu
 *
 */
public class Topic {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "topic_id")
	private long topicId;
	private String topicDescription;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
	@ElementCollection(targetClass = Idea.class)
	private Set<Idea> ideas;

	public Set<Idea> getIdeas() {
		return ideas;
	}

	public void setIdeas(Set<Idea> ideas) {
		this.ideas = ideas;
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public String getTopicDescription() {
		return topicDescription;
	}

	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Topic[");
		sb.append(this.topicId);
		sb.append(", description: ");
		sb.append(this.topicDescription);
		sb.append("] ");
		return sb.toString();
	}
}
