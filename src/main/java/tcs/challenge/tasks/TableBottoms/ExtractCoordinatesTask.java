package tcs.challenge.tasks.TableBottoms;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import tcs.challenge.models.TableBottoms.Coordinate;
import tcs.challenge.ui.TableBottoms.TableBottomsPage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.replaceAll;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class ExtractCoordinatesTask implements Task {

    private List<Coordinate> coordinates;
    private static final Pattern COORD_PATTERN = Pattern.compile("\\((-?\\d+),(-?\\d+)\\)");

    @Step("{0} extrae coordenadas del DOM")
    public <T extends Actor> void performAs(T actor) {
        try {
            String coordinatesText = Text.of(TableBottomsPage.COORDINATES_TEXT)
                    .answeredBy(actor);
            coordinates = parseCoordinates(coordinatesText);
            if (coordinates.isEmpty()) {
                throw new IllegalStateException("No se encontraron coordenadas en el texto: " + coordinatesText);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error extrayendo coordenadas", e);
        }
    }

    private List<Coordinate> parseCoordinates(String text) {
        List<Coordinate> result = new ArrayList<>();
        System.out.println("Texto original de coordenadas: " + text);

        try {
            // Paso 1: Eliminar espacios y caracteres no deseados preservando negativos
            String cleanedText = text.replaceAll("\\s", "");

            // Paso 2: Convertir separadores externos a puntos preservando negativos internos
            StringBuilder normalized = new StringBuilder();
            boolean insideParentheses = false;
            boolean lastCharWasSeparator = false;

            for (char c : cleanedText.toCharArray()) {
                if (c == '(') {
                    insideParentheses = true;
                    normalized.append(c);
                    lastCharWasSeparator = false;
                } else if (c == ')') {
                    insideParentheses = false;
                    normalized.append(c);
                    lastCharWasSeparator = false;
                } else if (!insideParentheses && (c == '-' || c == ',' || c == ';' || c == ':'|| c == '_')) {
                    // Convertir separadores externos en puntos (sin duplicados)
                    if (!lastCharWasSeparator && normalized.length() > 0) {
                        normalized.append('.');
                        lastCharWasSeparator = true;
                    }
                } else {
                    // Preservar guiones internos y dígitos
                    normalized.append(c);
                    lastCharWasSeparator = false;
                }
            }

            String processedText = normalized.toString();
            System.out.println("Texto normalizado: " + processedText);

            // Paso 3: Extraer coordenadas usando expresión regular mejorada
            Pattern pattern = Pattern.compile("\\((-?\\d*),?(-?\\d*)\\)");
            Matcher matcher = pattern.matcher(processedText);

            int count = 0;
            while (matcher.find() && result.size() < 5) {
                count++;
                try {
                    // Manejar valores vacíos o incompletos
                    String xStr = matcher.group(1).isEmpty() ? "0" : matcher.group(1);
                    String yStr = matcher.group(2).isEmpty() ? "0" : matcher.group(2);

                    int x = xStr.isEmpty() ? 0 : Integer.parseInt(xStr);
                    int y = yStr.isEmpty() ? 0 : Integer.parseInt(yStr);

                    result.add(new Coordinate(x, y));
                    System.out.println("Coordenada " + count + ": (" + x + ", " + y + ")");
                } catch (NumberFormatException e) {
                    System.err.println("Error parseando números en: " + matcher.group());
                }
            }

            // Paso 4: Método alternativo para formatos complejos
            if (result.size() < 5) {
                System.out.println("Buscando coordenadas con método alternativo...");
                Pattern altPattern = Pattern.compile("-?\\d+");
                Matcher altMatcher = altPattern.matcher(processedText);
                List<Integer> numbers = new ArrayList<>();

                while (altMatcher.find()) {
                    numbers.add(Integer.parseInt(altMatcher.group()));
                }

                // Agrupar números de 2 en 2
                for (int i = 0; i < numbers.size() && result.size() < 5; i += 2) {
                    if (i + 1 < numbers.size()) {
                        result.add(new Coordinate(numbers.get(i), numbers.get(i + 1)));
                        System.out.println("Coordenada alternativa: (" + numbers.get(i) + ", " + numbers.get(i + 1) + ")");
                    }
                }
            }

            if (result.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron coordenadas válidas en: " + text);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error crítico parseando coordenadas: " + text, e);
        }
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public static ExtractCoordinatesTask fromPage() {
        return instrumented(ExtractCoordinatesTask.class);
    }

}
