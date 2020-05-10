package xs.rfid.modules.stock.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author liao
* @date 2020-05-08
*/
@Data
public class RfidGiftTrnDto implements Serializable {

    private Long id;

    /** 客户名称 */
    private String clientCod;

    /** 礼品编号 */
    private String giftCod;

    /** 库位编码 */
    private String locationCod;

    /**
     * 礼品型号
     */
    private String giftModel;

    /** 数量 */
    private String giftCnt;

    /** 状态（选择下单、已备货、已上架、已生产、已打印、已领取、返库） */
    private String status;

    /** 库存事务编号 */
    private String transCod;

    /** 是否删除 */
    private String isDelete;

    /** 推荐货位编号 */
    private String toLocationCod;

    /** 订单编号 */
    private String orderSn;

    private String createTime;

    private String createName;

    private String updateTime;

    private String updateName;

    private String giftName;

    private String clientName;

    private Long giftId;
}