package registries.importers.image

import importers.image.FileImageImporter
import models.image.Image
import models.pixel.RGBAPixel
import org.scalatest.{FlatSpec, Matchers}

import java.io.File

class FileImageImporterRegistrySpecs extends FlatSpec with Matchers {
  behavior of "FileImageImporterRegistry"

  private class MockImporter(override val file: File)
      extends FileImageImporter {
    override def retrieve(): Option[Image[RGBAPixel]] =
      throw new NotImplementedError("mock importer")
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
