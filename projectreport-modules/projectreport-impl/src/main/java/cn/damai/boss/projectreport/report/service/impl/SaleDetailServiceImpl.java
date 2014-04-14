package cn.damai.boss.projectreport.report.service.impl;

import cn.damai.boss.projectreport.report.dao.ReportPriceDAO;
import cn.damai.boss.projectreport.report.dao.SaleDAO;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.SaleModeEnum;
import cn.damai.boss.projectreport.report.enums.TicketTypeEnum;
import cn.damai.boss.projectreport.report.service.SaleDetailService;
import cn.damai.boss.projectreport.report.vo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 注释：出票汇总service实现类 作者：liutengfei 【刘腾飞】 时间：14-3-6 下午2:04
 */
@Service
public class SaleDetailServiceImpl implements SaleDetailService {

    private static final Log log = LogFactory.getLog(SaleDetailServiceImpl.class);

    /**
     * 出票汇总DAO
     */
    @Resource
    private SaleDAO saleDAO;

    @Resource
    private ReportPriceDAO reportPriceDAO;

    /**
     * 查询出票汇总明细
     *
     * @return
     */
    @Override
    public List<SaleVo> querySaleDetail(SaleFilterVo filterVo) {
        // 切换数据库
        DynamicDataSourceHolder.putDataSourceName(filterVo.getSource());

        // 得到项目数据
        Map<Long, SaleVo> saleDetailVoMap = saleDAO.findPerformData(filterVo);

        // 得到场次id
        List<Long> performIdList = getPerformIdList(saleDetailVoMap);
        filterVo.setPerformIdList(performIdList);
        String performIds = getPerformIds(saleDetailVoMap);
        filterVo.setPerformIds(performIds);


        Map<Long, Map<String, SaleRowVo>> totalMap = saleDAO.findTotal(filterVo);

        Map<Long, Map<String, SaleRowVo>> disaccountMap = saleDAO.findDisaccount(filterVo);

        filterVo.setTicketType(TicketTypeEnum.PreSent.getCode());
        Map<Long, Map<String, SaleRowVo>> presentMap = saleDAO.findPresentOrStaff(filterVo);

        filterVo.setTicketType(TicketTypeEnum.Staff.getCode());
        Map<Long, Map<String, SaleRowVo>> staffMap = saleDAO.findPresentOrStaff(filterVo);

        Map<Long, Map<String, SaleRowVo>> reserveMap = saleDAO.findReserveBoxOffice(filterVo);

        Map<Long, Map<String, SaleRowVo>> totalPromotionMap = saleDAO.findTotalPromotion(filterVo);

        Map<Long, Map<String, SaleRowVo>> disaccountPromotionMap = saleDAO.findDisaccountPromotion(filterVo);

        filterVo.setTicketType(TicketTypeEnum.PreSent.getCode());
        Map<Long, Map<String, SaleRowVo>> presentPromotionMap = saleDAO.findPresentOrStaffPromotion(filterVo);

        filterVo.setTicketType(TicketTypeEnum.Staff.getCode());
        Map<Long, Map<String, SaleRowVo>> staffPromotionMap = saleDAO.findPresentOrStaffPromotion(filterVo);

        Map<Long, Map<String, SaleRowVo>> reservePromotionMap = saleDAO.findReservePromotion(filterVo);

        Map<Long, SaleVo> staffAndProtectMap = saleDAO.findPerformStaffOrProtect(filterVo);

        // 得到项目对应的价格
        Map<Long, List<PriceVo>> priceVoListMap = reportPriceDAO.queryPerformPrice(filterVo.getProjectId(), performIds);

        // 整合数据
        List<SaleVo> saleVoList = new ArrayList<SaleVo>();
        for (Long performId : performIdList) {
            List<SaleRowVo> saleRowVoList = new ArrayList<SaleRowVo>();

            Map<String, SaleRowVo> disaccount = disaccountMap.get(performId);
            Map<String, SaleRowVo> staff = staffMap.get(performId);
            Map<String, SaleRowVo> pesent = presentMap.get(performId);
            Map<String, SaleRowVo> total = totalMap.get(performId);
            Map<String, SaleRowVo> reserve = reserveMap.get(performId);

            Map<String, SaleRowVo> disaccountPromotion = disaccountPromotionMap.get(performId);
            Map<String, SaleRowVo> staffPromotion = staffPromotionMap.get(performId);
            Map<String, SaleRowVo> pesentPromotion = presentPromotionMap.get(performId);
            Map<String, SaleRowVo> totalPromotion = totalPromotionMap.get(performId);
            Map<String, SaleRowVo> reservePromotion = reservePromotionMap.get(performId);

            List<PriceVo> priceVoList = priceVoListMap.get(performId);
            if (priceVoList != null && priceVoList.size() != 0) {
                for (PriceVo priceVo : priceVoList) {
                    String showName = priceVo.getPriceShowName();
                    SaleRowVo saleRowVo = new SaleRowVo();
                    if (priceVo.getTicketType() == SaleModeEnum.Single.getCode()) {
                        saleRowVo = setSaleVo(disaccount, staff, pesent, total, reserve, showName);
                    } else {
                        saleRowVo = setSaleVo(disaccountPromotion, staffPromotion, pesentPromotion, totalPromotion, reservePromotion, showName);
                    }
                    // 设置小计
                    setTotalSaleCell(saleRowVo);
                    // 设置剩余票房
                    setLeftSaleCell(saleRowVo);
                    // 设置可用票房
                    setAvaliableSaleCell(saleRowVo);

                    saleRowVo.setPriceVo(priceVo);
                    saleRowVoList.add(saleRowVo);
                }
            }

            // 设置折扣
            List<String> allDisaccountList = getAllDisaccountList(saleRowVoList);
            setSaleVoDisaccount(saleRowVoList, allDisaccountList);

            SaleVo saleVo = new SaleVo();
            // 设置场次信息
            SaleVo performData = saleDetailVoMap.get(performId);
            saleVo.setProjectName(performData.getProjectName());
            saleVo.setPerformId(performId);
            saleVo.setPerformName(performData.getPerformName());
            saleVo.setPerformTime(performData.getPerformTime().toString());
            // 设置总计
            setPriceTotal(saleRowVoList);
            // 设置所有折扣名称
            saleVo.setAllDisaccountVoList(getDisaccountName(allDisaccountList));
            // 设置总出票数量和金额
            setPerformTotalQuantityAndAmount(saleVo, saleRowVoList);
            // 设置工作票数量和防涨票数量
            SaleVo quantityData = staffAndProtectMap.get(performId);
            if (quantityData != null) {
                saleVo.setStaffQuantity(quantityData.getStaffQuantity());
                saleVo.setProtectQuantity(quantityData.getProtectQuantity());
            }
            saleVo.setSaleRowVoList(saleRowVoList);
            saleVoList.add(saleVo);
        }
        return saleVoList;
    }

