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
import model.Database;
import model.Hotel;

/**
 *
 * @author Steven Kritikos
 */
@Stateless
public class HotelDAO {
  @EJB
  Database database;
  
  public CompletableFuture<Stream<Hotel>> getHotels() {
    return CompletableFuture.supplyAsync( getAllHotelsSupplier() );
  }
  
  public CompletableFuture<Hotel> getHotelById(int id) {
    return CompletableFuture.supplyAsync( getHotelByIdSupplier(id) );
  }
  
  public CompletableFuture<Boolean> insertHotel(Hotel hotel) {
    return CompletableFuture.supplyAsync( insertNewHotelSupplier(hotel));
  }
  
  public CompletableFuture<Boolean> updateHotel(Hotel hotel) {
    return CompletableFuture.supplyAsync( updateHotelSupplier(hotel) );
  }
  
  public CompletableFuture<Boolean> deleteHotel(Hotel hotel) {
    return CompletableFuture.supplyAsync( deleteHotelSupplier(hotel) );
  }
  
  private Supplier<Stream<Hotel>> getAllHotelsSupplier() {
    return new Supplier<Stream<Hotel>>() {
      @Override
      public Stream<Hotel> get() {
        try {
          return database.getHotels();
        } catch (Exception e) {
          e.printStackTrace();
          throw e;
        }
      }
    };
  }
  
  private Supplier<Hotel> getHotelByIdSupplier(int id) {           
    return new Supplier<Hotel>() {
      @Override
      public Hotel get() {
        try {
          return database.getHotels()
                  .filter(f -> f.getHotelId() == id)
                  .findFirst()
                  .orElse(null);
        } catch (Exception e) {
          e.printStackTrace();
          throw e;
        }
      }
    };
  }
  
  private Supplier<Boolean> insertNewHotelSupplier(Hotel hotel) {
    return new Supplier() {
      @Override
      public Boolean get() {
        try {
          database.addHotel(hotel);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
  
  private Supplier<Boolean> updateHotelSupplier(Hotel hotel) {
    return new Supplier<Boolean>() {
      @Override
      public Boolean get() {
        try {
          Hotel hotelRef = database.getHotels()
                  .filter(f -> f.getHotelId()== hotel.getHotelId())
                  .findFirst()
                  .get();
          hotelRef.setCompany(hotel.getCompany());
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
  
  private Supplier<Boolean> deleteHotelSupplier(Hotel hotel) {
    return new Supplier<Boolean>() {
      @Override
      public Boolean get() {
        try {
          database.getHotelList().removeIf(f -> f.equals(hotel));
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
}
