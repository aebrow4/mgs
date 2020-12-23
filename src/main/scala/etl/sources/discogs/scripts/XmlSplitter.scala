package etl.sources.discogs.scripts

import java.io.FileWriter
import scala.io.Source

/**
  *
  * @param inputPath path to input file
  * @param outputPath path to write output files
  * @param fileType one of Artists, Labels, Releases
  *
  *  Split an xml file into chunks.
  *
  */

class XmlSplitter(inputPath: String, outputPath: String, fileType: String) {

  /** The number of lines to read from the input file for each output file */
  val FileSize = 100
  //val fileLength = getFileLength
  val fileLength = 50

  /**
    * We maintain two line counters; one that tracks how many lines from the input we have
    * consumed, and one that tracks how many lines of output we have written. They are not
    * the same because multiple input lines may be joined into a single output line, such
    * that one XML tag of the type we care about (artist, release, label, etc) is written
    * per line.
    * In this way, all output files can contain the same number of records, except for the
    * final file.
    */

  // Current line in input file
  var inputLine = 0
  var currFile = 1

  def split: Unit = {
    while (inputLine <= fileLength) {
      inputLine = writeFile(
        fileType,
        s"$outputPath--${currFile * FileSize}.xml",
        inputLine
      )
      currFile += 1
    }
  }

  /**
    *
    * @param fileName Path of output file
    * @param start Line number of the input file to start writing
    * @return The last line of the input file that was written
    *
    *  Open the input file for reading, and skip to the start line.
    *  Write lines to the output file, sanitizing input and writing
    *  an entire xml record per line.
    */
  def writeFile(fileType: String, fileName: String, start: Int): Int = {
    println(s"writing file $fileName")
    var cursor = 0
    var numRecordsWritten = 0
    val bufferedWriter = new FileWriter(fileName)
    val openPlaceHolderTag = "<container>"
    val closePlaceHolderTag = "</container>"
    bufferedWriter.write(openPlaceHolderTag + " \n")
    val lines: Iterator[String] = Source.fromFile(inputPath).getLines
    while (lines.hasNext && numRecordsWritten < FileSize) {
      if (cursor < start) cursor = skipToStartLine(lines, start, cursor)
      val line = lines.next()
      if (!isLineTag(fileType, line)) {
        val (lineToWrite, newCursor) =
          constructOutputLine(fileType, line, lines, cursor)
        cursor = newCursor
        bufferedWriter.write(lineToWrite)
        numRecordsWritten += 1
      }
      cursor += 1
    }
    bufferedWriter.write(closePlaceHolderTag + " \n")
    bufferedWriter.close()
    cursor
  }

  /**
    *
    * @param fileType Expects one of "Artists", "Releases", "Labels"
    * @param line The initial line
    * @param lines Lines iterator for the file
    * @param cursor The initial position in the input file
    * @return The output line and the new position in the input file
    *
    * Starting from the beginning line of an xml record, read
    * lines until the end of the record is reach, adding each line
    * to the returned buffer.
    */
  private def constructOutputLine(
      fileType: String,
      line: String,
      lines: Iterator[String],
      cursor: Int
  ): (String, Int) = {
    var l = line
    var c = cursor
    var buffer = ""
    val closingTag = fileType.slice(0, fileType.length - 1).toLowerCase
    while (lines.hasNext && !l.contains(s"</$closingTag>")) {
      buffer += sanitizeCarriageReturn(l)
      c += 1
      l = lines.next
    }
    buffer += sanitizeCarriageReturn(l)
    buffer += " \n"
    (buffer, c)
  }

  /**
    *
    * @param lineIterator Line iterator for file
    * @param start Line number to advance to
    * @param cursor Current line number
    * @return
    *
    * Given the current position in the file and the
    * desired location in the file, advance the file iterator
    * to the start position and increment the cursor state.
    */
  private def skipToStartLine(
      lineIterator: Iterator[String],
      start: Int,
      cursor: Int
  ): Int = {
    var c = cursor
    while (c < start) {
      lineIterator.next
      c += 1
    }
    c
  }

  private def sanitizeCarriageReturn(line: String): String = {
    if (line.contains("&#13")) {
      line + ""
    } else {
      line
    }
  }

  /**
    *
    * @param tag Xml tag to search for. e.g. "Artists"
    * @param line Line to search
    * @return
    *
    * Returns true if line contains an opening or closing instance
    * of tag.
    */
  def isLineTag(tag: String, line: String): Boolean = {
    val normalizedTag = tag.toLowerCase
    if (
      line.contains(s"<$normalizedTag>") || line.contains(s"</$normalizedTag>")
    ) {
      true
    } else {
      false
    }
  }

  private def getFileLength: Int = {
    Source.fromFile(inputPath).getLines.size
  }
}
