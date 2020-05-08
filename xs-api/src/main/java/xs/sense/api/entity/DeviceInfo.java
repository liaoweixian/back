package xs.sense.api.entity;

import lombok.Data;

/**
 * @author mikehhuang
 * @date 2020/3/23 18:33
 */
@Data
public class DeviceInfo {
    private String deviceId ;//设备id 唯一ID
    private String deviceType ;// 1. 叉车  2. 码垛机 3. 手持 4.TR90 5.TR89 6.TR86C
}
