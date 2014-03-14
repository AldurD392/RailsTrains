package util;

import java.util.HashMap;
import java.util.LinkedList;

import net.Net;

import graph.Node;

public abstract class Request {
	protected final int S1;
	protected final int S2;
	
	protected String solution;
	
	public Request(int S1, int S2) {
		this.S1 = S1;
		this.S2 = S2;
	}

	public int getS1() {
		return S1;
	}

	public int getS2() {
		return S2;
	}
	
	public String getSolution() {
		return this.solution;
	}
	
	/* Questo è il metodo che si occupa di risolvere il problema dato. Si assume che gli venga passato la net al cui interno il grafo è stato già creato in modo opportuno. */
	public abstract void Solve(Net net);
	
	public static LinkedList<Node> getPath(Node u, Node v, HashMap<Node, Node> father) {
		/* Questo metodo, preso il vettore dei padri e il nodo di arrivo, ritorna il percorso. */
		
		LinkedList<Node> ret = new LinkedList<Node>();
		
		/* L'ultimo elemento è dove si arriva */
		ret.addFirst(v);
				
		/* Finchè non arrivo alla radice, con padre[v] = v.
		 * Da notare che il confronto non viene effettuato sugli oggetti Node in sè, ma sui codici che rappresentano,
		 * in quanto, a causa dell'implementazione usata nell'algoritmo di Dijkstra, 
		 * a tale metodo vengono passati un NodeStation ed un NodeTrain,
		 * che ovviamente non avranno mai stesso riferimento. */
		while (v.getStation().getCode() != u.getStation().getCode()) {
			v = father.get(v);
			ret.addFirst(v);
		}
		
		return ret;
	}
	
}