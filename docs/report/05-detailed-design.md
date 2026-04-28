# Design di Dettaglio

L’architettura del sistema è progettata per garantire una chiara separazione delle responsabilità. 
La struttura è divisa logicamente nei tre moduli principali del pattern MUV, 
con una forte enfasi sulla *composizione*, *immutabilità* e *Domain-Driven Design*.

## Model (Dominio e Stato)

Il Model è il cuore del gioco e contiene le entità e le regole spaziali. 
Le classi principali sono state modellate sfruttando le potenzialità di Scala 3 (es. `enum`, `extension methods`).

### Entità di Gioco (Entities)
Le entità sono modellate tramite un *Algebraic Data Type* (ADT) chiuso, 
garantendo un controllo rigoroso a compile-time tramite il trait `sealed`.

```mermaid 
classDiagram
direction TB

    class Entity {
        <<sealed_trait>>
        +String id
    }

    class Character {
        <<case_class>>
        +Faction faction
        +Role role
    }

    class Obstacle {
        <<case_class>>
        +Option~Int~ hp
        +Boolean isPassable
        +Boolean blocksVision
    }

    class Stats {
        <<case_class>>
    }

    %% Ereditarietà (Sum Type)
    Entity <|-- Character
    Entity <|-- Obstacle

    %% Composizione (Product Type)
    Character *-- Stats : has 
```

La suddivisione tra `Character` e `Obstacle` permette di 
applicare pattern differenti: un ostacolo può essere passabile o meno (`isPassable`) e 
può opzionalmente ricevere danni (`Option[Int] hp`). 
L'uso dell'Option esplicita a livello di tipo se un ostacolo è distruttibile o meno, 
eliminando i controlli sui `null`.

### Griglia e Posizioni
La mappa di gioco (`Grid`) rappresenta un punto focale. 
Invece di avere una classe monolitica, la classe `Grid` usa il pattern 
Delegation / Composition per orchestrare logiche complesse 
(movimento esagonale, visuale, collisioni).

```mermaid
classDiagram
    class Grid {
        +Int size
        +Map~Coordinate, Entity~ cells
        +setCell(Entity, Coordinate) Grid
        +moveEntity(EntityId, Coordinate) Grid
        +isLineOfSightClear(Coordinate, Coordinate) Boolean
    }
    
    class GridManager {
        <<trait>>
        +getDistance(Coordinate, Coordinate) Int
        +isWithinBounds(Coordinate) Boolean
    }
    
    class HexagonalGrid {
        <<class>>
    }
    
    class PlacementManager
    class HexagonalLineOfSightManager
    class HexagonalGridMovement
    class ObstacleManager

    Grid --> GridManager : delegates to
    GridManager <|.. HexagonalGrid : implements
    Grid *-- PlacementManager
    Grid *-- HexagonalLineOfSightManager
    Grid *-- HexagonalGridMovement
    Grid *-- ObstacleManager
```

La `Grid` espone un'interfaccia pulita ma internamente delega le responsabilità
(es. `HexagonalLineOfSightManager` calcola il raggio visivo). 
Essendo una `case class` immutabile, operazioni come `moveEntity` ritornano una nuova istanza di 
`Grid` con la mappa `cells` aggiornata.



### Characters AI
Un modulo che gestisce l'intelligenza artificiale dei personaggi,
decidendo le azioni da intraprendere in base allo stato del gioco.
Invece di adottare pesanti gerarchie orientate agli oggetti, il design di questo
modulo sfrutta un approccio marcatamente funzionale, combinando Pattern Strategy e Chain of Responsibility
attraverso funzioni pure e immutabili.

```mermaid
classDiagram
    class CharacterAI {
        <<trait>>
        +determineNextAction(GameState, EntityId) GameAction
    }
    
    class ConfigurableCharacterAI {
        <<class>>
        -behaviors: Seq~Behavior~
        +determineNextAction(...) GameAction
    }
    
    class Behavior {
        <<type_alias>>
        +(GameState, Character, Coordinate) => Option~GameAction~
    }
    
    class Behaviors {
        <<object>>
        +attackClosestEnemy: Behavior
        +moveTowardsClosestEnemy: Behavior
        +attackOrMoveToClosestEntity: Behavior
    }
    
    class Pathfinder {
        <<object>>
        +reachableCellsWithin(Grid, startPos, maxSteps) Set~Coordinate~
        +bestReachableTowardsTarget(...) Option~Coordinate~
    }

    CharacterAI <|.. ConfigurableCharacterAI : implements
    ConfigurableCharacterAI o-- Behavior : evaluates in order
    Behaviors ..> Behavior : defines strategies
    Behavior ..> Pathfinder : uses for navigation
```


### Lo Stato di Gioco (GameState)

Il `GameState` aggrega la griglia, la coda dei turni (`turnQueue`) e la fase attuale (`GamePhase`).
È l'unica "fonte di verità" immutabile passata all'`Engine`.

## Update (Logica di Gioco)
Il modulo di Update ruota attorno al trait `Engine`, che funge da risolutore di azioni (`Action Resolver`).

```mermaid
classDiagram
    class EngineOutcome {
        +GameState nextState
        +Seq~DomainEvent~ events
    }

    class CommandIntent {
        +EntityId actorId
        +GameAction action
    }

    class Engine {
        <<trait>>
        +startNewRound(GameState) GameState
        +applyUnitAction(GameState, CommandIntent) EngineOutcome
    }
    
    class PipelineEngine {
        <<abstract>>
        +resolveAction() EngineOutcome
    }
    
    class TurnBasedCombatEngine {
        <<object>>
        +resolveAction() EngineOutcome
    }

    Engine <|.. PipelineEngine
    PipelineEngine <|-- TurnBasedCombatEngine
    Engine ..> EngineOutcome : produces
    Engine ..> CommandIntent : consumes
```

1. `EngineOutcome`: invece di mutare direttamente l'ambiente, l'`Engine` restituisce un oggetto che contiene 
il nuovo stato e una lista di eventi di dominio (`DomainEvent`). 
Questo disaccoppia la computazione dalla comunicazione: la View può ispezionare gli eventi 
(es. `GameEnded`, `DamageDealt`) per riprodurre animazioni, log o suoni.
2. `TurnBasedCombatEngine`: rappresenta l'implementazione concreta, progettata con un approccio a _Pipeline_. 
Il comando viene destrutturato tramite pattern matching sull'`action` (`Pass`, `Move`, `Attack`) 
per poi essere smistato agli `ActionResolvers` specifici, validando le regole di combattimento.

    
### Design del Update

L'Update agisce da coordinatore centrale, gestendo il
flusso del gioco e orchestrando le interazioni tra Model e
View.
Il componente **GameSetup** si occupa di inizializzare lo
stato del gioco, mentre il GameLoop gestisce l'evoluzione
del gioco, processando gli input e aggiornando il Model di
conseguenza.
Il **GameLoop** è progettato per essere efficiente e
reattivo, garantendo un'esperienza di gioco fluida, anche
con un numero elevato di unità e interazioni in corso.


## View (Input/Output)
La View si appoggia agli eventi restituiti dall'`EngineOutcome` e interroga il `GameState` 
in sola lettura per effettuare il rendering.
Le classi (es. `DisplayGrid`, `ConsoleGameLogger`) operano interpretando le coordinate esagonali e 
mappandole in un output visivo per l'utente, isolando così l'I/O dal Functional Core del sistema.
