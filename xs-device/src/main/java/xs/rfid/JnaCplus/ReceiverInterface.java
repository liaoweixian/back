package xs.rfid.JnaCplus;

import com.sun.jna.Library;
import com.sun.jna.Native;

import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.springframework.util.ResourceUtils;
import xs.rfid.rest.JnaTest;

import java.io.File;
import java.net.URL;

public interface ReceiverInterface extends Library {
    String path = System.getProperty("user.dir");

//   File f =  ResourceUtils.getFile("dll/Dll2");
//   URL pa = ResourceUtil.getResouce("dll/Dll2");
    ReceiverInterface INSTANCE = (ReceiverInterface) Native.load(path+ File.separator+"xs-device\\src\\main\\java\\xs\\rfid\\rest\\Dll2", ReceiverInterface.class);
    public int cmd_tagdb(String  cmd, String para, String output);
}
