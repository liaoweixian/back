package xs.rfid.modules.stock.service.dto;

import lombok.Data;
import java.util.List;
import xs.rfid.annotation.Query;

/**
* @author liao
* @date 2020-05-07
*/
@Data
public class GiftMstQueryCriteria{

    @Query
    private Long id;

    /** 礼品编号 */
    @Query
    private String giftCod;

    /** 礼品名称 */
    @Query
    private String giftName;

    /** 礼品类型 */
    @Query
    private String giftType;

    /** 型号 */
    @Query
    private String giftModel;

    /** 推荐货位 */
    @Query
    private String toLocationCod;

    /** 停用标志 */
    @Query
    private String isUse;

    /** RFID编码 */
    @Query
    private String rfidCod;

    /** 到访状态 */
    @Query
    private String visitSta;

    /** 是否删除 */
    @Query
    private String isDelete;
}