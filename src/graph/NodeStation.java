package graph;

import java.util.ArrayList;
import java.util.HashMap;

import net.Station;
import net.Train;

public class NodeStation extends Node {
	/* Questa classe rappresenta una generica stazione nel grafo utilizzato per richieste di tipo MINTEMPO e MINORARIO.
	 * Tale nodo viene sfruttato come nodo di arrivo dei treni, ed in seguito per calcolare il peso degli archi che rappresentano l'attesa. */

	/* Ogni nodo stazione, ha una propria lista di archi, che rappresentano l'attesa in stazione per prendere un treno successivo. 
	 * Tale lista di archi è unica per ogni treno, in quanto a treni diversi corrisponderanno attese diverse in stazione. */
	private final HashMap<Train, ArrayList<WeightedEdge>> edgeMap;
	
	public NodeStation(Station code) {
		super(code);
		edgeMap = new HashMap<Train, ArrayList<WeightedEdge>>();
	}
	
	public ArrayList<WeightedEdge> getTrainEdges(Train train) {
		return edgeMap.get(train);
	}
	
	public void addTrainEdges(Train train, ArrayList<WeightedEdge> list) {
		edgeMap.put(train, list);
	}
	
	public void removeTrainEdges(Train train) {
		/* Questo metodo rimuove, in tempo costante, l'entry di un treno nell'hashmap che associa treno <-> archi.
		 * Tale metodo, sarà utile per ripulire le piccole aggiunte al grafo necessarie per i problemi MinOrario. */
		edgeMap.remove(train);
	}
	
	public HashMap<Train, ArrayList<WeightedEdge>> getEdgeMap() {
		return edgeMap;
	}
	
	public ArrayList<NodeTrain> getAdiacentNodeTrains() {
		ArrayList<NodeTrain> list = new ArrayList<NodeTrain>();
		
		for (Edge e : edgeMap.get(edgeMap.keySet().toArray()[0])) {
			list.add((NodeTrain)e.getNext());
		}
		
		return list;
	}
}
