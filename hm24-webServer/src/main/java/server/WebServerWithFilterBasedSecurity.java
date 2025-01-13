package server;

import com.google.gson.Gson;
import dao.UserDao;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import ru.otus.crm.service.DBServiceClient;
import services.TemplateProcessor;
import services.UserAuthService;
import servlet.AuthorizationFilter;
import servlet.ClientsServlet;
import servlet.LoginServlet;

public class WebServerWithFilterBasedSecurity extends UsersWebServerSimple {
    private final UserAuthService authService;
    private final DBServiceClient dbServiceClient;

    public WebServerWithFilterBasedSecurity(
            int port,
            UserAuthService authService,
            UserDao userDao,
            DBServiceClient dbServiceClient,
            Gson gson,
            TemplateProcessor templateProcessor) {
        super(port, userDao, gson, templateProcessor); // Передаем userDao в базовый класс
        this.authService = authService;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        servletContextHandler.addServlet(
                new ServletHolder(new ClientsServlet(templateProcessor, dbServiceClient)), "/clients");

        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        servletContextHandler.addFilter(new FilterHolder(authorizationFilter), "/clients", null);

        return servletContextHandler;
    }
}
