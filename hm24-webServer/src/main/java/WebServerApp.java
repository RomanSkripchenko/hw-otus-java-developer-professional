import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.InMemoryUserDao;
import dao.UserDao;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import server.WebServerWithFilterBasedSecurity;
import services.TemplateProcessor;
import services.TemplateProcessorImpl;
import services.UserAuthService;
import services.UserAuthServiceImpl;

public class WebServerApp {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        // Настроим Hibernate
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        // Настроим сервисы
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        // Создадим UserDao и сервис аутентификации
        UserDao userDao = new InMemoryUserDao();
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        // Запускаем сервер
        var webServer = new WebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT, authService, userDao, dbServiceClient, gson, templateProcessor);

        webServer.start();
        webServer.join();
    }
}
