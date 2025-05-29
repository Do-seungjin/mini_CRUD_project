package org.kosa.sj.board;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BoardController {

	@Autowired
	private BoardService boardService;

	// 게시물 목록 화면 이동
	@GetMapping("/postlist")
	public ResponseEntity<Map<String, Object>> list(@RequestParam(value = "page", defaultValue = "1") int page) {
		Paging paging = new Paging();
		paging.setNowPage(page);
		paging.setTotalData(boardService.getTotalDataCount());
		paging.calcPaging(); // limitPageNo, totalPage 등 계산

		List<PostVO> boardList = boardService.getList();

		return ResponseEntity.ok(Map.of(
				"res_code", "200",
				"res_msg", "게시물 목록 조회 성공",
				"boardList", boardList,
				"paging", paging));
	}

	// 게시물 상세 조회
	@GetMapping("/postDetailView/{postno}")
	public ResponseEntity<Map<String, Object>> getPostDetail(@PathVariable String postno, HttpServletRequest request) {
		PostVO postDetail = boardService.getPost(postno);
		if (postDetail == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("res_code", "404", "res_msg", "게시물이 존재하지 않습니다."));
		}

		String referer = request.getHeader("Referer");
		if (referer != null && !referer.contains("postUpdateForm")) {
			boardService.addViewCnt(postno);
		}
		return ResponseEntity.ok(Map.of("res_code", "200", "res_msg", "게시물 조회 성공", "data", postDetail));
	}

	// 게시물 등록 시 로그인 여부 확인
	@GetMapping("/postRegisterForm")
	public ResponseEntity<Map<String, Object>> registerForm(@RequestParam("userNo") String userno,
			@RequestParam("boardNo") String boardNo) {
		if (userno == null || userno.length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("res_code", "400", "res_msg", "로그인 후 이용해주세요."));
		}
		return ResponseEntity.ok(Map.of("res_code", "200", "boardNo", boardNo, "userNo", userno));
	}

	// 게시글 등록
	@PostMapping("/postRegist")
	public ResponseEntity<Map<String, Object>> postRegist(@RequestBody PostVO post) throws Exception {
		if (post == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("res_code", "400", "res_msg", "입력간에 오류가 발생하였습니다."));
		}
		int result = boardService.postRegist(post);
		if (result == 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("res_code", "400", "res_msg", "게시물 등록에 실패하였습니다."));
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("res_code", "200", "res_msg", "성공적으로 게시물을 등록 완료하였습니다."));
	}

	// 게시물 수정
	@PostMapping("/postUpdate")
	public ResponseEntity<Map<String, Object>> updatePost(@RequestBody PostVO post) throws Exception {
		int result = boardService.updatePost(post);
		if (result == 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("res_code", "400", "res_msg", "게시글 수정 중 오류가 발생하였습니다."));
		}
		return ResponseEntity.ok(Map.of("res_code", "200", "res_msg", "게시글 수정에 성공하였습니다."));
	}

	// 게시물 삭제
	@DeleteMapping("/postDelete")
	public ResponseEntity<Map<String, Object>> delete(@RequestBody Map<String, String> param) {
		int result = boardService.delete(param.get("postNo"));
		if (result == 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("res_code", "400", "res_msg", "게시물 삭제 중 오류가 발생하였습니다."));
		}
		return ResponseEntity.ok(Map.of("res_code", "200", "res_msg", "게시물 삭제를 성공하였습니다."));
	}

	// 작성자 게시물 삭제 비밀번호
	@DeleteMapping("/userPostDelete")
	public ResponseEntity<Map<String, Object>> userDelete(@RequestBody Map<String, String> param) {
		Map<String, Object> result = boardService.userDelete(param.get("postno"), param.get("userpwd"));
		return ResponseEntity.ok(result);
	}
}