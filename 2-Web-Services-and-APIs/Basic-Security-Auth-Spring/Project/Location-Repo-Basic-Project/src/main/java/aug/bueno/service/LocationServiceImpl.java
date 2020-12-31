package aug.bueno.service;

import aug.bueno.entity.Location;
import aug.bueno.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    LocationRepository locationRepository;

    @Override
    public List<Location> getLocations() {
        return (List<Location>) locationRepository.findAll();
    }
}
