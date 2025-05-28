package org.kosa.sj.board;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostVO {
	private String postNo;
	private String userNo;
	private String userName;
	private String boardNo;
	private String postTitle;
	private String postContent;
	private String commentStatus;
	private Date postRegDate;
	private Date postModDate;
	private String isPostDelete;
	private Date postDeleteDate;
	private String viewCnt;
}
