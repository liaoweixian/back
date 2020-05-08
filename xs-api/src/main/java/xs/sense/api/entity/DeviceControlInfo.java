package xs.sense.api.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mikehhuang
 * @date 2020/3/23 18:05
 */
@Data
public class DeviceControlInfo {
    private String msgID ;//消息唯一ID
    private String func ;//方法，命令标识
    private DeviceControl data ;//设备控制数据
    private DeviceInfo deviceInfo ;//设备数据
    private String  success; //控制结果 0：成功，其它：失败

}
