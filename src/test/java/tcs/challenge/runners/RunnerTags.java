package tcs.challenge.runners;


import io.cucumber.junit.CucumberOptions.SnippetType;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features/table_bottoms.feature",
        snippets = SnippetType.CAMELCASE,
        glue = "tcs.challenge.stepdefinitions",
        tags = "@testQAOrange"

)

public class RunnerTags {


}
