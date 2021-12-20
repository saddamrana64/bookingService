package com.paypal.bfs.bookingserv.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.paypal.bfs.bookingserv.api.model.Address;
import com.paypal.bfs.bookingserv.api.model.Booking;
import com.paypal.bfs.bookingserv.repository.impl.BookingRepository;
import com.paypal.bfs.test.bookingserv.api.BookingResource;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingResourceImpl implements BookingResource {

	@Autowired
	private BookingRepository repository;

	@Override
	public ResponseEntity<Booking> create(Booking booking) {
		try {
			Optional<Booking> existingbooking = repository.findById(booking.getId());
			if (existingbooking.isPresent() || validateBookings(booking)) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			Booking createdBooking = repository.save(booking);
			return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<List<Booking>> getAllBookings() {
		List<Booking> bookings = repository.findAll();
		return new ResponseEntity<>(bookings, HttpStatus.OK);
	}

	public boolean validateBookings(Booking booking) {
		if(booking.getId() == 0 || 
				StringUtils.isEmpty(booking.getFirstName()) || 
				StringUtils.isEmpty(booking.getLastName()) || 
				StringUtils.isEmpty(booking.getDateOfBirth()) || 
				booking.getCheckinDatetime() == null ||
				booking.getCheckoutDatetime() == null || 
				booking.getTotalPrice() == null || 
				booking.getDeposit() == null ||
				validateAddress(booking.getAddress())) return true;
		return false;
	}

	public boolean validateAddress(Address address) {
		if(StringUtils.isEmpty(address.getLine1()) ||
				StringUtils.isEmpty(address.getCity()) ||
				StringUtils.isEmpty(address.getState()) || 
				StringUtils.isEmpty(address.getState()) ||
				address.getZipCode() == null) return true;
		return false;
	}

}
