package xs.rfid.modules.stock.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author liao
* @date 2020-05-10
*/
@Data
public class RfidAreaSettingDto implements Serializable {

    private Long id;

    /** 区域名 */
    private String areaName;

    /** ip地址 */
    private String ip;

    /** 用户id */
    private Long userId;

    private String createdTime;

    /** 用户名 */
    private String userName;
}