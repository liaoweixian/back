package xs.rfid.modules.stock.service.mapper;

import xs.rfid.base.BaseMapper;
import xs.rfid.modules.stock.domain.RfidGiftTrn;
import xs.rfid.modules.stock.service.dto.RfidGiftTrnDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author liao
* @date 2020-05-08
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RfidGiftTrnMapper extends BaseMapper<RfidGiftTrnDto, RfidGiftTrn> {

}