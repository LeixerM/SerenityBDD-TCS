package tcs.challenge.tasks.TableBottoms;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;
import tcs.challenge.models.TableBottoms.Coordinate;
import tcs.challenge.models.TableBottoms.GridPosition;

import java.util.Arrays;
import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class SelectGridButtonTask implements Task {

    private final GridPosition position;
    private final String[][] grid;

    public SelectGridButtonTask(GridPosition position, String[][] grid) {
        this.position = position;
        this.grid = grid;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {


        String targetValue = grid[position.getRow()][position.getColumn()];

        if (position == null) {
            throw new IllegalStateException("La posición no puede ser nula");
        }

        int rowSum = calculateRowSum(position.getRow());

        actor.remember("Select_Button", targetValue);
        actor.remember("SUM_ROWS",rowSum);

        Target targetButton = Target.the("Button with value " + targetValue)
                .locatedBy(String.format(
                        "//button[contains(@class, 'grid_button') and normalize-space()='%s']",
                        targetValue
                ));

        actor.attemptsTo(
                WaitUntil.the(targetButton, isVisible()).forNoMoreThan(10).seconds(),
                Scroll.to(targetButton),  // Asegura que el elemento esté en vista
                Click.on(targetButton)
        );
    }

    private int calculateRowSum(int rowIndex) {
        return Arrays.stream(grid[rowIndex])
                .mapToInt(value -> {
                    try {
                        return Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        System.err.println("Valor no numérico en la fila: " + value);
                        return 0;
                    }
                })
                .sum();
    }
    public static SelectGridButtonTask at(GridPosition position, String[][] grid) {
        return instrumented(SelectGridButtonTask.class, position, grid);
    }

}
