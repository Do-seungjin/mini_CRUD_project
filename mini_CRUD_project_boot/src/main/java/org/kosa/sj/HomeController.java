package org.kosa.sj;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.kosa.sj.board.BoardService;
import org.kosa.sj.board.BoardTypeVO;
import org.kosa.sj.board.PostVO;
import org.kosa.sj.page.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private BoardService boardService;

	@GetMapping("/main")
	public Map<String, Object> getHomeData(Locale locale) {
		Map<String, Object> result = new HashMap<>();

		for (int i = 0; i < 3; i++) {
			String boardNo = String.valueOf(i + 1); // "1", "2", "3"

			Paging paging = new Paging();
			paging.setNowPage(1);
			paging.setNumPerPage(5);

			// 게시글 조회
			List<PostVO> posts = boardService.getHomeList(boardNo, paging);

			int total = boardService.getTotalBoardCount(boardNo);
			paging.setTotalData(total);
			paging.calcPaging();

			Map<String, Object> pageResponse = new HashMap<>();
			pageResponse.put("list", posts);
			pageResponse.put("paging", paging);

			result.put("pageResponse" + boardNo, pageResponse);
		}

		// 게시판 유형도 포함
		List<BoardTypeVO> boardType = boardService.getBoardType();
		result.put("boardType", boardType);

		return result;
	}
}
