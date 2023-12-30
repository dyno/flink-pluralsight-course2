package com.pluralsight.flink

import com.ariskk.flink4s.StreamExecutionEnvironment
import io.findify.flinkadt.api._
import org.apache.flink.api.common.typeinfo.TypeInformation

import java.util.Properties

object StreamingSquareRoots2 {
  val LOG = org.slf4j.LoggerFactory.getLogger(getClass)

  final case class Counter(id: String, count: Int)
  implicit val typeInfoOfCounter: TypeInformation[Counter] = TypeInformation.of(classOf[Counter])

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")

    val items = (1 to 1000).map(x => s"item-${x % 10}")

    val r = env
      .fromCollection[String](items)
      .map(x => Counter(x, 1))
      .keyBy(_.id)
      .reduce((acc, v) => acc.copy(count = acc.count + v.count))
      .runAndCollect

    // val x = stream.collect[Counter]()

    LOG.info(s"what is going on? $r")
  }
}
