package xs.sense.api.entity_local;

import lombok.Data;
import xs.sense.api.entity.DataTag;
import xs.sense.api.entity.DeviceInfo;

import java.util.List;

/**
 * @author mikehhuang
 * @date 2020/3/24 17:39
 */
@Data
public class TrxxDeviceStatusLocal {
    private String  msgID; //消息ID，唯一
    private String  func;  // 功能标识 // sendRdInfo:正常标签信息如:盘存      //sendTagLoadInfo:装卸载信息 //sendTagDoorInfo: 出入库信息
    private String  currentTime; //时间戳
    private String  msg; //待补充
    private TrxxDeviceStatus moduleStatus; //装卸载信息
}
