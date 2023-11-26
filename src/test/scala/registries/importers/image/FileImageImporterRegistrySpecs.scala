package registries.importers.image

import importers.image.FileImageImporter
import importers.image.inputstream.{JPEGFileImageImporter, PNGFileImageImporter}
import models.image.Image
import models.pixel.RGBAPixel
import org.scalatest.{FlatSpec, Matchers}

import java.io.File
import java.nio.file.Paths
import scala.util.Try

class FileImageImporterRegistrySpecs extends FlatSpec with Matchers {
  behavior of "FileImageImporterRegistry"

  class MockImporter(override val file: File) extends FileImageImporter {
    override def retrieve(): Try[Image[RGBAPixel]] =
      throw new NotImplementedError("mock importer")
  }

  it should "retrieve preregistered importers correctly" in {
    val testImageUri =
      getClass.getClassLoader.getResource("test/small-img3.jpeg").toURI
    val testImagePath = Paths.get(testImageUri).toAbsolutePath.toString
    val imageFile = new File(testImagePath)

    val png = FileImageImporterRegistry.get("png")
    png.isDefined shouldBe true
    png.get(imageFile) shouldBe a[PNGFileImageImporter]

    val jpg = FileImageImporterRegistry.get("jpg")
    jpg.isDefined shouldBe true
    jpg.get(imageFile) shouldBe a[JPEGFileImageImporter]

    val jpeg = FileImageImporterRegistry.get("jpeg")
    jpeg.isDefined shouldBe true
    jpeg.get(imageFile) shouldBe a[JPEGFileImageImporter]

    FileImageImporterRegistry
      .list() should contain theSameElementsAs Seq("png", "jpg", "jpeg")
  }

  it should "register and retrieve importers correctly" in {
    val mockImporter = (file: File) => new MockImporter(file)

    FileImageImporterRegistry.register("test", mockImporter)

    FileImageImporterRegistry.get("test").isDefined shouldBe true
    FileImageImporterRegistry.get("png").isDefined shouldBe true

    FileImageImporterRegistry.unregister("test")
  }

  it should "when using list return importers correctly" in {
    val list = FileImageImporterRegistry.list()
    val namesSet = list.toSet

    namesSet shouldNot contain("test")
    namesSet should contain("png")
  }

  it should "when retrieving importer return None for unregistered format" in {
    FileImageImporterRegistry.get("nonexistent").isEmpty shouldBe true
  }

  it should "unregister importers correctly" in {
    val mockImporter = (file: File) => new MockImporter(file)

    FileImageImporterRegistry.register("test", mockImporter)

    FileImageImporterRegistry.get("test").isDefined shouldBe true

    FileImageImporterRegistry.unregister("test")

    FileImageImporterRegistry.get("test").isDefined shouldBe false
  }
}
