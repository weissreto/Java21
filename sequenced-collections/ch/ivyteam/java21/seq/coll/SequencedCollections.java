package ch.ivyteam.java21.seq.coll;

import java.util.ArrayList;
import java.util.List;

public class SequencedCollections {
  private final List<String> persons = new ArrayList<>(List.of("Bruno", "Christian", "Lukas", "Elio", "Reguel", "Tobias", "Fabian", "Reto", "Jens", "Louis", "Alex", "Lukas"));

  public void first() {
    var first = persons.getFirst();
    System.out.println(first);

    persons.addFirst("Claudia");
    first = persons.getFirst();
    System.out.println(first);

    persons.removeFirst();
    first = persons.getFirst();
    System.out.println(first);
  }

  public void last() {
    var last = persons.getLast();
    System.out.println(last);

    persons.addLast("Claudia");
    last = persons.getLast();
    System.out.println(last);

    persons.removeLast();
    last = persons.getLast();
    System.out.println(last);
  }

  public void reversed() {
    System.out.println(persons);
    var reversed = persons.reversed();
    System.out.println(reversed);
  }

  public static void main(String[] args) {
    new SequencedCollections().first();
    new SequencedCollections().last();
    new SequencedCollections().reversed();
  }

}
