package cn.damai.boss.projectreport.report.vo;

/**
 * 预留级别Vo
 * @author Administrator
 *
 */
public class ReserveTyleVo {
	
	/**
	 * 预留级别Id
	 */
	private long performReserveTyleID;
	
	/**
	 * 预留级别
	 */
	private int reserveTyle;
	
	/**
	 * 预留级别名称
	 */
	private String tyleName;	
	
	public long getPerformReserveTyleID() {
		return performReserveTyleID;
	}

	public void setPerformReserveTyleID(long performReserveTyleID) {
		this.performReserveTyleID = performReserveTyleID;
	}

	public int getReserveTyle() {
		return reserveTyle;
	}

	public void setReserveTyle(int reserveTyle) {
		this.reserveTyle = reserveTyle;
	}

	public String getTyleName() {
		return tyleName;
	}

	public void setTyleName(String tyleName) {
		this.tyleName = tyleName;
	}	
}