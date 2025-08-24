package br.com.devSanttos.parking_control.controllers;

import br.com.devSanttos.parking_control.dtos.ParkingSpotDto;
import br.com.devSanttos.parking_control.entities.ParkingSpotEntity;
import br.com.devSanttos.parking_control.services.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> createParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A placa do carro já está cadastrada!");
        }

        if (parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("O número da vaga já está cadastrado!");
        }

        if (parkingSpotService.existsByApartment(parkingSpotDto.getApartment())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe uma vaga cadastrada para este apartamento!");
        }

        var parkingSpotEntity = new ParkingSpotEntity();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotEntity);
        parkingSpotEntity.setRegistrationDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.create(parkingSpotEntity));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpotEntity>> getAllParkingSpots() {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotEntity> parkingSpotEntityOptional = parkingSpotService.findById(id);
        if(!parkingSpotEntityOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotEntityOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        Optional<ParkingSpotEntity> parkingSpotEntityOptional = parkingSpotService.findById(id);
        if(!parkingSpotEntityOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot não encontrado!");
        }

        var parkingSpotEntity = new ParkingSpotEntity();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotEntity);
        parkingSpotEntity.setId(parkingSpotEntityOptional.get().getId());
        parkingSpotEntity.setRegistrationDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.create(parkingSpotEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotEntity> parkingSpotEntityOptional = parkingSpotService.findById(id);
        if(!parkingSpotEntityOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot inexistente!");
        }
        parkingSpotService.delete(parkingSpotEntityOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking spot removido com sucesso!");
    }
}
