package cn.damai.boss.projectreport.report.action.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.stereotype.Controller;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.model.ReturnData;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 分页基类 .
 */
@ParentPackage("basePackage")
@Namespace("/")
@Controller
public abstract class BaseAction extends ActionSupport {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1;

	/**
	 * 返回文件流
	 */
	public final static String RESULT_STREAM = "stream";
	/**
	 * excel文件扩展名
	 */
	public final static String EXT_XLS = ".xls";
	/**
	 * pdf文件扩展名
	 */
	public final static String EXT_PDF = ".pdf";

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
	 * 下载文件参数 contentType
	 */
	private String contentType = "";
	/**
	 * 下载文件参数 contentDisposition
	 */
	private String contentDisposition = "";
	/**
	 * 下载文件参数 inputStream
	 */
	private ByteArrayInputStream inputStream;
	/**
	 * 警告信息
	 */
	private static String warnMessage;

	/**
	 * @return 列表数据
	 */
	@JSON
	public PageResultData getPageData() {
		return pageData;
	}

	/**
	 * 设置分页数据.
	 * 
	 * @param pageData
	 *            分页数据
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
				return list.subList((getPage() - 1) * getRows(), getPage()
						* getRows());
			} else {
				return list.subList((getPage() - 1) * getRows(), list.size());
			}
		}
		return null;

	}

	public void addDownloadParameters(ByteArrayOutputStream outStream,
			String fileTitle, String ext) {
		inputStream = new ByteArrayInputStream(outStream.toByteArray());
		String fileName = new StringBuilder(fileTitle)
				.append(DateFormatUtils.format(new Date(),
						"yyyy-MM-dd-HH-mm-ss-S")).append(ext).toString();
		try {
			contentDisposition = "attachment; filename="
					+ new String(fileName.getBytes("GB2312"), "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (ext.equals(EXT_XLS)) {
			contentType = "application/vnd.ms-excel";
		} else {
			contentType = "application/pdf";
		}
	}

	public void checkRequest() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		String oldssid = (String) session.getAttribute("oldssid");
		if (oldssid != null) {
			String ssidRequest = request.getParameter("ssid");
			if (!oldssid.equals(ssidRequest)) {
				try {
					response.sendRedirect(request.getContextPath()
							+ "/requestError.do");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return easyUI前台传过来的请求页数.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page
	 *            easyUI前台传过来的请求页数.
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
	 * @param rows
	 *            easyUI前台传过来的请求记录数
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
	 * @param sort
	 *            easyUI前台传过来的排序字段
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
	 * @param order
	 *            easyUI前台传过来的排序方式(desc?asc)
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

	public String getContentType() {
		return contentType;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

	public static String getWarnMessage() {
		return warnMessage;
	}

	public static void setWarnMessage(String warnMessage) {
		BaseAction.warnMessage = warnMessage;
	}

}