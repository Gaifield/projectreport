package cn.damai.boss.projectreport.commons;



public class DemoRecord implements java.io.Serializable {
	private Long reservationRecordId;

	public Long getReservationRecordId() {
		return reservationRecordId;
	}
	
	@Override
	public String toString() {
		return "DemoRecordVo [reservationRecordId=" + reservationRecordId + "]";
	}
	
	public void setReservationRecordId(Long reservationRecordId) {
		this.reservationRecordId = reservationRecordId;

	}
}