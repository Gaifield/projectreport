package cn.damai.boss.projectreport.common.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页相关类
 */
public class PageResultData<T> {
    private List<T> rows;
    private long total;

    private Object dataObj;

    public Object getDataObj() {
        return dataObj;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
    }

    public PageResultData() {
        this.rows = new ArrayList<T>(0);
    }

    public PageResultData(List<T> rows) {
        this.rows = rows;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 检查页码范围
     *
     * @param total    总记录数
     * @param pageSize 每页大小
     * @param page     页码
     * @return
     */
    public static int calculatePageIndex(long total, int pageSize, int page) {
        int pageIndex = page > 0 ? page : 1;

        int pageCount = 1;
        if (pageSize > 0) {
            pageCount = (int) total / pageSize;
        }
        if (total % pageSize > 0) {
            pageCount++;
        }
        if (pageIndex > pageCount) {
            pageIndex = pageCount;
        }
        return pageIndex;
    }

    /**
     * 计算页数
     *
     * @param total
     * @param pageSize
     * @return 返回页数
     */
    public static int getPageSize(long total, int pageSize) {
        int pageCount = 1;
        if (pageSize > 0) {
            pageCount = (int) total / pageSize;
        }
        if (total % pageSize > 0) {
            pageCount++;
        }

        return pageCount;
    }

    /**
     * 计算分页起始行号（从0行开始）
     *
     * @param pageIndex 页码
     * @param pageSize  每页大小
     * @return 起始行号
     */
    public static int calculateStartRowindex(int pageIndex, int pageSize) {
        pageIndex = pageIndex <= 0 ? 1 : pageIndex;
        return (pageIndex - 1) * pageSize;
    }
}