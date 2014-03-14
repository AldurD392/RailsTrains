package net;

import java.util.ArrayList;
import java.util.HashMap;

public class Train {
	/* Questa classe contiene un generico treno.
	 * Essa contiene il codice del treno, e le stazioni in cui ferma. Tale elenco di stazioni verrà aggiornato man mano che il file viene parsato. */
	
	public class TrainATime implements Comparable<Train.TrainATime> {
		/* Questo è l'oggetto che ci serve per poter ottenere una lista ordinata delle fermate del treno.
		 * Esso contiene il codice della stazione ed il tempo di arrivo.
		 * Implementa Comparable per poter inserire facilmente in modo ordinato. */
		private final int stationCode;

		/* Boolean che controlla se la fermata è di capolinea. */
		private final boolean endOfLine;
		/* Tempo di arrivo. */
		private final short arrivalTime;
		
		public TrainATime(int stationCode, short arrivalTime, boolean startingTime) {
			this.stationCode = stationCode;
			this.arrivalTime = arrivalTime;
			this.endOfLine = startingTime;
		}

		public int getStationCode() {
			return stationCode;
		}
		
		public short getArrivalTime() {
			return arrivalTime;
		}
		
		public boolean isEndOfLine() {
			return endOfLine;
		}
		
		@Override
		public int compareTo(Train.TrainATime o) {
			if (this.arrivalTime == ((Train.TrainATime) o).arrivalTime)
				return 0;
			else if (this.arrivalTime > ((Train.TrainATime) o).arrivalTime)
				return 1;
			else
				return -1;
		}
	}
	
	/* Il codice del treno. */
	private final int code;
	
	/* Un'arrayList con le fermate del treno. Tale arrayList verrà dismessa in seguito, e sostituita con l'HashMap sottostante per ragioni di efficienza:
	 * infatti, per accedere ad un n-esimo elemento della lista si impiegna O(n), mentre per accedere ad un dato elemento della mappa si impiega O(1). */
	private ArrayList<Train.TrainATime> stops;	
	private HashMap<Integer, Integer> nextStops;
	
	/* Questa variabile, servirà in seguito, per poter ordinare in modo più efficiente le fermate del treno.
	 * Essa mantiene l'indice della stazione di arrivo del treno. */
	private int arrivalIndex;
	
	public Train(int code) {
		this.code = code;
		stops = new ArrayList<Train.TrainATime>();
		nextStops = new HashMap<Integer, Integer>();
		this.arrivalIndex = -1;
	}

	public int getCode() {
		return code;
	}	
	
	public int getArrivalIndex() {
		return arrivalIndex;
	}
	
	@Override
	public String toString() {
		return Integer.toString(this.code);
	}
	
	public ArrayList<Train.TrainATime> getStops() {
		return stops;
	}
	
	/* Questo metodo ritorna il codice della prossima fermata. */
	public int getNextStop(int current) {
		return nextStops.get(current);
	}
	
	public HashMap<Integer, Integer> getNextStops() {
		return nextStops;
	}
	
	/* A questo punto, sorge il problema, di come mantenere, per ogni Treno, la lista ordinata delle sue fermate.
	 * Ordinare in base al tempo di arrivo, di per sè, non basta, in quanto otterremmo, per un generico treno che
	 * parte dalla stazione P alle ore 22:00, per arrivare alla stazione A alle ore 23:59, e poi ad una stazione B alle ore 01:00,
	 * un ordine pari a: PBA, palesemente errato.
	 * 
	 * Per questo motivo, è necessario confrontare gli orari di arrivo in base al DELTA dall'ora di partenza.
	 * Ma, è tanto complicato, quanto poco efficiente ottenere l'orario di partenza dal capolinea di un treno, 
	 * prima di arrivarci scorrendo il file di input.
	 * 
	 * Per cui, la soluzione proposta, in prima analisi, è quella di "scalare" tutti i tempi di arrivo dopo aver conosciuto l'orario di partenza
	 * ed in seguito ordinare in base ai tempi di arrivo.
	 * 
	 * Un'implementazione più efficiente, consiste, sempre dopo aver conosciuto gli orari di arrivo,
	 * nel rimettere a posto gli orari "sballati", ad esempio, portando A (o l'insieme A) tra P ed B.
	 * P-> B-> A >> P -> A -> B
	 * 
	 * Un'ulteriore alternativa, è quella di spostare la stazione A esattamente tra B e C, per poi unire B e C:
	 * P -> B -> A >> B -> P -> A >> P -> A -> B
	 * Il posto "giusto" per trovare l'ultimo elemento di C, è trovare quell'elemento che ha orario di partenza = "-1".
	 * 
	 * A questo punto quindi, il problema si riconduce all'inserire in modo ordinato in base ai tempi di arrivo, ed in seguito "rimettere a posto" la lista
	 * per risolvere il problema dovuto al cambio di giornata.
	 */
	
