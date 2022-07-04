package com.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.board.domain.PostDTO;
import com.board.domain.BoardDTO;

@Mapper
public interface PostMapper {
	
	public int insertPost(PostDTO params);

	public PostDTO selectPostDetail(Long idx);

	public int updatePost(PostDTO params);

	public int deletePost(Long idx);

	public List<PostDTO> selectPostList(PostDTO boarddto);

	public int selectPostTotalCount(PostDTO boarddto);
}
