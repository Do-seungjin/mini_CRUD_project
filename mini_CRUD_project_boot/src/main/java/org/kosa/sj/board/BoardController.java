package org.kosa.sj.board;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BoardController {
    
    @Autowired
    private BoardService boardService;

    // 게시물 등록 시 로그인 여부 확인
    @RequestMapping("/postRegisterForm")
    public Map<String, Object> registerForm(@RequestParam("userNo") String userno,
                                            @RequestParam("boardNo") String boardNo) {
        Map<String, Object> map = new HashMap<>();

        if (userno == null || userno.length() == 0) {
            map.put("res_code", "400");
            map.put("res_msg", "로그인 후 이용해주세요.");
            return map;
        }
        map.put("res_code", "200");
        map.put("boardNo", boardNo);
        map.put("userNo", userno);
        return map;
    }

    // 게시글 등록
    @PostMapping("/postRegist")
    public Map<String, Object> postRegist(@RequestBody PostVO post) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (post == null) {
            map.put("res_code", "400");
            map.put("res_msg", "입력간에 오류가 발생하였습니다.");
            return map;
        }
        int result = boardService.postRegist(post);
        if (result == 0) {
            map.put("res_code", "400");
            map.put("res_msg", "게시물 등록에 실패하였습니다.");
        } else {
            map.put("res_code", "200");
            map.put("res_msg", "성공적으로 게시물을 등록 완료하였습니다.");
        }
        return map;
    }

    // 게시물 수정
    @PostMapping("/postUpdate")
    public Map<String, Object> updatePost(@RequestBody PostVO post) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int result = boardService.updatePost(post);
        if (result == 0) {
            map.put("res_code", "400");
            map.put("res_msg", "게시글 수정 중 오류가 발생하였습니다.");
        } else {
            map.put("res_code", "200");
            map.put("res_msg", "게시글 수정에 성공하였습니다.");
        }
        return map;
    }

    // 게시물 삭제
    @DeleteMapping("/postDelete")
    public Map<String, Object> delete(@RequestBody Map<String, String> param) {
        Map<String, Object> map = new HashMap<>();
        int result = boardService.delete(param.get("postNo"));
        if (result == 0) {
            map.put("res_code", "400");
            map.put("res_msg", "게시물 삭제 중 오류가 발생하였습니다.");
        } else {
            map.put("res_code", "200");
            map.put("res_msg", "게시물 삭제를 성공하였습니다.");
        }
        return map;
    }

    // 작성자 게시물 삭제 비밀번호
    @DeleteMapping("/userPostDelete")
    public Map<String, Object> userDelete(@RequestBody Map<String, String> param) {
        return boardService.userDelete(param.get("postno"), param.get("userpwd"));
    }
}