package tcs.challenge.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.questions.Text;
import tcs.challenge.Util.GetInfoFromTable;
import tcs.challenge.models.Login.LoginModel;
import tcs.challenge.navigation.NavigateTo;
import tcs.challenge.questions.TableBottoms.TableBottonsQuestions;
import tcs.challenge.tasks.Login.Login;
import tcs.challenge.tasks.TableBottoms.*;
import tcs.challenge.ui.TableBottoms.TableBottomsPage;


import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;

public class TableBottomStepDefinitions {

    @ParameterType(".*")
    public Actor actor(String actorName) {
        return OnStage.theActorCalled(actorName);
    }

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("enter the Evelart web portal")
    public void enterTheEvelartWebPortal(DataTable userLogin) {
        LoginModel loginModel = GetInfoFromTable.getLoginCredentials(userLogin);
        OnStage.theActorCalled("user").attemptsTo(
                NavigateTo.loginPage(),
                new Login(loginModel.getUsername(), loginModel.getPassword())
        );
    }

    @When("select the correct button to obtain the hash code.")
    public void selectTheCorrectButtonToObtainTheHashCode() {
        Actor actor = OnStage.theActorInTheSpotlight();
        int cycles = 11;
        OnStage.theActorInTheSpotlight().attemptsTo(
                CompleteCyclesTask.of(cycles)
        );
        // Recuperar el hash FINAL después de todos los ciclos
        String finalHashCode = Text.of(TableBottomsPage.HASH_CODE).answeredBy(actor);
        actor.remember("final_hash_code", finalHashCode); // Guarda con un nombre significativo

    }
    @Then("verify that the hash code is correct")
    public void verifyThatTheHashCodeIsCorrect() {
        Actor actor = OnStage.theActorInTheSpotlight();
        String finalHashCode = actor.recall("final_hash_code");
        Serenity.reportThat("The final hash code is: " + finalHashCode,
                () -> {} // No necesitamos acción real aquí
        );
        actor.should(
                seeThat("Code hash", TableBottonsQuestions.validateHashCode(),equalTo(finalHashCode))

        );
    }
}
