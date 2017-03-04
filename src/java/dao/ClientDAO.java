package dao;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import model.Client;
import model.Database;

/**
 *
 * @author Steven Kritikos
 */
@Stateless
public class ClientDAO {
  @EJB
  Database database;
  
  public CompletableFuture<Stream<Client>> getClients() {
    return CompletableFuture.supplyAsync( getAllClientsSupplier() );
  }
  
  public CompletableFuture<Client> getCliendById(int id) {
    return CompletableFuture.supplyAsync( getCliendByIdSupplier(id) );
  }
  
  public CompletableFuture<Boolean> insertClient(Client client) {
    return CompletableFuture.supplyAsync( insertNewClientSupplier(client) );
  }
  
  public CompletableFuture<Boolean> updateClient(Client client) {
    return CompletableFuture.supplyAsync( updateClientSupplier(client) );
  }
  
  private Supplier<Stream<Client>> getAllClientsSupplier() {
    return new Supplier<Stream<Client>>() {
      @Override
      public Stream<Client> get() {
        try {
          return database.getClients();
        } catch (Exception e) {
          e.printStackTrace();
          throw e;
        }
      }
    };
  }
  
  private Supplier<Client> getCliendByIdSupplier(int id) {           
    return new Supplier<Client>() {
      @Override
      public Client get() {
        try {
          return database.getClients()
                  .filter(client -> client.getCliendId() == id)
                  .findFirst()
                  .orElse(null);
        } catch (Exception e) {
          e.printStackTrace();
          throw e;
        }
      }
    };
  }
  
  private Supplier<Boolean> insertNewClientSupplier(Client client) {
    return new Supplier() {
      @Override
      public Boolean get() {
        try {
          database.addClient(client);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
  
  private Supplier<Boolean> updateClientSupplier(Client client) {
    return new Supplier<Boolean>() {
      @Override
      public Boolean get() {
        try {
          Client clientRef = database.getClients()
                  .filter(c -> c.getCliendId() == client.getCliendId())
                  .findFirst()
                  .get();
          clientRef.setClientName(client.getClientName());
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
  }
  
}
