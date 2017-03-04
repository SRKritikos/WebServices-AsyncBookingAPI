/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import com.google.gson.Gson;
import dao.BookingDAO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Booking;
import org.glassfish.jersey.server.ManagedAsync;

/**
 * REST Web Service
 *
 * @author Steven
 */
@Path("bookings")
public class BookingAPI {
  @EJB
  private BookingDAO bookingDAO;
  
  @GET
  @ManagedAsync
  @Produces(MediaType.APPLICATION_JSON)
  public void getBookings(@Suspended final AsyncResponse asyncResponse) {
    Gson gson = new Gson();
    bookingDAO.getBookings()
      .thenApply(bookingsStream -> asyncResponse.resume(
          Response.ok()
                  .entity(gson.toJson( bookingsStream.collect(Collectors.toList()) ))
                  .build())
      )
      .exceptionally(ex -> asyncResponse.resume(
              Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build())
      ); 
  }
  
  @GET
  @ManagedAsync
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON) 
  public void getBookingById(@Suspended final AsyncResponse asyncResponse, @PathParam("id") int id ) {
    Gson gson = new Gson();
    bookingDAO.getBookingById(id).thenApply(booking -> asyncResponse.resume(
      Response.ok()
              .entity(gson.toJson(booking))
              .build()
    ))
    .exceptionally(ex -> asyncResponse.resume(
      Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .entity(ex)
              .build()
    ));
  }
  
  @POST
  @ManagedAsync
  @Consumes(MediaType.APPLICATION_JSON)
  public void createBooking(@Suspended final AsyncResponse asyncResponse, String newBooking) {
    Gson gson = new Gson();
    Booking booking =  new Booking( gson.fromJson(newBooking, Booking.class) );
    bookingDAO.insertBooking(booking).thenApply(success -> {
      Response response;
      if (success) {
        try {
          response = Response.created(new URI("/api/bookings/" + booking.getBookingId())).build();
        } catch (URISyntaxException ex) {
          ex.printStackTrace();
          response = Response.status(Response.Status.CREATED).build();
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
  public void updateBooking(@Suspended final AsyncResponse asyncResponse, String updateBooking) {
    Gson gson = new Gson();
    Booking booking = gson.fromJson(updateBooking, Booking.class);
    bookingDAO.updateBooking(booking).thenApply(success -> { 
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
}
