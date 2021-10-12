import controller.AccountController;
import controller.RootController;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(JavalinConfig::enableDevLogging);

        addRoutes(app);
        app.before(ctx -> {
            ctx.attribute("ctx", ctx);
        });

        return app;
    }

    private static void addRoutes(Javalin app) {
        app.get("/", RootController.welcome);
        app.routes(() -> {
            path("users/", () -> {
                get(AccountController.getAccounts);
                post(AccountController.createAccount);
                put(AccountController.editAccount);
                path("{id}/", () -> {
                    delete(AccountController.deleteAccount);
                });
            });
        });
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "8093");
        return Integer.valueOf(port);
    }
}
