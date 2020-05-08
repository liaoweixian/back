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
package xs.rfid.modules.stock.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-07
**/
@Entity
@Data
@Table(name="rfid_vdr_mst")
public class RfidVdrMst implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;


    @Column(name = "client_cod")
    @ApiModelProperty(value = "客户编号")
    private String clientCod;

    @Column(name = "client_name")
    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @Column(name = "company_name")
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @Column(name = "position_name")
    @ApiModelProperty(value = "职务")
    private String positionName;

    @Column(name = "rfid_cod")
    @ApiModelProperty(value = "RFID编码")
    private String rfidCod;

    @Column(name = "visit_dat")
    @ApiModelProperty(value = "到访时间")
    private String visitDat;


    @Column(name = "visit_status")
    @ApiModelProperty(value = "访客状态1、预约， 2、到访， 3、已完成")
    private String visitStatus;

    @Column(name = "client_photo_cod")
    @ApiModelProperty(value = "客户照片编码")
    private String clientPhotoCod;

    @Column(name = "is_delete")
    @ApiModelProperty(value = "是否删除")
    private String isDelete;

    public void copy(RfidVdrMst source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}