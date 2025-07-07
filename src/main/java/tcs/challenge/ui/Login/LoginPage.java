package tcs.challenge.ui.Login;


import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class LoginPage {

    public static final Target INPUT_USERNAME = Target.the("username input").located(By.name("username"));
    public static final Target INPUT_PASSWORD = Target.the("password input").located(By.name("password"));
    public static final  Target BUTTON_LOG_IN = Target.the("login button").located(By.xpath("//button[@type='submit']"));

}
