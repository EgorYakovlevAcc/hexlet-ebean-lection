package controller;

import io.javalin.http.Handler;

import java.io.PrintWriter;

public class RootController {
    public static Handler welcome = ctx -> {
      PrintWriter printWriter = ctx.res.getWriter();
      printWriter.write("WELCOME");
    };
}
