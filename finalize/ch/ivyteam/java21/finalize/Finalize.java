package ch.ivyteam.java21.finalize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Finalize {

  private final OutputStream outputStream;

  public Finalize() {
    try {
      this.outputStream = Files.newOutputStream(Path.of("C:\temp\test.txt"));
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  public static void main(String[] argv) {
    var fin = new Finalize();
  }

  /**
   * Called by gc before the object is garbage collected.
   * Instead use {@link java.lang.ref.Cleaner}
   * @see java.lang.Object#finalize()
   */
  @Override
  protected void finalize() throws Throwable {
    outputStream.close();
    super.finalize();
  }
}
