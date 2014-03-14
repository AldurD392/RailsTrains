package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import net.Net;
import net.Station;
import net.Train;

public class Parser {
	/* Questa è la classe parser, che si occupa di parsare l'input, creare le relative net e ritornarle. */

	private Scanner sc;
	
	public Parser(String filepath) throws FileNotFoundException {
		/* Questo costruttore è stato lasciato soltanto per permettere il debugging. */
		FileInputStream is = new FileInputStream(filepath); 
		sc = new Scanner(is);
	}
	
	public Parser() throws FileNotFoundException {
		/* TODO: potrebbe dover essere modificato per leggere il file all'interno della stessa cartella del jar. */
		sc = new Scanner(new FileInputStream("input.txt"));
	}
	
	public void closeScanner() {
		sc.close();
	}
	
	public int numberTestCase() {
	/* Questo metodo legge la prima riga del file, e ritorna il numero di testcase che contiene. */
		String s = sc.nextLine();
		return Integer.parseInt(s);
	}
	
	public Net createNet() {
	/* Questo metodo si occupa del parsing di ogni test case, si assume che il cursore sia posizionato all'inizio della prima linea del testcase. 
	 * Ritorna il net creato, dopo aver:
	 * aggiunto ogni stazione al net;
	 * aggiunto ogni treno al net, ed aver ordinato le fermate dei treni nelle varie stazioni in ordine temporale.
	 */
		/* Leggo il nome del testcase */
		String testcase = sc.nextLine();				
		
		/* Leggo il numero di stazioni presenti nel testcase e vado a capo. */
		int n = Integer.parseInt(sc.nextLine());
		
		/* Genero la rispettiva Net. */
		Net net = new net.Net(testcase, n);
		
		/* Per ogni stazione: */
		for (int i = 0; i < n; i++) {
			
			/* Leggo codice e numero di treni per la stqzione: */
			String s = sc.nextLine();
			
			/* Ricavo il codice della stazione */
			int code = Integer.parseInt(s.split(" ")[0]);
			/* Creo il nodo riguardante la stazione */
			Station station = new net.Station(code);
			/* Aggiungo la stazione alla rete */
			net.addStation(code, station);
			
			/* E aggiungo ogni treno, alla stazione; vado a capo. */
			int k = Integer.parseInt(s.split(" ")[1]);
			
			for (int j = 0; j < k; j++) {
				s = sc.nextLine();
				
				/* Leggo il codice del treno. */
				int tcode = Integer.parseInt(s.split(" ")[0]);
				Train t = net.getTrain(tcode);
				
				/* Se il treno ancora non esiste: */
				if (t == null) {
					t = new Train(tcode);
				}
				
				/* Aggiungo il treno al Net */
				net.addTrain(tcode, t);
				
				/* Leggo gli orari di arrivo e di partenza */
				Short[] ttable = { Short.parseShort(s.split(" ")[1]), Short.parseShort(s.split(" ")[2]) };
				
				/* Aggiungo gli orari del treno alla tabella della stazione. */
				station.putTrain(tcode, ttable);
				
				/* Aggiungo al treno la stazione in cui passa, controllando se è capolinea o meno. */
				if (ttable[1] == -1)
					t.addStop(t.new TrainATime(code, ttable[0], true));
				else
					t.addStop(t.new TrainATime(code, ttable[0], false));				
			}
		}

		/* A questo punto, avendo finito di leggere treni e stazioni, possiamo ordinare nel modo corretto le fermate dei treni,
		 * ed iniziare a sfruttare le HashMap come strutture dati per la prossima fermata, più efficienti. */
		for (Train t : net.getTrains().values()) {
			t.orderStops();
			t.fillNextStops();
		}
		
		/* Leggo il numero di richieste: */
		int r = Integer.parseInt(sc.nextLine());

		/* Per ogni richiesta: */
		for (int i = 0; i < r; i++) {
			String s = sc.nextLine();
			String[] split = s.split(" ");
			
			if (split[0].equals("MINTEMPO")) {
				net.addRequest(new util.MinTempo(Integer.parseInt(split[1]), Integer.parseInt(split[2])));
			} else if (split[0].equals("MINCAMBI")) {
				net.addRequest(new util.MinCambi(Integer.parseInt(split[1]), Integer.parseInt(split[2])));
			} else {
				net.addRequest(new util.MinOrario(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Short.parseShort(split[3])));
			}
		}
		
		return net;
	}
}
