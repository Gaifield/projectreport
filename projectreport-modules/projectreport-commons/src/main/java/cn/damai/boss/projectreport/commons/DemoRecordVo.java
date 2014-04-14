package cn.damai.boss.projectreport.commons;

import cn.damai.boss.projectreport.commons.utils.ModelUtils;

public class DemoRecordVo  implements java.io.Serializable {
	private Long reservationRecordId;

	public Long getReservationRecordId() {
		return reservationRecordId;
	}

	public void setReservationRecordId(Long reservationRecordId) {
		this.reservationRecordId = reservationRecordId;

	}	
	
	@Override
	public String toString() {
		return "DemoRecordVo [reservationRecordId=" + reservationRecordId + "]";
	}

	public static void main(String[] args){
		DemoRecord record = new DemoRecord();
		record.setReservationRecordId(1l);		
		DemoRecordVo vo = ModelUtils.fromDomainObjectToVo(DemoRecordVo.class, record);		
		DemoRecord domainObject = ModelUtils.toDomainObject(vo, DemoRecord.class);
	}
}