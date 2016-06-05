package org.games.core;

import com.google.gson.annotations.SerializedName;

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
public class Move {
    @SerializedName("row")
    public int row;

    @SerializedName("col")
    public int col;

    @SerializedName("kind")
    public PieceKind kind;

    public Move(int row, int col, PieceKind kind) {
        this.row = row;
        this.col = col;
        this.kind = kind;
    }

    Move clone(PieceKind kind) {
        return new Move(row, col, kind);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;

        Move move = (Move) o;

        return row == move.row && col == move.col && kind == move.kind;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        result = 31 * result + (kind != null ? kind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        char[] chars = {'a', 'b', 'c', 'd', 'e'};
        return String.format("%c%d", chars[col], 5 - row);
    }
}
