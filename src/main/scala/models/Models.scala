package models

import utils.Constants.NOT_A_NUMBER

import scala.collection.immutable
import scala.util.Try

object Models {

  trait SensorMeasurement

  case class RawSensorMeasurement(sensorId: String, humidity: String) extends SensorMeasurement {
    def isValidAndSuccessMeasurement: Boolean = canCastHumidityToInt(humidity) && (humidity.toInt >= 0 && humidity.toInt <= 100)

    def isFailedMeasurement: Boolean = humidity == NOT_A_NUMBER
  }

  case class SuccessSensorMeasurement(sensorId: String, humidity: Int) extends SensorMeasurement

  case class FailedSensorMeasurement(sensorId: String, humidity: String) extends SensorMeasurement

  private def canCastHumidityToInt(humidity: String) = Try(humidity.toInt).isSuccess

  trait SensorStatistic

  case class SensorHumidityStat(sensorId: String, minHumidity: Int, avgHumidity: Int, maxHumidity: Int) extends SensorStatistic

  case class NaNSensorHumidityStat(sensorId: String, minHumidity: String, avgHumidity: String, maxHumidity: String) extends SensorStatistic

  case class SensorStatistics(numberOfFilesProcessed: Int, numberOfMeasurementsProcessed: Int,
                              numberOfMeasurementsFailed: Int, statistics: immutable.Seq[SensorStatistic])

}
