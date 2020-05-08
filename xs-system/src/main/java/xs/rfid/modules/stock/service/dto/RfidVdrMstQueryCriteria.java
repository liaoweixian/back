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
import java.util.List;
import xs.rfid.annotation.Query;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-07
**/
@Data
public class RfidVdrMstQueryCriteria{

    /** 精确 */
    @Query
    private String clientCod;

    /** 精确 */
    @Query
    private String clientName;

    /** 精确 */
    @Query
    private String companyName;

    /** 精确 */
    @Query
    private String positionName;

    /** 精确 */
    @Query
    private String rfidCod;

    /** 精确 */
    @Query
    private String visitDat;

    /** 精确 */
    @Query
    private Long id;

    /** 精确 */
    @Query
    private String visitStatus;

    /** 精确 */
    @Query
    private String clientPhotoCod;

    /** 精确 */
    @Query
    private String isDelete;
}