package ch.ivyteam.java21.ffm;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Path;

public class FFM {

  static void memory() {
    // A global Arena is opened with the Java program and lives as long as the program runs.
    // It is closed at the end of the Java program.
    // Memory allocated in a Arena will be deallocated when the Arena is closed.
    Arena global = Arena.global();
    MemorySegment hugeSegment = global.allocate(8_000_000_000L);

    for (long byteOffset = 0; byteOffset < hugeSegment.byteSize(); byteOffset++) {
      byte value = (byte) (byteOffset % 256);
      // Writes a byte value to the memory segment
      // Address of the value is hugeSegment.address() + byteOffset
      // because because set and get methods works with byte offset
      hugeSegment.set(ValueLayout.JAVA_BYTE, byteOffset, value);
    }

    long maxIndex = hugeSegment.byteSize() / ValueLayout.JAVA_INT.byteSize();
    for (long index = 0; index < maxIndex; index = index + 1_000_001L) {
      // Reads an int value from the memory segment.
      // Address of the value is hugeSegment.address() + ( index * ValueLayout.JAVA_INT.byteSize() )
      // because getAtIndex and setAtIndex works with layout sized indices
      int value = hugeSegment.getAtIndex(ValueLayout.JAVA_INT, index);
      System.out.print(index);
      System.out.print(": ");
      System.out.println(value);
    }
  }

  static void filemap() throws IOException {
    // Create a new file 'dump.txt' and opens a file channel
    try (FileChannel fileChannel = FileChannel.open(Path.of("dump.txt"), CREATE_NEW, WRITE, READ)) {

      // Maps the file into a 8 GB memory segment
      MemorySegment memory = fileChannel.map(MapMode.READ_WRITE, 0, 8_000_000_000L, Arena.global());
      for (long index = 0; index < memory.byteSize(); index++) {

        // set each byte of the memory segment with a character
        byte value = (byte)('A' + (index % 24));
        memory.set(ValueLayout.JAVA_BYTE, index, value);
      }
      // force to write the memory back into the file
      memory.force();
    }
  }

  static void strlen() throws Throwable {
    // Provides an arena that can be used by the current thread only
    try (Arena arena = Arena.ofConfined()) {
      // Provides lookup for all libraries that are loaded by the JVM itself inclusive the standard C library
      SymbolLookup libs = Linker.nativeLinker().defaultLookup();
      // Lookup the strlen function
      MemorySegment strlenSymbol = libs
              .find("strlen")
              .orElseThrow(() -> new IllegalStateException("strlen function not found"));

      // Links the strlen function to a method handle to call it
      FunctionDescriptor strlenSignature = FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS);
      MethodHandle strlen = Linker.nativeLinker().downcallHandle(strlenSymbol, strlenSignature);

      String hello = "Hello";
      // Allocates native memory and stores the given string in it
      MemorySegment helloInNativeMemory = arena.allocateUtf8String(hello);

      // Calls the strlen function in the C standard library
      long len = (long) strlen.invokeExact(helloInNativeMemory);

      System.out.println("Length of " + hello + " is " + len);
    }
  }


  public static void main(String[] args) throws Throwable {
    memory();
    filemap();
    strlen();
  }
}
