package graph;

import java.util.ArrayList;

import net.Station;

public class NodeChange extends Node {
	/* Questa classe rappresenta un generico nodo del grafo adatto a risolvere le richieste del tipo MINCAMBI.
	 * Esso contiene, oltre agli attributi ereditati, una lista di archi uscenti. */

	/* Lista di archi uscenti: */
	private ArrayList<Edge> edges;
	
	public NodeChange(Station code) {
		super(code);
		edges = new ArrayList<Edge>();
	}
	
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	
	public void addEdge(Edge e) {
		edges.add(e);
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

}
