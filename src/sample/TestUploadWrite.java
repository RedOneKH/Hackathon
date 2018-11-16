package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class TestUploadWrite extends AnchorPane {

    @FXML
    TableView tab;


    public TestUploadWrite(ArrayList<String> headers,String query) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/test.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }




        DataHelper t = new DataHelper();
        ArrayList<ArrayList<String>> records = t.executeQuery(query);
        ArrayList<ArrayList<String>> list =new ArrayList<>();
        list.add(headers);
        list.addAll(records);
        t.filldata(list,tab);
    }

    public TestUploadWrite(ArrayList<String> headers,ArrayList<ArrayList<String>> records) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/test.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        ArrayList<ArrayList<String>> list =new ArrayList<>();
        list.add(headers);
        list.addAll(records);
        DataHelper t =new DataHelper();
        t.filldata(list,tab);
    }


    @FXML
    private void initialize() {
       /* DataHelper t = new DataHelper();
        t.createNewDatabase();
        t.connect();
       // ArrayList<ArrayList<String>> records = t.readCSV("./data/Korea.csv", ';');
        ArrayList<ArrayList<String>> records = t.readXSLX("./data/EA.xlsx");

        ArrayList<String> columns=records.get(0);
        t.filldata(records,tab);

        records.remove(0);
        t.createNewTable("EA",columns);
        t.insert("EA",records);*/

    }
}
