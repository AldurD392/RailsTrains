package util;

import java.util.HashMap;
import java.util.PriorityQueue;

import graph.*;

public class Dijkstra {
	/* In questa classe si implementano l'algoritmo di Dijkstra. */

	public static class DistHeap implements Comparable<DistHeap> {
		/* Questa classe rappresenta un generico elemento dell'Heap utile all'algoritmo di Dijkstra.
		 * Ogni elemento contiene il nodo cui si riferisce e la distanza dal nodo di partenza.
		 * Implementa Comparable per poter essere inserito nel posto giusto all'interno dell'Heap. */
		
		private int dist;
		private NodeTrain n;
		
		public DistHeap(NodeTrain n, int dist) {
			this.dist = dist;
			this.n = n;
		}
		
		public DistHeap(NodeTrain n) {
			this.dist = Integer.MAX_VALUE;
			this.n = n;
		}

		@Override
		public int compareTo(DistHeap arg0) {
			if (this.dist > arg0.dist) return 1;
			if (this.dist == arg0.dist) return 0;
			else return -1;
		}
		
		public int getDist() {
			return dist;
		}
		
		public NodeTrain getNodeTrain() {
			return n;
		}
		
	}
	
	public static Object[] DijkstraMinTempo(NodeStation u, NodeStation x) {		
		/* Questo metodo implementa l'algoritmo di Dijkstra, leggermente modificata, a partire dal nodo u fino ad arrivare al nodo x.
		 * Esso ritorna il padre di x, la distanza minima di x da u ed il vettore dei padri. */
		
		/* Vettore delle distanze */
		HashMap<NodeTrain, Integer> dist = new HashMap<NodeTrain, Integer>();
		
		/* Vettore dei padri */
		HashMap<NodeTrain, NodeTrain> father = new HashMap<NodeTrain, NodeTrain>();
		
		/* Vettore di riferimento Node <-> Elemento dell'Heap */
		HashMap<NodeTrain, DistHeap> trace = new HashMap<NodeTrain, DistHeap>();
	
		/* minHeap */
		PriorityQueue<DistHeap> heap = new PriorityQueue<DistHeap>();
		
		/* Variabili "speciali" per il nodo di arrivo: */
		int xTravelTime = Integer.MAX_VALUE;
		NodeTrain xFather = null;
		
		/* Inizializzo la distanza ed i padri dei nodeTrain adiacenti al nodo iniziale. */
		for (NodeTrain nt : u.getAdiacentNodeTrains()) {
			dist.put(nt, 0);
			heap.add(new DistHeap(nt, 0));
			father.put(nt, nt);
		}
		
		/* Finchè ci sono elementi nell'heap: */
		while (heap.peek() != null) {
			/* Estraggo il minimo dall'heap. */
			DistHeap min = heap.poll();
			
			/* Risalgo al NodeTrain: */
			NodeTrain v = min.getNodeTrain();
			
			/* Se il treno in esame, arriva proprio al nodo di interesse, 
			 * devo salvare il padre e la distanza separatamente, perché non mi interessano eventuali tempi di attesa in stazione: */
			if (v.getExitingEdge().getNext() == x && xTravelTime > dist.get(v) + v.getExitingEdge().getWeight()) {
				xTravelTime = dist.get(v) + v.getExitingEdge().getWeight();
				xFather = v;
			}
			
			/* Per ogni adiacente w del nodo v: */
			for (WeightedEdge e : ((NodeStation)v.getExitingEdge().getNext()).getTrainEdges(v.getTrain())) {
				NodeTrain w = (NodeTrain)e.getNext();
				
				/* Se la distanza è già stata calcolata in precedenza, cioè è diversa da infinito: */
				if (dist.containsKey(w)) {				
					if (dist.get(w) > dist.get(v) + e.getWeight() + v.getExitingEdge().getWeight()) {
						dist.put(w, dist.get(v) + e.getWeight() + v.getExitingEdge().getWeight());
						father.put(w, v);
						
						/* Decremento l'heap */
						heap.remove(trace.get(w));
						DistHeap h = new DistHeap(w, dist.get(v) + e.getWeight() + v.getExitingEdge().getWeight());
						heap.add(h);
						trace.put(w, h);
					}
				} 
				
				/* Altrimenti, setto la distanza. */
				else {
					dist.put(w, dist.get(v) + e.getWeight() + v.getExitingEdge().getWeight());
					father.put(w, v);
					
					/* Decremento l'heap */
					DistHeap h = new DistHeap(w, dist.get(v) + e.getWeight() + v.getExitingEdge().getWeight());
					heap.add(h);
					trace.put(w, h);
				}
			}
			
		}
		
		/* Ritorno padre e distanza di x e vettore dei padri. */
		Object[] ret = {xFather, xTravelTime, father};
		return ret;
	}
}
