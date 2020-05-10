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
* @date 2020-05-06
**/
@Entity
@Data
@Table(name="rfid_gift_mst")
public class RfidGiftMst implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gift_cod")
    @ApiModelProperty(value = "礼品编号")
    private String giftCod;

    @Column(name = "gift_name")
    @ApiModelProperty(value = "礼品名称")
    private String giftName;

    @Column(name = "gift_type")
    @ApiModelProperty(value = "礼品类型")
    private String giftType;

    @Column(name = "gift_model")
    @ApiModelProperty(value = "型号")
    private String giftModel;

    @Column(name = "to_location_cod")
    @ApiModelProperty(value = "推荐货位")
    private String toLocationCod;

    @Column(name = "is_use")
    @ApiModelProperty(value = "停用标志")
    private String isUse;

    @Column(name = "rfid_cod")
    @ApiModelProperty(value = "RFID编码")
    private String rfidCod;

    @Column(name = "register_dat")
    @ApiModelProperty(value = "registerDat")
    private String registerDat;

    @Column(name = "visit_sta")
    @ApiModelProperty(value = "到访状态")
    private String visitSta;

    @Column(name = "is_delete")
    @ApiModelProperty(value = "是否删除")
    private String isDelete;

    @Column(name = "img_url")
    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    /**
     *  被下单 状态变更为1
     */
    @Column(name = "is_bind")
    private Integer isBind;

    @Transient
    private Integer inventoryCnt;

    public void copy(RfidGiftMst source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}