    /**
     * 查询出票汇总明细总数
     *
     * @param filterVo
     * @return
     */
    @Override
    public int querySaleDetailTotal(SaleFilterVo filterVo) {
        int total = saleDAO.findPerformDataTotal(filterVo);
        return total;
    }

    /**
     * 查询场次id
     * <p>
     * 如果projectId有值，则根据projectId查询当前项目下的场次 如果performIds有值，则是根据场次查询数据
     * </p>
     *
     * @param saleDetailVoMap 场次数据
     * @return
     */
    private List<Long> getPerformIdList(Map<Long, SaleVo> saleDetailVoMap) {
        List<Long> performIdList = new ArrayList<Long>();
        if (saleDetailVoMap != null && saleDetailVoMap.size() != 0) {
            Set<Long> performIdSet = saleDetailVoMap.keySet();
            performIdList.addAll(performIdSet);
        }

        return performIdList;
    }

    /**
     * 得到所有的场次id，以“,”分割
     *
     * @param saleDetailVoMap
     * @return
     */
    private String getPerformIds(Map<Long, SaleVo> saleDetailVoMap) {
        if (saleDetailVoMap == null || saleDetailVoMap.size() == 0) {
            return null;
        }
        String performIdStrs = null;
        Set<Long> performIdSet = saleDetailVoMap.keySet();
        for (Long performId : performIdSet) {
            performIdStrs = performIdStrs == null ? performId.toString() : performIdStrs + "," + performId;
        }

        return performIdStrs;
    }

