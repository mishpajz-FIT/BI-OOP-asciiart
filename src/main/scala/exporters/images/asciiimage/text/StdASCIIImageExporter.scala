package exporters.images.asciiimage.text

/**
  * [[ImageExporter]] that writes characters of [[ASCIIPixel]] [[Image]] to standard output.
  */
class StdASCIIImageExporter extends StreamASCIIImageExporter(System.out) {}
