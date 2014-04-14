package cn.damai.boss.projectreport.manager.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.RollbackException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.domain.Upt01MaitixUser;
import cn.damai.boss.projectreport.domain.Upt01Report;
import cn.damai.boss.projectreport.domain.Upt01ReportRole;
import cn.damai.boss.projectreport.domain.Upt01RoleReport;
import cn.damai.boss.projectreport.manager.context.ManagerUserContextUtil;
import cn.damai.boss.projectreport.manager.dao.MaitixUserDao;
import cn.damai.boss.projectreport.manager.dao.OperatorLogDAO;
import cn.damai.boss.projectreport.manager.dao.ReportDAO;
import cn.damai.boss.projectreport.manager.dao.ReportRoleDAO;
import cn.damai.boss.projectreport.manager.dao.RoleReportDAO;
import cn.damai.boss.projectreport.manager.enums.OperatorLogTypeEnum;
import cn.damai.boss.projectreport.manager.enums.RoleStatusEnum;
import cn.damai.boss.projectreport.manager.enums.UserTypeEnum;
import cn.damai.boss.projectreport.manager.service.RoleManagerService;
import cn.damai.boss.projectreport.manager.vo.ReportRoleVo;
import cn.damai.boss.projectreport.manager.vo.ReportVo;

/**
 * Created by 炜 on 14-2-20.
 */
@Service
public class RoleManagerServiceImpl implements RoleManagerService {
    /**
     * 报表角色DAO
     */
    @Resource
    private ReportRoleDAO reportRoleDAO;

    /**
     * 报表角色关系DAO
     */
    @Resource
    private RoleReportDAO roleReportDAO;

    /**
     * 报表DAO
     */
    @Resource
    private ReportDAO reportDAO;
    
    /**
     *
     */
    @Resource
    private OperatorLogDAO operatorLogDAO;
    
    @Resource
    private MaitixUserDao maitixUserDao;
    
    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    @Transactional(value = "projectReportTransactionManager")
    public List<ReportRoleVo> queryAllReportRoles() {
        List<Upt01ReportRole> upt01ReportRoleList = reportRoleDAO.findAll();
        List<ReportRoleVo> reportRoleVos = new ArrayList<ReportRoleVo>();
        for (Upt01ReportRole reportRole : upt01ReportRoleList) {
            ReportRoleVo reportRoleVo = convertUpt01ReportRoleToVo(reportRole);
            reportRoleVos.add(reportRoleVo);
            List<ReportVo> reportVoList = new ArrayList<ReportVo>();
            List<Upt01RoleReport> roleReportList = reportRole.getUpt01RoleReports();
            for (Upt01RoleReport roleReport : roleReportList) {
                ReportVo vo = new ReportVo();
                Upt01Report report = roleReport.getUpt01Report();
                vo.setReportId(report.getReportId());
                vo.setReportName(report.getReportName());
                reportVoList.add(vo);
            }
            reportRoleVo.setReportVoList(reportVoList);
        }
        return reportRoleVos;
    }

    /**
     * 根据角色id查询角色
     *
     * @param reportRoleId 角色id
     * @return
     * @throws ApplicationException
     */
    @Override
    @Transactional(value = "projectReportTransactionManager")
    public ReportRoleVo queryReportRoleById(long reportRoleId) throws ApplicationException {
        try {
            Upt01ReportRole entity = reportRoleDAO.findOne(reportRoleId);
            ReportRoleVo reportRoleVo = convertUpt01ReportRoleToVo(entity);
            return reportRoleVo;
        } catch (Exception e) {
            throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
        }
    }

