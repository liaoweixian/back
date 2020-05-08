package xs.sense.api.entity;

import lombok.Data;

/**
 * @author mikehhuang
 * @date 2020/3/11 9:26
 */
@Data
public class RfidSerial {
    private  String tagId;
//    private  String epcId;
    private  String helperId;
    private  String antId;
}
