package com.board.domain;

import java.util.List;

import lombok.Data;

@Data
public class BoardDTO extends CommonDTO{

	/** 번호 (PK) */
	private Long idx;
	
	/** 상위 카테고리 */
	private int upperBoardIdx;

	/** 이름 */
	private String name;

	/** 설명 */
	private String def;

	/** 보이기설정 */
	private String showYn;
	
	/** 작성자 */
	private String writer;
	
	/** 수정자 */
	private String modifier;
	
	/** 파일 변경 여부 */
	private String changeYn;
	
	/** 파일 인덱스 리스트 */
	private List<Long> fileIdxs;

}
