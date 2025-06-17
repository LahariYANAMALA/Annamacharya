import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class FileDBFX extends Application {
    final String url = "jdbc:mysql://localhost:3306/cse";
    final String user = "root";
    final String password = "lahari";

    TextArea outputArea = new TextArea();
    ComboBox<String> tableSelector = new ComboBox<>();

    @Override
    public void start(Stage primaryStage) {
        outputArea.setEditable(false);

        Button createBtn = new Button("Create Tables");
        Button insertBtn = new Button("Insert Data");
        Button deleteBtn = new Button("Delete Data");
        Button updateBtn = new Button("Update Data");
        Button selectBtn = new Button("Select/View Data");

        VBox buttonBox = new VBox(10, createBtn, insertBtn, deleteBtn, updateBtn, selectBtn);
        buttonBox.setPadding(new Insets(10));

        VBox layout = new VBox(10, buttonBox, outputArea);
        layout.setPadding(new Insets(10));

        createBtn.setOnAction(e -> handleCreate());
        insertBtn.setOnAction(e -> handleInsert());
        deleteBtn.setOnAction(e -> handleDelete());
        updateBtn.setOnAction(e -> handleUpdate());
        selectBtn.setOnAction(e -> handleSelect());

        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setTitle("SQL File Executor - JavaFX Version");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleCreate() {
        executeSQLFromFile("D:/crt/tablecreate.txt", "", false);
    }

    private void handleInsert() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("DEPT", "DEPT", "EMP", "SALGRADE");
        dialog.setTitle("Insert Data");
        dialog.setHeaderText("Select a table to insert data into");

        dialog.showAndWait().ifPresent(table -> {
            String filePath = switch (table) {
                case "DEPT" -> "D:/crt/insertdept.txt";
                case "EMP" -> "D:/crt/insertemp.txt";
                case "SALGRADE" -> "D:/crt/insertsalgrade.txt";
                default -> null;
            };
            if (filePath != null) executeSQLFromFile(filePath, table, true);
        });
    }
private void handleDelete() {
    try (Connection conn = DriverManager.getConnection(url, user, password)) {
        ArrayList<String> tables = getTables(conn);
        ChoiceDialog<String> dialog = new ChoiceDialog<>(tables.get(0), tables);
        dialog.setTitle("Delete Data");
        dialog.setHeaderText("Select a table to delete from");
        dialog.showAndWait().ifPresent(table -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Options");
            alert.setHeaderText("Delete row or entire table?");
            ButtonType deleteRow = new ButtonType("Delete Row");
            ButtonType deleteAll = new ButtonType("Delete All Rows");
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(deleteRow, deleteAll, cancel);
            alert.showAndWait().ifPresent(response -> {
                if (response == deleteAll) {
                    executeSQL("DELETE FROM " + table);
                } else if (response == deleteRow) {
                    String pkColumn = getPrimaryKeyColumn(table);
                    if (pkColumn != null) {
                        TextInputDialog pkInput = new TextInputDialog();
                        pkInput.setTitle("Delete Row");
                        pkInput.setHeaderText("Enter value for primary key column: " + pkColumn);
                        pkInput.showAndWait().ifPresent(pkValue -> {
                            String deleteQuery = "DELETE FROM " + table + " WHERE " + pkColumn + " = ?";
                            try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                                // Try parsing to int if the key is numeric
                                if (pkColumn.equalsIgnoreCase("GRADE") || pkColumn.endsWith("NO")) {
                                    pstmt.setInt(1, Integer.parseInt(pkValue.trim()));
                                } else {
                                    pstmt.setString(1, pkValue.trim());
                                }
                                int rowsAffected = pstmt.executeUpdate();
                                if (rowsAffected > 0) {
                                    outputArea.setText("Deleted row where " + pkColumn + " = " + pkValue);
                                } else {
                                    outputArea.setText("No row found with " + pkColumn + " = " + pkValue);
                                }
                            } catch (SQLException ex) {
                                outputArea.setText("Error deleting row: " + ex.getMessage());
                            } catch (NumberFormatException nfe) {
                                outputArea.setText("Invalid input for numeric key column.");
                            }
                        });
                    } else {
                        outputArea.setText("Could not determine primary key for table: " + table);
                    }
                }
            });
        });
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

    private void handleUpdate() {
        // Simplified update logic: ask for table and SQL manually
        TextInputDialog tableDialog = new TextInputDialog();
        tableDialog.setTitle("Update");
        tableDialog.setHeaderText("Enter table name to update");
        tableDialog.showAndWait().ifPresent(table -> {
            TextInputDialog sqlDialog = new TextInputDialog();
            sqlDialog.setTitle("Update SQL");
            sqlDialog.setHeaderText("Enter UPDATE SQL statement");
            sqlDialog.showAndWait().ifPresent(sql -> executeSQL(sql));
        });
    }

    private void handleSelect() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            ArrayList<String> tables = getTables(conn);
            ChoiceDialog<String> dialog = new ChoiceDialog<>(tables.get(0), tables);
            dialog.setTitle("Select/View");
            dialog.setHeaderText("Select a table to view data");
            dialog.showAndWait().ifPresent(table -> {
               try (Statement stmt = conn.createStatement()) {
    try (ResultSet rs = stmt.executeQuery("SELECT * FROM " + table)) {
        ResultSetMetaData md = rs.getMetaData();
        int cols = md.getColumnCount();

        while (rs.next()) {
            for (int i = 1; i <= cols; i++) {
                outputArea.appendText(rs.getString(i) + "\t");
            }
            outputArea.appendText("\n");
        }
    }
} catch (SQLException e) {
    outputArea.appendText("Error selecting table: " + e.getMessage() + "\n");
}
            });
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private void executeSQLFromFile(String filePath, String tableName, boolean checkDuplicates) {
        outputArea.clear();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            Class.forName("com.mysql.cj.jdbc.Driver");
            StringBuilder queryBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().replace('\u00A0', ' ').replaceAll("[\\p{C}]", "");
                if (line.isEmpty()) continue;
                queryBuilder.append(line).append(" ");
                if (line.endsWith(";")) {
                    String query = queryBuilder.toString().trim().replace(";", "");
                    if (checkDuplicates && query.toLowerCase().startsWith("insert into")) {
                        String keyValue = extractPrimaryKey(query);
                        String pkColumn = getPrimaryKeyColumn(tableName);
                        if (pkColumn != null && !keyValue.isEmpty()) {
                            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE " + pkColumn + " = " + keyValue);
                            if (rs.next()) {
                                outputArea.appendText("Duplicate entry for " + pkColumn + " = " + keyValue + "\n");
                            } else {
                                stmt.executeUpdate(query);
                                outputArea.appendText("Inserted: " + query + "\n");
                            }
                        }
                    } else {
                        stmt.executeUpdate(query);
                        outputArea.appendText("Executed: " + query + "\n");
                    }
                    queryBuilder.setLength(0);
                }
            }
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private String extractPrimaryKey(String query) {
        try {
            int start = query.indexOf("VALUES(") + 7;
            int end = query.lastIndexOf(")");
            String[] values = query.substring(start, end).split(",", -1);
            return values[0].replaceAll("[^0-9]", "").trim();
        } catch (Exception e) {
            return "";
        }
    }

    private String getPrimaryKeyColumn(String tableName) {
        return switch (tableName) {
            case "DEPT" -> "DEPTNO";
            case "EMP" -> "EMPNO";
            case "SALGRADE" -> "GRADE";
            default -> null;
        };
    }

    private ArrayList<String> getTables(Connection conn) throws SQLException {
        ArrayList<String> tables = new ArrayList<>();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"});
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        return tables;
    }

    private void executeSQL(String sql) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            outputArea.setText("Executed: " + sql);
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
