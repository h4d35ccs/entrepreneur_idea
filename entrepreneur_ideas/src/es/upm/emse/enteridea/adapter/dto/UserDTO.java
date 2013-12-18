package es.upm.emse.enteridea.adapter.dto;

import es.upm.emse.enteridea.annotation.dtoannotation.DTOMapping;


/**
 *  used to transport the user attributes to the rest services
 * @author ottoabreu
 *
 */
public class UserDTO {

	@DTOMapping(getValueFrom="userId")
	private long userId;
	@DTOMapping(getValueFrom="firstName")
	private String firstName;
	@DTOMapping(getValueFrom="lastName")
	private String lastName;
	@DTOMapping(getValueFrom="nickname")
	private String nickname;
	@DTOMapping(getValueFrom="password")
	private String password;
	@DTOMapping(getValueFrom="email")
	private String email;
	@DTOMapping(getValueFrom="moderator")
	private boolean moderator;
	
	
	
	
	
	public long getUserId() {
		return userId;
	}





	public void setUserId(Long userId) {
		this.userId = userId;
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





	public String getPassword() {
		return password;
	}





	public void setPassword(String password) {
		this.password = password;
	}





	public String getEmail() {
		return email;
	}





	public void setEmail(String email) {
		this.email = email;
	}





	public boolean isModerator() {
		return moderator;
	}





	public void setModerator(Boolean moderator) {
		this.moderator = moderator;
	}



	public UserDTO() {
		
	}

}
