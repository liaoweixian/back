package xs.sense.api.mqtt;


public class XsCollectionHandler {
    public static void swithHandler(String message, DataService dataService,String cmd) {
        processData(message,dataService,cmd);

    }

    private static void  processData( String receiveMsg,  DataService dataService,String cmd){
            switch (cmd){
                case "readTagInfo":
                    //客户读取指定标配信息
                    dataService.readTagInfo(receiveMsg);
                    break;
                case "deviceControl":
                    //客户控制设备
                    dataService.deviceControl(receiveMsg);
                    break;
            }


    }


//    public static void main(String[] args) {
////        int i = ByteUtil.bytes2IntBE(ByteUtil.hexStringToBytes("70"));
////        System.out.println("--------"+i);
////        ConvertUtils.
//    }
}
