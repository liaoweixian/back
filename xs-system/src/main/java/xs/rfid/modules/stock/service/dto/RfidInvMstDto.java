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
* @author lwx
* @date 2020-05-06
**/
@Data
public class RfidInvMstDto implements Serializable {

    /** id */
    private Long id;

    /** 礼品编号 */
    private String giftCod;

    /** 库位编号 */
    private String locationCod;

    /** 库存数量 */
    private String inventoryCnt;

    /** 最后变更时间 */
    private String lastChangeDat;

    /** 最后变更用户 */
    private String lastChangeUserName;

    /** 最后变更编号 */
    private String lastChangeTransCod;

    /** 是否删除 */
    private String isDelete;

    private String giftName;
}