    /*
     * 设置SaleVo值
     *
     * @param disaccount 折扣
     *
     * @param Staff 工作票
     *
     * @param present 赠票
     *
     * @param total 总票房
     *
     * @param showName 价格显示名称
     */
    private SaleRowVo setSaleVo(Map<String, SaleRowVo> disaccount, Map<String, SaleRowVo> staff, Map<String, SaleRowVo> present, Map<String, SaleRowVo> total, Map<String, SaleRowVo> reserve, String showName) {
        SaleRowVo saleRowVo = new SaleRowVo();
        PriceCellVo priceCellVo = new PriceCellVo();
        priceCellVo.setQuantity(0);
        priceCellVo.setAmount(BigDecimal.ZERO);

        if (disaccount != null && disaccount.size() != 0) {
            SaleRowVo singleVo = disaccount.get(showName);
            if (singleVo != null)
                saleRowVo.setDisaccountVoList(singleVo.getDisaccountVoList());
        }

        boolean isStaffContain = false;
        if (staff != null && staff.size() != 0) {
            SaleRowVo staffVo = staff.get(showName);
            if (staffVo != null) {
                PriceCellVo staffSale = staffVo.getStaffSale();
                if (staffSale != null) {
                    isStaffContain = true;
                    saleRowVo.setStaffSale(staffSale);
                }
            }
        }
        if (!isStaffContain) {
            saleRowVo.setStaffSale(priceCellVo);
        }

        boolean isPresentContain = false;
        if (present != null && present.size() != 0) {
            SaleRowVo presentVo = present.get(showName);
            if (presentVo != null) {
                PriceCellVo presentSale = presentVo.getPresentSale();
                if (presentSale != null) {
                    isPresentContain = true;
                    saleRowVo.setPresentSale(presentSale);
                }
            }
        }
        if (!isPresentContain) {
            saleRowVo.setPresentSale(priceCellVo);
        }

        boolean isTotalContain = false;
        if (total != null && total.size() != 0) {
            SaleRowVo boxOfficeVo = total.get(showName);
            if (boxOfficeVo != null) {
                PriceCellVo totalSale = boxOfficeVo.getPriceTotalSale();
                if (totalSale != null) {
                    isTotalContain = true;
                    saleRowVo.setPriceTotalSale(totalSale);
                }
            }
        }
        if (!isTotalContain) {
            saleRowVo.setPriceTotalSale(priceCellVo);
        }

        boolean isReserveContain = false;
        if (reserve != null && reserve.size() != 0) {
            SaleRowVo reserveSaleRowVo = reserve.get(showName);
            if (reserveSaleRowVo != null) {
                PriceCellVo reserveSale = reserveSaleRowVo.getReserveSale();
                if (reserveSale != null) {
                    isReserveContain = true;
                    saleRowVo.setReserveSale(reserveSale);
                }
            }
        }
        if (!isReserveContain) {
            saleRowVo.setReserveSale(priceCellVo);
        }
        return saleRowVo;
    }

