package ch.ivyteam.java21.web.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpHandlers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

public class WebServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    new WebServer().start();
    Thread.sleep(Duration.ofMinutes(1));
  }

  private void start() throws IOException {
    var server = HttpServer.create(new InetSocketAddress(8080), 0);
    var fileHandler = SimpleFileServer.createFileHandler(Path.of("C:\\temp"));
    server.createContext("/temp/", fileHandler);
    server.createContext("/gugus/", HttpHandlers.of(200, Headers.of(), "Sugus"));
    server.createContext("/elio/", new ElioHandler());
    server.start();
  }

  private static final class ElioHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      var response = "Gute Morge es isch " + LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMAN);
      try(exchange) {
        var bytes = response.getBytes();
        exchange.getRequestBody().readAllBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
      }
    }
  }

}
