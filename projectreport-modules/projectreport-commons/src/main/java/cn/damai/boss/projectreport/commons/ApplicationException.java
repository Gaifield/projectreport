package cn.damai.boss.projectreport.commons;

/**
 * service抛出此异常，action层进行捕获，然后转化为错误码传给客户端
 * @author Administrator
 *
 */
public class ApplicationException extends Exception{	
	private static final long serialVersionUID = -8371603883986887174L;
	
	private int errorCode;

	public ApplicationException(int errorCode,String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
}