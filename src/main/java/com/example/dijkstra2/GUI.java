package com.example.dijkstra2;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUI extends Application {
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int VERTEX_RADIUS = 20;

    private Dijkstra dijkstraAlgorithm;
    private NormalGraph ng;
    private RNG rng;
    private GG gg;
    private List<Circle> vertexCircles;
    private List<Line> edgeLines;

    @Override
    public void start(Stage primaryStage) {
        Random r = new Random();
        primaryStage.setTitle("Dijkstra Algorithm GUI");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Create input controls
        Label numVerticesLabel = new Label("Number of Vertices:");
        TextField numVerticesField = new TextField();
        numVerticesField.setMaxWidth(80);

        Label weightRangeLabel = new Label("Edge Weight Range:");
        TextField weightRangeField = new TextField();
        weightRangeField.setMaxWidth(80);

        Label startVertexLabel = new Label("Start Vertex Index:");
        TextField startVertexField = new TextField();
        startVertexField.setMaxWidth(80);

        Label endVertexLabel = new Label("End Vertex Index:");
        TextField endVertexField = new TextField();
        endVertexField.setMaxWidth(80);

        Button generateButton = new Button("Generate Graph");
        Button resetButton = new Button("Reset");
        Button redrawButton = new Button("Redraw");
        Button drawRNGButton = new Button("Draw RNG");
        Button drawGGButton = new Button("Draw GG");

        gridPane.add(numVerticesLabel, 0, 0);
        gridPane.add(numVerticesField, 1, 0);
        gridPane.add(weightRangeLabel, 2, 0);
        gridPane.add(weightRangeField, 3, 0);
        gridPane.add(startVertexLabel, 0, 1);
        gridPane.add(startVertexField, 1, 1);
        gridPane.add(endVertexLabel, 2, 1);
        gridPane.add(endVertexField, 3, 1);

        // Create canvas for graph visualization
        Pane canvas = new Pane();
        canvas.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
//        canvas.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // Create layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(gridPane, generateButton, resetButton, redrawButton, drawRNGButton, drawGGButton, canvas);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Set actions for buttons
        generateButton.setOnAction(e -> {
            int numVertices = Integer.parseInt(numVerticesField.getText());
            int weightRange = Integer.parseInt(weightRangeField.getText());
            int startVertex;
            int endVertex;

            if (startVertexField.getText().isEmpty()) {
                startVertex = r.nextInt(1, numVertices);
            }
            else {
                startVertex = Integer.parseInt(startVertexField.getText());
            }
            if (endVertexField.getText().isEmpty()) {
                endVertex = r.nextInt(1, numVertices);
            }
            else {
                endVertex = Integer.parseInt(endVertexField.getText());
            }

            ng = new NormalGraph(numVertices, weightRange);
            dijkstraAlgorithm = new Dijkstra(ng.getGraph(), startVertex, endVertex);
            drawGraph(canvas);
        });

        resetButton.setOnAction(e -> {
            resetGraph(canvas);
        });

        redrawButton.setOnAction(e -> {
            if (dijkstraAlgorithm != null) {
                int numVertices = Integer.parseInt(numVerticesField.getText());
                int startVertex = Integer.parseInt(startVertexField.getText());
                int endVertex = Integer.parseInt(endVertexField.getText());

                if (startVertex < 1 || startVertex > numVertices || endVertex < 1 || endVertex > numVertices) {
                    System.out.println("Invalid vertex values");
                    return;
                }

                dijkstraAlgorithm.setStartVertex(startVertex);
                dijkstraAlgorithm.setEndVertex(endVertex);
                dijkstraAlgorithm.getShortestPath(dijkstraAlgorithm.runDijkstraAlgorithm(dijkstraAlgorithm.getGraph(), dijkstraAlgorithm.getStartVertex()), dijkstraAlgorithm.getStartVertex(), dijkstraAlgorithm.getEndVertex(), dijkstraAlgorithm.getGraph()); // Compute the new shortest path
                drawGraph(canvas);
            }
        });

        drawRNGButton.setOnAction(e -> {
            int numVertices = Integer.parseInt(numVerticesField.getText());
            int startVertex = Integer.parseInt(startVertexField.getText());
            int endVertex = Integer.parseInt(endVertexField.getText());
            rng = new RNG(numVertices);
            dijkstraAlgorithm = new Dijkstra(rng.getGraph(), startVertex, endVertex);
            drawRNG(canvas);
//            System.out.println(dijkstraAlgorithm.getShortestPath(dijkstraAlgorithm.getDistances(), startVertex, endVertex, rng.getGraph()));
            rng.findAndDisplayCycles();
        });

        drawGGButton.setOnAction(e -> {
            int numVertices = Integer.parseInt(numVerticesField.getText());
            int startVertex = Integer.parseInt(startVertexField.getText());
            int endVertex = Integer.parseInt(endVertexField.getText());
            gg = new GG(numVertices);
            dijkstraAlgorithm = new Dijkstra(gg.getGraph(), startVertex, endVertex);
            drawGG(canvas);
            gg.findAndDisplayCycles();
        });

        Scene scene = new Scene(scrollPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawGraph(Pane canvas) {
        canvas.getChildren().clear();

        int numVertices = dijkstraAlgorithm.getNumVertices();
        double centerX = CANVAS_WIDTH / 2.0;
        double centerY = CANVAS_HEIGHT / 2.0;

        vertexCircles = new ArrayList<>();
        for (int i = 1; i <= numVertices; i++) {
            double angle = (2 * Math.PI / numVertices) * (i - 1);
            double x = centerX + (Math.cos(angle) * (CANVAS_WIDTH / 4));
            double y = centerY + (Math.sin(angle) * (CANVAS_HEIGHT / 4));
            Circle circle;
            if (i == dijkstraAlgorithm.getStartVertex()) {
                circle = new Circle(x, y, VERTEX_RADIUS, Color.GREEN);
            }
            else {
                circle = new Circle(x, y, VERTEX_RADIUS, Color.WHITE);
            }
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(1);
            vertexCircles.add(circle);
            canvas.getChildren().add(circle);

            // Create label for vertex
            Label vertexLabel = new Label("" + i);
            vertexLabel.setFont(Font.font(12));
            vertexLabel.setLayoutX(x);
            vertexLabel.setLayoutY(y - 20);
            canvas.getChildren().add(vertexLabel);
        }


        edgeLines = new ArrayList<>();
        for (int i = 1; i <= numVertices; i++) {
            for (Edge edge : dijkstraAlgorithm.getGraph()[i]) {
                double startX = vertexCircles.get(i - 1).getCenterX();
                double startY = vertexCircles.get(i - 1).getCenterY();
                double endX = vertexCircles.get(edge.getEnd() - 1).getCenterX();
                double endY = vertexCircles.get(edge.getEnd() - 1).getCenterY();

                Line line = new Line(startX, startY, endX, endY);
                edgeLines.add(line);
                canvas.getChildren().add(line);

                // Create label for edge
                double labelX = (startX + endX) / 2;
                double labelY = (startY + endY) / 2;

                Label weightLabel = new Label(String.valueOf(edge.getWeight()));
                weightLabel.setFont(Font.font(12));
                weightLabel.setLayoutX(labelX);
                weightLabel.setLayoutY(labelY);
                canvas.getChildren().add(weightLabel);

                if (DijkstraUtils.shortestPathContainsEdge(dijkstraAlgorithm.getShortestPath(dijkstraAlgorithm.getDistances(), dijkstraAlgorithm.getStartVertex(), dijkstraAlgorithm.getEndVertex(), dijkstraAlgorithm.getGraph()) ,i, edge.getEnd())) {
                    line.setStroke(Color.BLUE);
                    weightLabel.setTextFill(Color.BLUE);
                }
            }
        }
    }

    private void drawRNG(Pane canvas) {
        if (rng != null) {
            canvas.getChildren().clear();

            int numVertices = rng.getNumVertices();
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;

            vertexCircles = new ArrayList<>();
            for (int i = 1; i <= numVertices; i++) {
                double x = rng.getCoordinates(i)[0];
                double y = rng.getCoordinates(i)[1];

                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);

                Circle circle = new Circle(x, y, 3, Color.WHITE);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(1);
                vertexCircles.add(circle);
                canvas.getChildren().add(circle);

                Label vertexLabel = new Label("" + i);
                vertexLabel.setFont(Font.font(8));
                vertexLabel.setLayoutX(x);
                vertexLabel.setLayoutY(y - 20);
                canvas.getChildren().add(vertexLabel);
            }

            edgeLines = new ArrayList<>();
            for (int i = 1; i <= numVertices; i++) {
                for (Edge edge : dijkstraAlgorithm.getGraph()[i]) {
                    double startX = vertexCircles.get(i - 1).getCenterX();
                    double startY = vertexCircles.get(i - 1).getCenterY();
                    double endX = vertexCircles.get(edge.getEnd() - 1).getCenterX();
                    double endY = vertexCircles.get(edge.getEnd() - 1).getCenterY();

                    Line line = new Line(startX, startY, endX, endY);
                    edgeLines.add(line);
                    canvas.getChildren().add(line);

                    // Create label for edge
                    double labelX = (startX + endX) / 2;
                    double labelY = (startY + endY) / 2;

                    Label weightLabel = new Label(String.valueOf(edge.getWeight()));
                    weightLabel.setFont(Font.font(12));
                    weightLabel.setLayoutX(labelX);
                    weightLabel.setLayoutY(labelY);
                    canvas.getChildren().add(weightLabel);

                    if (DijkstraUtils.shortestPathContainsEdge(dijkstraAlgorithm.getShortestPath(dijkstraAlgorithm.getDistances(), dijkstraAlgorithm.getStartVertex(), dijkstraAlgorithm.getEndVertex(), dijkstraAlgorithm.getGraph()) ,i, edge.getEnd())) {
                        line.setStroke(Color.BLUE);
                        weightLabel.setTextFill(Color.BLUE);
                    }
                }
            }

            // Adjust canvas size based on vertex coordinates
            double canvasWidth = maxX - minX + (2 * VERTEX_RADIUS);
            double canvasHeight = maxY - minY + (2 * VERTEX_RADIUS);
            canvas.setPrefSize(canvasWidth, canvasHeight);
            canvas.setLayoutX(minX - VERTEX_RADIUS);
            canvas.setLayoutY(minY - VERTEX_RADIUS);
        }
    }

    private void drawGG(Pane canvas) {
        if (gg != null) {
            canvas.getChildren().clear();

            int numVertices = gg.getNumVertices();
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;

            vertexCircles = new ArrayList<>();
            for (int i = 1; i <= numVertices; i++) {
                double x = gg.getCoordinates(i)[0];
                double y = gg.getCoordinates(i)[1];

                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);

                Circle circle = new Circle(x, y, 3, Color.WHITE);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(1);
                vertexCircles.add(circle);
                canvas.getChildren().add(circle);

                Label vertexLabel = new Label("" + i);
                vertexLabel.setFont(Font.font(8));
                vertexLabel.setLayoutX(x);
                vertexLabel.setLayoutY(y - 20);
                canvas.getChildren().add(vertexLabel);
            }

            edgeLines = new ArrayList<>();
            for (int i = 1; i <= numVertices; i++) {
                for (Edge edge : dijkstraAlgorithm.getGraph()[i]) {
                    double startX = vertexCircles.get(i - 1).getCenterX();
                    double startY = vertexCircles.get(i - 1).getCenterY();
                    double endX = vertexCircles.get(edge.getEnd() - 1).getCenterX();
                    double endY = vertexCircles.get(edge.getEnd() - 1).getCenterY();

                    Line line = new Line(startX, startY, endX, endY);
                    edgeLines.add(line);
                    canvas.getChildren().add(line);

                    // Create label for edge
                    double labelX = (startX + endX) / 2;
                    double labelY = (startY + endY) / 2;

                    Label weightLabel = new Label(String.valueOf(edge.getWeight()));
                    weightLabel.setFont(Font.font(12));
                    weightLabel.setLayoutX(labelX);
                    weightLabel.setLayoutY(labelY);
                    canvas.getChildren().add(weightLabel);

                    if (DijkstraUtils.shortestPathContainsEdge(dijkstraAlgorithm.getShortestPath(dijkstraAlgorithm.getDistances(), dijkstraAlgorithm.getStartVertex(), dijkstraAlgorithm.getEndVertex(), dijkstraAlgorithm.getGraph()) ,i, edge.getEnd())) {
                        line.setStroke(Color.BLUE);
                        weightLabel.setTextFill(Color.BLUE);
                    }
                }
            }

            // Adjust canvas size based on vertex coordinates
            double canvasWidth = maxX - minX + (2 * VERTEX_RADIUS);
            double canvasHeight = maxY - minY + (2 * VERTEX_RADIUS);
            canvas.setPrefSize(canvasWidth, canvasHeight);
            canvas.setLayoutX(minX - VERTEX_RADIUS);
            canvas.setLayoutY(minY - VERTEX_RADIUS);
        }
    }

    private void resetGraph(Pane canvas) {
        canvas.getChildren().clear();
        vertexCircles = null;
        edgeLines = null;
        dijkstraAlgorithm = null;
        rng = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}