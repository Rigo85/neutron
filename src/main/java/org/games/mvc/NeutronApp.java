package org.games.mvc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.games.mvc.view.NeutronPresenter;
import org.games.mvc.view.NeutronView;

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
public class NeutronApp  extends Application {
        public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        NeutronView neutronView = new NeutronView();
        new NeutronPresenter(neutronView);

        Scene scene = new Scene(neutronView, 500, 500);
        stage.setScene(scene);
        stage.setTitle("Neutron game");

        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to exit?", ButtonType.YES, ButtonType.CANCEL);
            Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
            stage1.getIcons().add(new Image(
                    getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            alert.setTitle("Confirmation Dialog");
            alert.showAndWait().filter(b -> b == ButtonType.CANCEL).ifPresent(e -> event.consume());
        });

        stage.getIcons().add(new Image(
                getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        stage.show();
    }
}
