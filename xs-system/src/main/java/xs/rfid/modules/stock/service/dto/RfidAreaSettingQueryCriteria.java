package xs.rfid.modules.stock.service.dto;

import lombok.Data;
import java.util.List;
import xs.rfid.annotation.Query;

/**
* @author liao
* @date 2020-05-10
*/
@Data
public class RfidAreaSettingQueryCriteria{

    private Long id;

    /** 区域名 */
    @Query
    private String areaName;

    /** ip地址 */
    @Query
    private String ip;

    /** 用户id */
    @Query
    private Long userId;

    /** 用户名 */
    @Query
    private String userName;

    private String createdTime;

    private String rfidCode;
}