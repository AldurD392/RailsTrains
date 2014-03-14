package graph;

import net.Train;

public class WeightedEdge extends Edge {
	/* Questa classe rappresenta un generico arco pesato del grafo,
	 * quindi, oltre agli attributi ereditati da Edge,
	 * aggiunge il peso dell'arco. */
	
	private final int weight;

	public WeightedEdge(Node next, Train train, int weight) {
		super(next, train);
		this.weight = weight;
	}	
	
	public int getWeight() {
		return weight;
	}
}
