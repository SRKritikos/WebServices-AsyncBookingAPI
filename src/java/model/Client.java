package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Steven Kritikos
 */
public class Client {
  private String clientName;
  private int cliendId;
  private static int ids = 0;

  public Client(String clientName) {
    this.clientName = clientName;
    this.cliendId = ++ids;
  }

  public Client(Client client) {
    this.clientName = client.clientName;
    this.cliendId = ++ids;
  }
  
  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public int getCliendId() {
    return cliendId;
  }

  public void setCliendId(int cliendId) {
    this.cliendId = cliendId;
  }

  public int getIds() {
    return ids;
  }
}
