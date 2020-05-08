package xs.sense.api.entity_local;

import lombok.Data;

/**
 * @author mikehhuang
 * @date 2020/3/24 17:45
 */
@Data
public class DoorInfoLocal {
    private String doorStatus; //出入状态 ，如:1:入库 2:出库
    private String doorNo;  //库门号

}