	public static int timesIndex(Train.TrainATime input, ArrayList<Train.TrainATime> list, int sx, int dx) {
		/* Questa è la funzione che si occupa di trovare l'indice per l'inserimento ordinato in lista. 
		 * Essa sfrutta un algoritmo basato sulla ricerca binaria. */
		
		int size = dx - sx;

		/* Caso base per ogni altro elemento : */
		if (size == 0) {
			if (list.get(size+sx).compareTo(input)==1) {
				return sx;
			}
			/* Caso base per l'ultimo elemento della lista. */
			if (dx == list.size()-1) {
				if (list.get(size).compareTo(input)==-1) return dx + 1;
			}
			else return dx;
		}
		
		size = (size / 2) + sx;
		
		if (list.get(size).compareTo(input)==1)	return timesIndex(input, list, sx, size);
		else return timesIndex(input, list, size + 1, dx);
	}
	
	public void addStop(Train.TrainATime in) {
	/* Questa è la funzione che inserisce effettivamente l'elemento in modo ordinato. Per motivi di efficienza, sebbene i miglioramenti siano minimi,
	 * si è preferito controllare se si sta trattando la prima o l'ultima stazione, in modo da risparmiare due cicli di inserimento ordinato. */
				
		/* Se il la fermata è la prima ad essere aggiunta: */
		if (stops.size() == 0) {
			stops.add(in);
			
			/* Inoltre, se la prima fermata aggiunta è proprio quella di arrivo del treno, aggiorniamo il relativo indice. */
			if (in.isEndOfLine()) 
				this.arrivalIndex = 0;
		}
		
		/* Se stiamo aggiungendo la partenza da capolinea: */
		else if (in.getArrivalTime() == -1) {
			stops.add(0, in);
			if (this.arrivalIndex != -1 ) 
				this.arrivalIndex++;
		}
		
		/* Altrimenti: */
		else {
			int index = timesIndex(in, stops, 0, stops.size() - 1);
			stops.add(index, in);
			
			/* A questo punto, se abbiamo inserito un elemento nell'array in una posizione precedente a quella della stazione di arrivo,
			 * dobbiamo incrementare l'indice di quest'ultima.
			 * Se invece la stazione di arrivo, ancora non è stata settata, non abbiamo alcun problema in quanto non esiste alcun indice < -1.	
			 */
			if (index <= arrivalIndex) {
				arrivalIndex++;
			}
			
			/* A questo punto, se stiamo analizzando la fermata di capolinea, salviamo la posizione nell'array in arrivalIndex.
			 * Sarà utile in seguito per l'ordinamento. 
			 */
			if (in.isEndOfLine()) {
				this.arrivalIndex = index;
			}
		}
		
	}
	
	public void orderStops() {
		/* Questo metodo è utilizzato per porre nell'ordine corretto i treni che attraversano mezzanotte nel proprio tragitto. */
		
		/* Per una leggera miglioria all'efficienza, si è pensato di chiamare la funzione soltanto sui treni che hanno effettivamente questo problema,
		 * e cioè, su quelli che hanno la stazione di arrivo di una posizione diversa dall'ultima. */
		if (!stops.get(stops.size() - 1).isEndOfLine()) {
			/* Prima di tutto, prendo gli elementi da arrivalIndex + 1, fino alla fine, e li inserisco in una lista. */
						
			ArrayList<Train.TrainATime> r = new ArrayList<Train.TrainATime>(stops.subList(arrivalIndex + 1, stops.size()));
			
			/* Il primo elemento, è sempre nella posizione corretta, quindi lo aggiungo in testa. */
			r.add(0, stops.get(0));
			
			/* A questo punto punto, tutti gli elementi compresi da uno ad index, vanno in fondo. */
			r.addAll(stops.subList(1, arrivalIndex + 1));
			
			this.stops = r;
		}
	}
	
	public void fillNextStops() {
		/* Questo metodo si occupa di riempire un'opportuna HashMap, associando ad ogni fermata del treno la fermata successiva. 
		 * La stazione di partenza avrà come fermata -1.
		 * La stazione di arrivo avrà come prossima fermata -1. */
		
		/* Inserisco la stazione di partenza. */
		nextStops.put(-1, stops.get(0).getStationCode());
		
		/* Inserisco le fermata intermedie. */
		for (int i = 0; i < stops.size() -1; i++) {
			nextStops.put(stops.get(i).getStationCode(), stops.get(i+1).getStationCode());
		}
		
		/* Inserisco l'ultima fermata. */
		nextStops.put(stops.get(stops.size()-1).getStationCode(), -1);
		
		/* A questo punto, lascio al Garbage Collector il compito di ripulire la memoria non più utilizzata, 
		 * dismettendo l'ArrayList di fermate, ormai ordinate. */
		stops.clear();
		this.stops = null;
	}
}