    /**
     * 设置小计
     *
     * @param saleRowVo
     */
    private void setTotalSaleCell(SaleRowVo saleRowVo) {
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<DisaccountVo> disaccountVoList = saleRowVo.getDisaccountVoList();
        if (disaccountVoList != null && disaccountVoList.size() != 0) {
            for (DisaccountVo vo : disaccountVoList) {
                totalQuantity += vo.getQuantity();
                BigDecimal amount = vo.getAmount();
                if (amount != null && amount.compareTo(BigDecimal.ZERO) != 0) {
                    totalAmount = totalAmount.add(amount);
                }
            }
        }
        PriceCellVo presentSale = saleRowVo.getPresentSale();
        if (presentSale != null) {
            totalQuantity += presentSale.getQuantity();
            BigDecimal amount = presentSale.getAmount();
            if (amount != null && amount.compareTo(BigDecimal.ZERO) != 0) {
                totalAmount = totalAmount.add(amount);
            }
        }
        PriceCellVo staffSale = saleRowVo.getStaffSale();
        if (staffSale != null) {
            totalQuantity += staffSale.getQuantity();
            BigDecimal amount = staffSale.getAmount();
            if (amount != null && amount.compareTo(BigDecimal.ZERO) != 0) {
                totalAmount = totalAmount.add(amount);
            }
        }
        PriceCellVo vo = new PriceCellVo();
        vo.setQuantity(totalQuantity);
        vo.setAmount(totalAmount);
        saleRowVo.setTotalSaleCell(vo);
    }

    /**
     * 设置剩余票房
     *
     * @param saleRowVo
     */
    private void setLeftSaleCell(SaleRowVo saleRowVo) {
        // 总数量和金额
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        PriceCellVo totalSale = saleRowVo.getPriceTotalSale();
        if (totalSale != null) {
            totalQuantity = totalSale.getQuantity();
            totalAmount = totalSale.getAmount();
        }

        // 小计数量和金额
        int totalQuantityCell = 0;
        BigDecimal totalAmountCell = BigDecimal.ZERO;
        PriceCellVo totalSaleCell = saleRowVo.getTotalSaleCell();
        if (totalSaleCell != null) {
            totalQuantityCell = totalSaleCell.getQuantity();
            totalAmountCell = totalSaleCell.getAmount();
        }

        // 剩余数量和金额
        int leftQuantity = totalQuantity - totalQuantityCell;
        BigDecimal leftAmount = totalAmount.subtract(totalAmountCell);
        if (leftQuantity < 0) {
            leftQuantity = 0;
        }
        if (leftAmount.compareTo(BigDecimal.ZERO) < 0) {
            leftAmount = BigDecimal.ZERO;
        }

        // 设置剩余数量和金额
        PriceCellVo vo = new PriceCellVo();
        vo.setQuantity(leftQuantity);
        vo.setAmount(leftAmount);
        saleRowVo.setLeftSale(vo);
    }

    /**
     * 设置可售票房
     *
     * @param saleRowVo
     */
    private void setAvaliableSaleCell(SaleRowVo saleRowVo) {
        int availableQuantity = 0;
        BigDecimal availableAmount = BigDecimal.ZERO;

        PriceCellVo leftSale = saleRowVo.getLeftSale();
        if (leftSale != null) {
            availableQuantity = leftSale.getQuantity();
            BigDecimal amount = leftSale.getAmount();
            if (amount != null && amount.compareTo(BigDecimal.ZERO) != 0) {
                availableAmount = amount;
            }
        }

        PriceCellVo reserveSale = saleRowVo.getReserveSale();
        if (reserveSale != null) {
            availableQuantity -= reserveSale.getQuantity();
            BigDecimal amount = reserveSale.getAmount();
            if (amount != null && amount.compareTo(BigDecimal.ZERO) != 0) {
                availableAmount = availableAmount.subtract(amount);
            }
        }

        if (availableQuantity < 0) {
            availableQuantity = 0;
        }
        if (availableAmount.compareTo(BigDecimal.ZERO) < 0) {
            availableAmount = BigDecimal.ZERO;
        }
        PriceCellVo vo = new PriceCellVo();
        vo.setQuantity(availableQuantity);
        vo.setAmount(availableAmount);
        saleRowVo.setAvailableSale(vo);
    }

