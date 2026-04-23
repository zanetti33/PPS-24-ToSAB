## Testing

tecnologie usate, grado di copertura, metodologia usata, esempi rilevanti, altri elementi utili

### Tecnologie utilizzate
Il sistema è stato testato utilizzando due framework 
principali; entrambi pienamente compatibili con *sbt*, 
hanno consentito lo sviluppo di test unitari e di 
integrazione efficaci.
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
Development) durante lo sviluppo di componenti chiave:

Ogni modulo è stato accompagnato dalla scrittura di test 
prima della sua implementazione; fatta eccezione per i test
della View e del package *io*, che sono stati valutati 
direttamente attraverso l'analisi dell'output visivo 
fornito. 

I test sono serviti come specifica del comportamento 
atteso. Questo approccio ha favorito maggiore stabilità, 
sicurezza nella rifattorizzazione e chiarezza nel design.

I test sono stati progettati per coprire una vasta
gamma di scenari, inclusi casi normali, edge case e
condizioni di errore, garantendo così una robusta
copertura del codice.

### Copertura dei test
Per misurare il grado di copertura, è stato usato 
**scoverage**, uno strumento integrabile con sbt, 
questo ha reso possibile generare un report di copertura.

...

Il grado di copertura dei test è stato monitorato attentamente durante tutto il processo di sviluppo, con l’obiettivo di raggiungere una copertura superiore al 90% del codice. Grazie all’adozione di una strategia di testing rigorosa e all’utilizzo di strumenti di analisi della copertura, siamo riusciti a identificare e colmare eventuali lacune nei test, garantendo una solida base di test per tutte le componenti del sistema. La copertura è stata misurata sia a livello di linee di codice che a livello di branch, assicurando che tutte le possibili condizioni e scenari siano stati adeguatamente testati.

### Esempi di test rilevanti
Di seguito sono riportati alcuni esempi di test 
significativi che sono stati implementati durante lo 
sviluppo del progetto: