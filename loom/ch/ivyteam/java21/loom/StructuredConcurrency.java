package ch.ivyteam.java21.loom;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.concurrent.TimeUnit;

public class StructuredConcurrency {

  record Request(String path, String user) {}

  public static void main(String[] args) throws InterruptedException {
    Thread.startVirtualThread(() -> httpRequest(new Request("user", "Reto")));
    Thread.startVirtualThread(() -> httpRequest(new Request("complex", "Bruno")));
    Thread.startVirtualThread(() -> httpRequest(new Request("user", "Christian")));
    Thread.startVirtualThread(() -> httpRequest(new Request("complex", "Reto")));
    Thread.startVirtualThread(() -> httpRequest(new Request("user", "Bruno")));
    Thread.startVirtualThread(() -> httpRequest(new Request("complex", "Christian")));
    TimeUnit.SECONDS.sleep(10);
  }

  private static final ScopedValue<Request> REQUEST = ScopedValue.newInstance();
  private static void httpRequest(Request request) {
    ScopedValue.where(REQUEST, request).run(() -> {
      System.out.println("< "+httpRequest());
    });
  }


  private static String httpRequest() {
    if (REQUEST.get().path().equals("user")) {
      return userRequest();
    } else {
      return complexRequest();
    }
  }

  private static String userRequest() {
    System.out.println("> User Request=" + REQUEST.get() + " Thread="+Thread.currentThread().toString());
    return REQUEST.get().user();
  }

  private static String complexRequest() {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      var user = scope.fork(() -> userRequest());
      var db = scope.fork(() -> dbRequest());
      var rest = scope.fork(() -> restRequest());
      scope.join();
      var builder = new StringBuilder();
      result(builder, "User", user);
      result(builder, "DB", db);
      result(builder, "REST", rest);
      return builder.toString();
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
  }

  private static String restRequest() {
    sleep(1000);
    if (Math.random() < 0.2d) {
      throw new RuntimeException("Rest is down");
    }
    System.out.println("> REST Request=" + REQUEST.get() + " Thread="+Thread.currentThread().toString());
    return REQUEST.get().user() +" from REST";
  }


  private static String dbRequest() {
    sleep(200);
    if (Math.random() < 0.2d) {
      throw new RuntimeException("Database is down");
    }
    System.out.println("> DB Request=" + REQUEST.get() + " Thread="+Thread.currentThread().toString());
    return REQUEST.get().user() +" from DB";
  }

  private static void result(StringBuilder builder, String type, Subtask<String> result) {
    if (builder.length() > 0) {
      builder.append(" ");
    }
    builder.append(type);
    builder.append(": ");
    if (result.state() == State.SUCCESS) {
      builder.append(result.get());
    } else {
      builder.append(result.state());
    }
  }

  private static void sleep(int sleep) {
    try {
      Thread.sleep(sleep);
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
  }

}
