package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        try{
            User user = userRepository3.findById(userId).get();
            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setUser(user);
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            List<Spot> spots = parkingLot.getSpotList();
            Spot minRateSpot = null;
            int minRate = Integer.MAX_VALUE;
            SpotType spt;
            if (numberOfWheels == 2) {
                spt = SpotType.TWO_WHEELER;
            } else if (numberOfWheels == 4) {
                spt = SpotType.FOUR_WHEELER;
            } else {
                spt = SpotType.OTHERS;
            }
            for (Spot spot : spots) {
                if (spot.getSpotType() == spt && spot.getPricePerHour() < minRate) {
                    minRate = spot.getPricePerHour();
                    minRateSpot = spot;
                }
            }
            assert minRateSpot != null;
            minRateSpot.getReservationList().add(reservation);
            minRateSpot.setOccupied(true);
            reservationRepository3.save(reservation);
            return reservation;
        }
        catch(Exception e){
            throw new Exception("Cannot make reservation");
        }
    }
}
