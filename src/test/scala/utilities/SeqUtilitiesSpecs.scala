package utilities

import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.{FlatSpec, Matchers}
import utilities.SeqUtilities.{
  SeqTryExtensions,
  validateNonEmpty,
  validateSingle
}

import scala.util.{Failure, Success, Try}

class SeqUtilitiesSpecs extends FlatSpec with Matchers {
  behavior of "Seq.sequence"

  it should "return a successful Try with unwrapped sequence for sequence of successful Try" in {
    val seq = Seq(Success(1), Success(2), Success(3))

    val result = seq.sequence

    result.isSuccess shouldBe true
    result.get should contain theSameElementsAs Seq(1, 2, 3)
  }

  it should "fail for sequence of Try with Failure" in {
    val seq = Seq(
      Success(1),
      Failure(new IllegalArgumentException("mock exception")),
      Success(3))

    val result = seq.sequence

    result.isSuccess shouldBe false
    result.failure.exception.getMessage contains "mock exception"
  }

  it should "handle an empty sequence correctly" in {
    val seq = Seq.empty[Try[Int]]

    val result = seq.sequence

    result.isSuccess shouldBe true
    result.get.isEmpty shouldBe true
  }

  it should "fail with first Failure for sequence of Try with multiple Failure" in {
    val seq = Seq(
      Success(1),
      Failure(new IllegalArgumentException("mock exception 1")),
      Failure(new IllegalArgumentException("mock exception 2")))

    val result = seq.sequence

    result.isSuccess shouldBe false
    result.failure.exception.getMessage contains "mock exception 1"
  }

  behavior of "SeqUtilities"

  it should "return Success for empty sequence with validateSingle" in {
    val items = Seq.empty[Int]

    val result = validateSingle(items, "theItem")

    result.isSuccess shouldBe true
  }

  it should "return Success for sequence with one item with validateSingle" in {
    val items = Seq("hello")

    val result = validateSingle(items, "theItem")

    result.isSuccess shouldBe true
  }

  it should "fail for sequence with multiple items with validateSingle" in {
    val items = Seq("hello", "there")

    val result = validateSingle(items, "theItem")

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "single theItem is allowed"
  }

  it should "return Failure for empty sequence with validateNonEmpty" in {
    val items = Seq.empty[Int]

    val result = validateNonEmpty(items, "theItem")

    result.isFailure shouldBe true
    result.failure.exception.getMessage contains "one theItem is required"
  }

  it should "return Success for sequence with one item with validateNonEmpty" in {
    val items = Seq("hello")

    val result = validateNonEmpty(items, "theItem")

    result.isSuccess shouldBe true
  }

  it should "return Success with multiple items with validateNonEmpty" in {
    val items = Seq("hello", "there")

    val result = validateNonEmpty(items, "theItem")

    result.isSuccess shouldBe true
  }
}
