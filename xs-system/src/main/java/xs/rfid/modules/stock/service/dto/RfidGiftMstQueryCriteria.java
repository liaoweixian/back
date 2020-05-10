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
import xs.rfid.annotation.Query;

import java.util.List;


/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-06
**/
@Data
public class RfidGiftMstQueryCriteria{

    /** 精确 */
    @Query
    private String id;


    /** 精确 */
    @Query
    private String giftCod;

    /** 精确 */
    @Query
    private String giftName;

    /** 精确 */
    @Query
    private String giftType;

    /** 精确 */
    @Query
    private String giftModel;

    /** 精确 */
    @Query
    private String tolocationCod;

    /** 精确 */
    @Query
    private String isUse;

    /** 精确 */
    @Query
    private String rfidCod;

    /** 精确 */
    @Query
    private String registerDat;

    /** 精确 */
    @Query
    private String visitSta;

    /** 精确 */
    @Query
    private String isDelete;

}