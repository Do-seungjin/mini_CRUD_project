package org.kosa.sj.board;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardDAO {
	  public List<PostVO> findAll();
	  public int getTotalCount(Map<String, Object> map);
	  public List<BoardTypeVO> getBoardType();
	  public int postRegist(PostVO post);
	  public PostVO getPost(String postno);
	  public int postUpdate(PostVO post);
	  public int postDelete(String postNo);
	  public void addViewCnt(String postNo);
	  public int count();
}
