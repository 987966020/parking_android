package hbie.vip.parking.utils;


import hbie.vip.parking.aes.Aess;
import hbie.vip.parking.md5.Md5;

/**
 * 加密类
 *
 * @author Administrator
 *
 */
public class EncryotDecryptUtils {
    /**
     * 加密
     *
     * @param firstKey
     *            约定的KEY
     * @param mesterKey
     *            请求服务器获取的KEY
     * @param data
     *            要加密的数据
     * @param str2
     */
    public static String getEncrypt(String firstKey, String mesterKey, String data) {
        // String str = Aess.encrypt(data, firstKey);
        // String str1 = Aess.encrypt(str, mesterKey);
        return data;

    }

    /**
     * 加密
     *
     * @param firstKey
     * @param data
     * @return
     */
    public static String getEncrypt(String firstKey, String data) {
        String str = Aess.encrypt(data, firstKey);
        return str;

    }

    /**
     * 解密
     *
     * @param firstKey
     * @param mesterKey
     * @param data
     * @return
     */
    public static String getDecrypt(String data, String mesterKey, String firstKey) {
        // String phone = Aess.decrypt(data, mesterKey);
        // String phone_name = Aess.decrypt(phone, firstKey);
        // String phone_name=null;
        return data;
    }

    /**
     * 解密
     *
     * @param data
     * @param firstKey
     * @return
     */
    public static String getDecrypt(String data, String firstKey) {
        String phone = Aess.decrypt(data, firstKey);
        return phone;
    }

    /**
     * MD5加密
     *
     * @param str
     * @param canyin
     * @return
     */
    public static String getMD5(String str, String canyin) {
        if (str == Md5.getMd5(canyin)) {
            return str;
        } else {
            return "加密失败";
        }

    }
}
