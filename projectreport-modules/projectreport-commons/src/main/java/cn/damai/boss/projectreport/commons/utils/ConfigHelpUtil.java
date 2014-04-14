package cn.damai.boss.projectreport.commons.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by 顾炜    guwei
 * on 14-2-24. 下午7:42
 */
public class ConfigHelpUtil {

    private static ResourceBundle resourceBundle = null;

    static {
        resourceBundle = ResourceBundle.getBundle("projectreport", new Locale("zh", "CN"));
    }

    /**
     * 读取中文国际化配置文件
     *
     * @param key 关键字
     * @return
     * @author guwei
     */
    public static String getText(String key) {

        return resourceBundle.getString(key);
    }

    public static int getIntValue(String key) {
        Object ob = getObject(key);
        int value = 0;
        if (ob != null) {
            value = Integer.parseInt(String.valueOf(ob));
        }
        return value;
    }

    private static Object getObject(String properties) {

        return resourceBundle.getObject(properties);
    }
}
