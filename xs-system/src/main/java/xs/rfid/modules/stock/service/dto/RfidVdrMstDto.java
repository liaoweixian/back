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
* @date 2020-05-07
**/
@Data
public class RfidVdrMstDto implements Serializable {

    /** 客户编号 */
    private String clientCod;

    /** 客户名称 */
    private String clientName;

    /** 公司名称 */
    private String companyName;

    /** 职务 */
    private String positionName;

    /** RFID编码 */
    private String rfidCod;

    /** 到访时间 */
    private String visitDat;

    /** id */
    private Long id;

    /** 访客状态1、预约， 2、到访， 3、已完成 */
    private String visitStatus;

    /** 客户照片编码 */
    private String clientPhotoCod;

    /** 是否删除 */
    private String isDelete;
}