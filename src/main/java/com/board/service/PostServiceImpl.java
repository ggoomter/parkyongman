package com.board.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.board.domain.AttachDTO;
import com.board.domain.PostDTO;
import com.board.mapper.AttachMapper;
import com.board.mapper.BoardMapper;
import com.board.mapper.PostMapper;
import com.board.paging.PaginationInfo;
import com.board.util.FileUtils;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private AttachMapper attachMapper;

	@Autowired
	private FileUtils fileUtils;
	
	//글번호 있으면 수정, 없으면 등록
	@Override
	public boolean registerPost(PostDTO params) {
		int queryResult = 0;

		if (params.getIdx() == null) {	//idx가 없으면 입력
			queryResult = postMapper.insertPost(params);
			
		} else {		//idx가 있으면 수정
			queryResult = postMapper.updatePost(params);
			
			// 수정화면에서 찾아보기로 파일을 수정하면 기존의 파일은 삭제처리해야된다.
			if ("Y".equals(params.getChangeYn())) {	// 수정할때 화면에서 파일의 추가, 삭제, 변경된 경우
				// 해당게시글의 모든 첨부파일을 삭제처리한다음
				attachMapper.deleteAttach(params.getIdx());

				// fileIdxs에 포함된 idx(원래부터있던 파일. 즉 수정되지 않은 파일)를 가지는 파일의 삭제여부를 'N'으로 업데이트
				if (CollectionUtils.isEmpty(params.getFileIdxs()) == false) {
					attachMapper.undeleteAttach(params.getFileIdxs());
				}
			}
		}
		
		//PostDTO board = null;
		//System.out.println("타이틀 : "+board.getTitle());	//null exception이 발생한다. 
		//트랜잭션이 없었다면 게시글 등록은 되고 에러가 난다.
		//트랜잭션이 있다면 게시글 등록되었던것을 롤백하고 에러가 난다.

		return (queryResult == 1) ? true : false;
	}
	
	@Override
	public boolean registerPost(PostDTO params, MultipartFile[] files) {
		int queryResult = 1;

		if (registerPost(params) == false) {
			return false;
		}

		/*
		기존에 게시글을 썼을때는 게시글번호는 insert시점에 자동increse 했다.
		그런데 이제 파일을 첨부시킬때는 그 파일이 어느게시글에 달렸는지를 db에 쓰기 이전에 알아야 하기 때문에
		PostMapper.xml에서 useGeneratedKeys="true" keyProperty="idx" 해준다.
		*/
		List<AttachDTO> fileList = fileUtils.uploadFiles(files, params.getIdx());
		
		if (CollectionUtils.isEmpty(fileList) == false) {	//리스트가 비어있지않으면
			queryResult = attachMapper.insertAttach(fileList);
			if (queryResult < 1) {
				queryResult = 0;
			}
		}

		return (queryResult > 0);
	}

	@Override
	public PostDTO getPostDetail(Long idx) {
		return postMapper.selectPostDetail(idx);
	}

	@Override
	public boolean deletePost(Long idx) {
		int queryResult = 0;

		PostDTO board = postMapper.selectPostDetail(idx);

		//지워진상태가 아니고 비어있지 않으면 지우기
		if (board != null && "N".equals(board.getDeleteYn())) {
			queryResult = postMapper.deletePost(idx);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public List<PostDTO> getPostList(String category, PostDTO params) {
		System.out.println("서비스임플의 getPostList");
		List<PostDTO> postList = Collections.emptyList();
		int boardIdx = boardMapper.getBoardIdx(category);
		params.setBoardIdx((long) boardIdx);
		//전체 글의 갯수 읽기 
		int postTotalCount = postMapper.selectPostTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(postTotalCount);  //페이지네이션에 전체글갯수 세팅

		params.setPaginationInfo(paginationInfo);
		
		
		//토탈카운트가 0개 이상일경우만 리스트 갖고오기. 아니면 비어있는 리스트 반환
		if (postTotalCount > 0) {
			postList = postMapper.selectPostList(params);
		}

		return postList;
	}
	
	@Override
	public List<AttachDTO> getAttachFileList(Long boardIdx) {

		int fileTotalCount = attachMapper.selectAttachTotalCount(boardIdx);
		if (fileTotalCount < 1) {
			return Collections.emptyList();
		}
		return attachMapper.selectAttachList(boardIdx);
	}
	
	@Override
	public AttachDTO getAttachDetail(Long idx) {
		return attachMapper.selectAttachDetail(idx);
	}

}
