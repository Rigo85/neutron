package org.games.mvc.view;

import javafx.scene.control.Alert;
import org.games.core.Move;
import org.games.core.NeutronGame;
import org.games.core.PieceKind;

import java.util.List;

import static javafx.scene.control.ButtonType.NO;
import static javafx.scene.control.ButtonType.YES;
import static org.games.core.PieceKind.*;
import static org.games.mvc.view.NeutronPresenter.saveGame;

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
class PiecePresenter {
    private static NeutronView neutronView;
    private final PieceView pieceView;
    private final int col;
    private final int row;

    PiecePresenter(int row, int col, PieceView pieceView, NeutronView neutronView) {
        this.pieceView = pieceView;
        this.row = row;
        this.col = col;

        attachEvents();
        PiecePresenter.neutronView = neutronView;
    }

    static void update() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                neutronView.pieceViews[row][col].update(neutronView.game.table[row][col]);
            }
        }
    }

    private void attachEvents() {
        pieceView.setOnMouseClicked(event -> {
            if (neutronView.game.table[row][col] == neutronView.game.getWhoMove()) {
                update();
                PieceKind sKind = pieceView.kind;

                if (neutronView.game.table[row][col] == BLACK) sKind = SBLACK;
                else if (neutronView.game.table[row][col] == WHITE) sKind = SWHITE;
                else if (neutronView.game.table[row][col] == NEUTRON) sKind = SNEUTRON;

                final Move startPoint = new Move(row, col, pieceView.kind);
                pieceView.update(sKind);
                update(neutronView.game.moves(startPoint));
                //todo comprobar q existen jugadas para las fichas blancas.
            } else if (pieceView.kind == SCELL) {
                final Move neutronDestination = neutronView.game.applyMove(new Move(row, col, neutronView.game.getSelectedKind()));
                update();
                updateMoves();
                checkGameOver(neutronDestination);
            } else {
                update();
            }
        });
    }

    private void update(List<Move> moves) {
        moves.stream().forEach(move -> neutronView.pieceViews[move.row][move.col].update(move.kind));
    }

    private void updateMoves() {
        neutronView.movesListView.getItems().clear();
        neutronView.movesListView.getItems().addAll(neutronView.game.movements);
    }

    private void checkGameOver(Move neutronDestination) {
        int row = neutronDestination != null ? neutronDestination.row : 4;
        if (row == 0 || row == 4) {
            final Alert alert = NeutronPresenter.getAlert(YES, NO);
            alert.setTitle("Game Over");
            alert.setContentText(String.format("%s win!, do you wanna saver the game?", row == 0 ? BLACK : WHITE));
            alert.showAndWait().filter(b -> b == YES).ifPresent(e -> saveGame());

            neutronView.game = new NeutronGame();
            PiecePresenter.update();
            neutronView.movesListView.getItems().clear();
        }
    }
}
