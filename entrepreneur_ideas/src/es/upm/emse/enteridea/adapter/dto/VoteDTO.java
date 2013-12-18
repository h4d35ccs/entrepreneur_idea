package es.upm.emse.enteridea.adapter.dto;

import es.upm.emse.enteridea.annotation.dtoannotation.DTOMapping;
/**
 * used to transport the vote attributes to the rest services
 * @author ottoabreu
 *
 */
public class VoteDTO {

	
	@DTOMapping(getValueFrom = "voteId")
	private Long voteId;
	@DTOMapping(getValueFrom = "voteType")
	private String voteType;
	
	
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

	public VoteDTO() {
		
	}

}
