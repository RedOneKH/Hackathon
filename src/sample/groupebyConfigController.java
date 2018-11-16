package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class groupebyConfigController implements Initializable {

    public GridPane aggregateGridPane;
    public Button saveButton;
    @FXML
    ListView<String> columnsListView;
    @FXML
    ListView<String> groupByListView;
    @FXML
    Button addGroupBy;
    @FXML
    Button addAggregate;
    private ObservableList<String> observableList;
    private List<String> listGroupBy = new ArrayList<>();
    private List<Object> listAggregate = new ArrayList();


    private String tableName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    ArrayList<String> headers;



    int x = 1, y = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addAggregate.setOnAction(event -> {
            Label label = new Label(columnsListView.getSelectionModel().getSelectedItem());
            ComboBox<String> comboBox = new ComboBox<>();
            initComboBox(comboBox);
            aggregateGridPane.add(label, x, y);
            aggregateGridPane.add(comboBox, x + 1, y);
            y++;
            listAggregate.add(label);
            listAggregate.add(comboBox);
            columnsListView.getItems().remove(columnsListView.getSelectionModel().getSelectedItem());
        });

        addGroupBy.setOnAction(event -> {
            System.out.println(columnsListView.getSelectionModel().getSelectedItem());
            groupByListView.getItems().add(columnsListView.getSelectionModel().getSelectedItem());
            columnsListView.getItems().remove(columnsListView.getSelectionModel().getSelectedItem());
        });
        saveButton.setOnAction(event -> {

            headers = new ArrayList<>();
            StringBuffer query = new StringBuffer();
            query.append("Select ");
            //remplir la list de groupby
            StringBuffer groupby = new StringBuffer("group by ");

            listGroupBy.addAll(groupByListView.getItems());
            for (String s : listGroupBy) {
                headers.add(s);
                query.append(s + " ,");
                groupby.append(s + ",");
            }

            for (int i = 0; i < listAggregate.size(); i += 2) {
                if(((ComboBox) listAggregate.get(i + 1)).getValue()==null)
                    continue;
                String elementToadd = "";
                elementToadd = (((ComboBox) listAggregate.get(i + 1)).getValue() + "(" + ((Label) (listAggregate.get(i))).getText()) + "),";
                query.append(elementToadd);
                headers.add((((ComboBox) listAggregate.get(i + 1)).getValue() + "(" + ((Label) (listAggregate.get(i))).getText()) + ")");
            }

            query = new StringBuffer(query.substring(0, query.length() - 1));
            groupby = new StringBuffer(groupby.substring(0, groupby.length() - 1));

            query.append(" from "+tableName+" ");
            System.out.println(groupby);
            query.append(groupby);
            System.out.println(query);

            BorderPane root = new BorderPane();

            try{
                Stage stage = new Stage();

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
                stage.setScene(scene);
                stage.show();

            } catch(Exception e) {
                e.printStackTrace();
            }
            root.setCenter(new TestUploadWrite(headers, query.toString()));

        });

        groupByListView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                String selected = groupByListView.getSelectionModel()
                        .getSelectedItem();
                groupByListView.getItems().remove(selected);
                columnsListView.getItems().add(selected);

            }
        });


    }

    public void initListView(List<String> list) {
        observableList = FXCollections.observableArrayList(list);
        columnsListView.setItems(observableList);
        //groupByListView.setItems(observableList);
    }

    public void initComboBox(ComboBox comboBox) {
        comboBox.setPromptText("...");
        List<String> choix = Arrays.asList("MIN", "MAX", "AVG", "FIRST");
        ObservableList<String> observableList = FXCollections.observableArrayList(choix);
        comboBox.setItems(observableList);
    }

}
