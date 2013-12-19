package es.upm.emse.enteridea.adapter.dto;

import es.upm.emse.enteridea.annotation.dtoannotation.DTOMapping;
/**
 * used to transport the comment attributes to the rest services
 * @author ottoabreu
 *
 */
public class IdeaCommentDTO {

	@DTOMapping(getValueFrom = "commentId")
	private Long commentId;
	@DTOMapping(getValueFrom = "comment")
	private String comment;
	@DTOMapping(getValueFrom = "commentOwner", getSecondValueFrom = "firstName")
	private String commenterFirstName;
	@DTOMapping(getValueFrom = "commentOwner", getSecondValueFrom = "lastName")
	private String commenterLastName;
	@DTOMapping(getValueFrom = "commentOwner", getSecondValueFrom = "email")
	private String commenterEmail;
	@DTOMapping(getValueFrom = "commentOwner", getSecondValueFrom = "nickname")
	private String commenterNickName;
	
	
	
	public Long getCommentId() {
		return commentId;
	}



	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}



	public String getComment() {
		return comment;
	}



	public void setComment(String comment) {
		this.comment = comment;
	}



	public String getCommenterFirstName() {
		return commenterFirstName;
	}



	public void setCommenterFirstName(String commenterFirstName) {
		this.commenterFirstName = commenterFirstName;
	}



	public String getCommenterLastName() {
		return commenterLastName;
	}



	public void setCommenterLastName(String commenterLastName) {
		this.commenterLastName = commenterLastName;
	}



	public String getCommenterEmail() {
		return commenterEmail;
	}



	public void setCommenterEmail(String commenterEmail) {
		this.commenterEmail = commenterEmail;
	}



	public String getCommenterNickName() {
		return commenterNickName;
	}



	public void setCommenterNickName(String commenterNickName) {
		this.commenterNickName = commenterNickName;
	}



	public IdeaCommentDTO() {
		// TODO Auto-generated constructor stub
	}

}
