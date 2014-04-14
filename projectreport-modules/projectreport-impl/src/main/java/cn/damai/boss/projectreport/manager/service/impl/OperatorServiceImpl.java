package cn.damai.boss.projectreport.manager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.damai.boss.projectreport.manager.context.ManagerUserContextUtil;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.domain.Upt01Operator;
import cn.damai.boss.projectreport.domain.Upt01OperatorLog;
import cn.damai.boss.projectreport.manager.dao.OperatorDAO;
import cn.damai.boss.projectreport.manager.dao.OperatorLogDAO;
import cn.damai.boss.projectreport.manager.enums.OperatorLogTypeEnum;
import cn.damai.boss.projectreport.manager.enums.OperatorStatusEnum;
import cn.damai.boss.projectreport.manager.enums.PermissionLevelEnum;
import cn.damai.boss.projectreport.manager.service.OperatorService;
import cn.damai.boss.projectreport.manager.vo.OAUserVo;
import cn.damai.boss.projectreport.manager.vo.OperatorVo;
import cn.damai.component.common.ReturnData;
import cn.damai.component.organization.facade.OrganizationFacade;
import cn.damai.component.organization.facade.OrganizationPersonFacade;
import cn.damai.component.organization.vo.Organization;
import cn.damai.component.user.facade.UserFacade;
import cn.damai.component.user.vo.User;

/**
 * 注释：新建操作员service实现类
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-20 下午2:55
 */
@Service
public class OperatorServiceImpl implements OperatorService {
	
	
	@Resource
	private OperatorDAO operatorDAO;
	
	@Resource
	private OperatorLogDAO operatorLogDAO;
	
	/**
     * 组织机构接口
     */
    @Resource
    private UserFacade userFacade;

    /**
     * 组织机构接口
     */
    @Resource
    private OrganizationPersonFacade organizationPersonFacade;
    
    @Resource
    private OrganizationFacade organizationFacade;
    

    /**
     * 模糊查询匹配用户
     */
	@Override
	public List<OAUserVo> findUserByUserName(String userName) throws ApplicationException {
		ReturnData<List<User>> returnData = userFacade.findByUserNameLike(userName);
		List<OAUserVo> oaUserVo = new ArrayList<OAUserVo>();
		if (returnData != null && returnData.getResultData()!=null && returnData.getResultData().size()!=0)
        {
			for(int i=0;i<returnData.getResultData().size();i++){
				OAUserVo userVo = new OAUserVo();
				userVo.setUserId(returnData.getResultData().get(i).getUserId());
				userVo.setUserName(returnData.getResultData().get(i).getUserName());
				userVo.setUserDept(findOrganizationByUserId(returnData.getResultData().get(i).getUserId()));
				oaUserVo.add(userVo);
			}
			return oaUserVo;
        }
		return null;
	}

	/**
	 * 通过OA用户id查询组织机构
	 */
	@Override
	public String findOrganizationByUserId(Long userId) throws ApplicationException {
		ReturnData<List<Organization>> returnData = organizationPersonFacade.findOrganizationsByPersonId(userId, null, null);
		if(returnData!=null&&returnData.getResultData()!=null&&returnData.getResultData().size()!=0){
			return returnData.getResultData().get(0).getOrganizationName();
		}
		return null;
	}
	
	/**
	 * 检查是否可以禁用，删除操作员
	 * @param operatorId
	 * @param status
	 * @return
	 * @throws ApplicationException
	 */
	public boolean findCanChangeStatus(Long operatorId,short status,short level) throws ApplicationException{
		List<Upt01Operator> operators = operatorDAO.findByPermissionLevelAndStatus((short)PermissionLevelEnum.Admin.getCode(), (short)OperatorStatusEnum.Enable.getCode());
		
		if(operators!=null&&operators.size()==1){
			boolean iseq = operatorId.equals(operators.get(0).getOperatorId());	
			//判断状态			
			if(status>0 &&status!=OperatorStatusEnum.Enable.getCode() && iseq)
			{
				throw new ApplicationException(100,"禁止操作！系统至少保留一个启用管理员！");
			}			
			//判断级别调整
			if(level>0 && level!=PermissionLevelEnum.Admin.getCode() && iseq){
				throw new ApplicationException(100,"禁止操作！系统至少保留一个启用管理员！");
			}
		}
		return true;
	}
	
