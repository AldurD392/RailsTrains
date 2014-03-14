package net;

import graph.Node;

import java.util.HashMap;

public class Station {
	/* Questa classe contiene il prototipo di stazione.
	 * Essa ha un codice numerico identificativo;
	 * una tabella in cui, per ogni treno che passa in stazione, si ha l'orario di arrivo e l'orario di partenza in e dalla stazione.
	 * La tabella in questione è implementata tramite HashMap, per permettere l'accesso agli orari dei treni in tempo costante.
	 */
	
	/* Il codice della stazione. */
	private final int code;
	
	/* La tabella che, per ogni codice di treno, indica orario di arrivo e partenza in stazione. */
	private HashMap<Integer, Short[]> table;
	
	/* Questo è il riferimento al nodo che rappresenta la stazione nel grafo. */
	private Node node;	
	
	public Station(int code) {
		this.code = code;
		table = new HashMap<Integer, Short[]>();
	}
	
	public int getCode() {
		return this.code;
	}
	
	public void setNode(Node n) {
		this.node = n;
	}
	
	public Node getNode() {
		return this.node;
	}
	
	public Short[] getTrain(int code) {
		/* Questo metodo ritorna, nell'ordine, l'orario di arrivo e quello di partenza del treno con codice code in stazione. */
		return table.get(code);
	}
	
	public Short getArrivalTime(int code) {
		/* Questo metodo ritorna il tempo di arrivo in stazione del treno con codice in input. */
		return table.get(code)[0];
	}
	
	public Short getStartingTime(int code) {
		/* Questo metodo ritorna il tempi di partenza in stazione del treno codice in input. */
		return table.get(code)[1];
	}
	
	public void putTrain(int code, Short[] h) {
		table.put(code, h);
	}
	
	public HashMap<Integer, Short[]> getTrainTable() {
		return table;
	}
	
	public short getTravelTime(int trainCode, Station next) {
		/* Questo metodo si occupa di calcolare il tempo che impiega un treno dalla stazione corrente alla successiva. 
		 * Tiene conto dei treni che passano la stazione dopo la mezzanotte. */
		short max = 1440;
		
		short depart = getTrain(trainCode)[1];
		short arrive = next.getTrain(trainCode)[0];
				
		if (arrive - depart < 0) {
			return (short)(max + (arrive - depart));
		} else return (short)(arrive - depart);
	}

	public static short getWaitTime(short arrivalTime, short departingTime) {
		/* Questo metodo, dati tempo di arrivo e tempo di partenza, calcola l'attesa in stazione. */
		short max = 1440;
		if (departingTime - arrivalTime < 0) {
			return (short)(max + (departingTime - arrivalTime));
		} else return (short)(departingTime - arrivalTime);
	}
	
	public short getWaitTime(short arrivalTime, Train departing) {
		/* Questo metodo, dato un orario di arrivo in stazione ed un treno, calcola il tempo atteso prima della partenza. */
		return getWaitTime(arrivalTime, this.getTrain(departing.getCode())[1]);
	}
	
	public short getWaitTime(Train arrival, Train departing) {
		return getWaitTime(this.getTrain(arrival.getCode())[0], departing);
	}
	
	public String getTract(Train train, Net net) {
		/* Questo metodo, si occupa di ritornare una stringa che rappresenta la tratta dalla stazione corrente alla successiva tramite treno train. */
		
		int next = train.getNextStop(this.code);
		
		/* Ritorno la stringa con codice del treno, codice della stazione di partenza, orario di partenza, stazione di arrivo, orario di arrivo. */
		return
				train.getCode() + " " +
				this.getCode() + " " + 
				this.getTrain(train.getCode())[1] + " " + 
				next + " " + 
				net.getStation(next).getTrain(train.getCode())[0] + "\n";
	}
}
