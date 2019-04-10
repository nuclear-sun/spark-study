package org.sun.spark

import java.util.concurrent.TimeUnit

import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory
import org.sun.spark.monitor.{LogProfiler, Profiler}

object Main {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
    val sc = new SparkContext(sparkConf)

    val fakeData = 1 to 30
    val rdd = sc.parallelize(fakeData, fakeData.length)

    val prefix_config = "spark-test: "
    val integerHandler = new IntegerHandler
    val profiler: Profiler = new LogProfiler(prefix_config)
    val proxyHandler = new ProxyHandler(integerHandler, profiler)

    rdd.foreach(value => {
      proxyHandler.handle(value)
      TimeUnit.SECONDS.sleep(3600)
    })
  }
}


trait Handler[T] extends Serializable {
  def handle(t: T): T
}

class IntegerHandler extends Handler[Int] {

  private lazy val logger = LoggerFactory.getLogger(getClass)

  override def handle(t: Int): Int = {
    val result = t*t
    TimeUnit.SECONDS.sleep(t)
    logger.info(s"[${Thread.currentThread().getName}]${getClass.getSimpleName} handled ${t}, returned ${result}")
    result
  }
}

class ProxyHandler[T](private val handler: Handler[T], private val profiler: Profiler) extends Handler[T] {

  override def handle(t: T): T = {
    profiler.start()
    try {
      handler.handle(t)
    } finally {
      profiler.stop()
    }
  }
}



