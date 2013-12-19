package es.upm.emse.enteridea.adapter;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.upm.emse.enteridea.adapter.bean.RestResponse;
import es.upm.emse.enteridea.adapter.dto.DTOFactory;
import es.upm.emse.enteridea.adapter.dto.UserDTO;
import es.upm.emse.enteridea.business.UserManager;
import es.upm.emse.enteridea.persistence.entity.User;

@Controller
@RequestMapping("/user")
public class UserAdapter {

	private static Logger logger = LogManager.getLogger(UserAdapter.class);

	private UserManager userManager = new UserManager();

	/**
	 * Creates a user account p<br>
	 * URL: /signup
	 * 
	 * @param name
	 * @param lastname
	 * @param email
	 * @param nickname
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse<Object> signup(
			@RequestBody Map<Object, Object> requesDatatMap) {
		RestResponse<Object> rp = new RestResponse<Object>();

		try {
			userManager.createUser((String) requesDatatMap.get("name"),
					(String) requesDatatMap.get("lastname"),
					(String) requesDatatMap.get("nickname"),
					(String) requesDatatMap.get("password"),
					(String) requesDatatMap.get("email"));
			rp.setStatus(RestResponse.RestStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("Error signing up user.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
		}

		return rp;

	}

	/**
	 * Returns the information of the logged user
	 * 
	 * @param requesDatatMap
	 * @return RestResponse<Object>
	 */
	@RequestMapping(value = "/loggedUser", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse<Object> getLoggedUser() {

		RestResponse<Object> rp = new RestResponse<Object>();
		RestResponse.RestStatus status = null;
		String message = null;
		try {

			Subject currentUser = SecurityUtils.getSubject();

			if (currentUser.isAuthenticated()) {
				String username = currentUser.getPrincipal().toString();
				User logedUserInfo = this.userManager
						.getUserByUserName(username);
				@SuppressWarnings("unchecked")
				DTOFactory<UserDTO> factory = (DTOFactory<UserDTO>) DTOFactory
						.getInstance(logedUserInfo, UserDTO.class);
				UserDTO user = factory.generateDTOFromEntity();
				status = RestResponse.RestStatus.SUCCESS;
				rp.setResponseObject(user);

			} else {
				message = "The requested user is not authenticated";
				logger.error(message);
				status = RestResponse.RestStatus.ERROR;
			}

		} catch (Exception e) {
			message = "Can not get the logged user";
			logger.error(message, e);
			status = RestResponse.RestStatus.ERROR;
		}
		rp.setMessage(message);
		rp.setStatus(status);

		return rp;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse<List<UserDTO>> getListofUsers() {
		RestResponse<List<UserDTO>> rp = new RestResponse<List<UserDTO>>();

		try {
			List<User> users = userManager.listUsers();
			@SuppressWarnings("unchecked")
			DTOFactory<UserDTO> userFactory = (DTOFactory<UserDTO>) DTOFactory
					.getInstance(UserDTO.class);
			List<UserDTO> usersDTO = userFactory
					.generateListDTOFromEntity(users);
			rp.setResponseObject(usersDTO);
			rp.setStatus(RestResponse.RestStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("Error retreiving users.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
		}

		return rp;
	}

}
