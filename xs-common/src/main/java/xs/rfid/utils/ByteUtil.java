package xs.rfid.utils;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Random;

/**
 * 字节工具类
 * 
 * @author bao110908
 * @since 2009-02-19
 */
public class ByteUtil {
    private final static char[] HEX = "0123456789ABCDEF".toCharArray();
    
    public static void xor(byte[] base, byte[] data, int offset) {        
        for(int i = 0; i < base.length; i++) {
            base[i] ^= data[offset + i]; 
        }
    }

    /**
     * 16进制异或运算 高端
     * @param strHex_X
     * @param strHex_Y
     * @return
     */
    public static String xor(String strHex_X,String strHex_Y){
        //将x、y转成二进制形式
        String anotherBinary=Integer.toBinaryString(Integer.valueOf(strHex_X,16));
        String thisBinary=Integer.toBinaryString(Integer.valueOf(strHex_Y,16));
        String result = "";
        //判断是否为8位二进制，否则左补零
        if(anotherBinary.length() != 8){
            for (int i = anotherBinary.length(); i <8; i++) {
                anotherBinary = "0"+anotherBinary;
            }
        }
        if(thisBinary.length() != 8){
            for (int i = thisBinary.length(); i <8; i++) {
                thisBinary = "0"+thisBinary;
            }
        }
        //异或运算
        for(int i=0;i<anotherBinary.length();i++){
            //如果相同位置数相同，则补0，否则补1
            if(thisBinary.charAt(i)==anotherBinary.charAt(i))
                result+="0";
            else{
                result+="1";
            }
        }

        return Integer.toHexString(Integer.parseInt(result, 2));
    }

    /**
     *
     * @param data
     * @return
     */
    public static String makeChecksum(String data) {
        if (data == null || data.equals("")) {
            return "";
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            System.out.println(s);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 将字节数组转成 16 进制的字符串来表示，每个字节采用两个字符表表示，每个字节间采用
     * 一个空格分隔<br />
     * 采用实现较为高效的 bytes2Hex 方法
     * @param bys       需要转换成 16 进制的字节数组
     * @return
     * @deprecated
     */
    public static String bytes2Hex1(byte[] bys) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bys.length; i++) {
            if(i > 0) {
                sb.append(" ");
            }
            sb.append(HEX[bys[i] >> 4 & 0xf]);
            sb.append(HEX[bys[i] & 0xf]);
        }
        return sb.toString();
    }
    /**
     * 将字节数组转成 16 进制的字符串来表示，每个字节采用两个字符表表示，每个字节间采用
     * 一个空格分隔
     * @param bys       需要转换成 16 进制的字节数组
     * @return
     */
    public static String bytes2HexSpace(byte[] bys) {
        char[] chs = new char[bys.length * 2 + bys.length - 1];
        for(int i = 0, offset = 0; i < bys.length; i++) {
            if(i > 0) {
                chs[offset++] = ' ';
            }
            chs[offset++] = HEX[bys[i] >> 4 & 0xf];
            chs[offset++] = HEX[bys[i] & 0xf];
        }
        return new String(chs);
    }
    /**
     * 将字节数组转成 16 进制的字符串来表示，每个字节采用两个字符表表示
     *
     * @param bys       需要转换成 16 进制的字节数组
     * @return
     */
    public static String bytes2Hex(byte[] bys) {
        char[] chs = new char[bys.length * 2];
        for(int i = 0, offset = 0; i < bys.length; i++) {
            chs[offset++] = HEX[bys[i] >> 4 & 0xf];
            chs[offset++] = HEX[bys[i] & 0xf];
        }
        return new String(chs);
    }



