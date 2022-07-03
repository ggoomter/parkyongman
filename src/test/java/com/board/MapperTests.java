package com.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import com.board.domain.PostDTO;
import com.board.mapper.PostMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
public class MapperTests {

	@Autowired
	private PostMapper postMapper;
	
	@Test
	public void testOfInsert() {
		//1. 데이터 준비
		PostDTO params = new PostDTO();
		params.setIdx((long)1);
		params.setBoardIdx(1);	//게시판선택
		params.setTitle("1번 게시글 제목");
		params.setContent("1번 게시글 내용");
		params.setNoticeYn("N");
		params.setWriter("ggoomter");
		
		//2. 테스트
		int result = postMapper.insertPost(params);
		System.out.println("결과는 " + result + "입니다.");
		
		//3. 결과값 활용
	}
	
	
	@Test
	public void testOfSelectDetail() {
		PostDTO post = postMapper.selectPostDetail((long) 2);
		try {
			//String postJson = new ObjectMapper().writeValueAsString(post);
            String postJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(post);

			System.out.println("=========================");
			System.out.println(postJson);
			System.out.println("=========================");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOfUpdate() {
		PostDTO params = new PostDTO();
		params.setTitle("1번 게시글 제목을 수정합니다.");
		params.setContent("1번 게시글 내용을 수정합니다.");
		params.setWriter("홍길동");
		params.setIdx((long) 1);

		int result = postMapper.updatePost(params);
		if (result == 1) {
			PostDTO post = postMapper.selectPostDetail((long) 1);
			try {
				//String postJson = new ObjectMapper().writeValueAsString(post);
                String postJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(post);

				System.out.println("=========================");
				System.out.println(postJson);
				System.out.println("=========================");

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Test
	public void testOfDelete() {
		int result = postMapper.deletePost((long) 2);
		if (result == 1) {
			PostDTO post = postMapper.selectPostDetail((long) 2);
			try {
				//String postJson = new ObjectMapper().writeValueAsString(post);
                String postJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(post);

				System.out.println("=========================");
				System.out.println(postJson);
				System.out.println("=========================");

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testMultipleInsert() {
		for (int i = 2; i <= 50; i++) {
			PostDTO params = new PostDTO();
			params.setTitle(i + "번 게시글 제목");
			params.setContent(i + "번 게시글 내용");
			params.setWriter(i + "번 게시글 작성자");
			postMapper.insertPost(params);
		}
	}
	
	
	@Test
	public void testSelectList() {
//		int postTotalCount = postMapper.selectPostTotalCount();
//		if (postTotalCount > 0) {
//			List<PostDTO> postList = postMapper.selectPostList();
//			if (CollectionUtils.isEmpty(postList) == false) {
//				for (PostDTO post : postList) {
//					System.out.println("=========================");
//					System.out.println(post.getTitle());
//					System.out.println(post.getContent());
//					System.out.println(post.getWriter());
//					System.out.println("=========================");
//				}
//			}
//		}
	}
	
}