    /**
     * 得到所有的打折类型
     *
     * @param saleRowVoList
     * @return
     */
    private List<String> getAllDisaccountList(List<SaleRowVo> saleRowVoList) {
        Set<String> disaccountSet = new HashSet<String>();
        for (SaleRowVo saleRowVo : saleRowVoList) {
            List<DisaccountVo> disaccountVoList = saleRowVo.getDisaccountVoList();
            if (disaccountVoList == null || disaccountVoList.size() == 0) {
                continue;
            }
            for (DisaccountVo vo : disaccountVoList) {
                disaccountSet.add(vo.getDisaccountName());
            }
        }
        List<String> disaccountList = new ArrayList<String>(disaccountSet);
        Collections.sort(disaccountList);
        return disaccountList;
    }

    /**
     * 设置SaleVo的折扣
     * <p/>
     * 如果当前价格对应的折扣只有80折、85折，而场次对应的所有折扣是：80折、85折、90折 需设置当前价格对应的90折的数量和金额为0
     * </p>
     *
     * @param saleRowVoList
     * @param allDisaccountList 全部折扣
     */
    private void setSaleVoDisaccount(List<SaleRowVo> saleRowVoList, List<String> allDisaccountList) {
        for (SaleRowVo saleRowVo : saleRowVoList) {
            List<DisaccountVo> disaccountVoList = saleRowVo.getDisaccountVoList();
            if (disaccountVoList == null || disaccountVoList.size() == 0) {
                disaccountVoList = new ArrayList<DisaccountVo>();
                for (String disaccountName : allDisaccountList) {
                    DisaccountVo vo = new DisaccountVo();
                    vo.setAmount(BigDecimal.ZERO);
                    vo.setQuantity(0);
                    vo.setDisaccountName(disaccountName);
                    disaccountVoList.add(vo);
                }
                saleRowVo.setDisaccountVoList(disaccountVoList);
            } else {
                for (String disaccountName : allDisaccountList) {
                    boolean isContain = false;
                    for (DisaccountVo vo : disaccountVoList) {
                        String name = vo.getDisaccountName();
                        if (disaccountName != null && disaccountName.equals(name)) {
                            isContain = true;
                            break;
                        }
                    }
                    if (!isContain) {
                        DisaccountVo vo = new DisaccountVo();
                        vo.setDisaccountName(disaccountName);
                        vo.setQuantity(0);
                        vo.setAmount(BigDecimal.ZERO);
                        disaccountVoList.add(vo);
                    }
                }
            }
            saleRowVo.setDisaccountVoList(disaccountVoList);
        }
    }

    /**
     * 设置场次的总计出票数量和金额
     *
     * @param saleVo
     * @param saleRowVoList
     */
    private void setPerformTotalQuantityAndAmount(SaleVo saleVo, List<SaleRowVo> saleRowVoList) {
        for (SaleRowVo saleRowVo : saleRowVoList) {
            PriceVo priceVo = saleRowVo.getPriceVo();
            String priceShowName = priceVo.getPriceShowName();
            if ("总计".equals(priceShowName)) {
                PriceCellVo totalSaleCell = saleRowVo.getTotalSaleCell();
                if (totalSaleCell != null) {
                    int totalQuantity = totalSaleCell.getQuantity();
                    BigDecimal amount = totalSaleCell.getAmount();
                    saleVo.setTotalQuantity(totalQuantity);
                    saleVo.setTotalAmount(amount);
                }
            }
        }
    }