    /**
     * 新建角色
     *
     * @return
     * @author lituengfei【刘腾飞】
     */
    @Override
    @Transactional(value = "projectReportTransactionManager")
    public boolean saveRole(ReportRoleVo vo) throws ApplicationException {
        try {
            vo.setCreateTime(new Date(System.currentTimeMillis()));
            vo.setCreateUserId(ManagerUserContextUtil.getOperatorId());
            vo.setModifyTime(new Date(System.currentTimeMillis()));
            vo.setModifyUserId(ManagerUserContextUtil.getOperatorId());
            vo.setStatus((short) RoleStatusEnum.Normal.getCode());
            Upt01ReportRole reportRole = convertVOToUpt01ReportRole(null, vo);
            List<Upt01RoleReport> roleReportList = new ArrayList<Upt01RoleReport>();
            String[] reportVoIdArr = vo.getReportVoIds().split(",");
            //该角色的所有报表
            StringBuffer reports = new StringBuffer();
            for (String reportVoId : reportVoIdArr) {
                Upt01RoleReport roleReport = new Upt01RoleReport();
                Upt01Report report = reportDAO.findOne(Long.valueOf(reportVoId));
                roleReport.setUpt01ReportRole(reportRole);
                roleReport.setUpt01Report(report);
                roleReportList.add(roleReport);
                reports.append(report.getReportName()+" ");
            }
            reportRole.setUpt01RoleReports(roleReportList);
            reportRoleDAO.save(reportRole);
            //插入日志
            operatorLogDAO.insertOperatorLog(ManagerUserContextUtil.getOperatorId(),ManagerUserContextUtil.getUserName()+"新建"+UserTypeEnum.getName(vo.getReportUserType())+"角色:"+vo.getRoleName()+",报表权限："+reports,OperatorLogTypeEnum.NewRole.getCode());
            return true;
        } catch (Exception e) {
            throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
        }
    }

    /**
     * 更新角色
     *
     * @return
     * @author lituengfei【刘腾飞】
     */
    @Override
    @Transactional(value = "projectReportTransactionManager", rollbackFor = RollbackException.class)
    public boolean modifyRole(ReportRoleVo vo) throws ApplicationException {
        try {
            Upt01ReportRole reportRole = reportRoleDAO.findOne(vo.getRoleId());
            List<Upt01RoleReport> roleReportList = reportRole.getUpt01RoleReports();
            roleReportDAO.deleteInBatch(roleReportList);
            List<Upt01RoleReport> newRoleReportList = new ArrayList<Upt01RoleReport>();
            String[] reportIdArr = vo.getReportVoIds().split(",");
            //添加修改角色日志
            StringBuffer reports = new StringBuffer();
            for (String reportId : reportIdArr) {
                Upt01RoleReport roleReport = new Upt01RoleReport();
                roleReport.setUpt01ReportRole(reportRole);
                roleReport.setUpt01Report(reportDAO.findOne(Long.valueOf(reportId)));
                newRoleReportList.add(roleReport);
                reports.append(reportDAO.findOne(Long.valueOf(reportId)).getReportName()+" ");
            }
            vo.setModifyTime(new Date(System.currentTimeMillis()));
            vo.setModifyUserId(ManagerUserContextUtil.getOperatorId());
            reportRole.setUpt01RoleReports(newRoleReportList);
            Upt01ReportRole entity = convertVOToUpt01ReportRole(reportRole, vo);
            reportRoleDAO.save(entity);
            //插入日志
            operatorLogDAO.insertOperatorLog(ManagerUserContextUtil.getOperatorId(),ManagerUserContextUtil.getUserName()+"修改原角色"+reportRole.getRoleName()+"为 (角色名："+vo.getRoleName()+" 用户类型："+UserTypeEnum.getName(vo.getReportUserType())+" 报表权限："+reports+")",OperatorLogTypeEnum.MotifyRole.getCode());
            return true;
        } catch (Exception e) {
            throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
        }
    }

