package services

import java.io.File

import models.Errors
import models.Errors.ServiceException
import models.Models.{FailedSensorMeasurement, RawSensorMeasurement, SensorMeasurement, SuccessSensorMeasurement}
import services.SensorReportService.SensorMeasurementEither
import utils.Constants.CSV_FILE_EXTENSION

import scala.io.BufferedSource

class SensorReportService {
  def getSensorMeasurements(reportsPath: String): (Seq[SensorMeasurementEither], Int) = {
    val reportFiles = getReportFiles(reportsPath)
    val sensorMeasurementsWithErrors = reportFiles.flatMap(reportFile => generateSensorMeasurementModel(reportFile))

    (sensorMeasurementsWithErrors, reportFiles.size)
  }

  private def getReportFiles(reportsPath: String): Seq[File] =
    new File(reportsPath).listFiles.filter(_.getName.endsWith(CSV_FILE_EXTENSION)).toSeq

  private def generateSensorMeasurementModel(reportFile: File) = {
    val bufferedSource: BufferedSource = io.Source.fromFile(reportFile)
    val reportLines: Iterator[String] = bufferedSource.getLines().drop(1)

    reportLines.map {
      case s"$sensorId,$humidity" => sensorModel(sensorId, humidity)
      case err => Left(Errors.COULD_NOT_READ_REPORT_LINE(s"Could not read report line: $err"))
    }
  }

  private def sensorModel(sensorId: String, humidity: String): SensorMeasurementEither = {
    val rawSensorMeasurement = RawSensorMeasurement(sensorId, humidity)

    rawSensorMeasurement match {
      case measurement if measurement.isValidAndSuccessMeasurement => Right(SuccessSensorMeasurement(measurement.sensorId, measurement.humidity.toInt))
      case measurement if measurement.isFailedMeasurement => Right(FailedSensorMeasurement(measurement.sensorId, measurement.humidity))
      case _ => Left(Errors.COULD_NOT_READ_MEASUREMENT(s"Could not read measurement: $rawSensorMeasurement"))
    }
  }
}

object SensorReportService {
  type SensorMeasurementEither = Either[ServiceException, SensorMeasurement]
}
