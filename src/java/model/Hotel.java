/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author srostantkritikos06
 */
public class Hotel {
  private static int ids;
  private String company;
  private int hotelId;
  
  public Hotel(String company) {
    this.company = company;
    this.hotelId = ++ids;
  }

  public Hotel(Hotel hotel) {
    this.company = hotel.company;
    this.hotelId = ++ids;
  }
  
  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public int getHotelId() {
    return hotelId;
  }

  public void setHotelId(int hotelId) {
    this.hotelId = hotelId;
  }

  public static int getIds() {
    return ids;
  }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Hotel) {
            return ((Hotel)o).getHotelId() == this.hotelId;
        }
        return false;
    }
  
}
