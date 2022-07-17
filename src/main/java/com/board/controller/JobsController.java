package com.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.board.constant.Method;
import com.board.domain.AttachDTO;
import com.board.domain.PostDTO;
import com.board.service.PostService;
import com.board.util.UiUtils;

@Controller
public class JobsController extends UiUtils{
	
	@Autowired
	private PostService jobService;
	
	@GetMapping(value = "/job/write")
	public String openPostWrite(@ModelAttribute("params") PostDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {	//idx가 없으면 글쓰기
			String category = params.getCategory();
			System.out.println("글쓰기 화면으로 이동하기전 파악하고있는 카테고리 : "+category);
			
			PostDTO newPost = new PostDTO();
			newPost.setCategory(category);
			model.addAttribute("job", newPost);

		} else {		//idx가 있으면 수정
			PostDTO job = jobService.getPostDetail(idx);
			if (job == null || "Y".equals(job.getDeleteYn())) {
				return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/job/list", Method.GET, null, model);
			}
			model.addAttribute("job", job);
			
			
			List<AttachDTO> fileList = jobService.getAttachFileList(idx);
			model.addAttribute("fileList", fileList);
		}
		return "job/write";	//resources/templates하위의 View경로리턴 뒤에는 .html붙음
		//수정과 삭제 둘다 job/write가 처리.
	}
	
	@PostMapping(value = "/job/register")
	public String registerPost(@ModelAttribute("params") final PostDTO params,  final MultipartFile[] files, Model model) {
		String category = params.getCategory();
		System.out.println("job 글쓰기 컨트롤러에 넘어온 category : "+category);
		String returnURL = "/job/"+category+"/list";
		Map<String, Object> pagingParams = getPagingParams(params);
		try {
			boolean isRegistered = jobService.registerPost(params, files);
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록(수정)에 실패하였습니다.", returnURL, Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", returnURL, Method.GET, pagingParams, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", returnURL, Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 등록(수정)이 완료되었습니다.", returnURL, Method.GET, pagingParams, model);
	}
	
	//@ModelAttribute를 이용하면 파라미터로 전달받은 객체를 자동으로 뷰까지 전달할 수 있다.
	@GetMapping(value = "/job/list")
	public String openJobsList(Model model) {
		System.out.println("컨트롤러의 openJobsList");
		return "job/list";
	}
	
	
}
