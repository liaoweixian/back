package xs.sense.api.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mikehhuang
 * @date 2020/3/23 18:05
 */
@Data
public class RFIDInfo {
    private String msgID ;//消息唯一ID
    private String func ;//方法，命令标识
    private DeviceInfo deviceInfo ;//设备信息消息
    private List<DataTag> data ;//标签数据

}
