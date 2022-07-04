package com.board.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.board.domain.AttachDTO;
import com.board.domain.PostDTO;
import com.board.domain.BoardDTO;

public interface PostService {
	
	public boolean registerPost(PostDTO params);
	
	public boolean registerPost(PostDTO params, MultipartFile[] files);

	public PostDTO getPostDetail(Long idx);

	public boolean deletePost(Long idx);

	public List<PostDTO> getPostList(String category, PostDTO postdto);
	
	public List<AttachDTO> getAttachFileList(Long boardIdx);
	
	public AttachDTO getAttachDetail(Long idx);
}
