package org.sun.spark.monitor

trait Profiler extends Serializable {
  def start(): Unit
  def stop(): Unit
  def watch[T](expr: => T): T = {
    start()
    try {
      expr
    } finally {
      stop()
    }
  }
}
