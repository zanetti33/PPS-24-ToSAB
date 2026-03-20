## Requirment specification

### Requisiti di business

- Sviluppare un sistema in grado di simulare lo svolgimento di un compattimento automatico, a turni, tra le unità di due fazioni 
- Rendere possibile ad un utente di vedere una mappa, posizionare le proprie unità sul proprio lato della mappa, e simulare il combattimento contro un avversario (IA)

### Requisiti funzionali

#### Requisiti utente

L'utente deve essere in grado di:

1. Visualizzare una mappa di gioco generata
2. Posizionare le proprie unità sul proprio lato della mappa
3. Avviare la simulazione del combattimento contro un avversario controllato da un'IA
4. Visualizzare l'esito del combattimento
5. Visualizzare le azioni compiute durante la simulazione tramite log testuale
6. Visualizzare le azioni compiute durante la simulazione tramite feedback visivo (opzionale)
7. Scegliere una modalità di gioco alternativa, in questa modalità l'utente potrà (opzionale):
   1. Acquistare unità da un negozio virtuale, utilizzando un sistema di punti
   2. Posizionare le unità acquistate sulla mappa
   3. Avviare la simulazione del combattimento contro un avversario controllato da un'IA
   4. In caso di vittoria, guadagnare punti da spendere nel negozio virtuale per acquistare unità più potenti
   5. In caso di sconfitta verrà eliminato dalla partita, ma potrà ricominciare da capo con un nuovo set di unità acquistate

#### Requisiti di sistema

Il sistema deve essere in grado di: 

1. Generare una mappa di gioco con una griglia di dimensioni predefinite
2. Permettere all'utente di posizionare le unità sulla mappa, rispettando le regole di posizionamento (ad esempio, non posizionare unità su caselle occupate o fuori dal proprio lato della mappa)
3. Le truppe nemiche devo essere piazzate da un'IA, che deve posizionare le unità in modo strategico, rispettando le stesse regole di posizionamento dell'utente
4. Ogni unità deve avere specifiche statistiche:
    1. Punti vita (HP)
    2. Attacco fisico (PD)
    3. Attacco magico (MD)
    4. Difesa fisica (PR)
    5. Difesa magica (MR)
    6. Distanza di movimento (MS)
    7. Velocità (SPD)
    8. Gittata d'attacco (Range)
    9. Tipo di attacco (ad area, a bersaglio singolo, in una linea retta, ecc.)
5. Simulare il combattimento tra le unità posizionate, seguendo regole di combattimento predefinite:
   1. Durante ogni turno, ogni unità può compiere una azione in ordine di velocità decrescente. In caso di pareggio di velocità, l'ordine di sarà determinato dal posizionamento dell'unità sulla mappa, da sinistra a destra e dall'alto verso il basso
   2. Ogni unità può scegliere di compiere una delle seguenti azioni (o nessuna):
      1. attaccare un'unità avversaria
      2. spostarsi di un numero di caselle uguale alla propria distanza di movimento
      3. utilizzare una abilità speciale (opzionale)
   3. Il combattimento termina quando tutte le unità di una fazione sono state eliminate o dopo un numero predefinito di turni (TBD pareggio o vittoria per la fazione con più unità rimaste/più vita)


### Requisiti non funzionali

1. Dare al sistema un'architettura modulare, in modo da facilitare l'estensione e la manutenzione del codice
2. Avere un'interfaccia utente semplice e intuitiva, che permetta all'utente di interagire con il sistema in modo efficace
3. L'IA deve essere in grado di prendere decisioni (discretamente) strategiche basate sulla situazione attuale del combattimento

### Requisiti di implementazione

- JDK 21
- Scala 3.2 o superiore
- TuProlog