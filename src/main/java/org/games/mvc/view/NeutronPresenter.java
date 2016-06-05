package org.games.mvc.view;

import com.google.gson.Gson;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.games.core.NeutronGame;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javafx.scene.control.ButtonType.*;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2016 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
public class NeutronPresenter {

    private static NeutronView neutronView;

    public NeutronPresenter(NeutronView neutronView) {
        NeutronPresenter.neutronView = neutronView;
        attachEvents();
    }

    private void attachEvents() {
        neutronView.heightProperty().addListener((observable, oldValue, height) -> {
            double size = height.doubleValue() > neutronView.getWidth() ? neutronView.getWidth() : height.doubleValue();
            updateToolbar(size / 4.0);
            updateCenterPane(2.0 / 3.0 * size / 2.1);
            neutronView.movesListView.setPrefHeight(height.doubleValue() * 0.6);
        });

        neutronView.about.setOnAction(event -> about());
        neutronView.newGame.setOnAction(event -> newGame());
        neutronView.saveGame.setOnAction(event -> saveGame());
        neutronView.loadGame.setOnAction(event -> loadGame());
        neutronView.info.setOnAction(event -> showInfo());
    }

    private void showInfo() {
        final Alert alert = getAlert(OK);
        alert.setHeaderText("Rules and Game Play");
        final Stream<String> lines = new BufferedReader(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("info.txt"))).lines();
        alert.setContentText(lines.collect(Collectors.joining(System.getProperty("line.separator"))));
        alert.setTitle("Information");
        alert.getDialogPane().setPrefWidth(700);
        alert.showAndWait();
    }

    private void loadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose saved game");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Neutron Game Files", "*.neu"));

        final File file = fileChooser.showOpenDialog(neutronView.getScene().getWindow());

        if (file != null) {
            neutronView.game = null;

            try (FileReader fr = new FileReader(file)) {
                Gson g = new Gson();
                neutronView.game = g.fromJson(fr, NeutronGame.class);
            } catch (IOException e) {
                final Alert alert = getAlert(OK);
                alert.setContentText(e.getMessage());
                alert.setTitle("Error");
                alert.showAndWait();
            } finally {
                if (neutronView.game == null) neutronView.game = new NeutronGame();
            }

            PiecePresenter.update();
            neutronView.movesListView.getItems().clear();
            neutronView.movesListView.getItems().addAll(neutronView.game.movements);
        }
    }

    private void newGame() {
        Alert alert = getAlert(YES, NO);

        alert.setContentText("Do you want to save the current game?");
        alert.setTitle("New Game");
        alert.showAndWait().filter(b -> b == YES).ifPresent(e -> saveGame());

        neutronView.game = new NeutronGame();
        PiecePresenter.update();
        neutronView.movesListView.getItems().clear();
    }

    public static Alert getAlert(ButtonType... buttons) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", buttons);
        alert.setGraphic(new ImageView(new Image(NeutronPresenter.class.getClassLoader().getResource("images/icon.png")
                .toExternalForm())));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        stage.getIcons().add(new Image(NeutronPresenter.class.getClassLoader().getResource("images/icon.png")
                .toExternalForm()));
        alert.setHeaderText("Neutron game v0.1");
        return alert;
    }

    static void saveGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose save location");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Neutron Game Files", "*.neu"));
        final LocalDateTime now = LocalDateTime.now();
        fileChooser.setInitialFileName(String.format("neutron-game-%s.neu",
                LocalDateTime.of(now.getYear(),
                        now.getMonth(),
                        now.getDayOfMonth(),
                        now.getHour(),
                        now.getMinute(),
                        now.getSecond())
                        .toString()
                        .replaceAll(":", "-")));

        final File file = fileChooser.showSaveDialog(neutronView.getScene().getWindow());

        if (file != null) {
            try (FileWriter fw = new FileWriter(file)) {
                new Gson().toJson(neutronView.game, fw);
            } catch (Exception e) {
                final Alert alert = getAlert(OK);
                alert.setContentText(e.getMessage());
                alert.setTitle("Error");
                alert.showAndWait();
            }
        }
    }

    private void about() {
        Alert alert = getAlert(OK);

        alert.setContentText("Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>" +
                "\n\n" +
                "Copyright 2016 by Rigoberto Leander Salgado Reyes." +
                "\n\n" +
                "This program is licensed to you under the terms of version 3 of the" +
                "GNU Affero General Public License. This program is distributed WITHOUT" +
                "ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT," +
                "MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the" +
                "AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.");
        alert.setTitle("About Dialog");
        alert.showAndWait();
    }

    private void updateCenterPane(double size) {
        for (int row = 0; row < 5; row++) {
            ImageView number = (ImageView) neutronView.numbers[row].getGraphic();
            number.setFitHeight(size / 2.5);
            number.setFitWidth(size / 2.5);
            number.setPreserveRatio(true);

            for (int col = 0; col < 5; col++) {
                final ImageView graphic = (ImageView) neutronView.pieceViews[row][col].getGraphic();
                graphic.setFitHeight(size / 2.5);
                graphic.setFitWidth(size / 2.5);
                graphic.setPreserveRatio(true);
            }

            ImageView charN = (ImageView) neutronView.chars[row].getGraphic();
            charN.setFitHeight(size / 2.5);
            charN.setFitWidth(size / 2.5);
            charN.setPreserveRatio(true);
        }
    }

    private void updateToolbar(double size) {
        Arrays.stream(neutronView.actions).forEach(button -> {
            final ImageView graphic = (ImageView) button.getGraphic();
            graphic.setFitHeight(size / 3.0);
            graphic.setFitWidth(size / 3.0);
            graphic.setPreserveRatio(true);
        });
    }
}
