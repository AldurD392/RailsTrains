package graph;

import net.Station;

public class Node {
	/* Questa è la generica classe "nodo" del grafo. 
	 * Si è deciso di tenere questa classe quanto più generica possibile, in modo da poterla sfruttare ugualmente per ogni tipo di grafo.
	 * Di conseguenza, ogni nodo contiene soltanto il riferimento alla stazione che rappresenta.
	 */

	/* Il riferimento alla stazione che il nodo rappresenta. */
	private final Station station;
	
	public Node(Station station) {
		this.station = station;
	}

	public Station getStation() {
		return station;
	}
	
	@Override
	public String toString() {
		return Integer.toString(this.getStation().getCode());
	}
}
