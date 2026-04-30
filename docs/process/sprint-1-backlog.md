---
layout: default
title: Sprint 1 backlog
parent: Processo Scrum
nav_order: 2
---

# Sprint 1 - Backlog

24/03/2026

| Task / Issue          | ID  | Epica   | Ore stimate | Presa in carico da         |
|-----------------------|-----|---------|-------------|----------------------------|
| Definizione requisiti | 0.1 | #       | 4           | Tutti (definizione comune) |
| Design architetturale | 0.2 | #       | 4           | Tutti (definizione comune) |
| Statistiche Base      | 1.1 | Dominio | 8           | Fabio Pedrini              |
| Tipologie Unità       | 1.2 | Dominio | 8           | Fabio Pedrini              |
| Struttura Mappa       | 2.1 | Mappa   | 8           | Alessandro Zanzi           |
| GameState Globale     | 3.1 | Motore  | 5           | Lorenzo Zanetti            |
| Main Bootstrap        | 0.3 | #       | 5           | Tutti                      |

## Scelte tecnologiche iniziali
- Il team ha deciso di non concentrarsi inizialmente 
sull'interfaccia grafica.
- La prima versione sarà eseguita da terminale (CLI), 
stampando la griglia della partita e ricevendo input da 
tastiera per inserire le truppe.
- L'intero sviluppo verrà realizzato in modo da poter
facilmente aggiungere una GUI in futuro.

## Obiettivi sprint
- Definire i requisiti funzionali e non funzionali del
progetto, in modo da avere una chiara comprensione di ciò
che deve essere realizzato e delle aspettative del cliente.
- Progettare l'architettura del sistema, scegliendo un 
pattern di progettazione adatto (es. Model-View-Update) e
definendo le interfacce tra i componenti principali.
- Implementare le funzionalità di base del dominio, come
la modellazione delle unità e delle statistiche.
- Creare la struttura di base per la mappa e il GameState, 
in modo da avere un framework su cui costruire le 
funzionalità di gioco.
- Stabilire una base di codice solida e ben organizzata, 
con una strategia di versionamento chiara e un processo di
sviluppo collaborativo efficace.

## Sprint review e risultati ottenuti
Alla fine di questa sprint, il team ha presentato una
versione iniziale del gioco, con una mappa di base, unità
modellate e un GameState funzionante. 

Durante la review, sono stati discussi la struttura della 
griglia e le caratteristiche delle varie entità. Il 
feedback ricevuto è stato positivo, con alcune 
raccomandazioni per migliorare la struttura del codice e 
per iniziare a pensare alla logica di gioco e al sistema di
turni per la prossima sprint.