package services

import models.Models._
import utils.Constants.NOT_A_NUMBER

class SensorStatsService {
  def getStatistics(sensorMeasurements: Seq[SensorMeasurement], numberOfFilesProcessed: Int): SensorStatistics = {
    val numberOfMeasurementsProcessed = sensorMeasurements.size
    val numberOfMeasurementsFailed = collectFailedSensorMeasurements(sensorMeasurements).length
    val humidityStats = getHumidityStats(collectSuccessSensorMeasurements(sensorMeasurements))
    val humidityStatsOrdered = humidityStats.sortBy(_.avgHumidity)(Ordering[Int].reverse)
    val onlyNaNStats = getNaNStats(sensorMeasurements, humidityStats)

    SensorStatistics(numberOfFilesProcessed, numberOfMeasurementsProcessed, numberOfMeasurementsFailed, humidityStatsOrdered ++ onlyNaNStats)
  }

  private def getAllSensorIds(sensorMeasurements: Seq[SensorMeasurement]) = {
    sensorMeasurements.collect {
      case FailedSensorMeasurement(sensorId, _) => sensorId
      case RawSensorMeasurement(sensorId, _) => sensorId
      case SuccessSensorMeasurement(sensorId, _) => sensorId
    }
  }


  private def getHumidityStats(measurements: Seq[SuccessSensorMeasurement]) = {
    val perSensorMeasurements = measurements.groupBy(_.sensorId)

    perSensorMeasurements.map { case (sensorId, measurements) =>
      val humidityMeasurements = measurements.map(_.humidity)
      val minHumidity = humidityMeasurements.min
      val maxHumidity = humidityMeasurements.max
      val avgHumidity = humidityMeasurements.sum / humidityMeasurements.size

      SensorHumidityStat(sensorId, minHumidity, avgHumidity, maxHumidity)
    }.toSeq
  }

  private def getNaNStats(sensorMeasurements: Seq[SensorMeasurement], succeededMeasurementSensorStats: Seq[SensorHumidityStat]) = {
    val failedMeasurementSensorIds = getAllSensorIds(sensorMeasurements).toSet diff succeededMeasurementSensorStats.map(_.sensorId).toSet
    failedMeasurementSensorIds.map(sensorId => NaNSensorHumidityStat(sensorId, NOT_A_NUMBER, NOT_A_NUMBER, NOT_A_NUMBER)).toSeq
  }

  private def collectSuccessSensorMeasurements(sensorMeasurements: Seq[SensorMeasurement]): Seq[SuccessSensorMeasurement] =
    sensorMeasurements.collect({ case sm@SuccessSensorMeasurement(_, _) => sm })

  private def collectFailedSensorMeasurements(sensorMeasurements: Seq[SensorMeasurement]): Seq[FailedSensorMeasurement] =
    sensorMeasurements.collect({ case fm@FailedSensorMeasurement(_, _) => fm })
}