	/**
	 * 新建操作员
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public void saveOperator(String userName,Short level,Short status,Long createUserId) throws ApplicationException{
		//1、验证OA用户
		ReturnData<User> returnData = userFacade.findByUserName(userName);
		if(returnData==null || returnData.getResultData()==null){
			throw new ApplicationException(100,"用户不存在！");
		}
		
		//2、验证操作员是否已经存在
        //3、若状态为删除，更新状态；若不存在该用户，则执行插入操作
		Long userId = returnData.getResultData().getUserId();
		Upt01Operator operator = operatorDAO.findByUserId(userId);
		if(operator!=null&&operator.getStatus()!=3){
			throw new ApplicationException(101,"用户系统已存在！");
		}else if(operator!=null&&operator.getStatus()==3){
			operator.setStatus(status);
			operator.setCreateUserId(createUserId);
			operator.setCreateTime(new Date());
			operator.setModifyUserId(createUserId);
			operator.setModifyTime(new Date());
			operatorDAO.save(operator);
		}else{
			 operator = new Upt01Operator();
			 operator.setUserId(userId);
			 operator.setPermissionLevel(level);
			 operator.setStatus(status);
			 operator.setCreateUserId(createUserId);
			 operator.setCreateTime(new Date());
			 operator.setModifyUserId(createUserId);
			 operator.setModifyTime(new Date());
			 operatorDAO.save(operator);
		}
		operatorLogDAO.insertOperatorLog(ManagerUserContextUtil.getOperatorId(),ManagerUserContextUtil.getUserName()+"新建"+userName,OperatorLogTypeEnum.NewOperator.getCode());
	}
	
	/**
	 * 更改操作员权限
	 * @throws ApplicationException
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public void modifyOperatorLevel(Long operatorId,Short level) throws ApplicationException {
		Upt01Operator operator = operatorDAO.findByOperatorId(operatorId);
		if(operator!=null){
		if(operator.getPermissionLevel()==PermissionLevelEnum.Operator.getCode()){
		operator.setPermissionLevel(level);
		operator.setModifyUserId(ManagerUserContextUtil.getUserId());
		operator.setModifyTime(new Date());
		operatorDAO.save(operator);
		}else{
			findCanChangeStatus(operatorId,(short)0,level);
			//2、更改权限操作
			operator.setPermissionLevel(level);
			operator.setModifyUserId(ManagerUserContextUtil.getUserId());
			operator.setModifyTime(new Date());
			operatorDAO.save(operator);
		}
		}
		if(level==1){
			//添加日志
			operatorLogDAO.insertOperatorLog(ManagerUserContextUtil.getOperatorId(),ManagerUserContextUtil.getUserName()+"修改"+getOperatorName(operatorDAO.findByOperatorId(operatorId).getUserId())+"操作员权限为普通操作员",OperatorLogTypeEnum.MotifyOperatorLevel.getCode());
		}else{
			//添加日志
			operatorLogDAO.insertOperatorLog(ManagerUserContextUtil.getOperatorId(),ManagerUserContextUtil.getUserName()+"修改"+getOperatorName(operatorDAO.findByOperatorId(operatorId).getUserId())+"操作员权限为管理员",OperatorLogTypeEnum.MotifyOperatorLevel.getCode());
		}
		
	}
	
	
	/**
	 * 更改操作员状态
	 * @throws ApplicationException
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public void modifyOperatorStatus(Long operatorId,Short status) throws ApplicationException {
		findCanChangeStatus(operatorId,status,(short)0);
		//2、更改状态操作
		Upt01Operator operator = operatorDAO.findByOperatorId(operatorId);
		operator.setStatus(status);
		operator.setModifyUserId(ManagerUserContextUtil.getUserId());
		operator.setModifyTime(new Date());
		 operatorDAO.save(operator);
		if(status==1){
			operatorLogDAO.insertOperatorLog(ManagerUserContextUtil.getOperatorId(),ManagerUserContextUtil.getUserName()+"启用"+getOperatorName(operatorDAO.findByOperatorId(operatorId).getUserId()),OperatorLogTypeEnum.MotifyOperatorStatus.getCode());
		}else if(status==2){
			operatorLogDAO.insertOperatorLog(ManagerUserContextUtil.getOperatorId(),ManagerUserContextUtil.getUserName()+"禁用"+getOperatorName(operatorDAO.findByOperatorId(operatorId).getUserId()),OperatorLogTypeEnum.MotifyOperatorStatus.getCode());
		}else{
			operatorLogDAO.insertOperatorLog(ManagerUserContextUtil.getOperatorId(),ManagerUserContextUtil.getUserName()+"删除"+getOperatorName(operatorDAO.findByOperatorId(operatorId).getUserId()),OperatorLogTypeEnum.DeleteOperator.getCode());
		}
				
		

	}
	
	/**
	 * 通过OA用户ID查询组织机构,分公司级别（40）
	 * @param personId
	 * @return
	 * @throws ApplicationException
	 */
	public String getPersonCompany(Long personId) throws ApplicationException{
		ReturnData<List<Organization>> returnData = organizationPersonFacade.findOrganizationsByPersonId(personId, null, null);
		if(returnData!=null&&returnData.getResultData()!=null&&returnData.getResultData().size()!=0){
			Integer organizationLevel = returnData.getResultData().get(0).getOrganizationLevel();
			if(organizationLevel==40){
				return returnData.getResultData().get(0).getOrganizationName();
			}else{
				String[] splitOrganizationPath = returnData.getResultData().get(0).getOrganizationPath().split("\\|");
				for(int j=0;j<splitOrganizationPath.length;j++){
					ReturnData<Organization> organizationData = organizationFacade.findOrganizationById(Long.valueOf(splitOrganizationPath[j]));
					if(organizationData!=null&&organizationData.getResultData()!=null){
						Integer organizationLevelOther = organizationData.getResultData().getOrganizationLevel();
						if(organizationLevelOther == 40){
						return organizationData.getResultData().getOrganizationName();
						}
					}
				}
			}
		}
		return null;
	}
	/**
	 * 通过OA用户ID查询用户名称
	 * @param userId
	 * @return
	 * @throws ApplicationException
	 */
	public String getOperatorName(Long userId) throws ApplicationException{
		ReturnData<User> returnDatas = userFacade.findUserByUserId(userId);
		if(returnDatas!=null&&returnDatas.getResultData()!=null){
			return returnDatas.getResultData().getUserName();
		}
		return null;
	}
	
	
	/**
	 * 1、查询所有非删除状态下的操作员
	 * 2、且查询结果中不包括执行操作的管理员本身
	 * 3、按创建时间排序，新创建的操作员在上面
	 * @throws ApplicationException
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public List<OperatorVo> findAllOperator(String operatorName,Short status,Short level) throws ApplicationException {
		Long currentOperatorId = ManagerUserContextUtil.getOperatorId();
		List<Upt01Operator> operators = null;
		List<Long> operIds = new ArrayList<Long>();
		if(operatorName!=null&&!"".equals(operatorName)){
			ReturnData<List<User>> returnData = userFacade.findByUserNameLike(operatorName);
			if(returnData!=null && returnData.getResultData()!=null && returnData.getResultData().size()!=0){
				List<User> userList = returnData.getResultData();
				for(int i=0;i<userList.size();i++){
					if(userList.get(i).getUserId()==currentOperatorId){
						continue;
					}else{
						operIds.add(userList.get(i).getUserId());	
					}
				}
				operators = operatorDAO.queryOperatorByStatusAndLevel(operIds, status, level);
				if(operators==null || operators.size()==0){
					return null;
				}
			}else{
				return null;
			}
			
		}else{
			operators = operatorDAO.queryAllOperator(ManagerUserContextUtil.getOperatorId(),status,level);
		}
		return Upt01OperatorToOperatorVo(operators);
	}
	
	/**
	 * 操作员列表 PO转VO
	 * @param operators
	 * @return
	 * @throws ApplicationException
	 */
	public List<OperatorVo> Upt01OperatorToOperatorVo(List<Upt01Operator> operators) throws ApplicationException{
		List<OperatorVo> operatorVos = new ArrayList<OperatorVo>();
		if(operators!=null&&operators.size()!=0){
			for(int i=0;i<operators.size();i++){
				OperatorVo operatorVo = new OperatorVo();
				operatorVo.setOperatorId(operators.get(i).getOperatorId());
				operatorVo.setUserId(operators.get(i).getUserId());
				operatorVo.setPermissionLevel(operators.get(i).getPermissionLevel());
				operatorVo.setPermissionLevelName(PermissionLevelEnum.getName(operators.get(i).getPermissionLevel()));
				operatorVo.setStatus(operators.get(i).getStatus());
				operatorVo.setStatusName(OperatorStatusEnum.getName(operators.get(i).getStatus()));
				operatorVo.setCreateTime(operators.get(i).getCreateTime());
				operatorVo.setCreateTimeFormat(DateFormatUtils.format(operators.get(i).getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				try {
					operatorVo.setOperatorDept(getPersonCompany(operators.get(i).getUserId()));
					operatorVo.setOperatorName(getOperatorName(operators.get(i).getUserId()));
				} catch (ApplicationException e) {
					e.printStackTrace();
				}
				if(operators.get(i).getStatus()==OperatorStatusEnum.Enable.getCode()){
					operatorVo.setReverseStatus((short)OperatorStatusEnum.Disable.getCode());
					operatorVo.setReverseStatusName(OperatorStatusEnum.Disable.getName());
				}else{
					operatorVo.setReverseStatus((short)OperatorStatusEnum.Enable.getCode());
					operatorVo.setReverseStatusName(OperatorStatusEnum.Enable.getName());
				}
				operatorVos.add(operatorVo);
			}   
		}
		return operatorVos;
	}

	
	
	
}