    /**
     * 将字节数组转成 16 进制的字符串来表示，每个字节采用两个字符表表示
     *
     * @param bys       需要转换成 16 进制的字节数组
     * @return
     */
    public static String bytes2HexWithSpace(byte[] bys) {
        char[] chs = new char[bys.length * 3];
        for(int i = 0, offset = 0; i < bys.length; i++) {
            chs[offset++] = HEX[bys[i] >> 4 & 0xf];
            chs[offset++] = HEX[bys[i] & 0xf];
            chs[offset++] = ' ';
        }
        return new String(chs);
    }
	public static String getRandom(int min, int max,int flag)
	{
	Random random = new Random();
	int s = random.nextInt(max) % (max - min + 1) + min;
	
	return String.valueOf(new BigDecimal(s).movePointLeft(flag));
	}

    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }
    //byte转换为int


    /**
     * bytes 转int
     * @param b
     * @return
     */
    public static int byteToInt2(byte[] b) {

   	 int mask=0xff;
   	 int temp=0;
   	 int n=0;
   	 for(int i=0;i<b.length;i++){
   		 n<<=8;
   		 temp=b[i]&mask;
   		 n|=temp;
   	 }
   	 return n;
    }
    public static void appendFile(String file, String conent) {     
        BufferedWriter out = null;     
        try {     
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8"));     
            out.write(conent);     
        } catch (Exception e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(out != null){  
                    out.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }     
    }   

	 public static short byte2Short(byte[] b){  
	        return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));  
	    }
	 //删除文件内容
	 public static boolean delFile(String strPath) {
		 boolean filebool = false;
		 File file = new File(strPath);
		 if (file.exists() && file.isDirectory()) {
			 if (file.listFiles().length == 0) {
				 file.delete();
			 } else {
				 File[] ff = file.listFiles();
				 for (int i = 0; i < ff.length; i++) {
					 if (ff[i].isDirectory()) {
						 delFile(strPath);
					 }
					 ff[i].delete();


				 }


			 }
		 }
		  file.delete();// 如果要把文件夹也删除。。就去掉注释。。
		 filebool=true;
		 return filebool;


	 }

	 public static String hexStringToString(String hexString){
	     String response = null;
         String[] strings = hexString.split(" ");
         for(String s:strings){
             byte[] bytes = ConvertUtils.hexString2Bytes(s);
             int i = bytes2IntLE(bytes);
             response+=i;
//             if(!StringUtils.isEmpty(response)){
//               response+=" ";
//                 response+=i;
//             }else{
//
//                 response=String.valueOf(i);
//             }
         }
         return response;
     }

