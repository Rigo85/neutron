package org.games.mvc.view;

import org.games.core.Move;
import org.games.core.PieceKind;

import java.util.List;

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
class Updater {
    private final PieceView[][] pieceViews;

    Updater(PieceView[][] pieceViews) {
        this.pieceViews = pieceViews;
    }

    void update(PieceKind[][] table) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                pieceViews[row][col].update(table[row][col]);
            }
        }
    }

    void update(List<Move> moves){
        moves.stream().forEach(move -> {
            pieceViews[move.row][move.col].update(move.kind);
        });
    }
}
