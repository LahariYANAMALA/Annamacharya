import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CheckboxExample extends Application {

    ComboBox<String> tableSelector = new ComboBox<>();
    TextField tableNameField = new TextField();
    TextField tableTypeField = new TextField();
    TextField fieldsField = new TextField();
    TextField updatedValuesField = new TextField();
    Label actionLabel = new Label("Select an action.");

    @Override
    public void start(Stage primaryStage) {
        // Checkboxes
        CheckBox insertCheckBox = new CheckBox("Insertion");
        CheckBox deleteCheckBox = new CheckBox("Deletion");
        CheckBox updateCheckBox = new CheckBox("Updation");

        // Dummy table list
        tableSelector.setItems(FXCollections.observableArrayList("Students", "Courses", "Teachers"));
        tableSelector.setPromptText("Select Table");

        // Input fields
        tableNameField.setPromptText("Table Name");
        tableTypeField.setPromptText("Type (e.g., Text, Number)");
        fieldsField.setPromptText("Fields (e.g., ID, Name)");
        updatedValuesField.setPromptText("Updated Values");

        // Initially hidden
        tableNameField.setVisible(false);
        tableTypeField.setVisible(false);
        fieldsField.setVisible(false);
        tableSelector.setVisible(false);
        updatedValuesField.setVisible(false);

        // Insertion logic
        insertCheckBox.setOnAction(e -> {
            boolean selected = insertCheckBox.isSelected();
            actionLabel.setText("Insertion selected");

            tableNameField.setVisible(selected);
            tableTypeField.setVisible(selected);
            fieldsField.setVisible(selected);

            // hide others
            tableSelector.setVisible(false);
            updatedValuesField.setVisible(false);

            deleteCheckBox.setSelected(false);
            updateCheckBox.setSelected(false);
        });

        // Deletion logic
        deleteCheckBox.setOnAction(e -> {
            boolean selected = deleteCheckBox.isSelected();
            actionLabel.setText("Deletion selected");

            tableSelector.setVisible(selected);

            // hide others
            tableNameField.setVisible(false);
            tableTypeField.setVisible(false);
            fieldsField.setVisible(false);
            updatedValuesField.setVisible(false);

            insertCheckBox.setSelected(false);
            updateCheckBox.setSelected(false);

            if (selected) {
                tableSelector.setOnAction(ev -> {
                    String table = tableSelector.getValue();
                    actionLabel.setText("Table '" + table + "' marked for deletion.");
                });
            }
        });

        // Updation logic
        updateCheckBox.setOnAction(e -> {
            boolean selected = updateCheckBox.isSelected();
            actionLabel.setText("Updation selected");

            tableSelector.setVisible(selected);
            updatedValuesField.setVisible(selected);

            // hide others
            tableNameField.setVisible(false);
            tableTypeField.setVisible(false);
            fieldsField.setVisible(false);

            insertCheckBox.setSelected(false);
            deleteCheckBox.setSelected(false);

            if (selected) {
                tableSelector.setOnAction(ev -> {
                    String table = tableSelector.getValue();
                    actionLabel.setText("Updating table: " + table);
                });
            }
        });

        VBox vbox = new VBox(10,
                insertCheckBox, deleteCheckBox, updateCheckBox,
                tableNameField, tableTypeField, fieldsField,
                tableSelector, updatedValuesField,
                actionLabel
        );

        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Scene scene = new Scene(vbox, 400, 450);
        primaryStage.setTitle("Dynamic Table Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
