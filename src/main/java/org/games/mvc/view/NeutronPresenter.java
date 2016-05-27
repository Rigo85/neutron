package org.games.mvc.view;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.Arrays;

import static javafx.scene.control.ButtonType.OK;

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

    final NeutronView neutronView;

    public NeutronPresenter(NeutronView neutronView) {

        this.neutronView = neutronView;

        attachEvents();
    }

    private void attachEvents() {
        this.neutronView.heightProperty().addListener((observable, oldValue, height) -> {
            double size = height.doubleValue() > neutronView.getWidth() ? neutronView.getWidth() : height.doubleValue();
            updateToolbar(size / 4.0);
            updateCenterPane(2.0 / 3.0 * size / 2.1);
        });

        this.neutronView.about.setOnAction(event -> about());
    }

    private void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", OK);
        alert.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm())));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
        alert.setHeaderText("Neutron game v0.1");
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
            for (int col = 0; col < 5; col++) {
                final ImageView graphic = (ImageView) neutronView.pieceViews[row][col].getGraphic();
                graphic.setFitHeight(size / 2.0);
                graphic.setFitWidth(size / 2.0);
                graphic.setPreserveRatio(true);
            }
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
