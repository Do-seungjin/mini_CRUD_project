package org.kosa.sj.member;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDAO {
  public int register(MemberVO member);
  public MemberVO getMember(String userid);
  public MemberVO getMemberByNo(String userno);
  public void setLoginTime(String userid);
  public int update(MemberVO member);
  public void history(MemberVO member);
  public int delete(String userid);
  public List<MemberVO> list(Map<String, Object> map);
  public int getTotalCount(Map<String, Object> map);
  public void addFailCnt(String userid);
  public int getFailCnt(String userid);
  public void changeLoginStatus(String userid);
  public void resetFailCnt(String userid);
  public int resetStatus(String userid);
  public int grantManager(String userid);
  public int revokeManager(String userid);
}
