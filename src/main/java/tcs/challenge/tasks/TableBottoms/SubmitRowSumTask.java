package tcs.challenge.tasks.TableBottoms;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;
import tcs.challenge.ui.Login.LoginPage;
import tcs.challenge.ui.TableBottoms.TableBottomsPage;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class SubmitRowSumTask implements Task {
    @Override
    public <T extends Actor> void performAs(T actor) {

        // 1. Recuperar la suma del contexto
        int rowSum = actor.recall("SUM_ROWS");

        // 2. Convertir a String
        String sumAsString = String.valueOf(rowSum);

        // 3. Ingresar valor y enviar
        actor.attemptsTo(
                WaitUntil.the(TableBottomsPage.SUM_INPUT, isVisible()).forNoMoreThan(15).seconds(),
                Enter.theValue(sumAsString).into(TableBottomsPage.SUM_INPUT),
                WaitUntil.the(TableBottomsPage.SUBMIT_BUTTON, isClickable()).forNoMoreThan(10).seconds(),
                Click.on(TableBottomsPage.SUBMIT_BUTTON)
        );
    }

    public static SubmitRowSumTask now() {
        return instrumented(SubmitRowSumTask.class);
    }
}
