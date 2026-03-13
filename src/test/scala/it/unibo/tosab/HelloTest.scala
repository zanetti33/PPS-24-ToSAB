package it.unibo.tosab

import it.unibo.tosab.Hello
import org.junit.Assert.*
import org.junit.Test

class HelloTest {
  @Test def testGreet(): Unit = {
    val hello = new Hello()
    assertEquals("Hello, World!", hello.greet())
  }
}
