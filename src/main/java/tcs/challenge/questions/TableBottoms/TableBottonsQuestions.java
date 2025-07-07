package tcs.challenge.questions.TableBottoms;

import net.serenitybdd.screenplay.Question;
import tcs.challenge.ui.TableBottoms.TableBottomsPage;

public class TableBottonsQuestions {

    public static Question<String> validateHashCode(){
        return actor -> TableBottomsPage.HASH_CODE.resolveFor(actor).getText().trim();
    }
}
