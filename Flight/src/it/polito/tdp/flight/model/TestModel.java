package it.polito.tdp.flight.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();

		int idAirline = 2;
		int idSourceAirport;
		int idDestinationAirport;
		int idRoute;
		
		
		
		Airline airline = model.getAirlineById(idAirline);
		Airport sourceAirport = model.getAirports().get(0);
//		Airport destinationAirport= model.getAirportById(idDestinationAirport);
	
		System.out.println("Airline: "+airline);
		System.out.println("Rotte della Airline: \n"+airline.getRoutes());
	
		System.out.println("Airport: "+sourceAirport);
		System.out.println("Routes: \n"+sourceAirport.getRoutes());
		
		model.createGraph();
		System.out.println("Componenti connesse: ");
		model.printStats();
		
		Set<Airport> BigSCC = model.getBiggestSCCC();
		System.out.println("Dimensione Biggest Connected Component: "+BigSCC.size());
		
		Airport a1=null, a2=null;
		
		Iterator<Airport> i = BigSCC.iterator();
		for(int j =0; j<BigSCC.size(); j++ ) {
			
			if(j==1)
				a1 = i.next();

			else if(j==BigSCC.size()-1)
				a2 = i.next();
			else 
				i.next();
		}
		//8591
		//1525
		
		//Voglio un metodo del model che calcoli lo shortest path tra i due aereoporti
		
		int id1 = a1.getAirportId(), id2 = a2.getAirportId();
		
		System.out.println("STARTING AIRPORT: "+a1.getCity()+ "\nARRIVAL AIRPORT: "+a2.getCity());
		
		try {
		List<Airport> percorso = model.getShortestPath(id1, id2);
		double weight = model.getWeight();
		
		System.out.println("Percorso: "+percorso+"\nPeso: "+weight);
		}catch(Exception e) {
			System.out.println("Errore: non è possibile trovare un percorso");
			e.printStackTrace();
			return;
		}
	}

}
