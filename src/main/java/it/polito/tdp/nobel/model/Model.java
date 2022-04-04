package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> esami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		/*
		 * 2 Approcci:
		 * 1. Ad ogni livello L della ricorsione, inserisco un esame in PARZIALE. Devo decidere quale --> li
		 * 		provo tutti
		 * 		Approccio stupido perchè prova un sacco di volte le stesse soluzioni
		 * 2. Generare i sottoproblemi 1 per volta, scorrendo gli esami di partenza in ordine, decidendo per
		 * 		ogni esame se debba essere inserito o no;
		 * 		Il livello della ricorsione coincide con l'esame da considerare
		 */
		
		migliore = new HashSet<Esame>();
		mediaMigliore = 0.0;
		
		Set<Esame> parziale = new HashSet<Esame>();
		//cercaNooby(parziale, 0, numeroCrediti);
		cercaPro(parziale, 0, numeroCrediti);
		return migliore;	
	}

	/*
	 * Approccio intelligente (Complessità 2^N)
	 */
	private void cercaPro(Set<Esame> parziale, int L, int m) {
		
		int sommaCrediti = sommaCrediti(parziale);
		
		if(sommaCrediti > m)
			return;
		
		if(sommaCrediti == m) {
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				migliore = new HashSet<Esame>(parziale);
				mediaMigliore = mediaVoti;
			}
			return;
		}
		
		if(L == esami.size())
			return;
		
		//Provo ad aggiungere esami[L];
		parziale.add(esami.get(L));
		cercaPro(parziale, L+1, m);
		
		//Provo a non aggiungere esami[L];
		parziale.remove(esami.get(L));
		cercaPro(parziale, L+1, m);
	}

	/*
	 * Approccio stupido (Complessità N!):
	 */
	private void cercaNooby(Set<Esame> parziale, int L, int m) {
		
		int sommaCrediti = sommaCrediti(parziale);
		
		if(sommaCrediti > m)
			return;
		
		if(sommaCrediti == m) {
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				migliore = new HashSet<Esame>(parziale);
				mediaMigliore = mediaVoti;
			}
			return;
		}
		
		if(L == esami.size())
			return;
		
		for(Esame e : esami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cercaNooby(parziale, L+1, m);
				parziale.remove(e);
			}
		}
		
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
