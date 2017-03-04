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
import model.Flight;

/**
 *
 * @author Steven Kritikos
 */
@Stateless
public class FlightDAO {
  @EJB
  Database database;
  
  public CompletableFuture<Stream<Flight>> getFlights() {
    return CompletableFuture.supplyAsync( getAllFlightsSupplier() );
  }
  
  public CompletableFuture<Flight> getFlightById(int id) {
    return CompletableFuture.supplyAsync( getFlightByIdSupplier(id) );
  }
  
  public CompletableFuture<Boolean> insertFlight(Flight flight) {
    return CompletableFuture.supplyAsync( insertNewFlightSupplier(flight));
  }
  
  public CompletableFuture<Boolean> updateFlight(Flight flight) {
    return CompletableFuture.supplyAsync( updateFlightSupplier(flight) );
  }
  
  public CompletableFuture<Boolean> deleteFlight(Flight flight) {
    return CompletableFuture.supplyAsync( deleteFlightSupplier(flight) );
  }
  
  private Supplier<Stream<Flight>> getAllFlightsSupplier() {
    return new Supplier<Stream<Flight>>() {
      @Override
      public Stream<Flight> get() {
        try {
          return database.getFlights();
        } catch (Exception e) {
          e.printStackTrace();
          throw e;
        }
      }
    };
  }
  
  private Supplier<Flight> getFlightByIdSupplier(int id) {           
    return new Supplier<Flight>() {
      @Override
      public Flight get() {
        try {
          return database.getFlights()
                  .filter(f -> f.getFlightId() == id)
                  .findFirst()
                  .orElse(null);
        } catch (Exception e) {
          e.printStackTrace();
          throw e;
        }
      }
    };
  }
  
  private Supplier<Boolean> insertNewFlightSupplier(Flight flight) {
    return new Supplier() {
      @Override
      public Boolean get() {
        try {
          database.addFlight(flight);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
  
  private Supplier<Boolean> updateFlightSupplier(Flight flight) {
    return new Supplier<Boolean>() {
      @Override
      public Boolean get() {
        try {
          Flight flightRef = database.getFlights()
                  .filter(f -> f.getFlightId()== flight.getFlightId())
                  .findFirst()
                  .get();
          flightRef.setCompany(flight.getCompany());
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
  
  private Supplier<Boolean> deleteFlightSupplier(Flight flight) {
    return new Supplier<Boolean>() {
      @Override
      public Boolean get() {
        try {
          database.getFlightList().removeIf(f -> f.equals(flight));
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
}
