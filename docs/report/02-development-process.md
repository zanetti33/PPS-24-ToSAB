## Processo di sviluppo

Per il processo di sviluppo abbiamo scelto di seguire una metodologia Agile, in particolare abbiamo adottato Scrum come framework di lavoro. 
Abbiamo suddiviso il progetto in epiche e all'inizio di una specifica sprint andremo a stilare delle attività (relative ad una o più epiche) basandoci sulla loro priorità.
Durante le riunioni di sprint review andremo a discutere di eventuali problemi riscontrati, di come risolverli e di eventuali modifiche da apportare al backlog.

Per la strategia di versionamento abbiamo adottato una strategia di branching basata sul Github Flow. Il branch main sarà la fonte di verità e da esso verranno creati altri branch per lo sviluppo di nuove funzionalità, per la correzione di bug e per il refactoring del codice. Ogni branch dovrà essere sottoposto a una pull request prima di essere unito al branch main.
A questo fanno eccezione operazioni di configurazione e/o di documentazione che possono essere direttamente prodotte insieme e committate su main.

Nel primo incontro abbiamo quindi provveduto alla redazione del [product-backlog](../process/product-backlog.md) e del backlog relativo alla prima sprint ([backlog prima sprint](../process/sprint-1-backlog.md)).