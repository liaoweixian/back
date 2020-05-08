package xs.sense.api.utils;

import lombok.extern.slf4j.Slf4j;
import xs.sense.api.entity.DeviceInfo;
import xs.sense.api.serial.exception.SendDataToSerialPortFailure;
import xs.sense.api.serial.exception.SerialPortOutputStreamCloseFailure;
import xs.sense.api.serial.vo.SerialPortVo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mikehhuang
 * @date 2020/3/11 9:40
 */
@Slf4j
public class ConstantSense {
    //叉车设备状态标志 1.TR90 2 TR86C 3.BOTH
    public static   int deviceStatus;

    //工控机第一次开机标志
    public static  boolean isStart;
    //缓存设备信息（心跳中的设备信息）
    public static Set<DeviceInfo> deviceInfoList= new HashSet<>();
    //灯设备选择 1：c200 2.tr90
    public static  int lightDevice = 2;
    //c200 串口配置
    public static  int  comNo = 2;
    //设备控制主题
    public static  String  deviceControl = "/sense/server/deviceControl/";
    //控制结果主题
    public static  String  deviceControlResp = "/sense/client/deviceControl/";
    // 上报RFID信息主题
    public static  String  sendRdInfo = "/sense/client/rfid/data/";
    // 设备状态信息上报主题
    public static  String  RFIDQueryStatus = "/sense/client/deviceInfo/";

    // 按键事件通知TR86C主题
    public static  String  RFIDKeyEvent = "/sense/server/readTagInfo/";

    //API 唯一ID
    //receiver 的工作状态 1. 连接状态，2.断开状态，3. 工作状态(盘存状态)，4 .空闲状态
    public static  int receiverStatus =4;
    //已经连接的串口
    public static ArrayList<SerialPortVo> serialPortVoList = new ArrayList<>();

    //receiver 命令前缀
    public final static String  cmdPrefix = "6666040";
    public final static String  cmdPrefixWithoutZero = "666604";
    //Q值修改
    public final static String  Qcmd = "66660441";
    //QueryAdjust次数更改
    public final static String  QueryAdjustCmd = "66660442";
    //M值更改
    public final static String  Mcmd= "66660444";

    //BLF值(标签返回速率)更改
    public final static String  BLFcmd= "66660445";

    //发射通道使能选择
    public final static String  transChenelCmd= "6666054D";

    //Helper和天线选择
    public final static String  helperAntCmd= "6666064E";

    //上行频率选择选择
    public final static String  uplinkFrequencyCmd= "6666064F";

    //Helper发射功率更改
    public final static String  helperPowerCmd= "66660560";

    //Receiver发射模式选择
    public final static String  receiverTransmitCmd= "66660561";

    //Receiver盘存QueryAB次数修改
    public final static String  receiverQabCmd= "66660663";

    //停止Tag盘点
    public final static String  stopTagCmd= "66660490";

    //开始Tag盘点（定频无Select模式）
    public final static String  startTagFixFrequencyCmd= "66660493";

    //开始Tag盘点（跳频无Select模式）
    public final static String  startTagFrequencyHoppingCmd= "66660495";

    //开始Tag盘点（定频有Select模式）
    public final static String  startTagFixFrequencySelectCmd= "66660496";


    public static void serialSendData(SerialPortVo serialPortVo,String command) throws SerialPortOutputStreamCloseFailure, SendDataToSerialPortFailure {
        log.info("+++"+command);
//        log.info("==="+ ConvertUtils.hexString2Bytes(command));
//        serialPortVo.sendData(ConvertUtils.hexString2Bytes(command));
    }

}
