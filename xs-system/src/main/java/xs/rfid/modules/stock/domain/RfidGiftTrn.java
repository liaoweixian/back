package xs.rfid.modules.stock.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @author liao
* @date 2020-05-08
*/
@Entity
@Data
@Table(name="rfid_gift_trn")
public class RfidGiftTrn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** 客户名称 */
    @Column(name = "client_cod",nullable = false)
    @NotBlank
    private String clientCod;

    @Column(name = "client_name",nullable = false)
    private String clientName;

    /** 礼品编号 */
    @Column(name = "gift_cod",nullable = false)
    @NotBlank
    private String giftCod;

    /**
     * 礼品型号
     */
    @Column(name = "gift_model",nullable = false)
    @NotBlank
    private String giftModel;

    /** 库位编码 */
    @Column(name = "location_cod",nullable = false)
    private String locationCod;

    /** 数量 */
    @Column(name = "gift_cnt",nullable = false)
    private String giftCnt;

    /** 状态（选择下单、已备货、已上架、已生产、已打印、已领取、返库） */
    @Column(name = "status",nullable = false)
    @NotBlank
    private String status;

    /** 库存事务编号 */
    @Column(name = "trans_cod")
    private String transCod;

    /** 是否删除 */
    @Column(name = "is_delete")
    private String isDelete;

    /** 推荐货位编号 */
    @Column(name = "to_Location_cod",nullable = false)
    @NotBlank
    private String toLocationCod;

    /** 订单编号 */
    @Column(name = "order_sn")
    private String orderSn;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_name")
    private String createName;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "update_name")
    private String updateName;

    /**
     * 礼品id
     */
    @Column(name = "gift_id")
    private Long giftId;

    /**
     * 礼品名字
     */
    @Transient
    private String giftName;

    @Transient
    private String imgUrl;



    public void copy(RfidGiftTrn source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}