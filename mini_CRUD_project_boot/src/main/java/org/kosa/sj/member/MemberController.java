package org.kosa.sj.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kosa.sj.page.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MemberController {

	@Autowired
	MemberService memberService;

	// 유저 리스트
	@GetMapping("/userList")
	public ResponseEntity<Map<String, Object>> getUserList(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "searchValue", required = false) String searchValue) {

		Paging paging = new Paging();
		paging.setNowPage(page);
		paging.setNumPerPage(size);
		paging.setTotalData(memberService.getTotalMemberCount(searchValue));
		paging.calcPaging();

		List<MemberVO> memberList = memberService.list(paging, searchValue);
		return ResponseEntity
				.ok(Map.of("res_code", "200", "res_msg", "회원 목록 조회 성공", "boardList", memberList, "paging", paging));
	}

	// 회원 상세정보 조회
	@GetMapping("/detailView/{userId}")
	public ResponseEntity<Map<String, Object>> getMemberDetail(@PathVariable String userId) {
		MemberVO memberInfo = memberService.getMemberVO(userId);
		if (memberInfo == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("res_code", "404", "res_msg", "해당 사용자를 찾을 수 없습니다."));
		}

		return ResponseEntity.ok(Map.of("res_code", "200", "res_msg", "회원 정보 조회 성공", "member", memberInfo));
	}

	// 회원가입
	@PostMapping("/register")
	public Map<String, Object> register(@RequestBody MemberVO member) throws Exception {
		Map<String, Object> map = new HashMap<>();
		if (member == null) {
			map.put("res_code", "400");
			map.put("res_msg", "입력간에 오류가 발생하였습니다.");
			return map;
		}
		int result = memberService.register(member);
		if (result == 0) {
			map.put("res_code", "400");
			map.put("res_msg", "회원등록에 실패하였습니다.");
		} else {
			map.put("res_code", "200");
			map.put("res_msg", "성공적으로 회원등록을 완료하였습니다.");
		}
		return map;
	}

	// 로그인한 사용자 정보 가져오기
	@GetMapping("/member")
	@ResponseBody
	public Map<String, Object> getMember(HttpSession session) {
		MemberVO member = (MemberVO) session.getAttribute("member");
		if (member == null) {
			return Map.of("res_code", "401", "res_msg", "로그인 안됨");
		}
		return Map.of("res_code", "200", "res_msg", "로그인됨", "member", member);
	}

	// 아이디 중복 검사
	@PostMapping("/isExistUserId")
	public Map<String, Object> isExistUserId(@RequestBody MemberVO member) {
		Map<String, Object> map = new HashMap<>();
		map.put("existUserId", memberService.getMemberVO(member.getUserId()) != null);
		return map;
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(HttpSession session, @RequestBody MemberVO member) {
		if (member.getUserId() == null || member.getUserId().isEmpty() || member.getUserPwd() == null
				|| member.getUserPwd().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("res_code", "400", "res_msg", "아이디 또는 비밀번호를 입력해주세요"));
		}

		Map<String, Object> map = memberService.login(member.getUserId(), member.getUserPwd());

		if ("200".equals(map.get("res_code"))) {
			MemberVO loginMemberVO = memberService.getMemberVO(member.getUserId());
			session.setAttribute("member", loginMemberVO);
			return ResponseEntity.ok(map);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);

		}
	}

	// 로그아웃
	@GetMapping("/logout")
	@ResponseBody
	public Map<String, Object> logout(HttpSession session) {
		session.invalidate();
		return Map.of("res_code", "200", "res_msg", "로그아웃 완료");
	}

	// 수정 & 수정 이력 남기기
	@PostMapping("/update")
	public Map<String, Object> update(HttpSession session, @RequestBody MemberVO member) throws Exception {
		Map<String, Object> map = new HashMap<>();
		int result = memberService.update(member);
		if (result == 0) {
			map.put("res_code", "400");
			map.put("res_msg", "회원 정보 수정 중 오류가 발생하였습니다.");
		} else {
			map.put("res_code", "200");
			map.put("res_msg", "회원 정보 수정에 성공하였습니다.");
		}
		return map;
	}

	// 삭제
	@DeleteMapping("/delete")
	public Map<String, Object> delete(@RequestBody Map<String, String> param, HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		int result = memberService.delete(param.get("userid"));
		if (result == 0) {
			map.put("res_code", "400");
			map.put("res_msg", "회원 탈퇴 중 오류가 발생하였습니다.");
		} else {
			MemberVO loginUser = (MemberVO) session.getAttribute("member");
			boolean isSelfDelete = loginUser != null && param.get("userid").equals(loginUser.getUserId());
			if (isSelfDelete)
				session.invalidate();

			map.put("res_code", "200");
			map.put("res_msg", "회원 탈퇴에 성공하였습니다.");
			map.put("isSelfDelete", isSelfDelete);
		}
		return map;
	}

	// 사용자 로그인 잠금 해제
	@PostMapping("/resetStatus/{userId}")
	public ResponseEntity<Map<String, Object>> resetLoginStatus(@PathVariable String userId) {
		Map<String, Object> map = new HashMap<>();
		int result = memberService.resetStatus(userId);

		if (result == 0) {
			map.put("res_code", "400");
			map.put("res_msg", "로그인 잠금 해제 중 오류가 발생하였습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}

		map.put("res_code", "200");
		map.put("res_msg", "로그인 잠금 해제에 성공하였습니다.");
		return ResponseEntity.ok(map);
	}

	// 사용자 관리자 권한 부여
	@PostMapping("/grantManager")
	public Map<String, Object> grantManager(@RequestBody Map<String, String> param) {
		Map<String, Object> map = new HashMap<>();
		int result = memberService.grantManager(param.get("userid"));
		if (result == 0) {
			map.put("res_code", "400");
			map.put("res_msg", "관리자 권한 부여 중 오류가 발생하였습니다.");
		} else {
			map.put("res_code", "200");
			map.put("res_msg", "관리자 권한 부여에 성공하였습니다.");
		}
		return map;
	}

	// 사용자 관리자 권한 해제
	@PostMapping("/revokeManager")
	public Map<String, Object> revokeManager(@RequestBody Map<String, String> param) {
		Map<String, Object> map = new HashMap<>();
		int result = memberService.revokeManager(param.get("userid"));
		if (result == 0) {
			map.put("res_code", "400");
			map.put("res_msg", "관리자 권한 해제 중 오류가 발생하였습니다.");
		} else {
			map.put("res_code", "200");
			map.put("res_msg", "관리자 권한 해제에 성공하였습니다.");
		}
		return map;
	}
}