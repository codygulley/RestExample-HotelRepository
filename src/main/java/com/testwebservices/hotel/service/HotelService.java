package com.testwebservices.hotel.service;

import com.testwebservices.hotel.domain.Hotel;
import com.testwebservices.hotel.dao.jpa.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/*
 * Sample service to demonstrate what the API would use to get things done
 */
@Service
public class HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    GaugeService gaugeService;

    public HotelService() {
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel getHotel(long id) {
        return hotelRepository.findOne(id);
    }

    public void updateHotel(Hotel hotel, Long id) {
        hotel.setId(id);
        hotelRepository.save(hotel);
    }

    public void updateHotelItem(Hotel hotel, Long id) {
        hotel.setId(id);

        if(hotel.getName() == null){
            hotel.setName(hotelRepository.findOne(id).getName());
        }
        if(hotel.getDescription() == null){
            hotel.setDescription(hotelRepository.findOne(id).getDescription());
        }
        if(hotel.getCity() == null){
            hotel.setCity(hotelRepository.findOne(id).getCity());
        }
        if(hotel.getRating() == 0){
            hotel.setRating(hotelRepository.findOne(id).getRating());
        }
        hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.delete(id);
    }

    //http://goo.gl/7fxvVf
    public Page<Hotel> getAllHotels(Integer page, Integer size) {
        Page pageOfHotels = hotelRepository.findAll(new PageRequest(page, size));
        // example of adding to the /metrics
        if (size > 50) {
            counterService.increment("testwebservices.hotel.HotelService.getAll.largePayload");
        }
        return pageOfHotels;
    }
}
