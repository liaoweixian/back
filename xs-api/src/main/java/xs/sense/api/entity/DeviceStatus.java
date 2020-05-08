package xs.sense.api.entity;

import lombok.Data;


/**
 * @author mikehhuang
 * @date 2020/3/23 18:35
 */
@Data
public class DeviceStatus {
    private String type; //类型 0：所有设备正常，1.RFID采集器异常 2. 装卸载状态逻辑控制器异常3.作业状态控制器异常（灯）
    private String message;  //具体异常信息

}
