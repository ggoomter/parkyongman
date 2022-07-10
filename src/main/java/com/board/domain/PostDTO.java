package com.board.domain;

import java.util.List;

import lombok.Data;

@Data
public class PostDTO extends CommonDTO{

	/** 번호 (PK) */
	private Long idx;
	
	/** 게시판번호 */
	private Long boardIdx;
	
	/** 게시판번호에 의한 카테고리명 */
	private String category;

	/** 제목 */
	private String title;

	/** 내용 */
	private String content;

	/** 작성자 */
	private String writer;

	/** 조회 수 */
	private int viewCnt;

	/** 공지 여부 */
	private String noticeYn;

	/** 비밀 여부 */
	private String secretYn;

	/** 파일 변경 여부 */
	private String changeYn;

	/** 파일 인덱스 리스트 */
	private List<Long> fileIdxs;	/* 태가 변하지 않은 파일의 번호(PK)*/
	
	
}
