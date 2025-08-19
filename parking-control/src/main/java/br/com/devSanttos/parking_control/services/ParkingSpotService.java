package br.com.devSanttos.parking_control.services;

import br.com.devSanttos.parking_control.entities.ParkingSpotEntity;
import br.com.devSanttos.parking_control.repositories.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartment(String apartment) {
        return parkingSpotRepository.existsByApartment(apartment);
    }

    public List<ParkingSpotEntity> findAll() {
        return parkingSpotRepository.findAll();
    }
}
