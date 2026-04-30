---
layout: default
title: Processo di sviluppo
parent: Report
nav_order: 2
---

# Processo di sviluppo

Per il processo di sviluppo abbiamo scelto di seguire una
metodologia *Agile*, in particolare abbiamo adottato 
**Scrum** come framework di lavoro. 

## Scrum

Abbiamo suddiviso il progetto in *sprint* della durata di una 
settimana/dieci giorni, in cui andremo a stilare una lista delle 
attività da completare (relative ad una o più *epiche*) basandoci sulla loro
priorità. La divisione delle attività è stata effettuata in
modo da garantire un flusso di lavoro continuo e una 
consegna regolare di prodotti funzionanti. Si è cercato
di bilanciare il carico di lavoro tra i membri del team, 
tenendo conto delle competenze e delle preferenze 
individuali, cercando di tenere un filo logico del lavoro
prodotto in tutto il progetto.

## Primo incontro (Sprint Planning)

Sono stati definiti i ruoli di ciascun membro del team:

- Product owner: Lorenzo Zanetti
- Committente: Fabio Pedrini
- Responsabile del testing: Alessandro Zanzi
- Sviluppatori: tutti i membri del team

Il team ha redatto un [product backlog]({% link process/product-backlog.md %})
nel quale si è tenuto traccia dei task, indicando per ciascuno
il grado di difficoltà di progettazione e/o implementazione
e l’effort richiesto in ciascuno sprint.
Il product owner è responsabile della gestione del backlog, assicurandosi
che sia sempre aggiornato e che le attività siano ben definite e prioritarizzate.
Il committente è responsabile di fornire feedback e indicazioni sulle funzionalità da 
sviluppare, garantendo l'usabilità e qualità del prodotto.
Il responsabile del testing si occupa di definire e 
implementare le strategie di test, assicurando che il prodotto sia affidabile e privo di bug.

Durante le riunioni di *sprint review* andremo a 
discutere di eventuali problemi riscontrati, come 
risolverli ed eventuali modifiche da apportare al backlog.

I *meeting* sono un fattore fondamentale per il processo di
sviluppo, avvengono con cadenza quasi giornaliera, di 
persona o in chiamata su Discord e con durate differenti in
base all’importanza.

Negli **incontri successivi**, saranno definiti i task da completare in ciascuno sprint,
assegnando le attività ai membri del team. In ciascuno sprint
verrà redatto uno sprint backlog ([primo sprint backlog]({% link process/sprint-1-backlog.md %}))
in modo da tenere traccia delle attività da completare e del loro stato di avanzamento.
Ci siamo posti come obiettivo quello di fornire una
versione funzionante del prodotto alla fine di ogni sprint,
in modo da poter ricevere feedback tempestivi e apportare
eventuali modifiche al backlog in base alle esigenze
riscontrate.

### Continuous Integration

L’intero progetto è stato gestito tramite **GitHub**.
È stata adottata una strategia di branching basata sul *Github Flow*. 
Il branch **main** sarà la fonte di verità e da esso verranno creati 
altri branch per lo sviluppo di nuove funzionalità, per la 
correzione di bug e per il refactoring del codice. 
I possibili branch che possono essere creati da main sono:
- feature/*
- fix/*
- refactor/*

Ogni branch dovrà essere sottoposto a una *pull request*
prima di essere unito al branch main.
Una funzionalità di gioco viene definita completata quando,
a seguito di una revisione da parte di un altro componente
del team o in pair programming, viene pubblicata sul branch
principale di sviluppo.
A questo fanno eccezione operazioni di configurazione e/o 
di documentazione che possono essere direttamente prodotte 
insieme e messe sul main.

## Strumenti di test, build e formattazione

Per il testing si è scelto di utilizzare **ScalaTest** come framework 
di automazione, essendo una tecnologia matura e ben integrata 
nell’ecosistema Scala. 
Come build tool è stato scelto **sbt**, in quanto nasce specificatamente 
per Scala e offre un'ottima gestione delle dipendenze. 
Inoltre, è stato utilizzato **scalafmt** per formattare automaticamente 
il codice sorgente rendendolo coerente e standardizzato 
all’interno del team.