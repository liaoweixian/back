package xs.rfid.modules.mnt.service.mapper;

import xs.rfid.base.BaseMapper;
import xs.rfid.modules.mnt.domain.ServerDeploy;
import xs.rfid.modules.mnt.service.dto.ServerDeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServerDeployMapper extends BaseMapper<ServerDeployDto, ServerDeploy> {

}
