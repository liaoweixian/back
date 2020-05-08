package xs.sense.api.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mikehhuang
 * @date 2020/3/24 16:01
 */
@Data
public class DeviceStatusData {
    private String msgID ;//消息唯一ID
    private String func ;//方法，命令标识
    private Long currentTime ;//当前时间

    private DeviceInfo deviceInfo ;//设备数据
    private DeviceStatus data ;//设备状态数据
}
