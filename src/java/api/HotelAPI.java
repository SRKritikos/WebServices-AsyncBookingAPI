/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package api;

import com.google.gson.Gson;
import dao.HotelDAO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Hotel;
import org.glassfish.jersey.server.ManagedAsync;
import service.BookingService;

/**
 *
 * @author Steven Kritikos
 */
@Path("hotel")
public class HotelAPI {
  @EJB
  private HotelDAO hotelDAO;
  @EJB
  private BookingService bookingService; 
  
  @GET
  @ManagedAsync
  @Produces(MediaType.APPLICATION_JSON)
  public void getHotels(@Suspended final AsyncResponse asyncResponse) {
    Gson gson = new Gson();
    hotelDAO.getHotels().thenApply(clientStream -> asyncResponse.resume(
      Response.ok()
              .entity(gson.toJson( clientStream.collect(Collectors.toList()) ))
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
  public void getHotelById(@Suspended final AsyncResponse asyncResponse, @PathParam("id") int id ) {
    Gson gson = new Gson();
    hotelDAO.getHotelById(id).thenApply(client -> {
      Response response;
      if (client != null) {
        response = Response.ok().entity(gson.toJson(client)).build();
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
  public void createHotel(@Suspended final AsyncResponse asyncResponse, String newHotel) {
    Gson gson = new Gson();
    Hotel hotel =  new Hotel( gson.fromJson(newHotel, Hotel.class) );
    hotelDAO.insertHotel(hotel).thenApply(success -> {
      Response response;
      if (success) {
        try {
          response = Response.created(new URI("/api/client/" + hotel.getHotelId())).build();
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
  public void updateHotel(@Suspended final AsyncResponse asyncResponse, String updateHotel) {
    Gson gson = new Gson();
    Hotel hotel = gson.fromJson(updateHotel, Hotel.class);
    hotelDAO.updateHotel(hotel).thenApply(success -> { 
      Response response;
      if (success) {
        response = Response.status(Response.Status.NO_CONTENT).build();
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
  
  @DELETE
  @ManagedAsync
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void deleteHotel(@Suspended final AsyncResponse asyncResponse, @PathParam("id") int id) {
    hotelDAO.getHotelById(id).thenApply(hotel -> hotelDAO.deleteHotel(hotel).thenApply(success -> {
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
  @Path("{id}/clients")
  public void getClientsForHotel(@Suspended final AsyncResponse asyncResponse,
                                 @PathParam("id") int hotelId) throws InterruptedException, ExecutionException {
    Gson gson = new Gson();
    bookingService.getClientsForHotel(hotelId).thenApply(clientStream ->
      asyncResponse.resume( 
        Response.ok()
                .entity( gson.toJson( clientStream.collect(Collectors.toList())) )
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
