package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.IOService;
import ru.otus.services.IOServiceStreams;

@AppComponentsContainerConfig(order = 2)
public class AdditionalConfig {
    public AdditionalConfig() {}

    @AppComponent(order = 1, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}

