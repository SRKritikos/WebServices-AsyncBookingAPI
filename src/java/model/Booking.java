/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author srostantkritikos06
 */
public class Booking {
 
  private static int ids;
  private int bookingId;
  private Client client;
  private Hotel hotel;
  private Date bookingDate;

  public Booking(Client client, Hotel hotel, Date bookingDate) {
    this.client = client;
    this.hotel = hotel;
    this.bookingDate = bookingDate;
    this.bookingId = ++ids;
  }

  public Booking(Booking booking) {
    this.client = booking.client;
    this.hotel = booking.hotel;
    this.bookingId = ++ids;
    this.bookingDate = booking.getBookingDate();
  }

  
  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Hotel getHotel() {
    return hotel;
  }

  public void setHotel(Hotel hotel) {
    this.hotel = hotel;
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

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Booking) {
            return ((Booking)o).getBookingId()== this.bookingId;
        }
        return false;
    }
}
