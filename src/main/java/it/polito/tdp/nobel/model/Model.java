package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> esami;    //imput   con anche numero crediti che passo in metodo
	
	private Set<Esame> migliore;     //qua c'è soluzione temporanea migliore
	
	private double mediaMigliore;   //peso soluzione migliorre con nome lista
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();   //riempo lista esami con esami in database
	}

	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {            //OUTPUT
		  //ripristino soluzione migliore  -->   prepara per funzione ricorsiva
		migliore = new HashSet<Esame>();
		mediaMigliore = 0.0;                   //Impostare basso per andare a ricercare migliore da aggiornare
		
		Set<Esame> parziale = new HashSet<Esame>();            //Carichiamo e svuotiamo ogni passaggio       //Stesso tipo di milgliore (set)
		//cerca1(parziale,0,numeroCrediti);            //SBAGLIATO perche troppo costoso       //Scende nei vari livelli e quando lo trova ritorna migliore       //Passiamo LIVELLO 0
		cerca2(parziale,0,numeroCrediti);                                                   //Scende nei vari livelli e quando lo trova ritorna migliore       //Passiamo LIVELLO 0
		return migliore;	
	}
	
	//MIGLIORE
	private void cerca2(Set<Esame> parziale, int L, int numeroCrediti) {
		//Controllare casi temirnali per fermrci prima o poi senno loop infinito
	    // 1) Caso temrinale che finiamo prima
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > numeroCrediti) {       //Soluzione non valida
			return;
		}
		else if(sommaCrediti == numeroCrediti) {          
			//Soluzione valida --> controlliamo se è la migliore fino a qui  --> OTTIMIZZAZIONE
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {               //Soluzione promettente
				migliore = new HashSet<Esame>(parziale);             //facciamo copia di parziale       
				mediaMigliore = mediaVoti;
			}
			return;
		}
		
		// Se arriviamo qui i crediti < numeroCrediti       //Puo ancora diventare soluzione valida
		if(L == esami.size()) {      //non ho piu nulla da aggiungere
			return;
		}
		
		//provo ad aggiungere esami[L]
		parziale.add(esami.get(L));
		cerca2(parziale,L+1,numeroCrediti);
		
		//provo a non aggiungere esami[L]
		parziale.remove(esami.get(L));
		cerca2(parziale,L+1,numeroCrediti);
		
		
	}

	//STUPIDO
	//Complessita di calcolo notevole per tanti crediti
	
	private void cerca1(Set<Esame> parziale, int L, int numeroCrediti) {
		//Controllare casi temirnali per fermrci prima o poi senno loop infinito
	    // 1) Caso temrinale che finiamo prima
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > numeroCrediti) {       //Soluzione non valida
			return;
		}
		else if(sommaCrediti == numeroCrediti) {          //Soluzione valida --> controlliamo se è la migliore fino a qui
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {               //Soluzione promettente
				migliore = new HashSet<Esame>(parziale);             //facciamo copia di parziale       
				mediaMigliore = mediaVoti;
			}
			return;
		}
		
		// Se arriviamo qui i crediti < numeroCrediti       //Puo ancora diventare soluzione valida
		if(L == esami.size()) {      //non ho piu nulla da aggiungere
			return;
		}
		
		//Generiamo i sotto-problemi perche abbiamo esami da inserire
		for(Esame e : esami) {
			if(!parziale.contains(e)) {
				parziale.add(e);                       //aggiunta  --->   poi backtraking
				cerca1(parziale,L+1,numeroCrediti);
				parziale.remove(e);                    //Solo con set remove perche con set sempre solo 1 invece con lista [parziale.size()-1]
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
