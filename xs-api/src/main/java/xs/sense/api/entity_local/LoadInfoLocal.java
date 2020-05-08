package xs.sense.api.entity_local;

import lombok.Data;

import java.util.List;

/**
 * @author mikehhuang
 * @date 2020/3/24 17:45
 */
@Data
public class LoadInfoLocal {
    private String loadStatus; //1:装载 2: 卸载
    private String distance;  //距离，单位cm

}
