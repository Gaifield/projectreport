package cn.damai.boss.projectreport.manager.vo;



/**
 * 注释：模拟OA用户类
 * 作者：温俊荣 【温俊荣】
 * 时间：14-2-20 上午11:15
 */
public class OAUserVo
{
	
	/**
	 * 用户ID
	 */
	private Long userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 用户组织机构
	 */
	private String userDept;
	
	public OAUserVo(){
		
	}
	public OAUserVo(Long userId, String userName, String userDept){
		this.userId =userId;
		this.userName = userName;
		this.userDept = userDept;
	}
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserDept() {
		return userDept;
	}
	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}
	

}