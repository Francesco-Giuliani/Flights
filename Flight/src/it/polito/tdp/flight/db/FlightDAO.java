package it.polito.tdp.flight.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.flight.model.Airline;
import it.polito.tdp.flight.model.AirlineIdMap;
import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.AirportIdMap;
import it.polito.tdp.flight.model.Route;
import it.polito.tdp.flight.model.RouteIdMap;

public class FlightDAO {
	
	private AirlineIdMap airlinesIdMap;
	private AirportIdMap airportsIdMap; 
	private RouteIdMap routesIdMap;
	
	private int idRoute =0;
	
	public FlightDAO(AirlineIdMap airlinesIdMap, AirportIdMap airportsIdMap,  RouteIdMap routesIdMap) {
		this.airlinesIdMap = airlinesIdMap;
		this.airportsIdMap = airportsIdMap;
		this.routesIdMap = routesIdMap;
	}

	public List<Airline> getAllAirlines() {
		String sql = "SELECT * FROM airline";
		List<Airline> list = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Airline a = new Airline(res.getInt("Airline_ID"), res.getString("Name"), res.getString("Alias"),
						res.getString("IATA"), res.getString("ICAO"), res.getString("Callsign"),
						res.getString("Country"), res.getString("Active"));
				list.add(this.airlinesIdMap.getOrPutNew(a));
			}
			conn.close();
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Route> getAllRoutes() {
		String sql = "SELECT * FROM route";
		List<Route> list = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			this.idRoute =0;
			
			while (res.next()) {
				Airline airline = this.airlinesIdMap.getAirlinebyId(res.getInt("Airline_ID"));
				Airport sourceAirport = this.airportsIdMap.getAirportbyId(res.getInt("Source_airport_ID")) ;
				Airport destinationAirport = this.airportsIdMap.getAirportbyId(res.getInt("Destination_airport_ID")) ;
				
				Route r = new Route(this.idRoute++, airline, sourceAirport, destinationAirport,
						res.getString("Codeshare"), res.getInt("Stops"), 	res.getString("Equipment"));
				
				list.add(this.routesIdMap.getOrPutNew(r));

				airline.addRoute(this.routesIdMap.getOrPutNew(r));
				sourceAirport.addRoute(this.routesIdMap.getOrPutNew(r));
				destinationAirport.addRoute(this.routesIdMap.getOrPutNew(r));
			}
			this.idRoute =0;
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Airport> getAllAirports() {
		String sql = "SELECT * FROM airport";
		List<Airport> list = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Airport a = new Airport(res.getInt("Airport_ID"), res.getString("name"), res.getString("city"),
						res.getString("country"), res.getString("IATA_FAA"), res.getString("ICAO"),
						res.getDouble("Latitude"), res.getDouble("Longitude"), res.getFloat("timezone"),
						res.getString("dst"), res.getString("tz"));
				list.add(this.airportsIdMap.getOrPutNew(a));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
