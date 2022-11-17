package utils

import models.Errors.COULD_NOT_READ_REPORT_LINE
import models.Models.{FailedSensorMeasurement, SuccessSensorMeasurement}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.ArraySeq

class AppUtilTest extends AnyFunSpec with Matchers {
  describe("App Util") {

    val appUtil = new AppUtil {}

    it("should extract valid sensor measurements") {
      val sensorMeasurementsWithErrors = ArraySeq(Right(SuccessSensorMeasurement("s2", 80)), Right(FailedSensorMeasurement("s3", "NaN")),
        Right(SuccessSensorMeasurement("s2", 78)), Right(SuccessSensorMeasurement("s1", 98)),
        Right(SuccessSensorMeasurement("s1", 10)), Right(SuccessSensorMeasurement("s2", 88)), Right(FailedSensorMeasurement("s1", "NaN")))
      val actual = appUtil.extractValidSensorMeasurements(sensorMeasurementsWithErrors)
      val expected = ArraySeq(SuccessSensorMeasurement("s2", 80), FailedSensorMeasurement("s3", "NaN"), SuccessSensorMeasurement("s2", 78),
        SuccessSensorMeasurement("s1", 98), SuccessSensorMeasurement("s1", 10), SuccessSensorMeasurement("s2", 88), FailedSensorMeasurement("s1", "NaN"))

      actual shouldEqual expected
    }

    it("should extract service exceptions") {
      val sensorMeasurementsWithErrors = ArraySeq(Left(COULD_NOT_READ_REPORT_LINE(s"Could not read report line: s4100")))
      val actual = appUtil.extractServiceExceptions(sensorMeasurementsWithErrors)
      val expected = ArraySeq(COULD_NOT_READ_REPORT_LINE("Could not read report line: s4100"))

      actual shouldEqual expected
    }
  }
}
