package com.pluralsight.flink

import com.ariskk.flink4s.StreamExecutionEnvironment
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.slf4j.Logger

object StreamingSquareRoots {
  private val LOG: Logger = org.slf4j.LoggerFactory.getLogger(getClass)

  final case class Counter(id: String, count: Int)
  implicit val typeInfoOfCounter: TypeInformation[Counter] = TypeInformation.of(classOf[Counter])

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val source: KafkaSource[String] = KafkaSource
      .builder[String]
      .setBootstrapServers("kafka:9092")
      .setTopics("numbers")
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(new SimpleStringSchema())
      .build

    val stream: DataStreamSource[String] =
      env.javaEnv.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source")

    val r = stream
      .map(x => Math.sqrt(x.toDouble))

    r.print()

    env.javaEnv.execute("StreamingSquareRoots")

    LOG.info(s"what is going on? $r")
  }
}
