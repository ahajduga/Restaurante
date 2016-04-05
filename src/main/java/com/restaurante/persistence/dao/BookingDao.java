package com.restaurante.persistence.dao;

import com.restaurante.persistence.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by alex on 03/05/2015.
 */
@Repository
public interface BookingDao extends CrudRepository<Booking, Long> {

}
