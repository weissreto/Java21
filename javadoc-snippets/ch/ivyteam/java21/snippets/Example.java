package ch.ivyteam.java21.snippets;

import java.util.List;

public class Example {

  /**
   * Example:
   * {@snippet :
   *   var snippet = Example.snippet();
   * }
   */
  public static String snippet() {
    return null;
  }


  /**
   * Example:
   * {@snippet :
   *   var snippet = Example.highlight(); // @highlight substring="Example"
   * }
   */
  public static String highlight() {
    return null;
  }

  /**
   * Example:
   * {@snippet :
   *   var snippet = Example.replace("Hello"); // @replace regex='".*"' replacement='"World"'
   * }
   */
  public static String replace(String str) {
    return null;
  }

  /**
   * Example:
   * {@snippet :
   *   var snippet = Example.highlight(); // @link substring="Example" target="Example"
   * }
   */
  public static String link() {
    return null;
  }

  /**
   * Example:
   * {@snippet :
   *   List<String> snippet = Example.generics();
   * }
   */
  public static List<String> generics() {
    return null;
  }

  /**
   * First Example:
   * {@snippet class="ch.ivyteam.java21.snippets.Example" region="FirstExample"}
   * Second Example:
   * {@snippet class="ch.ivyteam.java21.snippets.Example" region="SecondExample"}
  */
  public static String region() {
    // @start region="FirstExample"
    List<String> snippet = Example.generics();
    // @end

    System.out.println("Hello World");

    //@start region="SecondExample"
    var snip = Example.region();
    //@end

    return null;
  }

  /**
   * Example:
   * {@snippet class="ch.ivyteam.java21.snippets.ShowExample" region="Example"}
   */
  public static String externalRegions() {
    return null;
  }

  /**
   * Example:
   * {@snippet file="ch/ivyteam/java21/snippets/Example.properties"}
   */
  public static String externalFiles() {
    return null;
  }
}

