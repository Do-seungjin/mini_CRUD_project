package org.kosa.sj.member;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberDAO {
  public int register(MemberVO member);
  public MemberVO getMember(@Param("userid") String userid);
  public MemberVO getMemberByNo(String userno);
  public void setLoginTime(String userid);
  public int update(MemberVO member);
  public void history(MemberVO member);
  public int delete(String userid);
  public List<MemberVO> list(
		  @Param("limitPageNo") int limitPageNo,
		  @Param("numPerPage")int numPerPage,
		  @Param("searchValue")String searchValue);
  public int getTotalCount(String searchValue);
  public void addFailCnt(String userid);
  public int getFailCnt(String userid);
  public void changeLoginStatus(String userid);
  public int resetFailCnt(String userid);
  public int resetStatus(String userid);
  public int grantManager(String userid);
  public int revokeManager(String userid);
}
