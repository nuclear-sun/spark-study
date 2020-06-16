package org.sun.spark

import java.io.File

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object CheckpointAndCache {

  def main(args: Array[String]): Unit = {

    val tmpPath = "output" + File.separator

    val conf: SparkConf = new SparkConf()
      .setAppName("test")
      .setMaster("local")
      .set("spark.testing.memory","2147480000")

    val sc = new SparkContext(conf)
    sc.setCheckpointDir(tmpPath)

    val rdd: RDD[Int] = sc.parallelize(1 to 1000)
    val kv: RDD[(Int, Int)] = rdd.map(x => (x % 2, x))

    //kv.persist(StorageLevel.MEMORY_ONLY_SER) // 序列化后节省约40%存储空间
    kv.persist()
    kv.checkpoint()


    val sum: RDD[(Int, Int)] = kv.reduceByKey(_ + _)
    sum.foreach(println) // job0

    val grouped: RDD[(Int, Iterable[(Int, Int)])] = kv.groupBy(_._1)
    grouped.foreach(println) //job1

    Thread.sleep(1000000)

  }

}
