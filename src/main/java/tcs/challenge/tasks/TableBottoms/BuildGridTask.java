package tcs.challenge.tasks.TableBottoms;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import tcs.challenge.ui.TableBottoms.TableBottomsPage;

import java.util.*;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class BuildGridTask implements Task {
    private String[][] grid;

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Obtener todos los botones del grid
        List<WebElementFacade> buttons = TableBottomsPage.GRID_BUTTONS.resolveAllFor(actor);

        // Manejar caso de grid vacío
        if (buttons.isEmpty()) {
            grid = new String[0][0];
            actor.remember("current_grid", grid);
            return;
        }

        // 1. Agrupar botones por filas usando coordenadas Y
        Map<Integer, List<WebElementFacade>> rowsMap = new TreeMap<>();
        for (WebElementFacade button : buttons) {
            int y = button.getLocation().getY();
            rowsMap.computeIfAbsent(y, k -> new ArrayList<>()).add(button);
        }

        // 2. Ordenar botones dentro de cada fila por coordenada X
        List<List<WebElementFacade>> rows = new ArrayList<>();
        for (List<WebElementFacade> row : rowsMap.values()) {
            row.sort(Comparator.comparingInt(b -> b.getLocation().getX()));
            rows.add(row);
        }

        // 3. Determinar dimensiones
        int rowCount = rows.size();
        int colCount = rows.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        // 4. Validar que todas las filas tengan columnas válidas
        if (rows.stream().anyMatch(List::isEmpty)) {
            throw new AssertionError("Algunas filas no tienen columnas");
        }

        // 5. Construir matriz rectangular
        grid = new String[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            List<WebElementFacade> currentRow = rows.get(i);
            for (int j = 0; j < colCount; j++) {
                // Manejar celdas vacías si hay columnas faltantes
                if (j < currentRow.size()) {
                    grid[i][j] = currentRow.get(j).getText().trim();
                } else {
                    grid[i][j] = ""; // Celda vacía
                }
            }
        }

        // Guardar dimensiones y matriz
        actor.remember("current_grid", grid);
        actor.remember("grid_rows", rowCount);
        actor.remember("grid_columns", colCount);
    }

    public static BuildGridTask fromPage() {
        return instrumented(BuildGridTask.class);
    }
}


