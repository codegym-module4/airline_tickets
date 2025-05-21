package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.dto.RevenueByDateDto;
import com.codegym.airline_tickets.dto.RevenueByFlightDto;
import com.codegym.airline_tickets.dto.RevenueByFlightTypeDto;
import com.codegym.airline_tickets.dto.RevenueByUserDto;
import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Flight;
import com.codegym.airline_tickets.entity.User;
import com.codegym.airline_tickets.repository.BookingRepository;
import com.codegym.airline_tickets.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getAll() {
        return List.of();
    }

    @Override
    public void save(Booking s) {
        bookingRepository.save(s);
    }

    @Override
    public void update(long id, Booking s) {

    }

    @Override
    public void remove(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Booking findById(long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public List<Booking> findByName(String name) {
        return List.of();
    }

    @Override
    public List<RevenueByDateDto> getRevenueByDay(LocalDate start, LocalDate end) {
        List<RevenueByDateDto> result = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {

            RevenueByDateDto revenueByDateDto = new RevenueByDateDto();

            revenueByDateDto.setDate(date);
            BigInteger revenue = bookingRepository.getTotalRevenueByDate(date);
            revenueByDateDto.setRevenue(revenue);

            List<User> users = bookingRepository.getUserIdByDate(date);
            List<RevenueByUserDto> revenueByUser = new ArrayList<>();
            for (User user : users) {
                BigInteger revenueByUserIdAndDate = bookingRepository.getRevenueByUserIdAndDate(user.getId(), date);
                Integer numberOfTickets = bookingRepository.getNumberOfTicketsByUserIdAndDate(user.getId(), date);
                revenueByUser.add(new RevenueByUserDto(user, numberOfTickets, date, revenueByUserIdAndDate));
            }
            revenueByDateDto.setRevenueByUser(revenueByUser);


            List<Object[]> flightPairs = bookingRepository.getFlightAndReturnFlightPairsByDate(date);

            List<RevenueByFlightDto> revenueByFlight = new ArrayList<>();

            for (Object[] pair : flightPairs) {
                Flight flight = (Flight) pair[0];
                Flight returnFlight = (Flight) pair[1];

                BigInteger revenueByFlightAndReturnFlightAndDate = bookingRepository.getRevenueByFlightAndReturnFlightAndDate(
                        flight.getId(),
                        returnFlight != null ? returnFlight.getId() : null,
                        date
                );

                revenueByFlight.add(new RevenueByFlightDto(flight, returnFlight, revenueByFlightAndReturnFlightAndDate));
            }
            revenueByDateDto.setRevenueByFlight(revenueByFlight);


            List<Integer> flight_types = bookingRepository.getFlightTypeByDate(date);
            List<RevenueByFlightTypeDto> revenueByFlightType = new ArrayList<>();
            for (Integer flight_type : flight_types) {
                BigInteger revenueByFlightTypeIdAndDate = bookingRepository.getRevenueByFlightTypeIdAndDate(flight_type, date);
                revenueByFlightType.add(new RevenueByFlightTypeDto(flight_type, revenueByFlightTypeIdAndDate));
            }
            revenueByDateDto.setRevenueByFlightType(revenueByFlightType);

            result.add(revenueByDateDto);
        }
        return result;
    }

    @Override
    public List<Booking> findByStatusAndUserId(int status, long userId) {
        return bookingRepository.findByStatusAndUserId(status, userId);
    }

    @Override
    public List<Booking> findByUserId(long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> findByIdIn(List<Long> ids) {
        return bookingRepository.findByIdIn(ids);
    }

    @Override
    public void updateVnPayOrderId(Long id, String vnPayOrderId) {
        bookingRepository.updateVnPayOrderId(id, vnPayOrderId);
    }

    @Override
    public void updateStatusById(Long id, Integer status) {
        bookingRepository.updateStatusById(id, status);
    }

    @Override
    public void updateStatusAndPaymentDateByVnPayId(String vnpayOrderId, Integer status, LocalDateTime date) {
        bookingRepository.updateStatusAndPaymentDateByVnPayId(vnpayOrderId, status, date);
    }

    @Override
    public Booking findLatest() {
        return bookingRepository.findLatest();
    }

    @Override
    public Booking updateOrCreate(Booking b) {
        Long id = b.getId();
        if (id == null) {
            Booking latest = findLatest();
            long number = latest.getId() == null ? 0 : latest.getId();
            String code = generateNextCode(number);
            b.setCode(code);
        }
        return bookingRepository.save(b);
    }

    @Override
    public List<Booking> findByCreatedAtLessThanEqual(LocalDateTime time, Integer status) {
        return bookingRepository.findByCreatedAtLessThanEqualAndStatus(time, status);
    }

    @Override
    public List<Booking> getBookingByFlightDate(LocalDate date) {
        return bookingRepository.getBookingByFlightDate(date);
    }

    private static String generateNextCode(long number) {
        if (number == 0) {
            return "BK001";
        }
        number++;
        return String.format("BK%03d", number);
    }
}