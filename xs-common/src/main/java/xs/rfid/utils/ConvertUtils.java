package xs.rfid.utils;


import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import xs.rfid.base.StringUtils;

/**
 * <pre>
 *     author: mikehhuang
 *     time  : 2016/8/13
 *     desc  : 转换相关工具类
 * </pre>
 */
public class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * hexString转byteArr
     * <p>例如：</p>
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String hexString) {
        if (xs.rfid.base.StringUtils.isSpace(hexString)) return null;
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * hexChar转int
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int hex2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

//    public static void main(String[] args) {
//        System.out.println(hex2Dec("11".));
//    }


    /**
     * charArr转byteArr
     *
     * @param chars 字符数组
     * @return 字节数组
     */
    public static byte[] chars2Bytes(char[] chars) {
        if (chars == null || chars.length <= 0) return null;
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * byteArr转charArr
     *
     * @param bytes 字节数组
     * @return 字符数组
     */
    public static char[] bytes2Chars(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (bytes[i] & 0xff);
        }
        return chars;
    }

    /**
     * 以unit为单位的内存大小转字节数
     *
     * @param memorySize 大小
     * @param unit 单位类型
     *             <ul>
     *             <li>{@link ConstUtils.MemoryUnit#BYTE}: 字节</li>
     *             <li>{@link ConstUtils.MemoryUnit#KB}  : 千字节</li>
     *             <li>{@link ConstUtils.MemoryUnit#MB}  : 兆</li>
     *             <li>{@link ConstUtils.MemoryUnit#GB}  : GB</li>
     *             </ul>
     * @return 字节数
     */
    public static long memorySize2Byte(long memorySize, ConstUtils.MemoryUnit unit) {
        if (memorySize < 0) return -1;
        switch (unit) {
            default:
            case BYTE:
                return memorySize;
            case KB:
                return memorySize * ConstUtils.KB;
            case MB:
                return memorySize * ConstUtils.MB;
            case GB:
                return memorySize * ConstUtils.GB;
        }
    }

    /**
     * 字节数转以unit为单位的内存大小
     *
     * @param byteNum 字节数
     * @param unit    单位类型
     *                <ul>
     *                <li>{@link ConstUtils.MemoryUnit#BYTE}: 字节</li>
     *                <li>{@link ConstUtils.MemoryUnit#KB}  : 千字节</li>
     *                <li>{@link ConstUtils.MemoryUnit#MB}  : 兆</li>
     *                <li>{@link ConstUtils.MemoryUnit#GB}  : GB</li>
     *                </ul>
     * @return 以unit为单位的size
     */
    public static double byte2MemorySize(long byteNum, ConstUtils.MemoryUnit unit) {
        if (byteNum < 0) return -1;
        switch (unit) {
            default:
            case BYTE:
                return (double) byteNum;
            case KB:
                return (double) byteNum / ConstUtils.KB;
            case MB:
                return (double) byteNum / ConstUtils.MB;
            case GB:
                return (double) byteNum / ConstUtils.GB;
        }
    }

    /**
     * 字节数转合适内存大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    public static String byte2FitMemorySize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < ConstUtils.KB) {
            return String.format("%.3fB", byteNum + 0.0005);
        } else if (byteNum < ConstUtils.MB) {
            return String.format("%.3fKB", byteNum / ConstUtils.KB + 0.0005);
        } else if (byteNum < ConstUtils.GB) {
            return String.format("%.3fMB", byteNum / ConstUtils.MB + 0.0005);
        } else {
            return String.format("%.3fGB", byteNum / ConstUtils.GB + 0.0005);
        }
    }

    /**
     * 以unit为单位的时间长度转毫秒时间戳
     *
     * @param timeSpan 毫秒时间戳
     * @param unit     单位类型
     *                 <ul>
     *                 <li>{@link ConstUtils.TimeUnit#MSEC}: 毫秒</li>
     *                 <li>{@link ConstUtils.TimeUnit#SEC }: 秒</li>
     *                 <li>{@link ConstUtils.TimeUnit#MIN }: 分</li>
     *                 <li>{@link ConstUtils.TimeUnit#HOUR}: 小时</li>
     *                 <li>{@link ConstUtils.TimeUnit#DAY }: 天</li>
     *                 </ul>
     * @return 毫秒时间戳
     */
    public static long timeSpan2Millis(long timeSpan, ConstUtils.TimeUnit unit) {
        switch (unit) {
            default:
            case MSEC:
                return timeSpan;
            case SEC:
                return timeSpan * ConstUtils.SEC;
            case MIN:
                return timeSpan * ConstUtils.MIN;
            case HOUR:
                return timeSpan * ConstUtils.HOUR;
            case DAY:
                return timeSpan * ConstUtils.DAY;
        }
    }

    /**
     * 毫秒时间戳转以unit为单位的时间长度
     *
     * @param millis 毫秒时间戳
     * @param unit   单位类型
     *               <ul>
     *               <li>{@link ConstUtils.TimeUnit#MSEC}: 毫秒</li>
     *               <li>{@link ConstUtils.TimeUnit#SEC }: 秒</li>
     *               <li>{@link ConstUtils.TimeUnit#MIN }: 分</li>
     *               <li>{@link ConstUtils.TimeUnit#HOUR}: 小时</li>
     *               <li>{@link ConstUtils.TimeUnit#DAY }: 天</li>
     *               </ul>
     * @return 以unit为单位的时间长度
     */
    public static long millis2TimeSpan(long millis, ConstUtils.TimeUnit unit) {
        switch (unit) {
            default:
            case MSEC:
                return millis;
            case SEC:
                return millis / ConstUtils.SEC;
            case MIN:
                return millis / ConstUtils.MIN;
            case HOUR:
                return millis / ConstUtils.HOUR;
            case DAY:
                return millis / ConstUtils.DAY;
        }
    }

    /**
     * 毫秒时间戳转合适时间长度
     *
     * @param millis    毫秒时间戳
     *                  <p>小于等于0，返回null</p>
     * @param precision 精度
     *                  <p>precision = 0，返回null</p>
     *                  <p>precision = 1，返回天</p>
     *                  <p>precision = 2，返回天和小时</p>
     *                  <p>precision = 3，返回天、小时和分钟</p>
     *                  <p>precision = 4，返回天、小时、分钟和秒</p>
     *                  <p>precision >= 5，返回天、小时、分钟、秒和毫秒</p>
     * @return 合适时间长度
     */
    public static String millis2FitTimeSpan(long millis, int precision) {
        if (millis <= 0 || precision <= 0) return null;
        StringBuilder sb = new StringBuilder();
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
        precision = Math.min(precision, 5);
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }

    /**
     * bytes转bits
     *
     * @param bytes 字节数组
     * @return bits
     */
    public static String bytes2Bits(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            for (int j = 7; j >= 0; --j) {
                sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    /**
     * bits转bytes
     *
     * @param bits 二进制
     * @return bytes
     */
    public static byte[] bits2Bytes(String bits) {
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        // 不是8的倍数前面补0
        if (lenMod != 0) {
            for (int i = lenMod; i < 8; i++) {
                bits = "0" + bits;
            }
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for (int i = 0; i < byteLen; ++i) {
            for (int j = 0; j < 8; ++j) {
                bytes[i] <<= 1;
                bytes[i] |= bits.charAt(i * 8 + j) - '0';
            }
        }
        return bytes;
    }

    /**
     * inputStream转outputStream
     *
     * @param is 输入流
     * @return outputStream子类
     */
    public static ByteArrayOutputStream input2OutputStream(InputStream is) {
        if (is == null) return null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] b = new byte[ConstUtils.KB];
            int len;
            while ((len = is.read(b, 0, ConstUtils.KB)) != -1) {
                os.write(b, 0, len);
            }
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(is);
        }
    }

    /**
     * outputStream转inputStream
     *
     * @param out 输出流
     * @return inputStream子类
     */
    public ByteArrayInputStream output2InputStream(OutputStream out) {
        if (out == null) return null;
        return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
    }

    /**
     * inputStream转byteArr
     *
     * @param is 输入流
     * @return 字节数组
     */
    public static byte[] inputStream2Bytes(InputStream is) {
        if (is == null) return null;
        return input2OutputStream(is).toByteArray();
    }

    /**
     * byteArr转inputStream
     *
     * @param bytes 字节数组
     * @return 输入流
     */
    public static InputStream bytes2InputStream(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) return null;
        return new ByteArrayInputStream(bytes);
    }

    /**
     * outputStream转byteArr
     *
     * @param out 输出流
     * @return 字节数组
     */
    public static byte[] outputStream2Bytes(OutputStream out) {
        if (out == null) return null;
        return ((ByteArrayOutputStream) out).toByteArray();
    }

    /**
     * outputStream转byteArr
     *
     * @param bytes 字节数组
     * @return 字节数组
     */
    public static OutputStream bytes2OutputStream(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) return null;
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            os.write(bytes);
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(os);
        }
    }

    /**
     * inputStream转string按编码
     *
     * @param is          输入流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String inputStream2String(InputStream is, String charsetName) {
        if (is == null || StringUtils.isSpace(charsetName)) return null;
        try {
            return new String(inputStream2Bytes(is), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转inputStream按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static InputStream string2InputStream(String string, String charsetName) {
        if (string == null || StringUtils.isSpace(charsetName)) return null;
        try {
            return new ByteArrayInputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * outputStream转string按编码
     *
     * @param out         输出流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String outputStream2String(OutputStream out, String charsetName) {
        if (out == null || StringUtils.isSpace(charsetName)) return null;
        try {
            return new String(outputStream2Bytes(out), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转outputStream按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static OutputStream string2OutputStream(String string, String charsetName) {
        if (string == null || StringUtils.isSpace(charsetName)) return null;
        try {
            return bytes2OutputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * int转hexString
     *
     */
    public static String intToHex(int s) {
        String hexString = Integer.toHexString(s);
        if(hexString.length()<2){
            hexString="0"+hexString;
        }
        return  hexString;
    }
    /**
     * int转hexString
     *
     */
    public static float floatTo2(float T) {
        BigDecimal b = new BigDecimal(T);
        return b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     *
     * @param hex
     * @return
     */
    public static float ieee754ToFloat(String hex) {
        if( StringUtils.isSpace(hex)){
            return 0;
        }
//        Float value=Float.intBitsToFloat(Integer.parseInt(hex, 16));
        Float value=Float.intBitsToFloat(new BigInteger(hex, 16).intValue());
        return  floatTo2(value.floatValue());
    }

//    public static void main(String[] args) {
//        float v = ieee754ToFloat("C1A355BA");
////       int v= Integer.parseInt("D3011F00",32);
//        System.out.println(v);
//    }

}
