package xs.rfid.modules.mnt.service.mapper;

import xs.rfid.base.BaseMapper;
import xs.rfid.modules.mnt.domain.Database;
import xs.rfid.modules.mnt.service.dto.DatabaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseMapper extends BaseMapper<DatabaseDto, Database> {

}
