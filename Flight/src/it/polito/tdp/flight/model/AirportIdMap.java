package it.polito.tdp.flight.model;

import java.util.HashMap;
import java.util.Map;

public class AirportIdMap {

	private Map <Integer, Airport> map;
	
	public AirportIdMap() {
		map = new HashMap<>();
	}

	public Map<Integer, Airport> getMap() {
		return map;
	}
	public void setMap(Map<Integer, Airport> map) {
		this.map = map;
	}
	
	public Airport getAirportbyId(int airportId) {
		return map.get(airportId);
	}
	public Airport getOrPutNew(Airport airport) {
		Airport old = map.get(airport.getAirportId());
		if(old == null ) {
			map.put(airport.getAirportId(), airport);
			return airport;
		}
		return old;
	}
	public void put(Airport airport, int airportId) {
		map.put(airportId, airport);
	}
	public void put(Airport airport) {
		map.put(airport.getAirportId(), airport);
	}
	
}