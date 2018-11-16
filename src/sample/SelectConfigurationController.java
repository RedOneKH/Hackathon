package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectConfigurationController implements Initializable {

    public Button notButton;
    public Button likeButton;
    public Button infEqualButton;
    public Button supEqualButton;
    public Button minButton;
    public Button maxButton;
    public Button rightParButton;
    public Button leftParButton;
    public Button multiButton;
    public Button infButton;
    public Button whereButton;
    public Button equalButton;
    public Button supButton;
    public Button divButton;
    public Button inputButton;
    public GridPane contentVbox;
    public Button diffButton;
    @FXML
    VBox columnsVbox;
    @FXML
    Button closeButton;
    @FXML
    Button orButton;
    @FXML
    Button andButton;
    @FXML
    Button minusButton;
    @FXML
    Button plusButton;
    @FXML
    Button avgButton;
    private List<String> list = new ArrayList<>();

    private int x, y;
    private List<Object> listcom = new ArrayList<>();

    private String tableName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        x = 1;
        y = 1;
        closeButton.setOnAction(event -> {
            //close the window
        });
    }

    ArrayList<String> headers;

    public void addColumnsToVbox(ArrayList<String> list) {
        for (String s : list) {
            CheckBox checkBox = new CheckBox(s);
            /*checkBox.setOnMouseClicked(event -> {
                if(event.getClickCount()==2){
                    Label l = new Label(((CheckBox) event.getSource()).getText());
                    l.setTextAlignment(TextAlignment.CENTER);
                    l.setAlignment(Pos.CENTER);
                    listcom.add(l.getText());
                    contentVbox.add(l, x, y);
                    if (x < 12) {
                        x++;
                    } else {
                        x = 1;
                        y++;
                    }
                }
            });*/
            columnsVbox.getChildren().add(checkBox);
        }

    }

    public void drag(ActionEvent actionEvent) {
        String text = ((Button) actionEvent.getSource()).getText();
        Label l = new Label(text);
        l.setTextAlignment(TextAlignment.CENTER);
        l.setAlignment(Pos.CENTER);
        listcom.add(l.getText());
        contentVbox.add(l, x, y);
        if (x < 12) {
            x++;
        } else {
            x = 1;
            y++;
        }

    }



    public void draginput(ActionEvent actionEvent) {
        TextField textField = new TextField();
        listcom.add(textField);
        contentVbox.add(textField, x, y);
        if (x < 12) {
            x++;
        } else {
            x = 1;
            y++;
        }


        System.out.println(tableName);
    }

    public void save(ActionEvent actionEvent) {
        headers = new ArrayList<>();
        StringBuffer query = new StringBuffer("Select ");
        Boolean selected = false;
        for (Node node : columnsVbox.getChildren()) {
            if (((CheckBox) node).isSelected()) {
                selected = true;
                String s = ((CheckBox) node).getText();
                headers.add(s);
                query.append(s + ",");


            }
        }


        if (!selected)
            query.append("* ");
        else
            query = new StringBuffer(query.substring(0, query.length() - 1));

        query.append(" from "+tableName+" ");


        int j=0;
        for (Object o : listcom) {
            if (o instanceof TextField){
                if(j!=0){
                    if (listcom.get(j-1) instanceof String){
                        String s=(String)listcom.get(j-1);
                        if(s.equals("=") || s.equals("<>") || s.equals("Like"))
                            query.append("'"+((TextField) o).getText() + "' ");
                    }
                else
                query.append(((TextField) o).getText() + " ");
                }
                else
                    query.append(((TextField) o).getText() + " ");
            }
            else
                query.append((String) o + " ");

            j++;
        }

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
    }

}
