import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class project extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Project Menu");

        VBox menuBox = new VBox(15);
        menuBox.setPadding(new Insets(20));
        menuBox.setAlignment(Pos.TOP_LEFT);

        Button btnCalculator = new Button("1. Calculator");
        Button btnCrudOps = new Button("2. CRUD Operations");
        Button btnCrudFx = new Button("3. CRUD FX");

        btnCalculator.setPrefWidth(180);
        btnCrudOps.setPrefWidth(180);
        btnCrudFx.setPrefWidth(180);

        TextArea outputArea = new TextArea();
        outputArea.setPrefSize(300, 200);
        outputArea.setEditable(false);

        btnCalculator.setOnAction(e -> {
            outputArea.setText("Opening Calculator module...");
            // Launch calculator in new window
            calculator.launchCalculator();
        });

        btnCrudOps.setOnAction(e -> outputArea.setText("Opening CRUD Operations module..."));
        btnCrudFx.setOnAction(e -> outputArea.setText("Opening CRUD FX module..."));

        menuBox.getChildren().addAll(btnCalculator, btnCrudOps, btnCrudFx);

        HBox root = new HBox(20);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(menuBox, outputArea);

        Scene scene = new Scene(root, 520, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
