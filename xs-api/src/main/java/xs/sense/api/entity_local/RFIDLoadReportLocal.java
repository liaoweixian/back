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
public class RFIDLoadReportLocal {
    private String  msgID; //消息ID，唯一
    private String  func;  // 功能标识 // sendRdInfo:正常标签信息如:盘存      //sendTagLoadInfo:装卸载信息 //sendTagDoorInfo: 出入库信息
    private String  currentTime; //时间戳
    private String  userID; //用户ID
    private String  vehicleID;//车牌
    private String  taskType ; //任务类型
    private String  taskID;  //任务id
    private String  version;  //版本信息
    private LoadInfoLocal loadInfo; //装卸载信息
    private DoorInfoLocal doorInfo; //出入库信息
    private DeviceInfo deviceInfo; //设备信息
    private List<DataTag>  data; //标签数据
}
