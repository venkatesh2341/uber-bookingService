package com.uber.booking_service.Repositories;

import com.uber_project.entity_provider.Models.Booking;
import com.uber_project.entity_provider.Models.BookingStatus;
import com.uber_project.entity_provider.Models.Driver;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Booking b SET b.bookingStatus = :status , b.driver = :driver WHERE b.id = :id")
    void updateBookingStatusAndDriverById(@Param("status") BookingStatus status, @Param("driver")Driver driver, @Param("id") Long id);
}
