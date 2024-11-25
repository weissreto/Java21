package ch.ivyteam.java21.pattern;


public class Pattern {
  sealed interface UserDialog permits JspPage, RichDialog, HtmlDialog, Form {
  }

  record JspPage(String jspFile) implements UserDialog {}
  record RichDialog(String ulcClass) implements UserDialog {}
  record HtmlDialog(String xhtmlFile, boolean offline) implements UserDialog {}
  record Form(String fomrFile) implements UserDialog {}

  public static void main(String[] args) {
    open(new JspPage("my.jsp"));
    open(new RichDialog("ch.ivyteam.ulc.MyRichDialog"));
    open(new HtmlDialog("my.xhtml", false));
    open(new HtmlDialog("my.xhtml", true));
    open(new Form("my.f.json"));
    open(null);
  }

  private static void open(UserDialog dialog) {
    switch (dialog) {
      case null -> System.out.println("Nothing to do");
      case JspPage(var jsp) -> System.out.println("Open JSP " + jsp);
      case RichDialog(var ulc) -> System.out.println("Open RichDialog " + ulc);
      case HtmlDialog(var xhtml, var offline) when offline == true -> System.out.println("Open offline HtmlDialog " + xhtml);
      case HtmlDialog(var xhtml, var offline) -> System.out.println("Open HtmlDialog " + xhtml);
      case Form(var json) -> System.out.println("Open Form " + json);
    }
  }
}
