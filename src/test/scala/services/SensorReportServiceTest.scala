package services

import models.Errors.{COULD_NOT_READ_MEASUREMENT, COULD_NOT_READ_REPORT_LINE}
import models.Models.{FailedSensorMeasurement, SuccessSensorMeasurement}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.ArraySeq

class SensorReportServiceTest extends AnyFunSpec with Matchers {
  describe("Sensor Report Service") {
    val service = new SensorReportService

    val actual = service.getSensorMeasurements

    it("should return valid sensor measurements") {
      val expected = ArraySeq(Right(SuccessSensorMeasurement("s2", 80)), Right(FailedSensorMeasurement("s3", "NaN")),
        Right(SuccessSensorMeasurement("s2", 78)), Right(SuccessSensorMeasurement("s1", 98)),
        Right(SuccessSensorMeasurement("s1", 10)), Right(SuccessSensorMeasurement("s2", 88)), Right(FailedSensorMeasurement("s1", "NaN")))

      actual._1.collect({ case Right(value) => Right(value) }) shouldEqual expected
    }

    it("should catch errors while reading from report file") {
      val expected = COULD_NOT_READ_REPORT_LINE(s"Could not read report line: s4100")

      actual._1.collect({ case Left(value) => value })(1) shouldEqual expected
    }

    it("should catch errors when measurements are out of range") {
      val expected = COULD_NOT_READ_MEASUREMENT("Could not read measurement: RawSensorMeasurement(s5,1000)")

      actual._1.collect({ case Left(value) => value }).head shouldEqual expected
    }
  }
}
