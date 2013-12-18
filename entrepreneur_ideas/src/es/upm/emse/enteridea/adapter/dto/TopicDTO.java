package es.upm.emse.enteridea.adapter.dto;

import es.upm.emse.enteridea.annotation.dtoannotation.DTOMapping;
/**
 * used to transport the Topic attributes to the rest services
 * @author ottoabreu
 *
 */
public class TopicDTO {

	@DTOMapping(getValueFrom="topicId")
	private long topicId;
	@DTOMapping(getValueFrom="topicDescription")
	private String topicDescription;
	
	public TopicDTO() {
		// TODO Auto-generated constructor stub
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getTopicDescription() {
		return topicDescription;
	}

	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}
	
	

}
