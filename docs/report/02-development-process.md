## Processo di sviluppo

Per il processo di sviluppo abbiamo scelto di seguire una
metodologia *Agile*, in particolare abbiamo adottato 
*Scrum* come framework di lavoro. 

Abbiamo suddiviso il progetto in *epiche*; effettuando 
*sprint*, della durata di una settimana/dieci giorni, all'
inizio delle quali, andremo a stilare delle attività 
(relative ad una o più epiche) basandoci sulla loro
priorità. La divisione delle attività è stata effettuata in
modo da garantire un flusso di lavoro continuo e una 
consegna regolare di funzionalità funzionanti. Si è cercato
di bilanciare il carico di lavoro tra i membri del team, 
tenendo conto delle competenze e delle preferenze 
individuali, cercando di tenere un filo logico del lavoro
prodotto in tutto il progetto.

Il product owner del team ha redatto un product backlog nel
quale si è tenuto traccia dei task, indicando per ciascuno
il grado di difficoltà di progettazione e/o implementazione
e l’effort richiesto in ciascuno sprint.

Ci siamo posti come obiettivo quello di fornire una 
versione funzionante del prodotto alla fine di ogni sprint,
in modo da poter ricevere feedback tempestivi e apportare
eventuali modifiche al backlog in base alle esigenze 
riscontrate.

Durante le riunioni di sprint review andremo a 
discutere di eventuali problemi riscontrati, come 
risolverli ed eventuali modifiche da apportare al backlog.

I *meeting* sono un fattore fondamentale per il processo di
sviluppo, avvengono con cadenza quasi giornaliera, di 
persona o in chiamata su Discord e con durate differenti in
base all’importanza.

Per la metodologia di versionamento abbiamo adottato una 
strategia di branching basata sul *Github Flow*. Il branch 
**main** sarà la fonte di verità e da esso verranno creati 
altri branch, per lo sviluppo di nuove funzionalità, per la 
correzione di bug e per il refactoring del codice. 

Ogni branch dovrà essere sottoposto a una *pull request*
prima di essere unito al branch main.
Una funzionalità di gioco viene definita completata quando,
a seguito di una revisione da parte di un altro componente
del team o in pair programming, viene pubblicata sul branch
principale di sviluppo.
A questo fanno eccezione operazioni di configurazione e/o 
di documentazione che possono essere direttamente prodotte 
insieme e messe sul main.

- main
- feature/*
- fix/*
- refactor/*

Nel primo incontro abbiamo quindi provveduto alla redazione
del [product-backlog](../process/product-backlog.md) e del backlog relativo alla 
prima sprint ([backlog prima sprint](../process/sprint-1-backlog.md)). Sono stati 
definiti i ruoli di ciascun membro del team, con un focus 
particolare sul product owner(Lorenzo Zanetti), che si è 
occupato di mantenere il backlog aggiornato e di definire 
le priorità delle attività; il committente(Fabio Pedrini),
che ha fornito feedback e indicazioni durante tutto il 
processo di sviluppo; e il responsabile del testing
(Alessandro Zanzi), che ha definito i criteri di 
accettazione per le funzionalità sviluppate e ha 
coordinato le attività di testing.

Per supportare il processo agile, il team utilizza 
strumenti volti a migliorare l’efficienza e a concentrarsi 
sullo sviluppo. Il progetto ha integrato pratiche moderne 
per il deployment e la manutenzione, rientranti nella 
*continuous integration/delivery*.
Da cambiare con i nostri
Continuous Integration (CI): Il workflow pr-test.yml esegue i test su ogni pull request aperta o sincronizzata sul branch main dal branch develop. Questo garantisce l’integrità continua del progetto e da la possibilità a tutti gli sviluppatori se una pull request ha fatto passare o meno tutti i test.
Continuous Delivery (CD)/Deployment: Il workflow release.yml gestisce il rilascio automatico del progetto. Si attiva su tag semantici (es. v*.*.*) e produce un JAR eseguibile (ToSAB-assembly-*.jar) utilizzando sbt assembly, che viene poi caricato come release su GitHub.
