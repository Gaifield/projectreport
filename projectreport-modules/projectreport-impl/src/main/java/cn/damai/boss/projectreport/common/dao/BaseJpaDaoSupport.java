package cn.damai.boss.projectreport.common.dao;


import cn.damai.crius.core.utils.Pager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * jpa dao支持的基础类
 *
 * @param <T>
 * @param <ID>
 * @author hanya
 */
public class BaseJpaDaoSupport<T extends Serializable, ID extends Serializable> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Pager executePageableQuery(StringBuffer mainJpql, List<Object> paramList, int page, int pageSize) {
        return executePageableQuery(mainJpql.toString(), paramList, page, pageSize);
    }

    /**
     * 执行分页查询的基础方法
     *
     * @param mainJpql  查询条件,所查询的对象别名必须为<strong>obj</strong>
     * @param paramList 查询所需参数
     * @param page      当前页
     * @param pageSize  当前页码
     * @return
     */
    protected Pager executePageableQuery(String mainJpql, List<Object> paramList, int page, int pageSize) {

        Query totalQuery = this.entityManager.createQuery(new StringBuffer("select count(*) ").append(mainJpql).toString());
        Query dataQuery = this.entityManager.createQuery(new StringBuffer("select obj ").append(mainJpql).toString());
        dataQuery.setFirstResult((page - 1) * pageSize);
        dataQuery.setMaxResults(pageSize);
        for (int i = 0; i < paramList.size(); i++) {
            totalQuery.setParameter(i + 1, paramList.get(i));
            dataQuery.setParameter(i + 1, paramList.get(i));
        }
        Long total = (Long) totalQuery.getSingleResult();
        List<T> dataList = dataQuery.getResultList();
        Pager departmentPageVO = new Pager(page, pageSize);
        departmentPageVO.setData(dataList);
        departmentPageVO.setTotalResults(total.intValue());
        return departmentPageVO;
    }

    protected Pager executePageableQuery(String mainJpql, HashMap<String, Object> paramMap, int page, int pageSize) {

        Query totalQuery = this.entityManager.createQuery(new StringBuffer("select count(*) ").append(mainJpql).toString());
        Query dataQuery = this.entityManager.createQuery(new StringBuffer("select obj ").append(mainJpql).toString());
        dataQuery.setFirstResult((page - 1) * pageSize);
        dataQuery.setMaxResults(pageSize);
        Iterator iter = paramMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = String.valueOf(entry.getKey());
            Object val = entry.getValue();
            totalQuery.setParameter(key, val);
            dataQuery.setParameter(key, val);
        }

        Long total = (Long) totalQuery.getSingleResult();
        List<T> dataList = dataQuery.getResultList();
        Pager departmentPageVO = new Pager(page, pageSize);
        departmentPageVO.setData(dataList);
        departmentPageVO.setTotalResults(total.intValue());
        return departmentPageVO;
    }

    /**
     * 查询数据库数据
     *
     * @param mainJpql
     * @param paramList
     * @return
     */
    protected List<T> executeQuery(String mainJpql, List<Object> paramList) {

        Query totalQuery = this.entityManager.createQuery(new StringBuffer("select count(*) ").append(mainJpql).toString());
        Query dataQuery = this.entityManager.createQuery(new StringBuffer("select obj ").append(mainJpql).toString());
        for (int i = 0; i < paramList.size(); i++) {
            totalQuery.setParameter(i + 1, paramList.get(i));
            dataQuery.setParameter(i + 1, paramList.get(i));
        }
        Long total = (Long) totalQuery.getSingleResult();
        List<T> dataList = dataQuery.getResultList();
        return dataList;
    }
}
