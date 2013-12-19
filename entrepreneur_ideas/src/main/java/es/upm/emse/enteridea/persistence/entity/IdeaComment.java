package es.upm.emse.enteridea.persistence.entity;

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
import javax.persistence.Table;

@Entity
@Table(name = "Comment")
/**
 * Represent a comment made into an idea
 * @author ottoabreu
 *
 */
public class IdeaComment {

	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="comment_id")
	private long commentId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false)
	@ElementCollection(targetClass=User.class)
	private User commentOwner;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDEA_ID", nullable = false)
	@ElementCollection(targetClass=Idea.class)
	private Idea idea;
	private String comment;
	
	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	
	public User getCommentOwner() {
		return commentOwner;
	}

	public void setCommentOwner(User commentOwner) {
		this.commentOwner = commentOwner;
	}
	
	public Idea getIdea() {
		return this.idea;
	}

	public void setIdea(Idea idea) {
		this.idea = idea;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public IdeaComment(User commentOwner, Idea idea, String comment) {
		super();
		this.commentOwner = commentOwner;
		this.idea = idea;
		this.comment = comment;
	}

	public IdeaComment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer("Comment[");
//		sb.append(this.commentId);
//		sb.append(", comment: ");
//		sb.append(this.comment);
//		sb.append(", owner: ");
//		sb.append(this.commentOwner);
//		sb.append("] ");
		return sb.toString();
	}

}
