package tcs.challenge.ui.TableBottoms;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class TableBottomsPage {
    public static final Target COORDINATES_TEXT =
            Target.the("Coordinates text")
                    .located(By.xpath("//div[contains(@class,'bg-white')]//p[@class='text-center text-xl font-bold']"));

    public static final Target GRID_BUTTONS =
            Target.the("Grid buttons")
                    .located(By.xpath("//button[contains(@class, 'grid_button')]"));


    public static final Target SUM_INPUT = Target.the("Sum input field")
            .locatedBy("//input[@name='modal_answer']");

    public static final Target SUBMIT_BUTTON = Target.the("Submit button")
            .locatedBy("//button[contains(text(),'Enviar')]");

    public static final Target HASH_CODE = Target.the("Text hashs code")
            .located(By.xpath("/html/body/div/div/div/p"));


}
