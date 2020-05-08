package xs.sense.api.mqtt_local;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xs.rfid.utils.StringUtils;
import xs.sense.api.entity.*;
import xs.sense.api.entity_local.RFIDLoadReportLocal;
import xs.sense.api.mqtt.SendMessage;
import xs.sense.api.utils.ConstantSense;

import java.util.LinkedList;


@Service
@AllArgsConstructor
@Slf4j
public class DataServiceLocal {
    private SendMessage sendMessage;
    private SendMessageLocal sendMessageLocal;

    public void readTagInfo(String receiveMsg) {
        log.error(receiveMsg);
        //1.解析数据

        LinkedList<RfidSerial>  rfidSerialList = new LinkedList<>();
        RfidSerial rfidSerial = new RfidSerial();

       //2.缓存解析完成后的数据并去重
        rfidSerialList.add(rfidSerial);
//        rfidSerialList.parallelStream().

       //3. 与数据库的资产数据比对，并对数据打标记,如入库，出库

       //4. 判断receiver 是否在空闲状态，如是，清空list
        rfidSerialList.clear();
    }

    public void RFIDLoadReport(String receiveMsg) {
        RFIDLoadReportLocal rfidLoadReportLocal = parseRfidLoadReportLocal(receiveMsg);
        if (rfidLoadReportLocal == null) return;
//
//       //2.处理数据
//        switch (rfidLoadReportLocal.getFunc()){
//            case "sendRdInfo":
//                 //正常标签信息，如盘存结果
//
//                 break;
//            case "sendTagLoadInfo":
//                // 装卸载信息
//
//                 break;
//            case "sendTagDoorInfo":
//                //sendTagDoorInfo
//
//                 break;
//        }

        rfidLoadReportLocal.getDeviceInfo().setDeviceType("1");
       //3. 上报数据到后台
        sendMessage.send(JSON.toJSONString(rfidLoadReportLocal), ConstantSense.sendRdInfo+rfidLoadReportLocal.getDeviceInfo().getDeviceId());

    }
    public void DeviceStatusReportLocal(String receiveMsg) {
        RFIDLoadReportLocal rfidLoadReportLocal = parseRfidLoadReportLocal(receiveMsg);
        if (rfidLoadReportLocal == null) return;

        //2.处理数据
        //2.1 缓存到set
        ConstantSense.deviceInfoList.add(rfidLoadReportLocal.getDeviceInfo());

       //3. 上报数据到后台
        if(rfidLoadReportLocal.getDeviceInfo().getDeviceType().equals("6")){
            //上报TR86C的数据到东鹏后台
            DeviceStatusData deviceStatusData = new DeviceStatusData();
            deviceStatusData.setMsgID(SystemClock.now()+ String.valueOf(RandomUtil.randomInt(10,99)));
            deviceStatusData.setFunc("RFIDQueryStatus");
            deviceStatusData.setCurrentTime(SystemClock.now());
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceId(rfidLoadReportLocal.getDeviceInfo().getDeviceId());
            deviceInfo.setDeviceType("1");
            deviceStatusData.setDeviceInfo(deviceInfo);
            DeviceStatus deviceStatus = new DeviceStatus();
            deviceStatus.setType("0");
            deviceStatus.setMessage("设备正常");
            deviceStatusData.setData(deviceStatus);
            sendMessage.send(JSON.toJSONString(deviceStatusData), ConstantSense.RFIDQueryStatus+rfidLoadReportLocal.getDeviceInfo().getDeviceId());
        }

    }

    public void ligthtControlRespLocal(String receiveMsg) {
        //1.解析数据
        DeviceControlInfo deviceControlInfo = JSON.parseObject(receiveMsg, DeviceControlInfo.class);
        if(null == deviceControlInfo ){
            return;
        }

        //2.处理数据
        //2.1 从设备信息缓存数据中找到TR86C设备 设备类型为6
        ConstantSense.deviceInfoList.forEach(deviceInfo -> {
            if(deviceInfo.getDeviceType().equals("6")){
                //TR86C设备
                //3. 发数据到TR86C
                deviceControlInfo.setDeviceInfo(deviceInfo);
                sendMessage.send(JSON.toJSONString(deviceControlInfo), ConstantSense.deviceControlResp+deviceInfo.getDeviceId());
            }
        });

    }
    public void RFIDKeyEventLocal(String receiveMsg) {
        RFIDLoadReportLocal rfidLoadReportLocal = parseRfidLoadReportLocal(receiveMsg);
        if (rfidLoadReportLocal == null) return;

        //2.处理数据
        //2.1 从设备信息缓存数据中找到TR86C设备 设备类型为6
        ConstantSense.deviceInfoList.forEach(deviceInfo -> {
            if(deviceInfo.getDeviceType().equals("6")){
                //TR86C设备
                //3. 发数据到TR86C
                sendMessageLocal.send(JSON.toJSONString(rfidLoadReportLocal), ConstantSense.RFIDKeyEvent+deviceInfo.getDeviceId());
            }
        });

    }

    private RFIDLoadReportLocal parseRfidLoadReportLocal(String receiveMsg) {
        log.error(receiveMsg);
        //1.解析数据
        RFIDLoadReportLocal rfidLoadReportLocal = JSON.parseObject(receiveMsg, RFIDLoadReportLocal.class);
        if (null == rfidLoadReportLocal || StringUtils.isEmpty(rfidLoadReportLocal.getFunc())) {
            return null;
        }
        return rfidLoadReportLocal;
    }

    @Scheduled(cron = "0 0/1 * * * ? ")
    private void deviceSelfTest(){
        if(!ConstantSense.isStart){
            //工控机开机成功，自检叉车设备，根据自动上报的设备状态来判定（默认60s 一次）
            //1. 检测设备缓存list中有没有类型为4.5.6的设备 4.TR90 5.TR89 6.TR86C
            if(ConstantSense.deviceInfoList.size()<2){
                //1.1 叉车设备不完整
                ConstantSense.deviceInfoList.forEach(deviceInfo -> {
                    if(deviceInfo.getDeviceType().equals("4")){
                       // TR90 灯控制设备，红灯常亮
                        //2. 如果有4设备，其它设备没有，报警
                      DeviceControlInfo deviceControlInfo = new DeviceControlInfo();
                      deviceControlInfo.setMsgID(SystemClock.now()+ String.valueOf(RandomUtil.randomInt(10,99)));
                      deviceControlInfo.setFunc("deviceControlLight");
                      deviceControlInfo.setDeviceInfo(deviceInfo);
                      DeviceControl deviceControl = new DeviceControl();
                      deviceControl.setCurrentTime(SystemClock.now());
                      deviceControl.setStatus("1");// 1:红灯亮 2:黄灯亮 3:绿灯亮 4. 红灯闪烁 8 黄灯闪烁 9 绿灯闪
                      deviceControl.setAlarm(1); //如0:无声 1.默认响声 2.刺啦声 3:滴滴声 等
                      deviceControl.setAlarmHoldTime(10);  //声音持续时间 单位秒
                        deviceControl.setHoldTime(60);
                        deviceControl.setPluseNo(1);
                      deviceControlInfo.setData(deviceControl);
                      sendMessageLocal.send(JSON.toJSONString(deviceControlInfo),ConstantSense.deviceControl+deviceInfo.getDeviceId());
                    }
                });
            }else {
                ConstantSense.isStart = true;
            }

        }
    }
}
