package cn.damai.boss.projectreport.manager.action.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.model.ReturnData;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 分页基类 .
 */
public abstract class BaseAction extends ActionSupport {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1;
    /**
     * 初始化默认行数.
     */
    public static final short INITROWS = 10;
    /**
     * easyUI前台传过来的请求页数，故必须以此命名，当然你也可以不这样，但set方法必须是setPage.
     */
    private int page = 1;
    /**
     * easyUI前台传过来的请求记录数，故必须以此命名，原因同上.
     */
    private int rows = INITROWS;
    /**
     * easyUI前台传过来的排序字段，故必须以此命名，原因同上.
     */
    private String sort;
    /**
     * easyUI前台传过来的排序方式(desc?asc)，故必须以此命名，原因同上.
     */
    private String order;
    /**
     * 列表数据.
     */
    private PageResultData pageData;

    private ReturnData returnData;


    /**
     * @return 列表数据
     */
    @JSON
    public PageResultData getPageData() {
    	if(pageData==null){
    		pageData = new PageResultData();
    	}
    	if(pageData.getRows()==null){
    		pageData.setRows(new ArrayList());
    	}
    	return pageData;
    }

    /**
     * 设置分页数据.
     *
     * @param pageData 分页数据
     */
    public void setPageData(PageResultData pageData) {
    	this.pageData = pageData;
    }

    /**
     * 手动分页处理
     *
     * @param list
     * @return
     */
    public List getPagerList(List list) {
        if (list != null) {
            if (getPage() * getRows() < list.size()) {
                return list.subList((getPage() - 1) * getRows(), getPage() * getRows());
            } else {
                return list.subList((getPage() - 1) * getRows(), list.size());
            }
        }
        return null;

    }

    /**
     * @return easyUI前台传过来的请求页数.
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page easyUI前台传过来的请求页数.
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return easyUI前台传过来的请求记录数
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows easyUI前台传过来的请求记录数
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return easyUI前台传过来的排序字段
     */
    public String getSort() {
        return sort;
    }

    /**
     * @param sort easyUI前台传过来的排序字段
     */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * @return easyUI前台传过来的排序方式(desc?asc)
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param order easyUI前台传过来的排序方式(desc?asc)
     */
    public void setOrder(String order) {
        this.order = order;
    }

    public ReturnData getReturnData() {
        return returnData;
    }

    public void setReturnData(ReturnData returnData) {
        this.returnData = returnData;
    }
}
