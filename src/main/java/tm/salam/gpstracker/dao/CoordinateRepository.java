package tm.salam.gpstracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.gpstracker.models.Coordinate;

import java.util.Date;
import java.util.List;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate,Integer> {

    List<Coordinate>findCoordinatesByOrderCard(String orderCard);
    List<Coordinate>findCoordinatesByOrderCardAndCreationDate(String orderCard, Date date);
    List<Coordinate>findCoordinatesByOrderCardAndCreationDateBetween(String orderCard, Date begin, Date end);
}
