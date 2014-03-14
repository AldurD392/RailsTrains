package graph;

import net.Station;
import net.Train;

public class NodeTrain extends Node {
	/* Questa classe rappresenta un generico nodo del grafo per le richieste di tipo MINTEMPO e MINORARIO, 
	 * da cui si può prendere il treno specificato in train.
	 * Da questo nodo esce soltanto un arco, verso la prossima destinazione del treno e con peso pari al tempo del tragitto. 
	 */

	private final Train train;
	private WeightedEdge exitingEdge;

	public NodeTrain(Station code, Train train) {
		super(code);
		this.train = train;
	}
	
	public Train getTrain() {
		return train;
	}
	
	public WeightedEdge getExitingEdge() {
		return exitingEdge;
	}

	public void setExitingEdge(WeightedEdge exitingEdge) {
		this.exitingEdge = exitingEdge;
	}
}
