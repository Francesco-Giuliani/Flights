package it.polito.tdp.flight.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.sun.javafx.collections.MappingChange.Map;

import it.polito.tdp.flight.db.FlightDAO;

public class Model {

	private FlightDAO fligthDAO;
	private List<Airline> airlines;
	private List<Airport> airports;
	private List<Route> routes;
	
	private AirlineIdMap airlinesIdMap;
	private AirportIdMap airportsIdMap; 
	private RouteIdMap routesIdMap;
	
	private SimpleDirectedWeightedGraph <Airport, DefaultWeightedEdge> graph;
	private double weight;
	
	public Model() {
		this.airlines = new ArrayList<>();
		this.airports = new ArrayList<>();
		this.routes = new ArrayList<>();

		this.airlinesIdMap = new AirlineIdMap();
		this.airportsIdMap = new AirportIdMap();
		this.routesIdMap = new RouteIdMap();
		
		this.fligthDAO = new FlightDAO(this.airlinesIdMap, this.airportsIdMap, this.routesIdMap);
		
		this.airlines = this.fligthDAO.getAllAirlines();
		System.out.println(airlines.size());

		this.airports = this.fligthDAO.getAllAirports();
		System.out.println(airports.size());

		this.routes = this.fligthDAO.getAllRoutes();
		System.out.println(routes.size());
		
	}

	public Airline getAirlineById(int idAirline) {
		return this.airlinesIdMap.getAirlinebyId(idAirline);
	}
	
	public List<Airport> getAirports(){
		if(this.airports == null)
			return new ArrayList<>();
		return this.airports;
	}
	
	public void createGraph() {
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.graph, this.airports);
		
		for(Route r: this.routes) {
			Airport source = r.getSourceAirport(), dest = r.getDestinationAirport();
			if(!source.equals(dest)) {
				double weight = LatLngTool.distance(new LatLng(source.getLatitude(), source.getLongitude()), 
						new LatLng(dest.getLatitude(), dest.getLongitude()), LengthUnit.KILOMETER);
				
				Graphs.addEdgeWithVertices(this.graph, source, dest, weight);
	//			DefaultWeightedEdge edge = this.graph.addEdge(source, dest);
	//			this.graph.setEdgeWeight(edge, weight);
			}
		}
		
		System.out.println("Numero vertici: "+ this.graph.vertexSet().size());
		System.out.println("Numero lati: "+ this.graph.edgeSet().size());
	}

	public void printStats() {
		if(this.graph == null) {
			this.createGraph();
		}
		
		ConnectivityInspector<Airport, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.graph);
		System.out.println(ci.connectedSets().size());
	
	}
	
	public Set<Airport> getBiggestSCCC(){
		ConnectivityInspector<Airport, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.graph);
		Set<Airport> best = null;
		int bestSize =0;
		for(Set<Airport> s : ci.connectedSets()) {
			if(s.size()> bestSize) {
				bestSize = s.size();
				best = new HashSet<>(s);
			}
		}
		System.out.println("Biggest connected set: \n"+best);
		return best;
	}

	public  List<Airport> getShortestPath(int id1, int id2) {
		Airport nyc = this.airportsIdMap.getAirportbyId(id1);
		Airport bgy = this.airportsIdMap.getAirportbyId(id2);
		
		if(nyc == null || bgy == null) {
			throw new RuntimeException("Aereoporti non presenti nell'applicazione");
		}
			//NON ho cicli negativi o pesi negativi --> posso usare Djikstra
			
		ShortestPathAlgorithm<Airport, DefaultWeightedEdge> spa = new DijkstraShortestPath<>(this.graph);
		GraphPath <Airport, DefaultWeightedEdge>  gp = spa.getPath(nyc, bgy);
		weight = gp.getWeight();
		
		return gp.getVertexList();
		}
	public double getWeight() {
		return this.weight;
	}
		
	}
	

