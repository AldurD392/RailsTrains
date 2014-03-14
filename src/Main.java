import java.io.FileNotFoundException;
import java.io.IOException;

import net.Net;

import util.MinCambi;
import util.MinOrario;
import util.MinTempo;
import util.Request;

public class Main {
	/* La classe main non fa altro che leggere il file testcase per testcase, 
	 * creare di volta in volta le relative reti,
	 * risolvere le relative richieste e
	 * scrivere sul file di output. */

	public static void main(String[] args) {
		io.Parser parser;
		io.Writer writer;
		
		try {
			/* Creo l'oggetto parser */
			parser = new io.Parser();
			
			/* Creo l'oggetto writer */
			writer = new io.Writer();
			
			/* Leggo il numero di test case contenuti. */
			int n = parser.numberTestCase();	
			
			/* Inizializzo la rete: */
			Net net;
			
			/* Per ogni testcase */
			for (int i = 0; i < n; i++) {
				/* Popolo la rete: */
				net = parser.createNet();
				
				/* Aggiungo nel buffer il numero del testcase: */
				writer.addBuffer(net.getTestCase()+"\n");
				
				/* Creo un grafo adatto a risolvere i problemi MINTEMPO e MINORARIO: */
				net.createMinTempoGraph();
				
				/* Per ogni richiesta di tipo MinOrario o MinTempo: */
				for (Request r : net.getRequests()) {
					if (r instanceof MinOrario || r instanceof MinTempo) {
						/* La risolvo, ed aggiungo la soluzione al buffer di output. */
						r.Solve(net);
						writer.addBuffer(r.getSolution());
					}
				}
				
				/* Creo un grafo adatto a risolvere i problemi MINCAMBI */
				net.createMinCambiGraph();
				
				/* Infine, per ogni MinCambi: */
				for (Request r : net.getRequests()) {
					if (r instanceof MinCambi) {
						/* Risolvo, ed aggiungo la soluzione al buffer di output. */
						r.Solve(net);
						writer.addBuffer(r.getSolution());
					}
				}
			}
			
			/* Chiudo lo scanner del parser: */
			parser.closeScanner();

			/* Infine, scrivo il buffer e lo chiudo. */
			writer.writeBuffer();
			writer.closeWriter();
			
			/* Ed esco con exit code affermativo. */
			System.exit(0);
			
		} catch (FileNotFoundException e) {
			/* Se il file di input non è stato trovato, stampo un messaggio di errore ed esco. */
			System.out.println("Impossibile caricare il file di input.\n" +
					"Assicurarsi che si trovi nella stessa cartella dell'eseguibile e sia nominato input.txt.");
			System.exit(1);
		} catch (IOException e) {
			/* Se è stato impossibile scrivere il file di output: */
			System.out.println("Impossibile scrivere il file di output. Assicurarsi di avere i permessi sufficenti.");
			System.exit(2);
		}
	}
}
