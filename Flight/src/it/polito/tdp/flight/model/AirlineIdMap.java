package it.polito.tdp.flight.model;

import java.util.HashMap;
import java.util.Map;

public class AirlineIdMap {

	private Map <Integer, Airline> map;
	
	public AirlineIdMap() {
		map = new HashMap<>();
	}

	public Map<Integer, Airline> getMap() {
		return map;
	}
	public void setMap(Map<Integer, Airline> map) {
		this.map = map;
	}
	
	public Airline getAirlinebyId(int airlineId) {
		return map.get(airlineId);
	}
	public Airline getOrPutNew(Airline airline) {
		Airline old = map.get(airline.getAirlineId());
		if(old == null ) {
			map.put(airline.getAirlineId(), airline);
			return airline;
		}
		return old;
	}
	public void put(Airline airline, int airlineId) {
		map.put(airlineId, airline);
	}
	public void put(Airline airline) {
		map.put(airline.getAirlineId(), airline);
	}
	
}
