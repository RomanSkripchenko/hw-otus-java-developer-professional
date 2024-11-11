package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.*;

@AppComponentsContainerConfig(order = 1)
public class AppConfig {

    @AppComponent(order = 1, name = "equationPreparer")
    public EquationPreparer equationPreparer() {
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 2, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }

    @AppComponent(order = 3, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 4, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService, EquationPreparer equationPreparer, PlayerService playerService) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}

