package org.sun.spark.monitor

trait Profiler extends Serializable {
  def start(): Unit
  def stop(): Unit
}