    /**
     * 设置总计
     *
     * @param saleRowVoList
     */
    private void setPriceTotal(List<SaleRowVo> saleRowVoList) {
        if (saleRowVoList == null || saleRowVoList.size() == 0) {
            return;
        }
        SaleRowVo tgtRowVo = new SaleRowVo();
        for (SaleRowVo srcRowVo : saleRowVoList) {
            setRowVo(srcRowVo, tgtRowVo);
        }
        //设置总计
        PriceVo priceVo = new PriceVo();
        priceVo.setPriceName("总计");
        priceVo.setPriceShowName("总计");
        priceVo.setTicketType((short) 0);
        tgtRowVo.setPriceVo(priceVo);
        saleRowVoList.add(tgtRowVo);
    }

    /**
     * 得到折扣名称
     * <p>
     * 先将折扣集合排序，然后设置折扣名称
     * </p>
     *
     * @param allDisaccountList 全部折扣
     */
    public List<String> getDisaccountName(List<String> allDisaccountList) {
        Collections.sort(allDisaccountList, new Comparator<String>() {
            @Override
            public int compare(String srcValue, String tgtValue) {
                BigDecimal src = new BigDecimal(srcValue);
                BigDecimal tgt = new BigDecimal(tgtValue);
                return tgt.compareTo(src);
            }
        });

        List<String> list = new ArrayList<String>();
        for (String name : allDisaccountList) {
            if (name.indexOf(".") > 0) {
                while (name.lastIndexOf("0") == name.length() - 1) {
                    name = name.substring(0, name.lastIndexOf("0"));
                }
                if (name.indexOf(".") == name.length() - 1) {
                    name = name.substring(0, name.length() - 1);
                }
            }
            name = "100".equals(name) ? "正常销售" : name + "折出售";
            list.add(name);
        }
        return list;
    }

    /**
     * 设置行信息
     * <p>
     * 把srcRowVo的值，设置到tgtRowVo
     * </p>
     *
     * @param srcRowVo
     * @param tgtRowVo
     */
    public void setRowVo(SaleRowVo srcRowVo, SaleRowVo tgtRowVo) {
        // 可售总票房
        PriceCellVo srcPriceTotalSale = srcRowVo.getPriceTotalSale();
        PriceCellVo tgtPriceTotalSale = tgtRowVo.getPriceTotalSale();
        tgtPriceTotalSale = getTgtPriceCellVo(srcPriceTotalSale, tgtPriceTotalSale);
        tgtRowVo.setPriceTotalSale(tgtPriceTotalSale);

        // 折扣
        List<DisaccountVo> srcDisaccountVoList = srcRowVo.getDisaccountVoList();
        List<DisaccountVo> tgtDisaccountVoList = tgtRowVo.getDisaccountVoList();
        tgtDisaccountVoList = getDisaccountVo(srcDisaccountVoList, tgtDisaccountVoList);
        tgtRowVo.setDisaccountVoList(tgtDisaccountVoList);

        // 赠票出票
        PriceCellVo srcPresentSale = srcRowVo.getPresentSale();
        PriceCellVo tgtPresentSale = tgtRowVo.getPresentSale();
        tgtPresentSale = getTgtPriceCellVo(srcPresentSale, tgtPresentSale);
        tgtRowVo.setPresentSale(tgtPresentSale);

        // 工作票出票
        PriceCellVo srcStaffSale = srcRowVo.getStaffSale();
        PriceCellVo tgtStaffSale = tgtRowVo.getStaffSale();
        tgtStaffSale = getTgtPriceCellVo(srcStaffSale, tgtStaffSale);
        tgtRowVo.setStaffSale(tgtStaffSale);

        // 小计
        PriceCellVo srcTotalSaleCell = srcRowVo.getTotalSaleCell();
        PriceCellVo tgtTotalSaleCell = tgtRowVo.getTotalSaleCell();
        tgtTotalSaleCell = getTgtPriceCellVo(srcTotalSaleCell, tgtTotalSaleCell);
        tgtRowVo.setTotalSaleCell(tgtTotalSaleCell);

        // 剩余票房
        PriceCellVo srcLeftSale = srcRowVo.getLeftSale();
        PriceCellVo tgtLeftSale = tgtRowVo.getLeftSale();
        tgtLeftSale = getTgtPriceCellVo(srcLeftSale, tgtLeftSale);
        tgtRowVo.setLeftSale(tgtLeftSale);

        // 预留票房
        PriceCellVo srcReserveSale = srcRowVo.getReserveSale();
        PriceCellVo tgtReserveSale = tgtRowVo.getReserveSale();
        tgtReserveSale = getTgtPriceCellVo(srcReserveSale, tgtReserveSale);
        tgtRowVo.setReserveSale(tgtReserveSale);

        // 当前可售票房
        PriceCellVo srcAvailableSale = srcRowVo.getAvailableSale();
        PriceCellVo tgtAvailableSale = tgtRowVo.getAvailableSale();
        tgtAvailableSale = getTgtPriceCellVo(srcAvailableSale, tgtAvailableSale);
        tgtRowVo.setAvailableSale(tgtAvailableSale);
    }

