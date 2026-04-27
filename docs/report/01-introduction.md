# Town of Saviom Auto-Battler

- Pedrini Fabio (fabio.pedrini3@studio.unibo.it)
- Zanetti Lorenzo (lorenzo.zanetti5@studio.unibo.it)
- Zanzi Alessandro (alessandro.zanzi2@studio.unibo.it)

## Introduzione
Il progetto consiste in un gioco auto-battler,
sviluppato in Scala, che simula combattimenti tra unità di 
due fazioni (**Player** e **AI**) su una griglia esagonale.
Le entità in gioco possono essere **Obstacles** o **Characters**, i 
quali a loro volta si dividono in più ruoli.
Ogni ruolo presenta varie caratteristiche (es. tipo di 
danno, range di attacco), le quali, insieme ad altre 
statistiche (es. HP, velocità, distanza di movimento),
definiscono il comportamento di ogni unità durante 
la simulazione del combattimento.

All'avvio dell'applicazione, l'utente e un'IA
posizionano le truppe sul proprio lato della griglia, senza 
conoscere la posizione delle truppe o degli ostacoli nel lato 
avversario. Dopo questa fase, i personaggi inizieranno a muoversi 
e combattere autonomamente, a turni.

La partita prosegue fino a quando non si arriva
all'eliminazione di un'intera fazione, che decreta la
vittoria della fazione rimanente, o fino a quando non si termina il numero
prestabilito di turni giungendo a un pareggio.