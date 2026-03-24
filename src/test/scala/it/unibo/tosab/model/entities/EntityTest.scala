package it.unibo.tosab.model.entities

import org.junit.Test
import org.junit.Assert.*
import it.unibo.tosab.model.entities.Entity

class EntityTest:

  @Test def entityExistTest(): Unit =
    val expectedValue = 1
    val e1 = Entity(expectedValue)
    assertEquals(expectedValue, e1.value)