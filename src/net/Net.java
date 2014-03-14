package net;

import graph.Edge;
import graph.NodeChange;
import graph.NodeStation;
import graph.NodeTrain;
import graph.WeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Net {
	/* Questa classe rappresenta il mondo, cioè un insieme di stazioni e di treni passanti per quella stazione.
	 * All'interno del "Mondo" inoltre, ci sarà l'identificativo del testcase a cui si riferisce, e la lista delle richieste che il testcase porta con sè.
	 * Sarà qui che verranno implementati i vari metodi di creazione del grafo, a seconda delle necessità.
	 * 
	 * Si assume che, ogni treno, abbia al suo interno la lista di fermate già ordinata in modo corretto.
	 * 
	 * L'insieme di stazioni e di treni contenuti nel Net è implementato tramite HashMap, in quanto offre tempi di accesso ad una 
	 * posizione specifica in tempo costante per la stragrande maggioranza delle entries (il rischio di collisioni è davvero ridotto).
	 * Per le richieste invece, si usa un semplice ArrayList, più economico in memoria, che permette l'iterazione.
	 * 
	 * Per garantire ulteriori performance, si inizializza la dimensione dell'HashMap di stazioni con la seconda riga del Testcase, 
	 * passandola come parametro al costruttore.
	 */

	/* HashMap delle stazioni: */
	private HashMap<Integer, Station> stations;
	
	/* HashMap dei treni: */
	private HashMap<Integer, Train> trains;
	
	/* Arraylist delle richieste */
	private ArrayList<util.Request> requests;
	
	/* Nome del testcase */
	private final String testcase;
	
	public Net(String testcase, int numberOfStation) {
		this.testcase = testcase;
		stations = new HashMap<Integer, Station>(numberOfStation);
		trains = new HashMap<Integer, Train>();
		requests = new ArrayList<util.Request>();
	}
	
	/* Setters e Getters per stazioni, treni e richieste */
	public void addStation(int code, Station station) {
		stations.put(code, station);
	}
	
	public Station getStation(int code)	{
		/* Questo metodo, ritorna in tempo costante la stazione con codice in input. */
		return stations.get(code);
	}

	public HashMap<Integer, Station> getStations() {
		return stations;
	}
	
	public void addTrain(int code, Train train) {
		trains.put(code, train);
	}
	
	public Train getTrain(int code) {
		/* Questo metodo, ritorna in tempo costante il treno con codice in input. */
		return trains.get(code);
	}
	
	public HashMap<Integer, Train> getTrains() {
		return trains;
	}
		
	public String getTestCase() {
		return testcase;
	}

	public ArrayList<util.Request> getRequests() {
		return requests;
	}

	public void addRequest(util.Request r) {
		requests.add(r);
	}
	
	/* Da qui in poi, verranno implementati metodi di creazioni dei grafi, in modo da poter differenziare i grafi in base al tipo di richieste. *
	 * ------------------------------------------------------ GENERAZIONE GRAFI: -------------------------------------------------------------- */

	public void createMinTempoGraph() {
		/* Questo metodo si occupa di crare un grafo relativo al problema del MINTEMPO.
		 * Il grafo è strutturato come segue:
		 * ogni stazione, ha un nodo di riferimento, nodeStation.
		 * ogni treno uscente dalla stazione, ha un nodo di riferimento, nodeTrain.
		 * Esiste un arco, per ogni treno entrante in stazione, ad ogni treno uscente in stazione, con peso pari al tempo di attesa in stazione.
		 * Ogni nodeTrain è collegato poi alla successiva nodeStation con un arco con peso pari al tempo di viaggio.
		 */
				
		/* HashMap di riferimento. */
		HashMap<Train, NodeTrain> map;
	
		/* Arraylist di riferimento dei NodeStation creati. */
		ArrayList<NodeTrain> nodeTrainList = new ArrayList<NodeTrain>();
		
		/* Per ogni stazione della rete: */
		for (Station s : this.stations.values()) {
			
			/* Creo un nodo che si riferisce alla stazione. */
			s.setNode(new NodeStation(s));
			/* inizializzo l'HashMap di riferimento con dimensione pari al numero di treni che passano in stazione. */
			map = new HashMap<Train, NodeTrain>(s.getTrainTable().size());
			
			/* Ottengo la lista dei treni che passano in stazione: */
			for (Map.Entry<Integer, Short[]> trainEntry : s.getTrainTable().entrySet()) {
				
				/* Se il treno parte dalla stazione: (è uscente) */
				if (trainEntry.getValue()[1] != -1) {
					
					/* Creo un rispettivo NodeTrain. */
					NodeTrain nt = new NodeTrain(s, this.getTrain(trainEntry.getKey()));
					/* Lo aggiungo alla mappa che ci servirà in seguito per creare gli archi da nodeStation a nodeTrain */
					map.put(this.getTrain(trainEntry.getKey()), nt);
					/* Lo aggiungo alla lista dei nodeTrain creati. */
					nodeTrainList.add(nt);
				}
			}
			
			/* ArrayList di riferimento. */
			ArrayList<WeightedEdge> list;
			
			/* Ottengo di nuovo la lista dei treni che passano in stazione: */
			for (Map.Entry<Integer, Short[]> trainEntry : s.getTrainTable().entrySet()) {
				
				/* Inizializzo l'arraylist di riferimento. */
				list = new ArrayList<WeightedEdge>();
				
				/* Per ogni treno che entra in stazione, dobbiamo creare la possibilità di prendere un qualsiasi altro treno, compreso sè stesso (se riparte) */
				if (trainEntry.getValue()[0] != -1) {
					
					Train train = getTrain(trainEntry.getKey());
					
					/* Per ognuno dei NodeTrain precedentemente creati: */
					for (Map.Entry<Train, NodeTrain> mapEntry : map.entrySet()) {
						
						/* Creo un arco dal nodeStation al nodeTrain, con peso uguale al tempo di attesa in stazione che passa dall'arrivo del treno su cui cicliamo alla partenza del treno di nodeTrain. */						
						WeightedEdge e = new WeightedEdge(mapEntry.getValue(), train, s.getWaitTime(this.getTrain(trainEntry.getKey()), mapEntry.getKey()));
						list.add(e);
					}
					
					((NodeStation)s.getNode()).addTrainEdges(train, list);
				}			
			}
		}
		
		/* Una volta finita la parte di inizializzazione della stazioni, quindi avendo creato ogni possibile nodeTrain: */
		for (NodeTrain nodeTrain : nodeTrainList) {
			Train train = nodeTrain.getTrain();
			
			/* Ottengo la prossima fermata del treno: */
			Station currentStop = nodeTrain.getStation();
			Station nextStop = this.getStation(train.getNextStop(nodeTrain.getStation().getCode()));
			
			/* Creo un arco dal nodeTrain alla prossima fermata. */
			WeightedEdge edge = new WeightedEdge(nextStop.getNode(), train, currentStop.getTravelTime(train.getCode(), nextStop));
			
			/* Aggiungiamo l'arco uscente. */
			nodeTrain.setExitingEdge(edge);
		}
	}	
	
	public void createMinOrarioGraph() {
		/* Questo metodo non fa altro che chiamare il metodo per creare il MinTempo, poichè il grafo utilizzato è lo stesso.
		 * Le piccole modifiche necessarie vengono effettuate direttamente dal metodo MinOrario.solve(). 
		 * Nella realtà dei fatti poi, viene generato il grafo soltanto una volta, quindi questo metodo non viene mai chiamato,
		 * è lasciato soltanto per completezza e simmetria. */
		createMinTempoGraph();
	}
	
	public void createMinCambiGraph() {
		/* Questo metodo, si occuperà di creare il grafo per il problema MinCambio.
		 * Tale grafo sarà strutturato come segue:
		 * un nodo, per ogni stazione, e un arco, per ogni possibile tratta di treno.
		 */
		
		/* Creo un nodo per ogni stazione. */
		for (Station s : this.getStations().values()) {
			s.setNode(new NodeChange(s));
		}
		
		/* A questo punto, devo creare un arco per ogni possibile tratta di treno. */
		
		/* Per ogni treno: */
		for (Train t : this.getTrains().values()) {
			
			/* Per ogni possibile fermata precedente <-> Fermata successiva: */
			for (Map.Entry<Integer, Integer> mapEntry : t.getNextStops().entrySet()) {
				
				/* Fisso la fermata di partenza. */
				Station start = this.getStation(mapEntry.getKey());
				/* Questa è una variabile per poter conoscere la prossima fermata: */
				Station value = this.getStation(mapEntry.getKey());
				
				/* Finchè esiste una prossima fermata: */
				while (value != null && t.getNextStop(value.getCode()) != -1) {
					/* Trovo la prossima fermata: */
					Station next = this.getStation(t.getNextStop(value.getCode()));
					/* Creo un arco dalla stazione di partenza alla fermata più lontana: */
					((NodeChange)start.getNode()).addEdge(new Edge(next.getNode(), t));
					/* Aggiorno value */
					value = next;
				}
				
			}
			
		}
	}

}
