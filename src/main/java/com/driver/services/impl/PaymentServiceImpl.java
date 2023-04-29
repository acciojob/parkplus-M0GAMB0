package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation=reservationRepository2.findById(reservationId).get();
        Payment payment=new Payment();
        payment.setReservation(reservation);
        payment.setPaymentMode(PaymentMode.valueOf(mode.toUpperCase()));
        reservation.setPayment(payment);

        //bill calculation
        int noOfHours=reservation.getNumberOfHours();
        int bill=reservation.getSpot().getPricePerHour()*noOfHours;

        //Check payment
        if(amountSent<bill){
            throw new Exception("Insufficient Amount");
        }
        return payment;
    }
}
