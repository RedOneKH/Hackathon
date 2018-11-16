package sample;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.IntStream;

public class DataHelper {

    public static final String DBNAME = "hack.db";
    public static final String CONNECTION_URL = "jdbc:sqlite:db/hack.db";

    public static Connection connection;

    public void createNewDatabase() {

        File file = new File("db/" + DBNAME);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (Connection conn = DriverManager.getConnection(CONNECTION_URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection connect() {
        try {
            if (connection == null || connection.isClosed())
                try {
                    connection = DriverManager.getConnection(CONNECTION_URL);

                    System.out.println("Connection to SQLite has been established.");

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection(){
        try {
            connect().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewTable(String tableName, ArrayList<String> columns) {

        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (\n");

        int i = 0;
        for (String value : columns) {
            if (i++ != columns.size() - 1) {
                sql.append(value).append(" text NOT NULL,\n");
            } else sql.append(value).append(" text NOT NULL\n);");
        }

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql.toString());
            System.out.println("A new table has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String tableName, ArrayList<ArrayList<String>> records) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " VALUES");


        int j = 0;
        for (ArrayList<String> row : records) {
            int i = 0;
            sql.append("(");
            for (String value : row) {
                sql.append("'" + value + "'");
                if (i++ != row.size() - 1) {
                    sql.append(",");
                }
            }

            sql.append(")");
            if (j++ != records.size() - 1) {
                sql.append(",");
            }
        }

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql.toString());
            System.out.println("A new line has been inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<ArrayList<String>> executeQuery(String query) {
        ArrayList<ArrayList<String>> records = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            // loop through the result set
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                IntStream.range(1, columnsNumber+1).forEach(i -> {
                    try {
                        row.add(rs.getString(i));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                records.add(row);
            }
            return records;
        } catch (SQLException e) {
            return records;
        }
    }

    public ArrayList<ArrayList<String>> readCSV(String filePath, char separator) {
        ArrayList<ArrayList<String>> records = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(filePath));
                //CSVReader csvReader = new CSVReader(reader);
                CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();
        ) {
            ArrayList<String> row;
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                row = new ArrayList<>();

                Collections.addAll(row, line);

                records.add(row);

            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        return records;

    }

    public ArrayList<ArrayList<String>> readXSLX(String filePath) {
        ArrayList<ArrayList<String>> records = new ArrayList<>();
        File excelFile = new File(filePath);
        try (

                FileInputStream fis = new FileInputStream(excelFile);
                XSSFWorkbook workbook = new XSSFWorkbook(fis)
        ) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            ArrayList<String> row;
            for (Row Srow : sheet) {
                row = new ArrayList<>();
                Iterator<Cell> cellIterator = Srow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    row.add(cell.toString());
                }

                records.add(row);
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        return records;

    }


    public void filldata(ArrayList<ArrayList<String>> records, TableView<ObservableList> tab) {


        ObservableList<ObservableList> cvsData = FXCollections.observableArrayList();
        boolean isFirst = true;
        for (ArrayList<String> row : records) {
            if (isFirst) {
                int columnIndex = 0;
                TableColumn[] tableColumns = new TableColumn[row.size()];
                for (String columName : row) {
                    int j = columnIndex;
                    tableColumns[columnIndex++] = new TableColumn(columName);
                    tableColumns[j].setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                tab.getColumns().addAll(tableColumns);
                isFirst = false;
            } else {
                ObservableList<String> tablerow = FXCollections.observableArrayList();
                tablerow.addAll(row);
                cvsData.add(tablerow);

            }
        }
        tab.setItems(cvsData);


    }

    public static void main(String[] args) {


        DataHelper t = new DataHelper();
        t.createNewDatabase();
        t.connect();
        ArrayList<ArrayList<String>> records = t.readCSV("./data/Korea.csv", ';');
        t.createNewTable("warehouses", records.get(0));
        t.insert("warehouses", t.readCSV("./data/iris.csv", ';'));
        t.executeQuery("SELECT * FROM warehouses");

    }
}
