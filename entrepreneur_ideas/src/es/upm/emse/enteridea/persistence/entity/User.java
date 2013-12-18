package es.upm.emse.enteridea.persistence.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;

@Entity
@Table(name = "UserEnteridea")
/**
 * Represent an user
 * @author ottoabreu
 *
 */
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private long userId;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	private String nickname;
	private String password;
	private String email;
	@JsonManagedReference // For JSON serialization
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ideaOwner")
	@ElementCollection(targetClass = Idea.class)
	private Set<Idea> ideas;
	@JsonManagedReference // For JSON serialization
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "commentOwner")
	@ElementCollection(targetClass = IdeaComment.class)
	private Set<IdeaComment> comments;
	private boolean moderator;

	public boolean isModerator() {
		return moderator;
	}

	public void setModerator(boolean moderator) {
		this.moderator = moderator;
	}

	public Set<IdeaComment> getComments() {
		return comments;
	}

	public void setComments(Set<IdeaComment> comments) {
		this.comments = comments;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Idea> getIdeas() {
		return ideas;
	}

	public void setIdeas(Set<Idea> ideas) {
		this.ideas = ideas;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User(String firstName, String lastName, String nickname,
			String password, String email, Set<Idea> ideas,
			Set<IdeaComment> comments) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickname = nickname;
		this.password = password;
		this.email = email;
		this.ideas = ideas;
		this.comments = comments;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}
@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("User[");
		sb.append(this.userId);
		sb.append(", name: ");
		sb.append(this.firstName);
		sb.append(", lastname: ");
		sb.append(this.lastName);
		sb.append(", nickname: ");
		sb.append(this.nickname);
		sb.append(", password: ");
		sb.append(this.password);
		sb.append(", email: ");
		sb.append(this.email);
		sb.append("] ");
		return sb.toString();
	}
}
