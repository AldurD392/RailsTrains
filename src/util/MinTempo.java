package util;

import java.util.HashMap;
import java.util.LinkedList;

import net.Net;
import net.Train;

import graph.Node;
import graph.NodeStation;
import graph.NodeTrain;

public class MinTempo extends Request {
	/* Questa è la classe per gestire le richieste di tipo MINTEMPO: 
	 * date due stazioni S1 ed S2 trovare, se esiste, un percorso di minimo tempo da S1 a S2. */
	
	public MinTempo(int S1, int S2) {
		super(S1, S2);
		solution = "MINTEMPO " + S1 + " " + S2 + "\n";
	}

	@SuppressWarnings("unchecked") /* Questo evita un Warning sul casting non protetto dei valori di ritorno di Dijkstra, inutile ai fini del progetto. */
	@Override
	public void Solve(Net net) {
		/* Questo metodo, nell'ordine:
		 * Esegue l'algoritmo di Dijkstra a partire dal nodo di partenza (la stazione con codice S1)
		 * Ritornare un eventuale percorso minimo dal nodo con codice S1 a quello con codice S2 e riporta tale percorso nella stringa solution.
		 */

		/* Chiamo Dijkstra che ritorna il padre del nodo di arrivo, la distanza e l'array dei padri. */
		Object[] dijkstra = Dijkstra.DijkstraMinTempo(
				(NodeStation)net.getStation(S1).getNode(), 
				(NodeStation)net.getStation(S2).getNode());
				
		Node xFather = (Node) dijkstra[0];
		HashMap<Node, Node> fathers = (HashMap<Node, Node>) dijkstra[2];
		
		if (xFather == null) {
			/* Se il padre di x è null, significa che non esiste un percorso minimo, quindi aggiungo -1 alla stringa di soluzione e ritorno. */
			solution += "-1\n";
			return;
		}
		
		/* Ottengo il percorso minimo dall'array dei padri: */
		LinkedList<Node> path = Request.getPath(
				net.getStation(S1).getNode(), 
				xFather,
				fathers);
		
		/* Aggiungo la stringa contenente distanza minima e numero di tratte: */
		solution += dijkstra[1] + " " + path.size() + "\n";
		
		/* E aggiungo una stringa per ogni tratta */
		for (Node n : path) {
			NodeTrain nt = (NodeTrain)n;
			Train t = nt.getTrain();
			solution += nt.getStation().getTract(t, net);
		}
		
	}
}
