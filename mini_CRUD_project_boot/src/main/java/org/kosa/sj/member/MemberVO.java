package org.kosa.sj.member;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberVO {
	private String userNo;
	private String userId;
	private String userPwd;
	private String userName;
	private Date userBirth;
	private String userPhone;
	private String userZipcode;
	private String userAddr;
	private String userDetailAddr;
	private Date userRegDate;
	private String exitStatus;
	private Date exitDate;
	private String accountStatus;
	private String failCnt;
	private String isManager;
	private Date loginTime;
}
