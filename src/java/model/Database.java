/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import javax.ejb.Singleton;

/**
 *
 * @author srostantkritikos06
 */
@Singleton
public class Database {
  private List<Client> clientList;
  private List<Hotel> hotelList;
  private List<Booking> bookingsList;

  public Database() {
    initClients();
    initHotels();
    initBookings();
  }
  
  private void initClients() {
    clientList = new ArrayList<>();
    for (int i = 0; i < 3000; i++) {
      clientList.add(new Client("Client " + i));
    }
  }
  
  private void initHotels() {
    this.hotelList = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      hotelList.add(new Hotel("Company " + i));
    } 
  }
  
  private void initBookings() {
    bookingsList = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      bookingsList.add(new Booking(clientList.get(i), hotelList.get(i)));
    }
  }
  
  public void addClient(Client client) {
    this.clientList.add(client);
  }
  
  public void addHotel(Hotel hotel) {
    this.hotelList.add(hotel);
  }
  
  public void addBooking(Booking booking) {
    this.bookingsList.add(booking);
  }

  public List<Client> getClientList() {
    return clientList;
  }

  public void setClientList(List<Client> clientList) {
    this.clientList = clientList;
  }

  public List<Hotel> getHotelList() {
    return hotelList;
  }

  public void setHotelList(List<Hotel> hotelList) {
    this.hotelList = hotelList;
  }

  public List<Booking> getBookingsList() {
    return bookingsList;
  }

  public void setBookingsList(List<Booking> bookingsList) {
    this.bookingsList = bookingsList;
  }
  
  public Stream<Client> getClients() {
    return this.clientList.stream();
  }
  
  public Stream<Hotel> getHotels() {
    return this.hotelList.stream();
  }
    
  public Stream<Booking> getBookings() {
    return this.bookingsList.stream();
  }
}
