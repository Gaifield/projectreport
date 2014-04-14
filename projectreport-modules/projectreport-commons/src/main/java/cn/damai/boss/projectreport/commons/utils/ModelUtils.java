package cn.damai.boss.projectreport.commons.utils;

import org.springframework.beans.BeanUtils;

/**
 * Po、Vo转换工具类
 * @author Administrator
 *
 */
public class ModelUtils {

	/**
	 * 将Domain Object转换为Value Object.
	 * 
	 * @param domainObject
	 */
	public static <T> T fromDomainObjectToVo(Class<T> valueClass, Object domain) {
		if (domain == null)
			return null;
		T retObject = null;
		try {
			retObject = valueClass.newInstance();
			BeanUtils.copyProperties(domain, retObject);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return retObject;
	}

	/**
	 * 转换为Domain Object.
	 * 
	 * @return DomainObject
	 */
	public static <T> T toDomainObject(Object valueObject, Class<T> domainClass) {
		if (valueObject == null)
			return null;
		T ret = null;
		try {
			ret = domainClass.newInstance();
			BeanUtils.copyProperties(valueObject, ret);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return ret;
	}
}
