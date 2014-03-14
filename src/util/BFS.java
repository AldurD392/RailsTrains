package util;

import java.util.HashMap;
import java.util.LinkedList;

import net.Train;

import graph.*;

public class BFS {

	public static Object[] BFSAlgorithm(NodeChange u, NodeChange x) {
		/* Questa funzione implementa la visita in ampiezza. */
		
		/* Coda, inizialmente vuota. */
		LinkedList<NodeChange> queue = new LinkedList<NodeChange>();
		
		/* Array delle distanze: */
		HashMap<NodeChange, Integer> dist = new HashMap<NodeChange, Integer>();
		
		/* Vettore dei padri: */
		HashMap<NodeChange, NodeChange> father = new HashMap<NodeChange, NodeChange>();
		
		/* Vettore dei treni: */
		HashMap<NodeChange, Train> train = new HashMap<NodeChange, Train>();
		
		/* Caso base: */
		father.put(u, u);
		/* Si inizializza la distanza a -1 poiché il primo treno preso non conta come un cambio. */
		dist.put(u, -1);
		
		queue.addLast(u);
		
		while (!queue.isEmpty()) {
			/* Estraggo il primo elemento della coda: */
			NodeChange v = queue.removeFirst();
			
			/* Per ogni adiacente di v */
			for (Edge e : v.getEdges()) {
				NodeChange w = (NodeChange)e.getNext();
				
				/* Se w non è stato visitato: */
				if (father.get(w) == null) {
					father.put(w, v);
					train.put(w, e.getTrain());
					dist.put(w, dist.get(v) + 1);
					queue.addLast(w);
				}
			}
		}
				
		/* Si ritorna il numero di cambi di x e l'array dei padri. */
		Object[] object = {dist.get(x), father, train};
		return object;
	}
	
}
