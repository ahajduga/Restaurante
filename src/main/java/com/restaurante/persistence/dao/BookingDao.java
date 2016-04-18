package com.restaurante.persistence.dao;

import com.restaurante.persistence.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by alex on 03/05/2015.
 */
@Repository
public interface BookingDao extends CrudRepository<Booking, Long> {


    List<Booking> findByActive(boolean active);
}
