package cn.damai.boss.projectreport.report.dao;

import cn.damai.boss.projectreport.report.vo.SaleFilterVo;
import cn.damai.boss.projectreport.report.vo.SaleRowVo;
import cn.damai.boss.projectreport.report.vo.SaleVo;

import java.util.Map;

/**
 * 注释：出票汇总DAO
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-6 下午2:05
 */
public interface SaleDAO {

    /**
     * 查询场次数据
     *
     * @param filterVo 过滤参数vo
     * @return
     */
    public Map<Long, SaleVo> findPerformData(SaleFilterVo filterVo);

    /**
     * 查询总场次数据
     *
     * @param filterVo 过滤参数vo
     * @return
     */
    public int findPerformDataTotal(SaleFilterVo filterVo);

    /**
     * 单张票（普通）
     *
     * @param filterVo 过滤参数vo
     * @return
     */
    public Map<Long, Map<String, SaleRowVo>> findDisaccount(SaleFilterVo filterVo);

    /**
     * 得到单张票（赠票、工作票）
     *
     * @param filterVo
     * @return
     */
    public Map<Long, Map<String, SaleRowVo>> findPresentOrStaff(SaleFilterVo filterVo);


    /**
     * 套票（普通）
     *
     * @param filterVo
     * @return
     */
    public Map<Long, Map<String, SaleRowVo>> findDisaccountPromotion(SaleFilterVo filterVo);

    /**
     * 得到套票（赠票、工作票）
     *
     * @param filterVo
     * @return
     */
    public Map<Long, Map<String, SaleRowVo>> findPresentOrStaffPromotion(SaleFilterVo filterVo);

    /**
     * 查询价格对应的总票房、剩余票房【单票】
     *
     * @param filterVo
     * @return
     */
    public Map<Long, Map<String, SaleRowVo>> findTotal(SaleFilterVo filterVo);

    /**
     * 查询价格对应的总票房、剩余票房【套票】
     *
     * @param filterVo
     * @return
     */
    public Map<Long, Map<String, SaleRowVo>> findTotalPromotion(SaleFilterVo filterVo);

    /**
     * 查询单票预留票房【单票】
     *
     * @param filterVo
     * @return
     */
    public Map<Long, Map<String, SaleRowVo>> findReserveBoxOffice(SaleFilterVo filterVo);

    /**
     * 查询套票预留票房【套票】
     *
     * @param filterVo
     * @return
     */
    public Map<Long, Map<String, SaleRowVo>> findReservePromotion(SaleFilterVo filterVo);

    /**
     * 查询工作票和防涨票
     *
     * @param filterVo
     * @return
     */
    public Map<Long, SaleVo> findPerformStaffOrProtect(SaleFilterVo filterVo);
}
