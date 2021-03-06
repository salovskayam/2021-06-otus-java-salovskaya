package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.otus.appcontainer.AppComponentsContainerImpl;
import ru.otus.config.AppConfig1;
import ru.otus.config.AppConfig2;
import ru.otus.config.AppConfig3;
import ru.otus.services.*;

import java.io.PrintStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

//    @Disabled //надо удалить
    @DisplayName("Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(value = {"GameProcessor, ru.otus.services.GameProcessor",
            "GameProcessorImpl, ru.otus.services.GameProcessor",
            "gameProcessor, ru.otus.services.GameProcessor",

            "IOService, ru.otus.services.IOService",
            "IOServiceConsole, ru.otus.services.IOService",
            "ioService, ru.otus.services.IOService",

            "PlayerService, ru.otus.services.PlayerService",
            "PlayerServiceImpl, ru.otus.services.PlayerService",
            "playerService, ru.otus.services.PlayerService",

            "EquationPreparer, ru.otus.services.EquationPreparer",
            "EquationPreparerImpl, ru.otus.services.EquationPreparer",
            "equationPreparer, ru.otus.services.EquationPreparer"
    })
    public void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass) throws Exception {
//        var ctx = new AppComponentsContainerImpl(AppConfig.class);
//        var ctx = new AppComponentsContainerImpl(AppConfig2.class, AppConfig1.class, AppConfig3.class);
        var ctx = new AppComponentsContainerImpl("ru.otus.config");

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component = null;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("ru.otus.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        for (var field: component.getClass().getDeclaredFields()){
            field.setAccessible(true);
            var fieldValue = field.get(component);
            if (!(fieldValue instanceof String || fieldValue instanceof PrintStream || fieldValue instanceof Scanner))
                assertThat(fieldValue).isNotNull().isInstanceOfAny(IOService.class, PlayerService.class, EquationPreparer.class);
        }

    }
}