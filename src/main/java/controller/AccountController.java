package controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import db.DBServerInstance;
import io.ebean.EbeanServer;
import io.javalin.http.Handler;
import model.User;

import java.io.PrintWriter;
import java.util.List;

public class AccountController {
    public static Handler getAccounts = ctx -> {
        EbeanServer ebeanServer = DBServerInstance.getInstance();
        List<User> users = ebeanServer.find(User.class)
                .findList();

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter printWriter = ctx.res.getWriter();
        printWriter.write(objectMapper.writeValueAsString(users));
    };

    public static Handler createAccount = ctx -> {
        User user = ctx.bodyAsClass(User.class);

        EbeanServer ebeanServer = DBServerInstance.getInstance();
        ebeanServer.save(user);

        PrintWriter printWriter = ctx.res.getWriter();
        printWriter.write("Пользователь создался так успешно, что аж ух!!!");
    };

    public static Handler editAccount = ctx -> {
        User user = ctx.bodyAsClass(User.class);

        EbeanServer ebeanServer = DBServerInstance.getInstance();
        ebeanServer.update(user);

        PrintWriter printWriter = ctx.res.getWriter();
        printWriter.write("User has been updated successfully");
    };

    public static Handler deleteAccount = ctx -> {
        Integer id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);
        EbeanServer ebeanServer = DBServerInstance.getInstance();
        ebeanServer.find(User.class)
                .where().eq("id", id)
                .delete();

        PrintWriter printWriter = ctx.res.getWriter();
        printWriter.write("User has been successfully removed");
    };
}
