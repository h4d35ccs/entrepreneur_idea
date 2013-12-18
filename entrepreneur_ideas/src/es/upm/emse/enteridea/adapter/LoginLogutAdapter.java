package es.upm.emse.enteridea.adapter;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.upm.emse.enteridea.adapter.bean.RestResponse;
import es.upm.emse.enteridea.adapter.bean.RestResponse.RestStatus;
import es.upm.emse.enteridea.adapter.dto.DTOFactory;
import es.upm.emse.enteridea.adapter.dto.UserDTO;
import es.upm.emse.enteridea.business.UserManager;
import es.upm.emse.enteridea.persistence.entity.User;

@Controller
@RequestMapping("/loginLogout")
public class LoginLogutAdapter {

	private static Logger logger = LogManager
			.getLogger(LoginLogutAdapter.class);

	/**
	 * Rest service that authorize an user to enter the app<br>
	 * URL: /login
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	// TODO CHANGE THE SIGNATURE OF THE METHOD WHEN THE REAL LOGIN IS DONE
//	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = { "content-type=application/x-www-form-urlencoded" })
//	@ResponseBody
//	public RestResponse<Object> login(
//			@RequestParam("username") String username,
//			@RequestParam("password") String password) {
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
		 public RestResponse<Object> login(@RequestBody Map<Object, Object>
		 requesDatatMap) {

		RestResponse<Object> rp = new RestResponse<Object>();
		RestStatus response = null;
		String message = null;
		try {

			 String username = (String)requesDatatMap.get("username");
			 String password = (String)requesDatatMap.get("password");

			Subject currentUser = SecurityUtils.getSubject();

			if (!currentUser.isAuthenticated()) {
				// creates the username/password token to perform the in the
				// security real ( done automatically by shiro)
				UsernamePasswordToken token = new UsernamePasswordToken(
						username, password);
				try {
					// only throws exception if the pair username/password fail,
					// the user is not present or some error occurs
					currentUser.login(token);
					UserManager um = new UserManager();
					User logedUserInfo = um.getUserByUserName(username); 
					//gets the info of the logged user
					try{
						@SuppressWarnings("unchecked")
						DTOFactory<UserDTO> factory = (DTOFactory<UserDTO>) DTOFactory.getInstance(logedUserInfo, UserDTO.class);
						UserDTO user =  factory.generateDTOFromEntity();
						response = RestResponse.RestStatus.SUCCESS;
						logger.info(username + " has enter");
						rp.setResponseObject(user);
					} catch (Exception e) {
						currentUser.logout();
						throw e;
					}
					
				} catch (UnknownAccountException uae) {
					message = "The user " + username + " is not registered";
					logger.error(message);
					response = RestResponse.RestStatus.ERROR;

				} catch (IncorrectCredentialsException ice) {
					message = "Wrong username or password";
					logger.error(message);
					response = RestResponse.RestStatus.ERROR;

				} catch (Exception e) {
					message = "Can not authenticate user due an general error";
					logger.error(message, e);
					response = RestResponse.RestStatus.ERROR;
				}

			} else {
				logger.info(username + " was authenticated");
				response = RestResponse.RestStatus.SUCCESS;
			}
			rp.setMessage(message);
			rp.setStatus(response);

		} catch (Exception e) {
			logger.error("Error in login", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
		}

		return rp;

	}

	/**
	 * Rest service that performs the logic of logout an user<br>
	 * URL: /logout
	 * 
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse<Object> logut() {

		RestResponse<Object> rp = new RestResponse<Object>();
		RestStatus response = null;
		String message = null;

		Subject currentUser = SecurityUtils.getSubject();
		// get the username of the user
		
		if (currentUser.isAuthenticated()) {
			String loggedOutUser = currentUser.getPrincipal().toString();
			currentUser.logout();
			logger.info(loggedOutUser + " logged out");
			response = RestStatus.SUCCESS;
			 message = "user logged out";

		} else {
			 message = "Can not logout the user because there is no user in the app";
			logger.error(message);
			response = RestResponse.RestStatus.ERROR;
		}
		rp.setMessage(message);
		rp.setStatus(response);
		return rp;
	}
	/**
	 * Can tell if there is a user logged
	 * @return
	 */
	@RequestMapping(value = "/islogged", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse<Object> isLogged() {
		RestResponse<Object> rp = new RestResponse<Object>();
		RestStatus response = null;
		Boolean isLogged = null;
		
		try {
			Subject currentUser = SecurityUtils.getSubject();

			if (currentUser.isAuthenticated()) {
				isLogged = new Boolean(true);
				logger.info(currentUser.getPrincipal()+ " is logged, will be redirected to index");
			}else{
				logger.info("no user logged");
				isLogged = new Boolean(false);
			}
			response = RestResponse.RestStatus.SUCCESS;
		} catch (Exception e) {
			logger.warn("can not verify if the user was logged ",e);
			response = RestResponse.RestStatus.ERROR;
		}
		rp.setResponseObject(isLogged);
		rp.setStatus(response);
		return rp;
		
	}

}
