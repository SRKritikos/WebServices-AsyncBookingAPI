/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import model.Booking;
import model.Database;

/**
 *
 * @author Steven Kritikos
 */
@Stateless
public class BookingDAO {
  @EJB
  private Database database;
  
  public CompletableFuture<Stream<Booking>> getBookings() {
    return CompletableFuture.supplyAsync( getAllBookings() );
  }
  
  public CompletableFuture<Booking> getBookingById(int id) {
    return CompletableFuture.supplyAsync( getBookingByIdSupplier(id) );
  }
  
  public CompletableFuture<Boolean> insertBooking(Booking booking) {
    return CompletableFuture.supplyAsync( insertNewBookingSupplier(booking));
  }
  
  public CompletableFuture<Boolean> updateBooking(Booking booking) {
    return CompletableFuture.supplyAsync( updateBookingSupplier(booking) );
  }
  
  private Supplier<Stream<Booking>> getAllBookings() {
    return new Supplier<Stream<Booking>>() {
      @Override
      public Stream<Booking> get() {
        return database.getBookings();
      } 
    };
  }
  
  private Supplier<Booking> getBookingByIdSupplier(int id) {           
    return new Supplier<Booking>() {
      @Override
      public Booking get() {
        try {
          return database.getBookings()
                  .filter(b -> b.getBookingId() == id)
                  .findFirst()
                  .orElse(null);
        } catch (Exception e) {
          e.printStackTrace();
          throw e;
        }
      }
    };
  }
  
  private Supplier<Boolean> insertNewBookingSupplier(Booking booking) {
    return new Supplier() {
      @Override
      public Boolean get() {
        try {
          database.addBooking(booking);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
  
  private Supplier<Boolean> updateBookingSupplier(Booking booking) {
    return new Supplier<Boolean>() {
      @Override
      public Boolean get() {
        try {
          Booking bookingRef = database.getBookings()
                  .filter(b -> b.getBookingId() == booking.getBookingId())
                  .findFirst()
                  .orElse(null);
          bookingRef.setClient(booking.getClient());
          bookingRef.setFlight(booking.getFlight());
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
}
