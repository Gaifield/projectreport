package cn.damai.boss.projectreport.report.service.impl;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import cn.damai.boss.maitix.vo.BusinessUserVo;
import cn.damai.boss.projectreport.common.service.ContextService;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;
import cn.damai.boss.projectreport.report.context.ReportUserContext;
import cn.damai.boss.projectreport.report.dao.ReportMaitixUserDAO;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.service.ReportAuthenticateService;
import cn.damai.component.user.facade.UserFacade;
import cn.damai.soa.maitix.user.service.OrgUserManagerService;

/**
 * 注释：用户认证service实现类
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午1:34
 */
@Service
public class ReportAuthenticateServiceImpl implements ReportAuthenticateService {

    /**
     * maitix主办方dao
     */
    @Resource
    private ReportMaitixUserDAO reportMaitixUserDAO;
    /**
     * 报表上下文信息
     */
    @Resource
    private ContextService contextService;

    /**
     * 组织机构接口
     */
    @Resource
    private UserFacade userFacade;

    /**
     * maitix dubbo服务接口
     */
    @Resource
    private OrgUserManagerService orgUserManagerService;

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(ReportAuthenticateServiceImpl.class);

    /**
     * json 解析处理器
     */
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * 建立用户上下文信息
     *
     * @return
     */
    @Override
    public ReportUserContext buildUserContext(String userName, String password) throws ApplicationException {
        try {
            ReportUserContext reportUserContext = buildContext(userName, password);
            String json = jsonMapper.writeValueAsString(reportUserContext);
            if (log.isDebugEnabled()) {
                log.debug("用户上下文信息\n" + json);
            }
            contextService.add(reportUserContext.getSessionID(), json);
            return reportUserContext;
        } catch (Exception e) {
            log.error("创建用户上下文信息失败");
            throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
        }
    }

    /**
     * 建立用户上下文信息
     *
     * @param userName 用户名
     * @return
     */
    private ReportUserContext buildContext(String userName, String password) throws ApplicationException {
        try {
            ReportUserContext reportUserContext = new ReportUserContext();
            Long userId = new Long(0);
            List<BusinessUserVo> businessUserVoList = orgUserManagerService.validateUserLogining(userName, new String(password));
            if (businessUserVoList != null && businessUserVoList.size() != 0) {
                userId = businessUserVoList.get(0).getUserId();
            }
            if (userId != null && userId != 0) {
                for (BusinessUserVo businessUserVo : businessUserVoList) {
                    Long oldDataId = businessUserVo.getOldDataId();
                    String dataSite = String.valueOf(businessUserVo.getDataSite());
                    if (dataSite.equals(DataSourceEnum.BJMaitix.getCodeStr())) {
                        reportUserContext.setBjBusinessId(oldDataId);
                    } else if (dataSite.equals(DataSourceEnum.SHMaitix.getCodeStr())) {
                        reportUserContext.setShBusinessId(oldDataId);
                    } else if (dataSite.equals(DataSourceEnum.GZMaitix.getCodeStr())) {
                        reportUserContext.setGzBusinessId(oldDataId);
                    }
                }
                DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Report.getCodeStr());
                MaitixUserVo maitixUserVo = reportMaitixUserDAO.findByUserId(userId);
                reportUserContext.setBusinessUserId(maitixUserVo.getMaitixUserId());
                reportUserContext.setMaitixBusinessUserId(maitixUserVo.getUserId());
                reportUserContext.setUserType(maitixUserVo.getRoleType());

                reportUserContext.setBusinessUserName(userName);
                reportUserContext.setTimeStamp(String.valueOf(System.currentTimeMillis()));
                StringBuffer sessionID = new StringBuffer();
                String sysPrefix = "CN_DAMAI_BOSS_PROJECTREPORT_REPORT";
                sessionID.append(sysPrefix).append("_").append(userId);
                reportUserContext.setSessionID(sessionID.toString());
            }
            return reportUserContext;
        } catch (Exception e) {
            throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
        }
    }


}
