package exporters.asciiimage

import java.io.{File, FileOutputStream}

class ASCIIImageFileExporter(file: File)
    extends ASCIIImageStreamExporter(new FileOutputStream(file)) {}
