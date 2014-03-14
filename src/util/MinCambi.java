package util;

import java.util.HashMap;
import java.util.LinkedList;

import net.Net;
import net.Train;

import graph.Node;
import graph.NodeChange;

public class MinCambi extends Request {
	/* Questa è la classe addetta a gestire le richieste di tipo MINCAMBI:
	 * date due stazioni S1 e S2 trovare, se esiste, un percorso da S1 a S2 che minimizza il numero di cambi di treno. */

	public MinCambi(int S1, int S2) {
		super(S1, S2);
		solution = "MINCAMBI " + S1 + " " + S2 + "\n";
	}

	@SuppressWarnings("unchecked") /* Questo evita un Warning sul casting non protetto dei valori di ritorno della BFS, inutile ai fini del progetto. */
	@Override
	public void Solve(Net net) {
		/* Questo metodo, preso un grafo opportunamente generato, esegue una BFS dal nodo di partenza.
		 * In seguito, ottiene i nodi intermedi, ed infine, compila la stringa di soluzione come da specifiche. */
		
		/* Ottengo dalla BFF distanza, array dei padri e array dei treni. */
		Object[] bfs = BFS.BFSAlgorithm((NodeChange)net.getStation(S1).getNode(), (NodeChange)net.getStation(S2).getNode());
		
		/* Casto il vettore dei padri e quello dei treni: */
		HashMap<Node, Node> fathers = (HashMap<Node,Node>)bfs[1];
		HashMap<Node, Train> trains = (HashMap<Node,Train>)bfs[2];

		if (fathers.get(net.getStation(S2).getNode()) == null) {
			/* Se il padre di S2 è null, significa che non esiste un percorso minimo, quindi aggiungo -1 alla stringa di soluzione e ritorno. */
			solution += "-1\n";
			return;
		}
		
		/* Dall'array dei padri ricavo il percorso. */
		LinkedList<Node> path = Request.getPath(net.getStation(S1).getNode(), net.getStation(S2).getNode(), fathers);
		
		/* Ottengo i nodi intermedi tra i cambi: */
		LinkedList<Node> intermediate = intermediateNodes(net, path, trains);
		
		/* Aggiungo il numero minimo di cambi alla stringa di soluzione ed il numero di tratte. */
		solution += bfs[0] + " " + intermediate.size() + "\n";		
		
		/* Aggiungo le tratte alla soluzione. */
		int father = 1;
		for (Node n : intermediate) {
			if (n == path.get(father) && father + 1 < path.size())
				father++;
			solution += n.getStation().getTract(trains.get(path.get(father)), net);
		}
		
	}
	
	public LinkedList<Node> intermediateNodes(Net net, LinkedList<Node> path, HashMap<Node, Train> trains) {
		/* Questo metodo, data la rete, il vettore dei padri e quello dei treni, ritorna una lista linkata con tutte le fermate intermedie,
		 * non riportate inizialmente poiché la BFS salta tutte le fermate e conta soltando i cambi. */
		
		/* Inizializzo il primo ed il secondo nodo del percorso, e il treno che ho usato. */
		Node start = path.getFirst();
		Node second = path.get(1);
		Train train = trains.get(second);
		
		/* Inizializzo la lista ed aggiungo il nodo di partenza. */
		LinkedList<Node> intermediate = new LinkedList<Node>();
		intermediate.addFirst(start);
		
		/* Finchè non arrivo alla stazione di arrivo: */
		while (second != null && second != path.getLast()) {			
			/* Finchè non arrivo alla seconda stazione: */
			while (train.getNextStop(start.getStation().getCode()) != -1 && train.getNextStop(start.getStation().getCode()) != second.getStation().getCode()) {
				/* Aggiorno start e lo aggiungo alla lista. */
				start = net.getStation(train.getNextStop(start.getStation().getCode())).getNode();
				intermediate.addLast(start);
			} intermediate.addLast(second);
			
			/* Aggiorno start, second, ed il treno. */
			start = second;
			second = path.get(path.indexOf(second)+1);
			train = trains.get(second);
		}
		
		/* Questo while viene eseguito nel caso in cui second è proprio uguale a path.getLast(), cioè se non si effettuano cambi. */
		while (train.getNextStop(start.getStation().getCode()) != -1 && train.getNextStop(start.getStation().getCode()) != second.getStation().getCode()) {
			/* Aggiorno start e lo aggiungo alla lista. */
			start = net.getStation(train.getNextStop(start.getStation().getCode())).getNode();
			intermediate.addLast(start);
		}
		
		return intermediate;
	}

}
