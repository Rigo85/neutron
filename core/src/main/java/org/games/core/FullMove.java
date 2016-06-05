package org.games.core;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
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
public class FullMove {
    @SerializedName("score")
    public int score;

    @SerializedName("moves")
    public List<Move> moves;

    FullMove(Move... moves) {
        this.moves = Arrays.asList(moves);
        this.score = 0;
    }

    FullMove(int score) {
        this.moves = null;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullMove)) return false;

        FullMove fullMove = (FullMove) o;

        return score == fullMove.score && (moves != null ? moves.equals(fullMove.moves) : fullMove.moves == null);
    }

    @Override
    public int hashCode() {
        int result = score;
        result = 31 * result + (moves != null ? moves.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String str = "EMPTY FULLMOVE with score = " + score;
        if (moves != null && !moves.isEmpty()) {
            final String piece1Kind = moves.get(1).kind.name();
            final String piece2Kind = moves.get(3).kind.name();
            str = String.format("%s: %s-%s, %s: %s-%s", piece1Kind, moves.get(0), moves.get(1), piece2Kind, moves.get(2), moves.get(3));
        }

        return str;
    }

    boolean isEmpty() {
        return moves == null || moves.isEmpty();
    }
}
