package xs.rfid.modules.stock.service.mapper;

import xs.rfid.base.BaseMapper;
import xs.rfid.modules.stock.domain.RfidAreaSetting;
import xs.rfid.modules.stock.service.dto.RfidAreaSettingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author liao
* @date 2020-05-10
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RfidAreaSettingMapper extends BaseMapper<RfidAreaSettingDto, RfidAreaSetting> {

}