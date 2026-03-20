| ID  | Epica     | Task / Issue            | Priorità  | Descrizione Sintetica                                                 |
|-----|-----------|-------------------------|-----------|-----------------------------------------------------------------------|
| 1.1 | Dominio   | Statistiche Base        | Alta      | Modellazione statistiche per HP, Attacco, Difesa, Velocità, Range.    |
| 1.2 | Dominio   | Tipologie Unità         | Alta      | Gerarchie per unità Melee, Ranged, ecc.                               |
| 1.3 | Dominio   | Fazioni                 | Alta      | Struttura dati per Giocatore vs IA e relativi team.                   |
| 1.4 | Dominio   | Tipi di Danno           | Alta      | Modellazione danni fisici, magici e calcolo resistenze.               |
| 2.1 | Griglia   | Struttura Mappa         | Alta      | Mappa 2D immutabile.                                                  |
| 2.2 | Griglia   | Generazione Procedurale | Alta      | Algoritmo per posizionare ostacoli o terreni casuali.                 |
| 2.3 | Griglia   | Piazzamento Manuale     | Alta      | Logica per validare e inserire unità alleate sulla mappa.             |
| 2.4 | Griglia   | Piazzamento IA          | Alta      | Disposizione automatica e valida delle truppe nemiche.                |
| 3.1 | Motore    | GameState Globale       | Altissima | Container immutabile per Griglia, Fazioni, Turno e Coda.              |
| 3.2 | Motore    | Inizializzazione Turni  | Altissima | Ordinamento unità in base alla Velocità per il round.                 |
| 3.3 | Motore    | Movimento / Pathfinding | Altissima | Logica di avvicinamento ai nemici (es. distanza Manhattan).           |
| 3.4 | Motore    | Logica di Attacco       | Altissima | Funzione pura di calcolo danni (Unità + Attacco -> Unità modificata). |
| 3.5 | Motore    | Morte e Pulizia         | Altissima | Filtraggio unità con HP <= 0 dalla griglia e dalla coda.              |
| 3.6 | Motore    | Condizione di Fine      | Altissima | Controllo ricorsivo di vittoria/sconfitta per fermare il loop.        |
| 4.1 | I/O       | Game Loop               | Media     | Ciclo State -> State che fa avanzare il combattimento.                |
| 4.2 | I/O       | Sistema di Logging      | Media     | Tracciamento eventi (spostamenti, danni, eliminazioni).               |
| 4.3 | I/O       | Output a Terminale      | Media     | Stampa formattata della telecronaca dello scontro.                    |
| 5.1 | Opzionale | GUI Minimale            | Bassa     | Rappresentazione grafica 2D dell'arena e delle unità.                 |
| 5.2 | Opzionale | Economia / Drafting     | Bassa     | Negozio pre-battaglia per acquistare unità con valuta.                |
| 5.3 | Opzionale | Effetti: Cure           | Bassa     | Unità guaritrici che bersagliano gli alleati feriti.                  |
| 5.4 | Opzionale | Effetti: Buff/Debuff    | Bassa     | Alterazioni di stato temporanee o passive (es. Spine, Veleno).        |
