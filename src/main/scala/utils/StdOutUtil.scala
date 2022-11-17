package utils

import models.Errors.{COULD_NOT_READ_MEASUREMENT, COULD_NOT_READ_REPORT_LINE, ServiceException}
import models.Models.{NaNSensorHumidityStat, SensorHumidityStat, SensorStatistics}

trait StdOutUtil {
  def printSensorStats(sensorStats: SensorStatistics): Seq[Unit] = {
    println(s"Number of processed files: ${sensorStats.numberOfFilesProcessed}")
    println(s"Number of processed measurements: ${sensorStats.numberOfMeasurementsProcessed}")
    println(s"Number of failed measurements: ${sensorStats.numberOfMeasurementsFailed}")
    println(s"Sensors with highest avg humidity:")
    println("sensor-id, min, avg, max")
    sensorStats.statistics.collect {
      case NaNSensorHumidityStat(sensorId, minHumidity, avgHumidity, maxHumidity) => println(s"$sensorId, $minHumidity, $avgHumidity, $maxHumidity")
      case SensorHumidityStat(sensorId, minHumidity, avgHumidity, maxHumidity) => println(s"$sensorId, $minHumidity, $avgHumidity, $maxHumidity")
    }
  }

  def printServiceExceptions(serviceExceptions: Seq[ServiceException]): Seq[Unit] = {
    serviceExceptions.collect {
      case COULD_NOT_READ_MEASUREMENT(message) => println(message)
      case COULD_NOT_READ_REPORT_LINE(message) => println(message)
    }
  }
}
