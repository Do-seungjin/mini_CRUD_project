package org.kosa.sj.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public class MemberService {
	@Autowired
	private MemberDAO memberDAO;

	public void list(int pageNO, int size, String searchValue) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("start", (pageNO - 1) * size + 1);
		map.put("list", "member");
		map.put("end", pageNO * size);
		map.put("searchValue", searchValue);

	}

	public int register(MemberVO member) {
		int result = memberDAO.register(member);
		return result;
	}

	public MemberVO getMemberVO(String userid) {
		return memberDAO.getMember(userid);
	}

	public Map<String, Object> login(String userid, String passwd) {
		Map<String, Object> map = new HashMap<String, Object>();
		MemberVO member = memberDAO.getMember(userid);
		int failCnt = memberDAO.getFailCnt(userid);

		if (member == null) {
			map.put("res_code", "400");
			map.put("res_msg", "존재하는 회원정보가 없습니다.");
			return map;
		}

		if ("Y".equals(member.getExitStatus())) {
			map.put("res_code", "400");
			map.put("res_msg", "존재하는 회원정보가 없습니다.");
			return map;
		}

		if ("Y".equals(member.getAccountStatus())) {
			map.put("res_code", "400");
			map.put("res_msg", "로그인 잠금된 회원입니다. 관리자에게 문의하세요.");
			return map;
		}

		boolean result = member.getUserPwd().equals(passwd);
		if (result == false) {
			memberDAO.addFailCnt(userid);
			if (memberDAO.getFailCnt(userid) == 5) {
				memberDAO.changeLoginStatus(userid);
			}
			map.put("res_code", "400");
			map.put("res_msg", "비밀번호가 일치하지 않습니다.");
			return map;
		}
		memberDAO.resetFailCnt(userid);
		memberDAO.setLoginTime(userid);
		map.put("res_code", "200");
		return map;
	}

	public int update(MemberVO member) {
		int result = 0;
		MemberVO memberDB = memberDAO.getMember(member.getUserId());
		if (memberDB == null) {
			return result;
		}
		result = memberDAO.update(member);
		memberDAO.history(memberDB);
		if (result == 0) {
			return result;
		} else {
			result = 1;
			return result;
		}
	}

	public int delete(String userid) {
		int result = 0;
		MemberVO memberDB = memberDAO.getMember(userid);
		if (memberDB == null) {
			return result;
		}
		return memberDAO.delete(userid);

	}

	public int resetStatus(String userid) {
		int result = 0;
		MemberVO memberDB = memberDAO.getMember(userid);
		if (memberDB == null) {
			return result;
		}
		return memberDAO.resetStatus(userid);

	}

	public int grantManager(String userid) {
		int result = 0;
		MemberVO memberDB = memberDAO.getMember(userid);
		if (memberDB == null) {
			return result;
		}
		return memberDAO.grantManager(userid);
	}

	public int revokeManager(String userid) {
		int result = 0;
		MemberVO memberDB = memberDAO.getMember(userid);
		if (memberDB == null) {
			return result;
		}
		return memberDAO.revokeManager(userid);
	}
}
