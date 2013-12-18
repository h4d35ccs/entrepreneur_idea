package es.upm.emse.enteridea.adapter.dto;

import java.util.List;

import es.upm.emse.enteridea.annotation.dtoannotation.DTOMapping;

/**
 * Used to transport the idea attributes to the rest services
 * 
 * @author ottoabreu
 * 
 */
public class IdeaDTO {

	@DTOMapping(getValueFrom = "ideaiId")
	private long ideaiId;
	@DTOMapping(getValueFrom = "title")
	private String title;
	@DTOMapping(getValueFrom = "description")
	private String description;
	@DTOMapping(getValueFrom = "topic", getSecondValueFrom = "topicDescription")
	private String topicDescription;
	@DTOMapping(getValueFrom="ideaComments",generateListOf=IdeaCommentDTO.class)
	private List<IdeaCommentDTO> ideaComments;
	@DTOMapping(getValueFrom = "ideaOwner", getSecondValueFrom = "firstName")
	private String ownerFirstName;
	@DTOMapping(getValueFrom = "ideaOwner", getSecondValueFrom = "lastName")
	private String ownerLastName;
	@DTOMapping(getValueFrom = "ideaOwner", getSecondValueFrom = "email")
	private String ownerEmail;
	@DTOMapping(getValueFrom = "ideaOwner", getSecondValueFrom = "nickname")
	private String ownerNickName;
	@DTOMapping(getValueFrom="votes",generateListOf=VoteDTO.class)
	private List<VoteDTO> votes;
	@DTOMapping(getValueFrom = "dateToShow")
	private String dateToShow;
	@DTOMapping(getValueFrom = "modDateToShow")
	private String modDateToShow;

	// private static final int DATE_FORMAT = DateFormat.SHORT;

	public IdeaDTO() {

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
		sb.append(this.topicDescription);
		sb.append(", description: ");
		sb.append(this.description);
		sb.append(", Creation date: ");
		sb.append(this.dateToShow);
		sb.append(", Modification date: ");
		sb.append(this.modDateToShow);
		sb.append(",  votes: ");
		// sb.append(this.votes);
		sb.append(", owner : ");
		sb.append(this.ownerFirstName);
		sb.append(" ");
		sb.append(this.ownerLastName);
		sb.append(" ");
		sb.append(this.ownerEmail);
		sb.append(" ");
		sb.append(this.ownerNickName);
		sb.append(", comments : ");
		// sb.append(this.ideaComments);
		sb.append("]  ");
		return sb.toString();
	}

	public long getIdeaiId() {
		return ideaiId;
	}

	public void setIdeaiId(Long ideaiId) {
		this.ideaiId = ideaiId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTopicDescription() {
		return topicDescription;
	}

	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}

	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	public String getOwnerLastName() {
		return ownerLastName;
	}

	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getOwnerNickName() {
		return ownerNickName;
	}

	public void setOwnerNickName(String ownerNickName) {
		this.ownerNickName = ownerNickName;
	}

	public String getDateToShow() {
		return dateToShow;
	}

	public void setDateToShow(String dateToShow) {
		this.dateToShow = dateToShow;
	}

	public String getModDateToShow() {
		return modDateToShow;
	}

	public void setModDateToShow(String modDateToShow) {
		this.modDateToShow = modDateToShow;
	}

	public List<IdeaCommentDTO> getIdeaComments() {
		return ideaComments;
	}

	public void setIdeaComments(List<IdeaCommentDTO> ideaComments) {
		this.ideaComments = ideaComments;
	}

	public List<VoteDTO> getVotes() {
		return votes;
	}

	public void setVotes(List<VoteDTO> votes) {
		this.votes = votes;
	}
	
	

}
