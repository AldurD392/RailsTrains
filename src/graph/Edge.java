package graph;

import net.Train;

public class Edge {
	/* Questa è la classe che rappresenta un generico arco del grafo.
	 * Essa contiene il puntatore al nodo successivo. */

	protected final Node next;	
	protected final Train train;
	
	public Edge(Node next, Train train) {
		this.next = next;
		this.train = train;
	}
	
	public Edge(Node next, Train train, int weight) {
		this.next = next;
		this.train = train;
	}
	
	public Node getNext() {
		return next;
	}
	
	public Train getTrain() {
		return train;
	}
	
}
