package tcs.challenge.Util;

import io.cucumber.datatable.DataTable;
import tcs.challenge.models.Login.LoginModel;

import java.util.List;

public class GetInfoFromTable {

    public static LoginModel getLoginCredentials(DataTable userLogin){
        List<List<String>> rows = userLogin.asLists(String.class);
        String username = "";
        String password = "";

        for (List<String> columns :rows){
            username = columns.get(0);
            password = columns.get(1);
        }
        LoginModel loginModel = new LoginModel();
        loginModel.setUsername(username);
        loginModel.setPassword(password);

        return loginModel;
    }
}
