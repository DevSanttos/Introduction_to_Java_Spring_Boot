package br.com.devSanttos.parking_control.controllers;

import br.com.devSanttos.parking_control.dtos.ParkingSpotDto;
import br.com.devSanttos.parking_control.entities.ParkingSpotEntity;
import br.com.devSanttos.parking_control.services.ParkingSpotService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;

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
        parkingSpotEntity.setRegistrationDate(java.time.LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.create(parkingSpotEntity));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpotEntity>> getAllParkingSpots() {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
    }


}
