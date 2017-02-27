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
public class Flight {
  private static int ids;
  private String company;
  private int flightId;
  
  public Flight(String company) {
    this.company = company;
    this.flightId = ++ids;
  }

  public Flight(Flight flight) {
    this.company = flight.company;
    this.flightId = ++ids;
  }
  
  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public int getFlightId() {
    return flightId;
  }

  public void setFlightId(int flightId) {
    this.flightId = flightId;
  }

  public static int getIds() {
    return ids;
  }
  
}
