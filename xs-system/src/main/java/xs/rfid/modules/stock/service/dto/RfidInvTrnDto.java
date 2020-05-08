/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package xs.rfid.modules.stock.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @website https://docs.auauz.net
* @description /
* @author stock
* @date 2020-05-06
**/
@Data
public class RfidInvTrnDto implements Serializable {

    private Long id;

    /** 礼品编号 */
    private String giftCod;

    /** 库位编码 */
    private String locationCod;

    /** 事务数量 */
    private String transCnt;

    /** 事务类型（入库/出库） */
    private String transType;

    /** 事务时间 */
    private String transDat;

    /** 状态（未上架/已上架/已下架/已出库/已返库） */
    private String status;

    /** 是否删除 */
    private String isDelete;
}