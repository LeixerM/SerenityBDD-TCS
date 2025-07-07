package tcs.challenge.tasks.TableBottoms;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import tcs.challenge.models.TableBottoms.Coordinate;
import tcs.challenge.models.TableBottoms.GridPosition;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CompleteCyclesTask implements Task {

    private final int totalCycles;
    private static final int MAX_RETRIES = 3; // Máximo de reintentos por ciclo

    public CompleteCyclesTask(int totalCycles) {
        this.totalCycles = totalCycles;
    }

    @Override
    @Step("{0} completes #totalCycles cycles of button selection and sum submission")
    public <T extends Actor> void performAs(T actor) {
        for (int cycle = 1; cycle <= totalCycles; cycle++) {
            int retryCount = 0;
            boolean cycleCompleted = false;

            while (retryCount <= MAX_RETRIES && !cycleCompleted) {
                try {
                    System.out.println("--- Starting cycle " + cycle + " of " + totalCycles + " (attempt " + (retryCount + 1) + ") ---");

                    // 1. Extracción de coordenadas (con instancia única)
                    ExtractCoordinatesTask extractTask = ExtractCoordinatesTask.fromPage();
                    actor.attemptsTo(extractTask);
                    List<Coordinate> coordinates = extractTask.getCoordinates();
                   // System.out.println("Coordenadas con formato: " + coordinates);

                    // 2. Construcción de la matriz
                    BuildGridTask buildTask = BuildGridTask.fromPage();
                    actor.attemptsTo(buildTask);
                    String[][] grid = actor.recall("current_grid");
                    /*for (String[] row : grid) {
                        System.out.println(Arrays.toString(row));
                    }
                    System.out.println("Matriz construida: " + grid.length + "x" + grid[0].length);*/
                    CalculatePositionTask calculateTask = CalculatePositionTask.using(coordinates);
                    actor.attemptsTo(calculateTask);

                    // VALIDAR QUE LA POSICIÓN NO SEA NULA
                    GridPosition position = calculateTask.getFinalPosition();
                    if (position == null) {
                        throw new IllegalStateException("La posición calculada es nula");
                    }
                    System.out.println("Posición final: (" + position.getRow() + ", " + position.getColumn() + ")");

                    // 4. Selección de botón y envío
                    actor.attemptsTo(SelectGridButtonTask.at(position, grid));
                    actor.attemptsTo(SubmitRowSumTask.now());

                    System.out.println("--- Completed cycle " + cycle + " ---");
                    cycleCompleted = true;

                } catch (Exception e) {
                    retryCount++;
                    System.err.println("Error en ciclo " + cycle + " (intento " + retryCount + "): " + e.getMessage());

                    if (retryCount <= MAX_RETRIES) {
                        // Recargar página y esperar antes de reintentar
                        //actor.attemptsTo(RefreshPage.now());
                        pauseFor(2000).performAs(actor); // Esperar 2 segundos
                    } else {
                        // Si falla después de todos los reintentos, relanzar excepción
                        throw new RuntimeException("Fallo después de " + MAX_RETRIES + " reintentos en ciclo " + cycle, e);
                    }
                }
            }
            if (cycle < totalCycles) {
                pauseFor(800).performAs(actor);
            }
        }
        // Espera final
        pauseFor(10000).performAs(actor); // 10 segundos (10000 ms)
    }

    private Performable pauseFor(long milliseconds) {
        return new Performable() {
            @Override
            public <T extends Actor> void performAs(T actor) {
                try {
                    TimeUnit.MILLISECONDS.sleep(milliseconds);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }
    public static CompleteCyclesTask of(int totalCycles) {
        return instrumented(CompleteCyclesTask.class, totalCycles);
    }
}
