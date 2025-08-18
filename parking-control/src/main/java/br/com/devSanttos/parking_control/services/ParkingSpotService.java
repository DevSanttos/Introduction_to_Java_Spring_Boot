package br.com.devSanttos.parking_control.services;

import br.com.devSanttos.parking_control.entities.ParkingSpotEntity;
import br.com.devSanttos.parking_control.repositories.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpotService {

    final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpotEntity create(ParkingSpotEntity parkingSpotEntity) {
        return parkingSpotRepository.save(parkingSpotEntity);
    }
}
