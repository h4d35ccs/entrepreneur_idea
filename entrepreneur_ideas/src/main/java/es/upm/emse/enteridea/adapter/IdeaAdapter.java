package es.upm.emse.enteridea.adapter;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.upm.emse.enteridea.adapter.bean.RestResponse;
import es.upm.emse.enteridea.adapter.dto.DTOFactory;
import es.upm.emse.enteridea.adapter.dto.IdeaCommentDTO;
import es.upm.emse.enteridea.adapter.dto.IdeaDTO;
import es.upm.emse.enteridea.business.IdeaManager;
import es.upm.emse.enteridea.persistence.entity.Idea;
import es.upm.emse.enteridea.persistence.entity.IdeaComment;

@Controller
@RequestMapping("/idea")
public class IdeaAdapter {
	private static Logger logger = LogManager.getLogger(IdeaAdapter.class);
	private IdeaManager ideaManager = new IdeaManager();

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse<Object> createIdea(
			@RequestBody Map<Object, Object> requesDatatMap) {
		RestResponse<Object> rp = new RestResponse<Object>();

		try {
			ideaManager.createIdea((String) requesDatatMap.get("owner"), "1",
					(String) requesDatatMap.get("title"),
					(String) requesDatatMap.get("description"));
			rp.setStatus(RestResponse.RestStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("Error creating idea.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
		}

		return rp;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse<List<IdeaDTO>> getListofIdeas() {
		RestResponse<List<IdeaDTO>> rp = new RestResponse<List<IdeaDTO>>();

		try {
			List<Idea> ideas = ideaManager.listIdea();
			@SuppressWarnings("unchecked")
			DTOFactory<IdeaDTO> ideaFactory = (DTOFactory<IdeaDTO>) DTOFactory
					.getInstance(IdeaDTO.class);
			List<IdeaDTO> ideasDTO = ideaFactory
					.generateListDTOFromEntity(ideas);
			rp.setResponseObject(ideasDTO);
			rp.setStatus(RestResponse.RestStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("Error retreiving idea.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
		}

		return rp;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse<Object> editIdea(
			@RequestBody Map<Object, Object> requesDatatMap) {
		RestResponse<Object> rp = new RestResponse<Object>();

		try {
			Subject currentUser = SecurityUtils.getSubject();
			this.ideaManager.editIdea((String) requesDatatMap.get("idIdea"),
					(String) requesDatatMap.get("title"),
					(String) requesDatatMap.get("description"), currentUser
							.getPrincipal().toString());
			rp.setStatus(RestResponse.RestStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("Error editing idea.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
		}
		return rp;
	}

	@RequestMapping(value = "/get/{idIdea}", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse<IdeaDTO> getIdea(@PathVariable String idIdea) {
		RestResponse<IdeaDTO> rp = new RestResponse<IdeaDTO>();

		try {
			Idea theIdea = ideaManager.getIdea(idIdea);
			@SuppressWarnings("unchecked")
			DTOFactory<IdeaDTO> ideaFactory = (DTOFactory<IdeaDTO>) DTOFactory
					.getInstance(theIdea, IdeaDTO.class);
			IdeaDTO ideaDto = ideaFactory.generateDTOFromEntity();
			rp.setResponseObject(ideaDto);
			rp.setStatus(RestResponse.RestStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("Error getting the idea.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
		}
		return rp;
	}

	@RequestMapping(value = "/comment/create", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse<Object> createComment(
			@RequestBody Map<Object, Object> requesDatatMap) {
		RestResponse<Object> rp = new RestResponse<Object>();

		try {
			IdeaComment comment = this.ideaManager.createComment(
					(String) requesDatatMap.get("idIdea"),
					(String) requesDatatMap.get("idUser"),
					(String) requesDatatMap.get("text"));
			@SuppressWarnings("unchecked")
			DTOFactory<IdeaCommentDTO> commentsFactory = (DTOFactory<IdeaCommentDTO>) DTOFactory
					.getInstance(comment, IdeaCommentDTO.class);
			IdeaCommentDTO commentDTO = commentsFactory.generateDTOFromEntity();

			rp.setStatus(RestResponse.RestStatus.SUCCESS);
			rp.setResponseObject(commentDTO);
		} catch (Exception e) {
			logger.error("Error creating comment.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
		}

		return rp;

	}

	@RequestMapping(value = "comment/list/{idIdea}", method = RequestMethod.GET)
	@ResponseBody
	public RestResponse<List<IdeaCommentDTO>> getComments(
			@PathVariable String idIdea) {
		RestResponse<List<IdeaCommentDTO>> rp = new RestResponse<List<IdeaCommentDTO>>();

		try {
			List<IdeaComment> comments = this.ideaManager.getComments(idIdea);
			@SuppressWarnings("unchecked")
			DTOFactory<IdeaCommentDTO> commentsFactory = (DTOFactory<IdeaCommentDTO>) DTOFactory
					.getInstance(IdeaCommentDTO.class);

			List<IdeaCommentDTO> commentDTOList = commentsFactory
					.generateListDTOFromEntity(comments);
			rp.setResponseObject(commentDTOList);
			rp.setStatus(RestResponse.RestStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("Error retreiving comments.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
		}

		return rp;
	}

	@RequestMapping(value = "comment/delete", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse<Object> deleteComment(@RequestBody Map<Object, Object> requesDatatMap) {
		RestResponse<Object> rp = new RestResponse<Object>();

		try {
			Subject currentUser = SecurityUtils.getSubject();
			ideaManager.deleteComment((String) requesDatatMap.get("idComment"), currentUser.getPrincipal()
					.toString());
			rp.setStatus(RestResponse.RestStatus.SUCCESS);
			rp.setMessage("Comment has been deleted");
		} catch (Exception e) {
			logger.error("Error deleting the comment.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
			rp.setMessage("Error while deleting the comment");
		}

		return rp;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse<Object> deleteIdea(@RequestBody Map<Object, Object> requesDatatMap) {
		RestResponse<Object> rp = new RestResponse<Object>();

		try {
			Subject currentUser = SecurityUtils.getSubject();
			ideaManager.deleteIdea((String) requesDatatMap.get("idIdea"), currentUser.getPrincipal()
					.toString());
			rp.setStatus(RestResponse.RestStatus.SUCCESS);
			rp.setMessage("Idea has been deleted");
		} catch (Exception e) {
			logger.error("Error deleting the idea.", e);
			rp.setStatus(RestResponse.RestStatus.ERROR);
			rp.setMessage("Error while deleting the idea");
		}
		return rp;
	}
}