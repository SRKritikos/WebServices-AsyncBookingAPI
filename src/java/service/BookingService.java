/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import com.sun.xml.internal.ws.util.CompletedFuture;
import dao.BookingDAO;
import dao.ClientDAO;
import dao.HotelDAO;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import model.Booking;
import model.Client;
import model.Hotel;

/**
 *
 * @author Steven Kritikos
 */
@Stateless
public class BookingService {
  @EJB
  private ClientDAO clientDAO;
  @EJB
  private HotelDAO hotelDAO;
  @EJB
  private BookingDAO bookingDAO;
  
  public CompletableFuture<Stream<Client>> getClientsForHotel(int hotelId) {
    CompletableFuture<Stream<Booking>> bookingsFuture = this.bookingDAO.getBookings();
    CompletableFuture<Hotel> hotelFuture = this.hotelDAO.getHotelById(hotelId);
    return bookingsFuture.thenCombine(hotelFuture, (bookings, hotel) ->
            bookings.filter(booking -> booking.getHotel().equals(hotel))
                    .map(b -> b.getClient()));
  }
  
  public CompletableFuture<Stream<Hotel>> getHotelsForClient(int clientId) {
    CompletableFuture<Stream<Booking>> bookingsFuture = this.bookingDAO.getBookings();
    CompletableFuture<Client> clientFuture = this.clientDAO.getCliendById(clientId);
    return bookingsFuture.thenCombine(clientFuture, (bookings, client) -> 
            bookings.filter(booking -> booking.getClient().equals(client))
                    .map(b -> b.getHotel()));
  }

    public CompletableFuture<Stream<Booking>> getBookingsForDate(Date startDate, Date endDate) {
        CompletableFuture<Stream<Booking>> bookingsFuture = this.bookingDAO.getBookings();
        return bookingsFuture.thenApply(bookingStream -> 
                bookingStream.filter(booking -> booking.getBookingDate().after(startDate)
                                  && booking.getBookingDate().before(endDate)));
    }
}
