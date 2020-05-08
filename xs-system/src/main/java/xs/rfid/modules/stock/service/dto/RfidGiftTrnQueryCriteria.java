package xs.rfid.modules.stock.service.dto;

import lombok.Data;
import java.util.List;
import xs.rfid.annotation.Query;

/**
* @author liao
* @date 2020-05-08
*/
@Data
public class RfidGiftTrnQueryCriteria{

    /** 精确 */
    @Query
    private String clientCod;

    /** 精确 */
    @Query
    private String giftCod;

    /**
     * 礼品型号
     */
    @Query
    private String giftModel;

    /** 精确 */
    @Query
    private String locationCod;

    /** 精确 */
    @Query
    private String giftCnt;

    /** 精确 */
    @Query
    private String status;

    private String giftName;

}