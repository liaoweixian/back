package xs.rfid.modules.monitor.service.mapper;

import xs.rfid.base.BaseMapper;
import xs.rfid.modules.monitor.domain.Server;
import xs.rfid.modules.monitor.service.dto.ServerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zhang houying
* @date 2019-11-03
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServerMapper extends BaseMapper<ServerDTO, Server> {

}
