package org.datax.console.common.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 自定义异常
 * Created by ChengJie on 2018/12/7 12:24
 */
public class GlobalegrowExpcetion extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;


    public GlobalegrowExpcetion(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public GlobalegrowExpcetion(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public GlobalegrowExpcetion(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public GlobalegrowExpcetion(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Throwable ex) {
		if (ex instanceof RuntimeException) {
			return (RuntimeException) ex;
		} else {
			return new RuntimeException(ex);
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Throwable ex) {
		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 获取组合本异常信息与底层异常信息的异常描述, 适用于本异常为统一包装异常类，底层异常才是根本原因的情况。
	 */
	public static String getErrorMessageWithNestedException(Throwable ex) {
		Throwable nestedException = ex.getCause();
		return new StringBuilder().append(ex.getMessage()).append(" nested exception is ")
				.append(nestedException.getClass().getName()).append(":").append(nestedException.getMessage())
				.toString();
	}

	/**
	 * 获取异常的Root Cause.
	 */
	public static Throwable getRootCause(Throwable ex) {
		Throwable cause;
		while ((cause = ex.getCause()) != null) {
			ex = cause;
		}
		return ex;
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex;
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}


	/**
	 * 功能描述:
	 * 业务异常不需要异常栈信息
	 *
	 * @return this
	 * @auther ClownfishYang
	 * created on 2019-08-07 09:52:36
	 */
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
