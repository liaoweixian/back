package xs.sense.api.mqtt;


import cn.hutool.core.date.SystemClock;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xs.rfid.utils.StringUtils;
import xs.sense.api.c200.C200Driver;
import xs.sense.api.entity.DeviceControl;
import xs.sense.api.entity.DeviceControlInfo;
import xs.sense.api.entity.RFIDInfo;
import xs.sense.api.entity.RfidSerial;
import xs.sense.api.mqtt_local.SendMessageLocal;
import xs.sense.api.utils.ConstantSense;

import java.util.LinkedList;


@Service
@AllArgsConstructor
@Slf4j
public class DataService {


    private final SendMessageLocal sendMessageLocal;
    private final  SendMessage sendMessage;

    public void readTagInfo(String receiveMsg) {
        log.error(receiveMsg);
        if(StringUtils.isEmpty(receiveMsg)){
            return;
        }
        //1.解析数据
        RFIDInfo rfidInfo = JSON.parseObject(receiveMsg,RFIDInfo.class);

       //2.保存（此功能下一版再做）
//        sendMessageLocal.send(receiveMsg,"");

       //3. 转发给采集器
        //因TRXX系列没有对应的接口 暂停


    }

    public void deviceControl(String receiveMsg) {
        log.error(receiveMsg);
        if(StringUtils.isEmpty(receiveMsg)){
            return;
        }
        //1.解析数据
        DeviceControlInfo deviceControlInfo = JSON.parseObject(receiveMsg, DeviceControlInfo.class);
        if(null == deviceControlInfo ){
            return;
        }
        if("deviceControlLight".equals(deviceControlInfo.getFunc())){ //灯设备
            int resp=1;
            if(ConstantSense.lightDevice==1){
                C200Driver.init_board();
                //C200
                switch (deviceControlInfo.getData().getStatus()){

                    case "1":
                        if(deviceControlInfo.getData().getAlarm() !=0){
                            //开启警告声
                            ThreadUtil.execute(new Runnable() {
                                @Override
                                public void run() {
                                     openAlarm(deviceControlInfo);
                                }
                            });

                        }
                        //红灯亮
                        try {
                            resp=C200Driver.red_on();
                            Thread.sleep(deviceControlInfo.getData().getHoldTime()*1000);
                            resp =C200Driver.red_off();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "2":
                        //黄灯亮
                        if(deviceControlInfo.getData().getAlarm() !=0){
                            //开启警告声
                            ThreadUtil.execute(new Runnable() {
                                @Override
                                public void run() {
                                    openAlarm(deviceControlInfo);
                                }
                            });
                        }
                        try {
                            resp=C200Driver.yellow_on();
                            Thread.sleep(deviceControlInfo.getData().getHoldTime()*1000);
                            resp =C200Driver.yellow_off();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "3":
                        if(deviceControlInfo.getData().getAlarm() !=0){
                            //开启警告声
                            ThreadUtil.execute(new Runnable() {
                                @Override
                                public void run() {
                                    openAlarm(deviceControlInfo);
                                }
                            });
                        }
                        //绿灯亮
                        try {
                            resp=C200Driver.green_on();
                            Thread.sleep(deviceControlInfo.getData().getHoldTime()*1000);
                            resp =C200Driver.green_off();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "4":
                        //红灯闪烁
                        if(deviceControlInfo.getData().getAlarm() !=0){
                            //开启警告声
                            ThreadUtil.execute(new Runnable() {
                                @Override
                                public void run() {
                                    openAlarm(deviceControlInfo);
                                }
                            });
                        }
                        try {
                            if(deviceControlInfo.getData().getPluseNo()!=0){
                                for(int i=0;i<deviceControlInfo.getData().getPluseNo();i++){
                                    resp=C200Driver.red_on();
                                    Thread.sleep(deviceControlInfo.getData().getHoldTime()*1000);
                                    resp =C200Driver.red_off();
                                    Thread.sleep(deviceControlInfo.getData().getHoldTime()*1000);
                                }
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "5":
                        //黄灯闪烁
                        if(deviceControlInfo.getData().getAlarm() !=0){
                            //开启警告声
                            ThreadUtil.execute(new Runnable() {
                                @Override
                                public void run() {
                                    openAlarm(deviceControlInfo);
                                }
                            });
                        }
                        try {
                            if(deviceControlInfo.getData().getPluseNo()!=0){
                                for(int i=0;i<deviceControlInfo.getData().getPluseNo();i++){

                                    resp=C200Driver.yellow_on();
                                    Thread.sleep(deviceControlInfo.getData().getHoldTime()*1000);
                                    resp =C200Driver.yellow_off();
                                    Thread.sleep(deviceControlInfo.getData().getHoldTime()*1000);
                                }
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "6":
                        //绿灯闪烁
                        if(deviceControlInfo.getData().getAlarm() !=0){
                            //开启警告声
                            ThreadUtil.execute(new Runnable() {
                                @Override
                                public void run() {
                                    openAlarm(deviceControlInfo);
                                }
                            });
                        }
                        try {
                            if(deviceControlInfo.getData().getPluseNo()!=0){
                                for(int i=0;i<deviceControlInfo.getData().getPluseNo();i++){
                                    resp=C200Driver.green_on();
                                    Thread.sleep(deviceControlInfo.getData().getHoldTime()*1000);
                                    resp =C200Driver.green_off();
                                    Thread.sleep(deviceControlInfo.getData().getHoldTime()*1000);
                                }
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                deviceControlInfo.setFunc("deviceControlLightResp");
                deviceControlInfo.setSuccess(String.valueOf(resp));
                deviceControlInfo.getData().setCurrentTime(SystemClock.now());
                sendMessage.send(JSON.toJSONString(deviceControlInfo),ConstantSense.deviceControlResp+"12341234123");

                C200Driver.close_board();
            }else{
                //TR90
                ConstantSense.deviceInfoList.forEach(deviceInfo -> {
                    if(deviceInfo.getDeviceType().equals("4")){
                        deviceControlInfo.setDeviceInfo(deviceInfo);
                        sendMessageLocal.send(JSON.toJSONString(deviceControlInfo),ConstantSense.deviceControl+deviceInfo.getDeviceId());
                    }
                });

            }
        }

       //2.保存（此功能下一版再做）
//        sendMessageLocal.send(receiveMsg,"");

       //3. 转发给采集器
        //因TRXX系列没有对应的接口 暂停


    }
    private static int openAlarm(DeviceControlInfo deviceControlInfo){
        int resp =1;
        try {

            resp=C200Driver.alarm_on();
            Thread.sleep(deviceControlInfo.getData().getAlarmHoldTime()*1000);
            resp =C200Driver.alarm_off();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resp;
    }
}
