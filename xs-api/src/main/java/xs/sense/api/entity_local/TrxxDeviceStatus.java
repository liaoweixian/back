package xs.sense.api.entity_local;

import lombok.Data;

/**
 * @author mikehhuang
 * @date 2020/3/24 17:39
 */
@Data
public class TrxxDeviceStatus {
    private String  moduleRFIDVers; //RFID模块版本
    private String  moduleRFIDAnt;  // RFID天线状态:0表示天线不存在，1表示天线存在  "0,1,1,0" 以，逗号分隔
    private String  moduleRFIDTemp; //RFID模块温度
    private String  moduleRFIDPower; //RFID模块功率 "30,20,20,20" 单位db,以，逗号分隔
    private String  moduleInfraTrigger; //红外感应器:0表示不正常，1表示正常
    private String  moduleLoadTrigger; //装载感应器:0表示不正常，1表示正常
}
