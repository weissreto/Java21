package ch.ivyteam.java21.loom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualThread {

  static void one_million() throws InterruptedException {
    Thread[] threads = new Thread[1_000_000];
    for (int pos = 0; pos < threads.length; pos++) {
      threads[pos] = Thread.ofVirtual().name("Thread " + pos).unstarted(() -> {
        System.out.println(Thread.currentThread().getName());
      });
    }
    for (int pos = 0; pos < threads.length; pos++) {
      threads[pos].start();
    }
    for (int pos = 0; pos < threads.length; pos++) {
      threads[pos].join();
    }
  }

  static void executor() throws InterruptedException, ExecutionException {
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      @SuppressWarnings("unchecked")
      Future<String>[] futures = new Future[10_000];
      for (int pos = 0; pos < futures.length; pos++) {
        futures[pos] = executor.submit(() -> {
          return Thread.currentThread().getName() +
                 " " +
                 (Thread.currentThread().isVirtual() ? "virtual" : "platform");
        });
      }

      for (int pos = 0; pos < futures.length; pos++) {
        System.out.println(futures[pos].get());
      }
    }
  }

  static void carrierThread() throws InterruptedException {
    Thread[] threads = new Thread[10];
    for (int pos = 0; pos < threads.length; pos++) {
      threads[pos] = Thread.startVirtualThread(() -> {
        System.out.println(Thread.currentThread().toString());
        readFromDb();
        System.out.println(Thread.currentThread().toString());
        readFromDb();
        System.out.println(Thread.currentThread().toString());
        readFromDb();
        System.out.println(Thread.currentThread().toString());
      });
    }
    for (int pos = 0; pos < threads.length; pos++) {
      threads[pos].join();
    }
  }

  private static void readFromDb() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    one_million();
    executor();
    carrierThread();
  }
}
