/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import com.google.gson.Gson;
import dao.ClientDAO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Client;
import org.glassfish.jersey.server.ManagedAsync;
import service.BookingService;

/**
 * REST Web Service
 *
 * @author Steven
 */
@Path("client")
public class ClientAPI {

  @EJB
  private ClientDAO clientDAO;
  @EJB
  private BookingService bookingService;
  
  @GET
  @ManagedAsync
  @Produces(MediaType.APPLICATION_JSON)
  public void getClients(@Suspended final AsyncResponse asyncResponse) {
    Gson gson = new Gson();
    clientDAO.getClients().thenApply(clientStream -> asyncResponse.resume(
      Response.ok()
              .entity(gson.toJson( clientStream.collect(Collectors.toList())))
              .build()
    ))
    .exceptionally(ex -> asyncResponse.resume(
      Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .entity(ex)
              .build()
    ));
  }
  
  @GET
  @ManagedAsync
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON) 
  public void getClientById(@Suspended final AsyncResponse asyncResponse, @PathParam("id") int id ) {
    Gson gson = new Gson();
    clientDAO.getCliendById(id).thenApply(client -> {
      Response response;
      if (client != null) {
        response = Response.ok().entity( gson.toJson(client)).build();
      } else {
        response = Response.status(Response.Status.NOT_FOUND).build();
      }
      return asyncResponse.resume(response);
    })
    .exceptionally(ex -> asyncResponse.resume(
      Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .entity(ex)
              .build()
    ));
  }

  @POST
  @ManagedAsync
  @Consumes(MediaType.APPLICATION_JSON)
  public void createClient(@Suspended final AsyncResponse asyncResponse, String newClient) {
    Gson gson = new Gson();
    Client client =  new Client( gson.fromJson(newClient, Client.class) );
    clientDAO.insertClient(client).thenApply(success -> {
      Response response;
      if (success) {
        try {
          response = Response.created(new URI("/api/client/" + client.getCliendId())).build();
        } catch (URISyntaxException ex) {
          ex.printStackTrace();
          response = Response.status(Response.Status.NOT_FOUND).build();
        }
      } else {
        response = Response.serverError().build();
      }
      return asyncResponse.resume(response);
    })
    .exceptionally(ex -> asyncResponse.resume(
      Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .entity(ex)
              .build()
    ));
  }
  
  @PUT
  @ManagedAsync
  @Consumes(MediaType.APPLICATION_JSON)
  public void updateClient(@Suspended final AsyncResponse asyncResponse, String updateClient) {
    Gson gson = new Gson();
    Client client = gson.fromJson(updateClient, Client.class);
    clientDAO.updateClient(client).thenApply(success -> { 
      Response response;
      if (success) {
        response = Response.status(Response.Status.NO_CONTENT).build();
      } else {
        response = Response.status(Response.Status.NOT_FOUND).build();
      }
      return asyncResponse.resume(response);
    })
    .exceptionally(ex -> asyncResponse.resume(
      Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .entity(ex)
              .build()
    ));
  }
  
  @DELETE
  @ManagedAsync
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void deleteClient(@Suspended final AsyncResponse asyncResponse, @PathParam("id") int id) {
    clientDAO.getCliendById(id).thenApply(client -> clientDAO.deleteClient(client).thenApply(success -> {
        Response response;
        if (success) {
          response = Response.status(Response.Status.OK).build();
        } else {
          response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return asyncResponse.resume(response);
      })
      .exceptionally(ex -> asyncResponse.resume(
        Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ex)
                .build()
      ))
    );
  }
  
  @GET
  @ManagedAsync
  @Path("{id}/hotels")
  public void getHotelsForClient(@Suspended final AsyncResponse asyncResponse, @PathParam("id") int clientId) {
    Gson gson = new Gson();
    bookingService.getHotelsForClient(clientId).thenApply(hotelStream ->
      asyncResponse.resume(
        Response.ok()
                .entity( gson.toJson( hotelStream.collect(Collectors.toList() )))
                .build()
      )
    )
    .exceptionally(ex -> asyncResponse.resume(
        Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ex)
                .build()
    ));
  }
}
