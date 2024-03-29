package com.board.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
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
/* 보드. 게시판
 * 포스트. 게시글.
 * 둘다 처리
 */

@Controller
public class BoardController extends UiUtils{
	
	@Autowired
	private PostService boardService;
	
	@GetMapping(value = "/post/write.do")
	public String openPostWrite(@ModelAttribute("params") PostDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model, @RequestParam String category) {
		if (idx == null) {	//idx가 없으면 글쓰기
			System.out.println("글쓰기 화면으로 이동하기전 파악하고있는 카테고리 : "+category);
			
			PostDTO newPost = new PostDTO();
			newPost.setCategory(category);
			model.addAttribute("post", newPost);

		} else {		//idx가 있으면 수정
			PostDTO post = boardService.getPostDetail(idx);
			if (post == null || "Y".equals(post.getDeleteYn())) {
				return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/post/list", Method.GET, null, model);
			}
			model.addAttribute("post", post);
			
			
			List<AttachDTO> fileList = boardService.getAttachFileList(idx);
			model.addAttribute("fileList", fileList);
		}
		return "post/write";	//resources/templates하위의 View경로리턴 뒤에는 .html붙음
		//수정과 삭제 둘다 post/write가 처리.
	}
	
	@PostMapping(value = "/post/register.do")
	public String registerPost(@ModelAttribute("params") final PostDTO params,  final MultipartFile[] files, Model model) {
		String category = params.getCategory();
		System.out.println("post 글쓰기 컨트롤러에 넘어온 category : "+category);
		String returnURL = "/board/list";
		Map<String, Object> pagingParams = getPagingParams(params);
		pagingParams.put("category", category);
		try {
			boolean isRegistered = boardService.registerPost(params, files);
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
	
	//@ModelAttribute를 이용하면 파라미터로 전달받은 객체를 자동으로 뷰까지 전달할 수 있다. params라는 이름으로 화면에 넘겼다.
	@GetMapping(value = "/board/list")
	public String openPostList(@ModelAttribute("params") PostDTO postdto, Model model, @RequestParam (required = false) String category) {
		System.out.println("컨트롤러의 openPostList. 컨트롤러가 파악한 카테고리 문자열: "+category);
		if(category==null) {
			category = "free";
		}
		postdto.setCategory(category);
		List<PostDTO> postList = boardService.getPostList(postdto);
		model.addAttribute("postList", postList);
		return "post/list";
	}

	
	@GetMapping(value = "/post/view.do")
	public String openPostDetail(@ModelAttribute("params") PostDTO params, @RequestParam(value = "idx", required = false) Long idx,  Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/post/list/free", Method.GET, null, model);
		}

		PostDTO post = boardService.getPostDetail(idx);
		System.out.println("컨트롤러에서 상세글 가져온것 : "+post);

		if (post == null || "Y".equals(post.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/post/list", Method.GET, null, model);
		}
		model.addAttribute("post", post);

		List<AttachDTO> fileList = boardService.getAttachFileList(idx); // 상세보기에서 파일리스트
		model.addAttribute("fileList", fileList); //뷰로 전달

		return "post/view";
	}
	
	@PostMapping(value = "/post/delete.do")
	public String deletePost(@ModelAttribute("params") PostDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		String category = params.getCategory();
		System.out.println("삭제한 카테고리 : "+category);
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/post/list", Method.GET, null, model);
		}

		Map<String, Object> pagingParams = getPagingParams(params);
		pagingParams.put("category", category);

		try {
			boolean isDeleted = boardService.deletePost(idx);
			if (isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/list", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "board/list", Method.GET, pagingParams, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/list", Method.GET, pagingParams, model);
	}
	
	@GetMapping("/post/download.do")
	public void downloadAttachFile(@RequestParam(value = "idx", required = false) final Long idx, Model model, HttpServletResponse response) {

		if (idx == null) throw new RuntimeException("올바르지 않은 접근입니다.");

		AttachDTO fileInfo = boardService.getAttachDetail(idx);
		if (fileInfo == null || "Y".equals(fileInfo.getDeleteYn())) {
			throw new RuntimeException("파일 정보를 찾을 수 없습니다.");
		}

		String uploadDate = fileInfo.getInsertTime().format(DateTimeFormatter.ofPattern("yyMMdd"));
		String uploadPath = Paths.get("C:", "develop", "upload", uploadDate).toString();

		String filename = fileInfo.getOriginalName();
		File file = new File(uploadPath, fileInfo.getSaveName());

		try {
			byte[] data = FileUtils.readFileToByteArray(file);
			response.setContentType("application/octet-stream");
			response.setContentLength(data.length);
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(filename, "UTF-8") + "\";");

			response.getOutputStream().write(data);
			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (IOException e) {
			throw new RuntimeException("파일 다운로드에 실패하였습니다.");

		} catch (Exception e) {
			throw new RuntimeException("시스템에 문제가 발생하였습니다.");
		}
	}
}
