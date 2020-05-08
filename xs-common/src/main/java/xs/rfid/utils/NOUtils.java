package xs.rfid.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NOUtils {

    public static String nextItemNo(String top){
        Date date = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String id = sdformat.format(date);
        return top+id;
    }
}