    /**
     * 根据用户查询授权的报表
     * @param userId 用户Id
     * @return List<ReportRoleVo> 
     * @throws ApplicationException
     */
    @Transactional(value = "projectReportTransactionManager", readOnly=true)
    public List<ReportVo> findUserReportList(long userId) throws ApplicationException{
    	List<ReportVo> reportList=null;
    	Upt01MaitixUser user = maitixUserDao.findByUserId(userId);
    	
    	Upt01ReportRole reportRole=	user.getUpt01ReportRole();
    	if(reportRole!=null){
    		List<Upt01RoleReport> upt01RoleReportsList = reportRole.getUpt01RoleReports(); 
    		if(upt01RoleReportsList!=null){
    			reportList = new ArrayList<ReportVo>();
    			for(Upt01RoleReport roleReport:upt01RoleReportsList){
    				Upt01Report upt01Report = roleReport.getUpt01Report();
    				
    				ReportVo reportVo = new ReportVo();
    				reportVo.setReportId(upt01Report.getReportId());
    				reportVo.setReportName(upt01Report.getReportName());
    				reportVo.setReportUrl(upt01Report.getReportUrl());
    				
    				reportList.add(reportVo);
    			}
    			Collections.sort(reportList, new Comparator<ReportVo>(){
    				@Override
    			    public int compare(ReportVo o1, ReportVo o2) {
    			        if(o1.getReportId()>o2.getReportId()) return 1;
    			        return -1;
    			    }});
    		}
    	}
    	return reportList;
    }
    
    /**
     * 得到报表角色关系实体
     *
     * @param reportId
     * @param reportRole
     * @return
     * @author lituengfei【刘腾飞】
     */
    private Upt01RoleReport getRoleReport(Long reportId, Upt01ReportRole reportRole) {
        List<Upt01RoleReport> roleReportList = reportRole.getUpt01RoleReports();
        for (Upt01RoleReport entity : roleReportList) {
            Upt01Report upt01Report = entity.getUpt01Report();
            if (String.valueOf(reportId).equals(String.valueOf(upt01Report.getReportId()))) {
                return entity;
            }
        }
        return null;
    }


    /**
     * 将报表角色po转换vo
     *
     * @param po
     * @return vo
     * @author guwei
     */
    private ReportRoleVo convertUpt01ReportRoleToVo(Upt01ReportRole po) {
        ReportRoleVo vo = new ReportRoleVo();
        vo.setRoleId(po.getRoleId());
        vo.setRoleName(po.getRoleName());
        Short userType = po.getReportUserType();
        vo.setReportUserType(userType);
        vo.setReportUserTypeName(UserTypeEnum.getName(userType));
        vo.setStatus(po.getStatus());
        vo.setCreateTime(po.getCreateTime());
        vo.setCreateUserId(po.getCreateUserId());
        vo.setModifyTime(po.getModifyTime());
        vo.setModifyUserId(po.getModifyUserId());
        String reportVoIds = null;
        List<Upt01RoleReport> upt01RoleReportList = po.getUpt01RoleReports();
        for (Upt01RoleReport entity : upt01RoleReportList) {
            String reportId = String.valueOf(entity.getUpt01Report().getReportId());
            reportVoIds = reportVoIds == null ? reportId : reportVoIds + "," + reportId;
        }
        vo.setReportVoIds(reportVoIds);
        return vo;
    }

    /**
     * 将报表角色vo转换成po
     *
     * @param po
     * @param vo
     * @return
     * @author liutenfei【刘腾飞】
     */
    private Upt01ReportRole convertVOToUpt01ReportRole(Upt01ReportRole po, ReportRoleVo vo) {
        if (po == null) {
            po = new Upt01ReportRole();
            po.setStatus(vo.getStatus());
            po.setCreateTime(vo.getCreateTime());
            po.setCreateUserId(vo.getCreateUserId());
        }
        po.setRoleName(vo.getRoleName());
        po.setReportUserType(vo.getReportUserType());
        po.setModifyTime(vo.getModifyTime());
        po.setModifyUserId(vo.getModifyUserId());
        return po;
    }
}
