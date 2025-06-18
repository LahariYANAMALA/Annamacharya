import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MenuCardApp extends Application {

    Label actionLabel = new Label("Choose an action from the menu.");

    @Override
    public void start(Stage primaryStage) {
        // Menu bar
        MenuBar menuBar = new MenuBar();

        // Menu
        Menu menu = new Menu("Database Options");

        // Menu items
        MenuItem createItem = new MenuItem("Create");
        MenuItem insertItem = new MenuItem("Insert");
        MenuItem updateItem = new MenuItem("Update");
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem selectItem = new MenuItem("Select");

        // Add menu items to menu
        menu.getItems().addAll(createItem, insertItem, updateItem, deleteItem, selectItem);
        menuBar.getMenus().add(menu);

        // Set actions for each item
        createItem.setOnAction(e -> actionLabel.setText("Create table option selected."));
        insertItem.setOnAction(e -> actionLabel.setText("Insert data option selected."));
        updateItem.setOnAction(e -> actionLabel.setText("Update data option selected."));
        deleteItem.setOnAction(e -> actionLabel.setText("Delete data option selected."));
        selectItem.setOnAction(e -> actionLabel.setText("Select data option selected."));

        // Layout
        VBox root = new VBox(menuBar, actionLabel);
        root.setSpacing(10);
        root.setStyle("-fx-padding: 20;");

        // Scene
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setTitle("JavaFX MenuCard - CRUD Options");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
