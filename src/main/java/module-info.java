module com.example.dijkstra2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.dijkstra2 to javafx.fxml;
    exports com.example.dijkstra2;
}