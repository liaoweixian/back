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
public class RfidGiftMstDto implements Serializable {

    /** id */
    private String id;

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

    private String imgUrl;
}