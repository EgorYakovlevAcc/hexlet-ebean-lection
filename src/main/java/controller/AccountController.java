package controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import db.DBServerInstance;
import io.ebean.EbeanServer;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.Validator;
import io.javalin.http.Handler;
import model.User;
import pojo.Error;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

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
//        User user = ctx.bodyAsClass(User.class);
        Validator<String> nameValidator = ctx.formParamAsClass("name", String.class)
                .check(value -> {
                    if (value != null) {
                        if (value.length() > 0) {
                            return true;
                        }
                    }
                    return false;
                }, "Name cannot be empty")
                .check(value -> value.length() > 7, "Amount of chars in name should be grater then 7");

        Validator<String> lastnameValidator = ctx.formParamAsClass("lastname", String.class)
                .check(Objects::nonNull, "Lastname cannot be null")
                .check(value -> {
                    if (value != null) {
                        char firstChar = value.charAt(0);
                        return Character.isUpperCase(firstChar);

                    }
                    return false;
                }, "First letter should be uppercase!");

        Map<String, List<ValidationError<?>>> errors = JavalinValidation.collectErrors(nameValidator, lastnameValidator);

        PrintWriter printWriter = ctx.res.getWriter();

        ObjectMapper objectMapper = new ObjectMapper();
        if (!errors.isEmpty()) {
            List<Error> errorList = errors.entrySet().stream()
                    .map(Map.Entry::getValue)
                    .flatMap(List::stream)
                    .map(ValidationError::getMessage)
                    .map(Error::new)
                    .collect(Collectors.toList());
            printWriter.write(String.valueOf(objectMapper.writeValueAsString(errorList)));
            return;
        }


        User user = new User();
//        user.setBirthdate(birthdate);
        user.setLastname(lastnameValidator.get());
        user.setName(nameValidator.get());

        EbeanServer ebeanServer = DBServerInstance.getInstance();
        ebeanServer.save(user);


        printWriter.write("User has been successfully created!");
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
