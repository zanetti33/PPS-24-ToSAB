## Testing

### Tecnologie utilizzate
Il sistema è stato testato utilizzando due framework 
principali, entrambi pienamente compatibili con *sbt*, 
che hanno consentito lo sviluppo di test unitari e di 
integrazione efficaci:

- **JUnit**, impiegato inizialmente per scrivere test 
semplici e rapidi durante le prime fasi di sviluppo;

- **ScalaTest**, introdotto successivamente per testare in 
modo più approfondito e idiomatico le componenti core del 
sistema, sfruttando appieno le potenzialità del linguaggio 
Scala. L’adozione di ScalaTest ha inoltre favorito un 
approccio più espressivo e modulare, in linea con la 
struttura del progetto.

### Metodologia di testing
È stato adottato un approccio **TDD** (Test-Driven 
Development) durante lo sviluppo di componenti chiave.

I test sono serviti come specifica del comportamento 
atteso. Questo approccio ha favorito maggiore stabilità, 
sicurezza nella rifattorizzazione e chiarezza nel design.

I test sono stati progettati per coprire una vasta
gamma di scenari, inclusi casi normali, casi limite e
condizioni di errore, garantendo così una robusta
copertura del codice.

### Copertura dei test
Per misurare il grado di copertura, è stato usato 
**scoverage**, uno strumento integrabile con sbt che 
ha reso possibile generare un report di copertura.

Ogni modulo è stato accompagnato dalla scrittura di test
prima della sua implementazione, fatta eccezione per la
sezione della View, in cui si è valutato direttamente 
l'output visivo fornito.

Il grado di copertura dei test è stato monitorato con 
l’obiettivo di testare il più possibile ogni elemento del 
progetto. 
In questo modo siamo riusciti a identificare e colmare 
eventuali lacune nei test e nelle implementazioni, garantendo 
una solida base per tutte le componenti del sistema. 

La copertura è stata misurata in tutti i file, assicurando 
che tutte le possibili condizioni e scenari siano stati 
adeguatamente testati.