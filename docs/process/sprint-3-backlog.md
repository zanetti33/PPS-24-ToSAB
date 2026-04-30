---
layout: default
title: Sprint 3 backlog
parent: Processo Scrum
nav_order: 4
---

# Sprint 3 - Backlog

14/04/2026

| Task / Issue        | ID  | Epica  | Ore stimate | Presa in carico da |
|---------------------|-----|--------|-------------|--------------------|
| Piazzamento IA      | 2.4 | Mappa  | 8           | Alessandro Zanzi   |
| Logica di Attacco   | 3.4 | Motore | 8           | Lorenzo Zanetti    |
| Morte e Pulizia     | 3.5 | Motore | 5           | Lorenzo Zanetti    |
| Condizione di Fine  | 3.6 | Motore | 5           | Lorenzo Zanetti    |
| Sistema di Logging  | 4.2 | I/O    | 5           | Fabio Pedrini      |
| Output a Terminale  | 4.3 | I/O    | 8           | Fabio Pedrini      |

La sprint è focalizzata sul completamento del combattimento start-to-finish e della relativa cronaca 
testuale.

## Obiettivi sprint

- Implementare la logica di piazzamento IA, creando un 
algoritmo che posizioni automaticamente le truppe nemiche 
sulla mappa in modo strategico, rispettando le stesse 
regole di posizionamento dell'utente.
- Sviluppare la logica di attacco, implementando una 
funzione che calcoli i danni inflitti da un'unità a un'altra, 
tenendo conto delle statistiche di attacco e difesa.
- Implementare la logica di "morte e pulizia", creando un 
sistema che rimuova le unità con HP <= 0 dalla griglia 
e dalla coda dei turni, garantendo che il combattimento 
proceda senza intoppi.
- Sviluppare la condizione di fine partita, implementando 
un controllo ricorsivo che verifichi se una fazione ha 
vinto o perso, fermando il loop di gioco quando una delle 
due fazioni è completamente eliminata.
- Implementare un sistema di logging, creando un meccanismo
per tracciare gli eventi chiave del combattimento (come 
spostamenti, attacchi, danni ed eliminazioni) in modo 
strutturato.
- Sviluppare un output a terminale, creando una stampa 
formattata della cronaca dello scontro che permetta all'utente 
di seguire l'evoluzione del combattimento.

## Sprint review e risultati ottenuti
Alla fine di questa sprint, il team ha presentato una 
versione del gioco in cui le truppe nemiche vengono 
posizionate automaticamente da un IA. Dopo un confronto si 
è optato per la suddivisione della mappa in fasce in modo 
da facilitare il piazzamento strategico dell'IA (truppe con 
più vita avanti e quelle con meno vita dietro).
La logica di attacco è stata implementata e le unità 
vengono rimosse correttamente quando i loro HP sono <= a 0.
La condizione di fine partita è stata finalizzata, 
permettendo al combattimento di terminare quando una fazione è stata
completamente eliminata. 
Inoltre, è stata valutata la possibilità che lo scontro finisca in 
pareggio qualora si raggiunga un numero prestabilito di turni. 
Il sistema di logging è stato sviluppato, tracciando gli eventi chiave 
del combattimento ed è stato implementato l'output a terminale, 
fornendo una cronaca testuale dello scontro.
