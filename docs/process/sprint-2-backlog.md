# Sprint 2 - Backlog

04/04/2026

| Task / Issue                | ID   | Epica   | Ore stimate | Presa in carico da |
|-----------------------------|------|---------|-------------|--------------------|
| Generazione Procedurale     | 2.2  | Mappa   | 8           | Alessandro Zanzi   |
| Modellazione Entità         | 1.5* | Dominio | 8           | Fabio Pedrini      |
| Inizializzazione Turni      | 3.2  | Motore  | 5           | Lorenzo Zanetti    |
| Movimento / Pathing         | 3.3  | Motore  | 8           | Lorenzo Zanetti    |
| Piazzamento Manuale         | 2.3  | Mappa   | 8           | Alessandro Zanzi   |
| Interazione per piazzamento | 4.4* | I/O     | 5           | Fabio Pedrini      |
*non presente nel backlog iniziale

## Obiettivi sprint
- Implementare la generazione procedurale della mappa, 
creando un algoritmo che posizioni ostacoli in modo 
casuale, garantendo al contempo la giocabilità della mappa.
- Modellare le entità di gioco, definendo classi o 
strutture per le unità, le fazioni e le statistiche, 
assicurandosi che siano flessibili e facilmente 
estendibili.
- Implementare l'inizializzazione dei turni, creando una 
logica che ordini le unità in base alla loro velocità 
all'inizio di ogni round, stabilendo l'ordine di 
azione per il combattimento.
- Sviluppare la logica di movimento e pathfinding, 
implementando un algoritmo che permetta alle unità di 
avvicinarsi strategicamente ai nemici.
- Implementare la funzionalità di piazzamento manuale, 
consentendo all'utente di posizionare le unità sulla mappa,
con una logica che validi le posizioni in base alle regole 
di gioco (ad esempio, non permettere di posizionare unità 
su caselle occupate o fuori dal proprio lato della mappa).
- Sviluppare un'interazione per il piazzamento delle unità,
creando un sistema di input da terminale che permetta all'utente 
di selezionare le unità e posizionarle sulla mappa 
in modo intuitivo, con feedback visivo o testuale per 
confermare le azioni.

## Sprint review e risultati ottenuti
Alla fine di questa sprint, il team ha presentato un sistema 
funzionante che permette all'utente di inserire manualmente le 
proprie truppe in una mappa generata proceduralmente
(con ostacoli posizionati in casualmente). 
Le unità sono state suddivise in Personaggi e Ostacoli (definendone le
statistiche) ed è stata implementata la logica di inizializzazione 
dei turni, ordinando le unità in base alla loro velocità. 
La logica di movimento è stata sviluppata, 
permettendo alle unità di avvicinarsi ai nemici. 

Durante la review, è stato discusso il funzionamento del 
sistema di piazzamento manuale e sono stati raccolti 
feedback per migliorare l’interazione utente e per iniziare
a pensare alla logica di attacco e al sistema di turni per 
la prossima sprint.