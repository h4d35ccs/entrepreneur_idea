/**
 * 
 */
package es.upm.emse.enteridea.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Represent a vote to a idea
 * @author ottoabreu
 * 
 */
@Entity
@Table(name = "votes")
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "vote_id")
	private Long voteId;
	@Column(name = "vote_type", columnDefinition = "enum('positive','negative','neutral')")
	private String voteType;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDEA_ID", nullable = false)
	private Idea idea;
	/**
	 * Sets the posibles values to vote
	 * @author ottoabreu
	 *
	 */
	public enum VoteTypes {
		POSITIVE("positive"), NEGATIVE("negative"), NEUTRAL("neutral");

		private String value;

		private VoteTypes(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public Idea getIdea() {
		return idea;
	}

	public void setIdea(Idea idea) {
		this.idea = idea;
	}

	public Long getVoteId() {
		return voteId;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}


	public String getVoteType() {
		return voteType;
	}

	public void setVoteType(String voteType) {
		this.voteType = voteType;
	}

}
