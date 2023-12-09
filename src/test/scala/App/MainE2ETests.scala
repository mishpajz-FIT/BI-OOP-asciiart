package App

import org.scalatest.{FlatSpec, Matchers}

import java.io.{ByteArrayOutputStream, File, PrintStream}
import java.nio.file.Paths
import scala.io.Source
import scala.util.Using

class MainE2ETests extends FlatSpec with Matchers {
  behavior of "Main"

  private val correctStatus = "ASCIIArt by Michal Dobes"

  // https://stackoverflow.com/questions/15411728/scala-process-capture-standard-out-and-exit-code
  private def captureOutput(command: => Unit): String = {
    val oldOut = System.out
    val newOut = new ByteArrayOutputStream()
    val outWriter = new PrintStream(newOut)
    System.setOut(outWriter)

    try {
      command
      newOut.toString()
    } finally System.setOut(oldOut)
  }

  private def runTestAgainstFile(
    args: Array[String],
    imagePath: String,
    resultPath: String) = {
    val testImageUri =
      getClass.getClassLoader.getResource(imagePath).toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString

    val resultFile = File.createTempFile("e2etestresult", ".txt")
    resultFile.deleteOnExit()

    val sourceArgs = Array(
      "--image",
      testImagePath,
      "--output-file",
      resultFile.getAbsolutePath)

    val status = captureOutput(Main.main(args.appendedAll(sourceArgs)))

    val correctResultUri =
      getClass.getClassLoader.getResource(resultPath).toURI
    val correctResultPath = Paths.get(correctResultUri).toAbsolutePath.toString

    val result = Using(Source.fromFile(resultFile)) { resource =>
      resource.getLines().mkString("\n")
    }.getOrElse(fail("Couldn't read output string"))

    val correctResult = Using(Source.fromFile(correctResultPath)) { resource =>
      resource.getLines().mkString("\n")
    }.getOrElse(fail("Couldn't read correct result string"))

    result shouldBe correctResult
    status should include(correctStatus)
  }

  private def runTestShouldHaveStatus(
    args: Array[String],
    statusText: String) = {
    val status = captureOutput(Main.main(args))

    status should include(statusText)
  }

  it should "run --image e2e/emoji1.png --output-file tempFile (asciiart from file to a text file)" in
    runTestAgainstFile(
      Array.empty[String],
      "e2e/emoji1.png",
      "e2e/results/emoji1-normal.txt")

  it should "run --image e2e/emoji1.png --output-file tempFile --custom-table \"* \" (asciiart from file to a text file with custom table)" in {
    val args = Array("--custom-table", "* ")

    runTestAgainstFile(
      args,
      "e2e/emoji1.png",
      "e2e/results/emoji1-custom-table.txt")
  }

  it should "run --image e2e/emoji1.png --output-file tempFile --table grayramp (asciiart from file to a text file with named table)" in {
    val args = Array("--table", "grayramp")

    runTestAgainstFile(
      args,
      "e2e/emoji1.png",
      "e2e/results/emoji1-named-table.txt")
  }

  it should "run --image e2e/emoji1.png --output-file tempFile --scale 4 --flip X --flip Y --invert --brightness -100 --invert (asciiart from file to a text file with filters)" in {
    val args = Array(
      "--scale",
      "4",
      "--flip",
      "X",
      "--flip",
      "Y",
      "--invert",
      "--brightness",
      "-100",
      "--invert")

    runTestAgainstFile(args, "e2e/emoji1.png", "e2e/results/emoji1-filters.txt")
  }

  it should "run --image e2e/pixels1.jpg --output-console --scale 0.25 --flip Y --brightness 15 --custom-table \"abcd\" (asciiart from file to a console with filters and custom table)" in {
    val testImageUri =
      getClass.getClassLoader.getResource("e2e/pixels1.jpg").toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString

    val args = Array(
      "--image",
      testImagePath,
      "--output-console",
      "--scale",
      "0.25",
      "--flip",
      "Y",
      "--brightness",
      "15",
      "--custom-table",
      "abcd")

    val result = captureOutput(Main.main(args))

    result shouldBe
      """bb
        |bd
        |ASCIIArt by Michal Dobes
        |""".stripMargin
  }

  it should "run --image-random --output-file tempFile (random asciiart)" in {
    val resultFile = File.createTempFile("e2etestresult", ".txt")
    resultFile.deleteOnExit()

    val args =
      Array("--image-random", "--output-file", resultFile.getAbsolutePath)

    val status = captureOutput(Main.main(args))

    status should include(correctStatus)

    Using(Source.fromFile(resultFile)) { source =>
      val lines = source.getLines().toList
      assert(lines.nonEmpty, "File should not be empty")

      assert(lines.length == 300, "File has wrong amount of lines")

      assert(
        lines.forall(line => line.length == 300),
        "Lines have wrong amount of characters")
    }
  }

  it should "fail without input source" in {
    val args = Array("--output-console")

    runTestShouldHaveStatus(args, "At least one importer")
  }

  it should "fail with too much input sources" in {
    val testImageUri =
      getClass.getClassLoader.getResource("e2e/pixels1.jpg").toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString

    val args =
      Array("--image-random", "--image", testImagePath, "--output-console")

    runTestShouldHaveStatus(args, "Only single importer")
  }

  it should "fail with no output source" in {
    val args = Array("--image-random")

    runTestShouldHaveStatus(args, "At least one exporter")
  }

  it should "fail with nonexisting input parameter" in {
    val args = Array("--image", "nonexistingimage", "--output-console")

    runTestShouldHaveStatus(args, "is not usable file or doesn't exist")
  }

  it should "fail with wrong input parameter" in {
    val testImageUri =
      getClass.getClassLoader.getResource("test/directory1").toURI

    val args = Array("--image", testImageUri.toString, "--output-console")

    runTestShouldHaveStatus(args, "is not usable file or does't exist")
  }

  it should "fail with wrong filter parameter" in {
    val args = Array("--image-random", "--flip", "Z", "--output-console")

    runTestShouldHaveStatus(args, "Unknown axis for flipping")
  }

  it should "fail with wrong table parameter" in {
    val args =
      Array("--image-random", "--table", "nonexistent", "--output-console")

    runTestShouldHaveStatus(args, "Unknown ASCII conversion table")
  }
}
