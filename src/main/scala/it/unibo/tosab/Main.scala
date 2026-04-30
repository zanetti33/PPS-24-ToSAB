package it.unibo.tosab

import it.unibo.tosab.model.ai.CharacterAI
import it.unibo.tosab.model.ai.PlacementAI.placeAITroops
import it.unibo.tosab.model.engine.Engine.TurnBasedCombatEngine
import it.unibo.tosab.model.{GamePhase, GameState}
import it.unibo.tosab.model.grid.*
import it.unibo.tosab.update.{GameLoop, GameSetup}
import it.unibo.tosab.view.{ConsoleGameLogger, DisplayGrid}

/** Main entry point for the game application. It initializes the game state, runs the setup phase
  * and then starts the combat phase.
  */
@main def runApp(): Unit =
  val startingGrid = GridFactory.createHexagonal(8).placeObstacles()

  val updatedGrid = placeAITroops(startingGrid)
  val initialState = GameState(GamePhase.Setup, updatedGrid)

  DisplayGrid.displayInitialGrid(initialState.grid)

  val completeGrid = GameSetup.runSetupLoop(updatedGrid).run()
  val updatedState = initialState.copy(grid = completeGrid)
  DisplayGrid.display(updatedState.grid)

  println("\nEnter any key to start combat phase...")
  System.in.read()

  println("\nStarting Combat Phase...")
  GameLoop.subscribe(ConsoleGameLogger)
  GameLoop.run(updatedState)(using CharacterAI.BasicCharacterAI, TurnBasedCombatEngine)
  GameLoop.unsubscribe(ConsoleGameLogger)
