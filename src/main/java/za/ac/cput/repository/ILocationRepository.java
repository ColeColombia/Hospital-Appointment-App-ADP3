/* IAppointmentRepository.java
 Interface for the Appointment Repository Class
 Author: Dominic Dave Przygonski (219206414)
 Date: 09 April 2022
*/

package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Location;

@Repository
public interface ILocationRepository extends JpaRepository<Location, String> {

    public Location findLocationByPostalCodeContaining(int postalCode);

}