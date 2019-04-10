package org.sun.spark.monitor

import javax.annotation.concurrent.ThreadSafe
import org.slf4j.LoggerFactory

@ThreadSafe
class LogProfiler(prefix: String) extends Profiler {

  private lazy val logger = LoggerFactory.getLogger(getClass)

  private lazy val lastTime: ThreadLocal[Long] = new ThreadLocal[Long]

  override def start(): Unit = {
    val time = System.currentTimeMillis()
    lastTime.set(time)
    logger.info(s"[${Thread.currentThread().getName}]${getClass.getSimpleName} ${prefix} start profiler at ${time}")
  }

  override def stop(): Unit = {
    val stopTime = System.currentTimeMillis()
    //println(s"[${Thread.currentThread().getName}]${getClass.getSimpleName} ${prefix} elapse: ${stopTime - lastTime.get()}")
    logger.info(s"[${Thread.currentThread().getName}]${getClass.getSimpleName} ${prefix} elapse: ${stopTime - lastTime.get()}")
  }
}
