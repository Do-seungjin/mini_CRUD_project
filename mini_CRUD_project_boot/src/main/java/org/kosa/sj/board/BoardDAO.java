package org.kosa.sj.board;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardDAO {
	  public List<PostVO> findAll();
	  public List<PostVO> findBoardPost(@Param("boardNo") String boardNo,
              @Param("numberPerPage") int numberPerPage);
	  public int getBoardCount(String BoardNo);
	  public int getTotalCount(Map<String, Object> map);
	  public List<BoardTypeVO> getBoardType();
	  public int postRegist(PostVO post);
	  public PostVO getPost(String postno);
	  public int postUpdate(PostVO post);
	  public int postDelete(String postNo);
	  public void addViewCnt(String postNo);
	  public int count();
	  
}
