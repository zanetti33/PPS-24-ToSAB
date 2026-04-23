# Town of Saviom Auto-Battler

- Pedrini Fabio (fabio.pedrini3@studio.unibo.it)
- Zanetti Lorenzo (lorenzo.zanetti5@studio.unibo.it)
- Zanzi Alessandro (alessandro.zanzi2@studio.unibo.it)

## Introduzione
Il progetto consiste in un piccolo gioco auto-battler,
sviluppato in Scala, che simula combattimenti tra unità di
due fazioni(**Player** ed **AI**) su una griglia esagonale.
Le entità possono essere **Obstacles** o **Characters**, i 
quali a loro volta si dividono in più ruoli.
Ogni ruolo presenta varie caratteristiche (es. tipo di 
danno, range di attacco); queste, insieme ad altre 
statistiche(es. HP, velocità, distanza di movimento),
definiscono il comportamento di ogni unità durante 
la simulazione del combattimento.

All'avvio dell'applicazione, l'utente e un'IA
posizionano le proprie truppe sul lato della griglia a 
loro dedicato, senza sapere la posizione delle truppe
avversarie; successivamente, al via, queste, a turni,
inizieranno a muoversi e combattere autonomamente.

La partita prosegue fino a quando non si arriva
all'eminazione dell'intera fazione nemica, che decreta la
vittoria/sconfitta, o fino a quando non si termina il numero
prestabilito di turni giungendo ad un pareggio.