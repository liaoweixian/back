package xs.sense.api.mqtt_local;


import xs.sense.api.mqtt.DataService;

public class XsCollectionHandlerLocal {
    //TRXX 系列数据处理
    public static void swithHandlerLocal(String message, DataServiceLocal dataService, String cmd) {
        processDataLocal(message,dataService,cmd);

    }

    private static void  processDataLocal( String receiveMsg,  DataServiceLocal dataService,String cmd){
        switch (cmd){
            case "SendRdInfo":
                //TRXX系列主动上报的RFID信息
                dataService.readTagInfo(receiveMsg);
                break;
            case "RFIDLoadReportLocal":
                //TRXX系列装卸载上报的RFID信息
                dataService.RFIDLoadReport(receiveMsg);
                break;
            case "DeviceStatusReportLocal":
                //TRXX系列设备状态信息上报
                dataService.DeviceStatusReportLocal(receiveMsg);
                break;
            case "RFIDKeyEventLocal":
                //TR90 按键事件信息上报
                dataService.RFIDKeyEventLocal(receiveMsg);
                break;
            case "deviceControlLightResp":
                //TR90 灯控制结果返回信息上报
                dataService.ligthtControlRespLocal(receiveMsg);
                break;
        }


    }

//    public static void main(String[] args) {
////        int i = ByteUtil.bytes2IntBE(ByteUtil.hexStringToBytes("70"));
////        System.out.println("--------"+i);
////        ConvertUtils.
//    }
}
