package cn.damai.boss.projectreport.report.vo;

import java.util.List;

import cn.damai.boss.projectreport.report.enums.ReserveStatTypeEnum;

/**
 * 预留统计类型Vo
 * @author Administrator
 *
 */
public class ReserveStatTypeVo {	
	/**
	 * 预留统计类型：0 总预留，1管理端预留、2客户端预留
	 */
	private short reserveStatType;
	
	/**
	 * 预留统计类型名称
	 */
	private String reserveStatName;
	
	/**
	 * 价格行列表
	 */
	private List<PriceStatRowVo> rows;

	public short getReserveStatType() {
		return reserveStatType;
	}

	public void setReserveStatType(short reserveStatType) {
		this.reserveStatType = reserveStatType;
		this.reserveStatName= ReserveStatTypeEnum.getName(reserveStatType);
	}

	public String getReserveStatName() {
		return reserveStatName;
	}

	public void setReserveStatName(String reserveStatName) {
		this.reserveStatName = reserveStatName;		
	}

	public List<PriceStatRowVo> getRows() {
		return rows;
	}

	public void setRows(List<PriceStatRowVo> rows) {
		this.rows = rows;
	}	
}