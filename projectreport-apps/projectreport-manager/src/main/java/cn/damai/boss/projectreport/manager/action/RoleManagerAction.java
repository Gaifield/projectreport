package cn.damai.boss.projectreport.manager.action;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.model.ReturnData;
import cn.damai.boss.projectreport.manager.action.base.BaseAction;
import cn.damai.boss.projectreport.manager.service.RoleManagerService;
import cn.damai.boss.projectreport.manager.vo.ReportRoleVo;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import javax.annotation.Resource;
import java.util.List;

/**
 * 注释：角色管理action
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-21 下午1:40
 */
@ParentPackage("json-default")
@Namespace("/")
public class RoleManagerAction extends BaseAction {
    //角色管理service
    @Resource
    private RoleManagerService roleManagerService;

    //报表角色VO
    public ReportRoleVo reportRoleVo;

    /**
     * 得到角色信息
     *
     * @return
     */
    @Action(value = "queryAllReportRoles", results = {@Result(type = "json", params = {"root", "pageData", "contentType", "text/html"})})
    public String queryAllReportRoles() {
        PageResultData<ReportRoleVo> pageData = new PageResultData<ReportRoleVo>();
        try {
            List<ReportRoleVo> reportRoleVoList = roleManagerService.queryAllReportRoles();
            pageData.setRows(reportRoleVoList);
            setPageData(pageData);
        } catch (ApplicationException ae) {
        }
        return SUCCESS;
    }

    /**
     * 跳转到“新建/编辑角色”界面
     *
     * @return
     */
    @Action(value = "skipToRoleEdit", results = {@Result(name = "success", location = "/projectreport/roleedit.jsp")})
    public String skipToRoleEdit() {
        try {
            if (reportRoleVo != null) {
                Long roleId = reportRoleVo.getRoleId();
                reportRoleVo = roleManagerService.queryReportRoleById(roleId);
            } else {
                reportRoleVo = new ReportRoleVo();
                reportRoleVo.setRoleId(Long.valueOf(-1));
            }
        } catch (ApplicationException ae) {

        }
        return SUCCESS;
    }


    /**
     * 新建角色
     *
     * @return
     */
    @Action(value = "saveRole", results = {@Result(type = "json", params = {"root", "returnData", "contentType", "text/html"})})
    public String saveRole() {
        ReturnData<Boolean> returnData = new ReturnData<Boolean>();
        try {
            if (roleManagerService.saveRole(reportRoleVo)) {
                returnData.setStatus(HttpStatusEnum.Success.getCode());
                returnData.setData(true);
            }
        } catch (ApplicationException ae) {
            returnData.setStatus(ae.getErrorCode());
            returnData.setData(false);
            returnData.setDescription(ae.getMessage());
        } finally {
            setReturnData(returnData);
        }
        return SUCCESS;
    }

    /**
     * 编辑角色
     *
     * @return
     */
    @Action(value = "modifyRole", results = {@Result(type = "json", params = {"root", "roleList", "contentType", "text/html"})})
    public String modifyRole() {
        ReturnData<Boolean> returnData = new ReturnData<Boolean>();
        try {
            if (roleManagerService.modifyRole(reportRoleVo)) {
                returnData.setStatus(HttpStatusEnum.Success.getCode());
                returnData.setData(true);
            }
        } catch (ApplicationException ae) {
            returnData.setStatus(ae.getErrorCode());
            returnData.setData(false);
            returnData.setDescription(ae.getMessage());
        } finally {
            setReturnData(returnData);
        }
        return SUCCESS;
    }

    public ReportRoleVo getReportRoleVo() {
        return reportRoleVo;
    }

    public void setReportRoleVo(ReportRoleVo reportRoleVo) {
        this.reportRoleVo = reportRoleVo;
    }
}