//	public static void main(String []args){
////		System.out.println(new BigDecimal("123").movePointLeft(2));
////		System.out.println(getRandom(0,7,1));
////		System.out.println(new Random().nextInt(5));
////        byte[] bytes = ConvertUtils.hexString2Bytes("40");
////        System.out.println(bytes2IntLE(bytes));
//        int i = bytes2IntBE(ByteUtil.hexStringToBytes("095B"));
//        System.out.println(i);
//
//    }
	public static String getRandom(Double min,Double max,int flag){
		Random random = new Random();
		Double s=random.nextDouble()*(max - min )+min ;
		NumberFormat ddf1=NumberFormat.getNumberInstance() ; 


		ddf1.setMaximumFractionDigits(flag); 

		return  ddf1.format(s) ;
	}
    
    private static String hexStr =  "0123456789ABCDEF"; 
    public static String BinaryToHexString(byte[] bytes){  
             
            String result = "";  
            String hex = "";  
            for(int i=0;i<bytes.length;i++){  
                //字节高4位  
                hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));  
                //字节低4位  
                hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));  
                result +=hex;  
            }  
            return result;  
    }
    /**
     * 将字节数组转成 16 进制的字符串来表示，每个字节采用两个字符表表示，字节间没有分隔
     * 符。<br />
     * 采用实现较为高效的 bytes2Hex 方法
     * @param bys       需要转换成 16 进制的字节数组
     * @return
     * @deprecated
     */
    public static String bytes2Hex2(byte[] bys) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bys.length; i++) {
            sb.append(HEX[bys[i] >> 4 & 0xf]);
            sb.append(HEX[bys[i] & 0xf]);
        }
        return sb.toString();
    }
    public static byte[] int2BytesBE(int num) {
        byte[] bys = new byte[Integer.SIZE / Byte.SIZE];
        for(int i = 0, k = bys.length; i < k; i++) {
            bys[i] = (byte)(num >>> ((k - 1 - i) * Byte.SIZE) & 0xff);
        }
        return bys;
    }
    public static byte[] int2BytesLE(int num) {
        return int2BytesBE(Integer.reverseBytes(num));
    }
    /**
     * 采用 Big-Endian 方式将 long 数据转为 byte 数组
     *
     * @param num
     * @return  转为 Big-Endian 方式的 byte 数组
     */
    public static byte[] long2BytesBE(long num) {
        byte[] bys = new byte[Long.SIZE / Byte.SIZE];
        for(int i = 0, k = bys.length; i < k; i++) {
            bys[i] = (byte)(num >>> ((k - 1 - i) * Byte.SIZE) & 0xff);
        }
        return bys;
    }
    /**
     * 采用 Little-Endian 方式将 long 数据转为 byte 数组
     *
     * @param num
     * @return  转为 Little-Endian 方式的 byte 数组
     */
    public static byte[] long2BytesLE(long num) {
        return long2BytesBE(Long.reverseBytes(num));
    }
    /**
     * 将 Little-Endian 的字节数组转为 int 类型的数据<br />
     * Little-Endian 表示高位字节在高位索引中
     * @param bys       字节数组
     * @param start     需要转换的开始索引位数
     * @param len       需要转换的字节数量
     * @return  指定开始位置和长度以 LE 方式表示的 int 数值
     */
    public static int bytes2IntLE(byte[] bys, int start, int len) {
        return bytes2Int(bys, start, len, false);
    }
    public static int bytes2IntLE(byte[] bys) {
        return bytes2Int(bys, 0, bys.length, false);
    }
    /**
     * 将 Big-Endian 的字节数组转为 int 类型的数据<br />
     * Big-Endian 表示高位字节在低位索引中
     * @param bys       字节数组
     * @param start     需要转换的开始索引位数
     * @param len       需要转换的字节数量
     * @return  指定开始位置和长度以 BE 方式表示的 int 数值
     */
    public static int bytes2IntBE(byte[] bys, int start, int len) {
        return bytes2Int(bys, start, len, true);
    }
    public static int bytes2IntBE(byte[] bys) {
        return bytes2Int(bys, 0, bys.length, true);
    }
    /**
     * 将字节数组转为 Java 中的 int 数值
     * @param bys           字节数组
     * @param start         需要转换的起始索引点
     * @param len           需要转换的字节长度
     * @param isBigEndian   是否是 BE（true -- BE 序，false -- LE 序）
     * @return
     */
    private static int bytes2Int(byte[] bys, int start, int len,
            boolean isBigEndian) {
        int n = 0;
        for(int i = start, k = start + len % (Integer.SIZE / Byte.SIZE + 1); i < k; i++) {
            n |= (bys[i] & 0xff) << ((isBigEndian ? (k - i - 1) : i) * Byte.SIZE);
        }
        return n;
    }
    /**
     * 将 Little-Endian 的字节数组转为 long 类型的数据<br />
     * Little-Endian 表示高位字节在高位索引中
     * @param bys       字节数组
     * @param start     需要转换的开始索引位数
     * @param len       需要转换的字节数量
     * @return  指定开始位置和长度以 LE 方式表示的 long 数值
     */
    public static long bytes2LongLE(byte[] bys, int start, int len) {
        return bytes2Long(bys, start, len, false);
    }
    public static long bytes2LongLE(byte[] bys) {
        return bytes2Long(bys, 0, bys.length, false);
    }
    /**
     * 将 Big-Endian 的字节数组转为 long 类型的数据<br />
     * Big-Endian 表示高位字节在低位索引中
     * @param bys       字节数组
     * @param start     需要转换的开始索引位数
     * @param len       需要转换的字节数量
     * @return  指定开始位置和长度以 BE 方式表示的 long 数值
     */
    public static long bytes2LongBE(byte[] bys, int start, int len) {
        return bytes2Long(bys, start, len, true);
    }
    public static long bytes2LongBE(byte[] bys) {
        return bytes2Long(bys, 0, bys.length, true);
    }
    private static long bytes2Long(byte[] bys, int start, int len,
            boolean isBigEndian) {
        long n = 0L;
        for(int i = start, k = start + len % (Long.SIZE / Byte.SIZE + 1); i < k; i++) {
            n |= (bys[i] & 0xffL) << ((isBigEndian ? (k - i - 1) : i) * Byte.SIZE);
        }
        return n;
    }
    
    private static byte charToByte(char c) {   
        return (byte) "0123456789ABCDEF".indexOf(c);   
    }  
    public static byte[] hexStringToBytes(String hexString) {   
        if (hexString == null || hexString.equals("")) {   
            return null;   
        }   
        hexString = hexString.toUpperCase();   
        int length = hexString.length() / 2;   
        char[] hexChars = hexString.toCharArray();   
        byte[] d = new byte[length];   
        for (int i = 0; i < length; i++) {   
            int pos = i * 2;   
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
        }   
        return d;   
    }  
    public static final String encodeHex(String msg) {
	    byte[] bytes = null;
	    try {
	        bytes = msg.getBytes("GBK");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    StringBuffer buff = new StringBuffer(bytes.length * 4);
	    String b;
	    char a;
	    int n = 0;
	    int m = 0;
	    for (int i = 0; i < bytes.length; i++) {
	        b = Integer.toHexString(bytes[i]);
	        if (bytes[i] > 0) {
	            buff.append("00");
	            buff.append(b);
	            n = n + 1;
	        } else {
	            a = msg.charAt((i - n) / 2 + n);
	            m = a;
	            b = Integer.toHexString(m);
	            buff.append(b.substring(0, 4));
	 
	            i = i + 1;
	        }
	    }
	    return buff.toString();
	}
    
    
    /**
     * binary : 二进制数数值
     * digit  : 要保留几位字符串
     */
    public static final String encodeHex(int binary,int digit) {
    	
    	try {
			return String.format("%" + digit + "s", Integer.toHexString(binary)).replace(' ', '0');
		} catch (Exception e) {
			return "";
		}
    }
}
