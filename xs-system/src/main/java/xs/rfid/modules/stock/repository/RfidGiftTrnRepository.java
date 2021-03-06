package xs.rfid.modules.stock.repository;

import xs.rfid.modules.stock.domain.RfidGiftTrn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
* @author liao
* @date 2020-05-08
*/
public interface RfidGiftTrnRepository extends JpaRepository<RfidGiftTrn, Long>, JpaSpecificationExecutor<RfidGiftTrn> {

    public RfidGiftTrn findByGiftId(Long giftId);

    public List<RfidGiftTrn> findByStatusIn(List<String> status);
}