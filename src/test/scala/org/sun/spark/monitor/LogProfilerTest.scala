package org.sun.spark.monitor

import java.util.concurrent.TimeUnit

import org.testng.Assert
import org.testng.annotations.Test

class LogProfilerTest {

  @Test
  def testWatch(): Unit = {
    val profiler = new LogProfiler("hello: ")
    val a =
      profiler.watch {
        TimeUnit.SECONDS.sleep(5)
        5
      }
    Assert.assertEquals(a, 5)
  }

}
