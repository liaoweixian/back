package xs.sense.api.c200;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.File;

public interface C200Interface extends Library {
    //如果打包运行，需要把dll文件放到jre/bin 目录下
//    String path = System.getProperty("user.dir");
//    C200Interface INSTANCE = (C200Interface) Native.load(path+ File.separator+"xs-api\\src\\main\\java\\xs\\sense\\api\\c200\\FAIO_x64", C200Interface.class);
//    C200Interface INSTANCE = (C200Interface) Native.load(path+ File.separator+"FAIO_x64", C200Interface.class);
    C200Interface INSTANCE = (C200Interface) Native.load("FAIO_x64", C200Interface.class);
    public int iob_board_init(int comno, String outputStatus); //初始化板卡
    public int iob_board_close(int comno, Boolean bOutputKeep); //关闭板卡
    public int iob_write_outbit(int comno, int bitno,Boolean bOn); // bitno = 0时会返回错误代码FAIO_ERROR_BITNUMVALUEWRONG 1: alarm  2: red light 3: yellow light 4:green light
}
