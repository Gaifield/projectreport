package cn.damai.boss.projectreport.manager.dao.impl;

import java.util.Date;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import cn.damai.boss.projectreport.common.dao.BaseJpaDaoSupport;
import cn.damai.boss.projectreport.domain.Upt01Project;
import cn.damai.boss.projectreport.manager.dao.ProjectDaoCustom;

@Repository
public class ProjectDaoCustomImpl extends BaseJpaDaoSupport<Upt01Project, Long>
		implements ProjectDaoCustom {

	@Override
	public Date queryLatestProjectDeadline() {

		Query query = entityManager.createQuery("select Max(projectDeadline) from Upt01Project");
		Object data = query.getSingleResult();
		if (data != null) {
			return (Date) data;
		}
		return null;
	}
}