package xs.rfid.rest;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.win32.StdCallLibrary;
import xs.rfid.JnaCplus.ReceiverInterface;

import java.io.File;


/**
 * @author mikehhuang
 * @date 2020/3/5 10:20
 */
public class JnaTest {
//    /*需要定义一个接口，继承自Library 或StdCallLibrary。
//     默认的是继承Library ，如果动态链接库里的函数是以stdcall方式输出的，那么就继承StdCallLibrary，比如kernel32库。
//   */
//    public interface CLibrary extends Library {
//        //加载msvcrt.dll库,此处不需要后缀.dll或.so
//        CLibrary INSTANCE = (CLibrary)
//                Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"),
//                        CLibrary.class);
//        //对msvcrt.dll中需要使用的printf函数进行声明
//        void printf(String format, Object... args);
//    }
//
//    public static void main(String[] args) {
//        //使用printf函数
//        CLibrary.INSTANCE.printf("Hello, World\n");
//        for (int i=0;i < args.length;i++) {
//            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
//        }
//    }

//    public interface CLibrary extends Library{
//        //此处我的jdk版本为64位,故加载64位的Dll
//        CLibrary INSTANCE = (CLibrary)Native.loadLibrary("C:\\work\\xs\\eladmin\\xs-device\\src\\main\\java\\me\\zhengjie\\rest\\Dll2x64",CLibrary.class);
//        //Dll2x64中定义的函数
//        double Add(double a,double b);
//    }
//
//    public static void main(String[] args) {
//        System.out.println("3+4=Add(3,4)="+CLibrary.INSTANCE.Add(3,4));
//    }


    interface Dll3 extends Library {
        //获取项目路径
        String path = System.getProperty("user.dir");
        Dll3 INSTANCE = (Dll3) Native.load(path+ File.separator+"xs-device\\src\\main\\java\\xs\\rfid\\rest\\Dll2", Dll3.class);
        public int cmd_tagdb(String  cmd, String para, String output);

    }


    public static void main(String[] args) {
//         System.out.println(System.getProperty("java.version"));
//         System.out.println(System.getProperty("sun.arch.data.model"));
        String path = System.getProperty("user.dir");
        System.out.println(path);
//        Dll3 clib = Dll3.INSTANCE;
        ReceiverInterface clib = ReceiverInterface.INSTANCE;
        StringBuffer buffer = new StringBuffer();
        System.out.println("测试返回结果：" + clib.cmd_tagdb("1", "2",""));

    }
//    interface Dll3 extends Library {
//        Dll3 INSTANCE = (Dll3) Native.load("C:\\Users\\mikehhuang\\source\\repos\\Dll1\\x64\\Release\\Dll1", Dll3.class);
//
//        public int add(int a, int b);
//
//        public int factorial(int n);
//
//        public String say(String msg);
//
//        public boolean sayno(boolean flag);
//
//    }
//
//    public static void main(String[] args) {
////         System.out.println(System.getProperty("java.version"));
////         System.out.println(System.getProperty("sun.arch.data.model"));
//
//        Dll3 clib = Dll3.INSTANCE;
//        System.out.println("测试返回结果：" + clib.add(13, 17));
//        System.out.println("测试返回结果：" + clib.factorial(1));
//        System.out.println("测试返回结果：" + clib.say("heoll wrold！！"));
//        System.out.println("测试返回结果：" + clib.sayno(true));
//    }
}