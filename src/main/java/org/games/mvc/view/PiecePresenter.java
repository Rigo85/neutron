package org.games.mvc.view;

import org.games.core.NeutronGame;
import org.games.core.PieceKind;

import static org.games.core.PieceKind.*;

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
    private final PieceView pieceView;
    private final NeutronGame game;
    private final Updater updater;
    private final int col;
    private final int row;

    PiecePresenter(int row, int col, PieceView pieceView, Updater updater, NeutronGame game) {
        this.pieceView = pieceView;
        this.game = game;
        this.updater = updater;
        this.row = row;
        this.col = col;

        attachEvents();
    }

    private void attachEvents() {
        pieceView.setOnMouseClicked(event -> {
            if (game.table[row][col] == game.getWhoMove()) {
                updater.update(game.table);
                PieceKind kind = pieceView.kind;
                if (game.table[row][col] == BLACK) {
                    kind = SBLACK;
                } else if (game.table[row][col] == WHITE) {
                    kind = SWHITE;
                } else if (game.table[row][col] == NEUTRON) {
                    kind = SNEUTRON;
                }
                pieceView.update(kind);
                updater.update(game.moves(row, col));
            } else if (pieceView.kind == SCELL) {
                try {
                    game.applyMove(row, col);
                    updater.update(game.table);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                updater.update(game.table);
            }
        });


    }
}
