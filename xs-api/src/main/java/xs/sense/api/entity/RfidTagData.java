package xs.sense.api.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mikehhuang
 * @date 2020/3/24 16:01
 */
@Data
public class RfidTagData {
    private String msgID ;//消息唯一ID
    private String func ;//方法，命令标识
    private Long currentTime ;//当前时间
    private String actionType ;//任务类型：0-没有任务 1-上架 2-下架 3-出库 4-入库 5-小件集结 6-任务完成 7-任务取消 8-任务中止
    private String taskNum ; //任务号 入库有没有任务号待确认
    private String vehicleID;//车牌号 如无车牌信息，此字段value为空

    private DeviceInfo deviceInfo ;//设备数据
    private List<DataTag> data ;//标签数据
}
