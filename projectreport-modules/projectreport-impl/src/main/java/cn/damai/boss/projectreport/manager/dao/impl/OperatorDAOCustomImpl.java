package cn.damai.boss.projectreport.manager.dao.impl;

import cn.damai.boss.projectreport.domain.Upt01Operator;
import cn.damai.boss.projectreport.common.dao.BaseJpaDaoSupport;
import cn.damai.boss.projectreport.manager.dao.OperatorDAOCustom;
import cn.damai.boss.projectreport.manager.enums.UserStatusEnum;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 注释：操作员DAO扩展实现类 作者：liutengfei 【刘腾飞】 时间：14-2-20 下午2:24
 */
@Repository
public class OperatorDAOCustomImpl extends BaseJpaDaoSupport<Upt01Operator, Long> implements OperatorDAOCustom {

    /**
     * 查看是否已存在非删除状态下的操作员
     */
    @Override
    public List<Upt01Operator> queryOperatorByUserId(Long userId) {
        StringBuffer sql = new StringBuffer();
        List<Object> paramList = new ArrayList<Object>();
        sql.append("from Upt01Operator obj where 1=1 ");
        if (userId != null) {
            sql.append(" and obj.userId = ? ");
            paramList.add(userId);
        }
        sql.append("and obj.status < ?");
        paramList.add(Short.valueOf(UserStatusEnum.Delete.getCode() + ""));
        return super.executeQuery(sql.toString(), paramList);
    }

    /**
     * 1、查询所有非删除状态下的操作员 2、且查询结果中不包括执行操作的管理员本身 3、按创建时间排序，新创建的操作员在上面
     *
     * @param operatorId
     * @return
     */
    @Override
    public List<Upt01Operator> queryAllOperator(Long operatorId,Short status,Short level) {
    	List<Object> paramList = new ArrayList<Object>();
    	StringBuffer sql;
    	if(status!=0){
    		sql = new StringBuffer("from Upt01Operator obj WHERE obj.status = ?");
    		paramList.add(status);
    	}else{
    		sql = new StringBuffer("from Upt01Operator obj WHERE obj.status <> ?");
    		paramList.add(Short.valueOf(UserStatusEnum.Delete.getCode() + ""));
    	}
    	if(level!=0){
    		sql.append(" AND obj.permissionLevel = ?");
    		paramList.add(level);
    	}
        sql.append(" AND obj.operatorId <> ? ");
        paramList.add(operatorId);
        sql.append(" order by obj.createTime desc");
        return super.executeQuery(sql.toString(), paramList);
    }
    
    /**
	 * 1、查询符合非删除状态下的操作员
	 * 2、且查询结果中不包括执行操作的管理员本身
	 * 3、按创建时间排序，新创建的操作员在上面
	 * 4、符合条件的操作员
	 * @param userIds
	 * @param status
	 * @param level
	 * @return
	 */
	@Override
	public List<Upt01Operator> queryOperatorByStatusAndLevel(
			List<Long> userIds, Short status, Short level) {
		StringBuffer sql = new StringBuffer("from Upt01Operator obj WHERE 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(status==0){
			sql.append(" AND obj.status <> ?");
			paramList.add(Short.valueOf(UserStatusEnum.Delete.getCode() + ""));
		}else{
			sql.append(" AND obj.status = ?");
			paramList.add(status);
		}
		if(level!=0){
			sql.append(" AND obj.permissionLevel = ?");
			paramList.add(level);
		}
		if(userIds!=null&&userIds.size()!=0){
			sql.append(" AND obj.userId in (");
			for(int i=0;i<userIds.size()-1;i++){
				sql.append(" ?,");
			}
			sql.append(" ?)");
			for(int i=0;i<userIds.size();i++){
				paramList.add(userIds.get(i));
			}
		}
		sql.append(" order by obj.createTime desc");
		return super.executeQuery(sql.toString(), paramList);
	}


}
