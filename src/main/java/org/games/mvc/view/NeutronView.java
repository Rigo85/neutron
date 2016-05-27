package org.games.mvc.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.games.core.NeutronGame;

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
public class NeutronView extends BorderPane {
    private final NeutronGame game;
    PieceView[][] pieceViews;
    Button[] actions;
    Button newGame;
     Button saveGame;
     Button loadGame;
     Button about;
     Button info;

    public NeutronView() {
        this.game = new NeutronGame();

        createToolBar(32);
        createCenterPanel(32);
    }

    private void createCenterPanel(double size) {
        pieceViews = new PieceView[5][5];
        Updater updater = new Updater(pieceViews);
        GridPane table = new GridPane();
        table.setPadding(new Insets(5, 5, 5, 5));
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                pieceViews[row][col] = new PieceView(game.table[row][col], size / 2.0);
                new PiecePresenter(row, col, pieceViews[row][col], updater, game);
                table.add(pieceViews[row][col], col, row);
            }
        }

        Label l1 = new Label();
        l1.setMaxWidth(Double.MAX_VALUE);
        Label l2 = new Label();
        l2.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(l1, Priority.ALWAYS);
        HBox.setHgrow(l2, Priority.ALWAYS);
        HBox hBox = new HBox(l1, table, l2);

        Label l3 = new Label();
        l3.setMaxHeight(Double.MAX_VALUE);
        Label l4 = new Label();
        VBox.setVgrow(l3, Priority.ALWAYS);
        VBox.setVgrow(l4, Priority.ALWAYS);
        l4.setMaxHeight(Double.MAX_VALUE);

        VBox vBox = new VBox(l3, hBox, l4);
        setCenter(vBox);
    }

    private void createToolBar(double size) {
        final ImageView imageView1 = new ImageView(new Image(getClass().getClassLoader().getResource("images/newGame.png").toExternalForm()));
        imageView1.setFitHeight(size);
        imageView1.setFitWidth(size);
        imageView1.setPreserveRatio(true);
        newGame = new Button("", imageView1);

        final ImageView imageView2 = new ImageView(new Image(getClass().getClassLoader().getResource("images/saveGame.png").toExternalForm()));
        imageView2.setFitHeight(size);
        imageView2.setFitWidth(size);
        imageView2.setPreserveRatio(true);
        saveGame = new Button("", imageView2);

        final ImageView imageView3 = new ImageView(new Image(getClass().getClassLoader().getResource("images/loadGame.png").toExternalForm()));
        imageView3.setFitHeight(size);
        imageView3.setFitWidth(size);
        imageView3.setPreserveRatio(true);
        loadGame = new Button("", imageView3);

        final ImageView imageView4 = new ImageView(new Image(getClass().getClassLoader().getResource("images/info.png").toExternalForm()));
        imageView4.setFitHeight(size);
        imageView4.setFitWidth(size);
        imageView4.setPreserveRatio(true);
        info = new Button("", imageView4);

        final ImageView imageView5 = new ImageView(new Image(getClass().getClassLoader().getResource("images/about.png").toExternalForm()));
        imageView5.setFitHeight(size);
        imageView5.setFitWidth(size);
        imageView5.setPreserveRatio(true);
        about = new Button("", imageView5);

        actions = new Button[5];
        actions[0] = newGame;
        actions[1] = saveGame;
        actions[2] = loadGame;
        actions[3] = info;
        actions[4] = about;
        Label l1 = new Label();
        Label l2 = new Label();
        l1.setMaxWidth(Double.MAX_VALUE);
        l2.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(l1, Priority.ALWAYS);
        HBox.setHgrow(l2, Priority.ALWAYS);
        HBox toolBar = new HBox(5, l1, newGame, saveGame, loadGame, info, about, l2);
        toolBar.setPadding(new Insets(5, 5, 5, 5));
        this.setTop(toolBar);
    }
}
