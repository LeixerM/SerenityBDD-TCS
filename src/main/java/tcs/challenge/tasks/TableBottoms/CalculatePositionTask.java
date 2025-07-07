package tcs.challenge.tasks.TableBottoms;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import tcs.challenge.models.TableBottoms.Coordinate;
import tcs.challenge.models.TableBottoms.GridPosition;

import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CalculatePositionTask implements Task {

    private final List<Coordinate> coordinates;
    private GridPosition finalPosition;
    private int gridRows;
    private int gridColumns;

    public CalculatePositionTask(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    @Step("{0} calcula la posición final")
    public <T extends Actor> void performAs(T actor) {
        try {
            // Recuperar dimensiones dinámicas de la memoria del actor
            gridRows = actor.recall("grid_rows");
            gridColumns = actor.recall("grid_columns");

            finalPosition = calculatePosition();
            System.out.println("Posición calculada: (" + finalPosition.getRow() + ", " + finalPosition.getColumn() + ")");
        } catch (Exception e) {
            throw new RuntimeException("Error calculando posición", e);
        }
    }

    private GridPosition calculatePosition() {
        if (coordinates == null || coordinates.isEmpty()) {
            throw new IllegalStateException("La lista de coordenadas está vacía");
        }

        int currentRow = 0;
        int currentCol = 0;

        for (Coordinate coord : coordinates) {
            // Usar dimensiones dinámicas en lugar de 12
            currentCol = modulo(currentCol + coord.getX(), gridColumns);
            currentRow = modulo(currentRow + coord.getY(), gridRows);
        }

        return new GridPosition(currentRow, currentCol);
    }

    // Método seguro para manejar módulo con valores negativos
    private int modulo(int value, int modulus) {
        return ((value % modulus) + modulus) % modulus;
    }

    public GridPosition getFinalPosition() {
        return finalPosition;
    }

    public static CalculatePositionTask using(List<Coordinate> coordinates) {
        return instrumented(CalculatePositionTask.class, coordinates);
    }
}
