package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DataGeneratorTest {

    @BeforeAll
    static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");

    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test

    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {

        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе
        $("[data-test-id = city] input").setValue(validUser.getCity());
        $("[data-test-id = date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id = name] input").setValue(validUser.getName());
        $("[data-test-id = phone] input").setValue(validUser.getPhone());
        $("[data-test-id = agreement]").click();
        $("button.button").click();
        $(".notification__content").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate));

        $("[data-test-id = date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $("button.button").click();

        $("[data-test-id = replan-notification] .notification__content").shouldBe(Condition.visible).shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(".button__text").click();
        $(".notification__content").shouldBe(Condition.visible).shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate));

    }
}