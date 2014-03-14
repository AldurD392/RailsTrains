package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import net.Net;
import net.Station;
import net.Train;

import graph.*;

public class MinOrario extends Request {
	/* Questa è la classe addetta a gestire richieste di tipo MINORARIO:
	 * date due stazioni S1 e S2 e un orario O di trovare, se esiste,
	 * un percorso da S1 a S2 che minimizza il tempo che passa tra l'orario O e quello di arrivo nella stazione di destinazione S2.
	 */

	private final short o;
	
	public MinOrario(int S1, int S2, short o) {
		super(S1, S2);
		this.o = o;
		solution = "MINORARIO " + S1 + " " + S2 + " " + o + "\n";
	}

	@SuppressWarnings("unchecked") /* Questo evita un Warning sul casting non protetto dei valori di ritorno di Dijkstra, inutile ai fini del progetto. */
	@Override
	public void Solve(Net net) {
		/* L'idea, per risolvere questo tipo di problema, è creare un finto treno che arriva in S1 esattamente nell'orario O. */
		
		Station fakeStation = new Station(-1);
		NodeStation fakeNodeStation = new NodeStation(fakeStation);
		Train fakeTrain = new Train(-1);
		NodeTrain fakeNodeTrain = new NodeTrain(fakeStation, fakeTrain);
		
		ArrayList<WeightedEdge> list = new ArrayList<WeightedEdge>();
		list.add(new WeightedEdge(fakeNodeTrain, fakeTrain, 0));
		
		fakeNodeStation.addTrainEdges(fakeTrain, list);		
		fakeNodeTrain.setExitingEdge(new WeightedEdge(net.getStation(S1).getNode(), fakeTrain, 0));
		
		list = new ArrayList<WeightedEdge>();
		
		NodeStation u = (NodeStation)net.getStation(S1).getNode();
		for (NodeTrain nt : u.getAdiacentNodeTrains()) {
			list.add(new WeightedEdge(nt, fakeTrain, net.getStation(S1).getWaitTime(o, nt.getTrain())));
		}
		u.addTrainEdges(fakeTrain, list);
		
		/* Chiamo Dijkstra: */
		Object[] dijkstra = Dijkstra.DijkstraMinTempo(
				fakeNodeStation, 
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
		
		/* A questo punto, rimuovo le aggiunte aggiunte al grafo, non più necessarie,
		 * lasciando al GC il compito di ripulire la memoria utilizzata. */
				
		u.removeTrainEdges(fakeTrain);
		list = null;
		fakeNodeTrain.setExitingEdge(null);
		fakeNodeStation.removeTrainEdges(fakeTrain);
		
		fakeNodeTrain = null;
		fakeTrain = null;
		fakeNodeStation = null;
		fakeStation = null;
	}
}
