## Implementazione - Pedrini Fabio

Il mio contributo al progetto si è concentrato principalmente sulla
progettazione e implementazione delle seguenti sezioni:
- Modellazione del dominio:
    - Definizione delle Entità di gioco (Characters, Obstacles)
      e delle loro caratteristiche.
    - Implementazione delle statistiche dei personaggi.
- Input/Output:
    - Progettazione e implementazione dell'interfaccia utente,
      inclusa la visualizzazione delle unità e del combattimento.
    - Gestione degli input da parte dell'utente per il posizionamento
      delle unità e altre interazioni.

#### Modellazione del dominio:

Per modellare gli elementi che popolano la griglia di gioco, si è scelto
di utilizzare degli Algebraic Data Types (ADT). L'interfaccia base è stata
definita come un sealed trait Entity, generando un dominio chiuso.

Entity-Character-Obstacle:
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
    Character *-- Stats : ha 
```

Damageable:
```mermaid
classDiagram
    direction LR

    class Damageable~T~ {
        <<type_class>>
        +takeDamage(entity: T, amount: DamageInstance) T
    }

    class DamageableCharacter {
        <<given>>
    }

    class DamageableObstacle {
        <<given>>
    }

    class Character {
        <<ADT>>
    }

    class Obstacle {
        <<ADT>>
    }

    Damageable~T~ <|.. DamageableCharacter : implementa
    Damageable~T~ <|.. DamageableObstacle : implementa

    DamageableCharacter --> Character : agisce su
    DamageableObstacle --> Obstacle : agisce su
```

#### Gestione Input/Output

MonadIO, InputParser, Logging, GameSetup, Main

#### Altre sezioni in cui ho collaborato

CharacterAI, Behaviour, Grid/DisplayGrid, LineOfSightManager
