/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author srostantkritikos06
 */
public class Booking {
 
  private static int ids;
  private int bookingId;
  private Client client;
  private Flight flight;

  public Booking(Client client, Flight flight) {
    this.client = client;
    this.flight = flight;
    this.bookingId = ++ids;
  }

  public Booking(Booking booking) {
    this.client = booking.client;
    this.flight = booking.flight;
    ids++;
  }

  
  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
  }

  public int getBookingId() {
    return bookingId;
  }

  public void setBookingId(int bookingId) {
    this.bookingId = bookingId;
  }

  public static int getIds() {
    return ids;
  } 
}
