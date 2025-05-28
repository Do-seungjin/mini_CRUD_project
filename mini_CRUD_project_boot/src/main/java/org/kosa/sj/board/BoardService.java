package org.kosa.sj.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kosa.sj.member.MemberDAO;
import org.kosa.sj.member.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
	@Autowired
	private BoardDAO boardDAO;
	@Autowired
	private MemberDAO memberDAO;

	public List<BoardTypeVO> getBoardType() {
		List<BoardTypeVO> result = boardDAO.getBoardType();
		return result;
	}

	public int postRegist(PostVO post) {
		int result = boardDAO.postRegist(post);
		return result;
	}

	public PostVO getPost(String postno) {
		return boardDAO.getPost(postno);
	}

	public int updatePost(PostVO post) {
		int result = boardDAO.postUpdate(post);
		return result;
	}

	public int delete(String postNo) {
		int result = boardDAO.postDelete(postNo);
		return result;
	}

	public void addViewCnt(String postno) {
		boardDAO.addViewCnt(postno);
	}

	public Map<String, Object> userDelete(String postno, String userpwd) {
		Map<String, Object> map = new HashMap<String, Object>();
		PostVO post = boardDAO.getPost(postno);
		MemberVO member = memberDAO.getMemberByNo(post.getUserNo());
		if (member == null) {
			map.put("res_code", "400");
			map.put("res_msg", "존재하지 않는 사용자입니다.");
			return map;
		}
		if (member.getUserPwd().equals(userpwd)) {
			int result = boardDAO.postDelete(postno);
			if (result == 1) {
				map.put("res_code", "200");
				map.put("res_msg", "게시물 삭제를 성공하였습니다.");
			} else {
				map.put("res_code", "400");
				map.put("res_msg", "게시물 삭제 중 오류가 발생하였습니다.");
			}
		} else {
			map.put("res_code", "400");
			map.put("res_msg", "비밀번호가 일치하지 않습니다.");
		}
		return map;
	}
}