    /**
     * 设置单元格信息
     *
     * @param srcPriceCellVo 源单元格
     * @param tgtPriceCellVo 目标单元格
     */
    private PriceCellVo getTgtPriceCellVo(PriceCellVo srcPriceCellVo, PriceCellVo tgtPriceCellVo) {
        if (tgtPriceCellVo == null) {
            tgtPriceCellVo = new PriceCellVo();
            tgtPriceCellVo.setQuantity(0);
            tgtPriceCellVo.setAmount(BigDecimal.ZERO);
        }
        if (srcPriceCellVo == null) {
            return tgtPriceCellVo;
        }
        int quantity = tgtPriceCellVo.getQuantity();
        BigDecimal amount = tgtPriceCellVo.getAmount();
        amount = amount == null ? BigDecimal.ZERO : amount;
        quantity += srcPriceCellVo.getQuantity();
        amount = amount.add(srcPriceCellVo.getAmount());
        tgtPriceCellVo.setQuantity(quantity);
        tgtPriceCellVo.setAmount(amount);
        return tgtPriceCellVo;
    }

    /**
     * 设置折扣
     *
     * @param srcDisaccountVoList
     * @param tgtDisaccountVoList
     */
    private List<DisaccountVo> getDisaccountVo(List<DisaccountVo> srcDisaccountVoList, List<DisaccountVo> tgtDisaccountVoList) {
        if (tgtDisaccountVoList == null) {
            tgtDisaccountVoList = new ArrayList<DisaccountVo>();
        }
        if (tgtDisaccountVoList.size() == 0) {
            for (DisaccountVo vo : srcDisaccountVoList) {
                DisaccountVo disaccountVo = new DisaccountVo();
                disaccountVo.setDisaccountName(vo.getDisaccountName());
                disaccountVo.setQuantity(vo.getQuantity());
                disaccountVo.setAmount(vo.getAmount());
                tgtDisaccountVoList.add(disaccountVo);
            }
            return tgtDisaccountVoList;
        } else {
            for (DisaccountVo tgtVo : tgtDisaccountVoList) {
                String tgtName = tgtVo.getDisaccountName();
                int tgtQuantity = tgtVo.getQuantity();
                BigDecimal tgtAmount = tgtVo.getAmount();
                tgtAmount = tgtAmount == null ? BigDecimal.ZERO : tgtAmount;
                for (DisaccountVo srcVo : srcDisaccountVoList) {
                    String srcName = srcVo.getDisaccountName();
                    if (tgtName != null && tgtName.equals(srcName)) {
                        int quantity = srcVo.getQuantity();
                        BigDecimal amount = srcVo.getAmount();
                        tgtQuantity += quantity;
                        tgtAmount = tgtAmount.add(amount);
                    }
                }
                tgtVo.setQuantity(tgtQuantity);
                tgtVo.setAmount(tgtAmount);
            }
            return tgtDisaccountVoList;
        }
    }

}
