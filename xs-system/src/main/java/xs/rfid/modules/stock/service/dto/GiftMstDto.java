package xs.rfid.modules.stock.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author liao
* @date 2020-05-07
*/
@Data
public class GiftMstDto implements Serializable {

    private Long id;

    /** 礼品编号 */
    private String giftCod;

    /** 礼品名称 */
    private String giftName;

    /** 礼品类型 */
    private String giftType;

    /** 型号 */
    private String giftModel;

    /** 推荐货位 */
    private String toLocationCod;

    /** 停用标志 */
    private String isUse;

    /** RFID编码 */
    private String rfidCod;

    private String registerDat;

    /** 到访状态 */
    private String visitSta;

    /** 是否删除 */
    private String isDelete;

    private  Integer isBind;
}