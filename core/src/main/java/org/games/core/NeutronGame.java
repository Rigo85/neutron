package org.games.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.games.core.NeutronGame.Direction.*;
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
public class NeutronGame {
    public final PieceKind[][] table;
    private final PieceKind[] rotation = {NEUTRON, WHITE, NEUTRON, BLACK};
    private int whoMove = 0;
    private Move selected = null;

    public NeutronGame() {
        table = new PieceKind[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                table[row][col] = CELL;
            }
        }

        table[0][0] = BLACK;
        table[0][1] = BLACK;
        table[0][2] = BLACK;
        table[0][3] = BLACK;
        table[0][4] = BLACK;

        table[2][2] = NEUTRON;

        table[4][0] = WHITE;
        table[4][1] = WHITE;
        table[4][2] = WHITE;
        table[4][3] = WHITE;
        table[4][4] = WHITE;
    }

    public List<Move> moves(int row, int col) {
        selected = null;
        selected = new Move(row, col, null);
        Direction[] directions = {NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST};
        return Arrays.stream(directions)
                .map(direction -> checkMove(row, col, direction))
                .filter(move -> move != null)
                .collect(Collectors.toList());
    }

    private Move checkMove(int row, int col, Direction direction) {
        int incRow = getRowMove(direction);
        int incCol = getColMove(direction);
        int newRow = row;
        int newCol = col;

        while (rowInBounds(newRow, incRow) && colInBounds(newCol, incCol) &&
                table[newRow + incRow][newCol + incCol] == CELL) {
            newRow += incRow;
            newCol += incCol;
        }

        return newRow == row && newCol == col ? null : new Move(newRow, newCol, SCELL);
    }

    private boolean colInBounds(int newCol, int incCol) {
        return newCol + incCol >= 0 && newCol + incCol < 5;
    }

    private boolean rowInBounds(int newRow, int incRow) {
        return newRow + incRow >= 0 && newRow + incRow < 5;
    }

    private int getRowMove(Direction direction) {
        int inc;
        switch (direction) {
            case NORTH:
                inc = -1;
                break;
            case SOUTH:
                inc = 1;
                break;
            case NORTHEAST:
                inc = -1;
                break;
            case NORTHWEST:
                inc = -1;
                break;
            case SOUTHEAST:
                inc = 1;
                break;
            case SOUTHWEST:
                inc = 1;
                break;
            default:
                inc = 0;
                break;
        }
        return inc;
    }

    private int getColMove(Direction direction) {
        int inc;
        switch (direction) {
            case WEST:
                inc = -1;
                break;
            case EAST:
                inc = 1;
                break;
            case NORTHEAST:
                inc = 1;
                break;
            case NORTHWEST:
                inc = -1;
                break;
            case SOUTHEAST:
                inc = 1;
                break;
            case SOUTHWEST:
                inc = -1;
                break;
            default:
                inc = 0;
                break;
        }
        return inc;
    }

    private void updateWhoMove() {
        whoMove = (whoMove + 1) % 4;
    }

    public PieceKind getWhoMove() {
        return rotation[whoMove];
    }

    public void applyMove(int row, int col) throws Exception {
        if (selected != null) {
            table[row][col] = table[selected.row][selected.col];
            table[selected.row][selected.col] = CELL;
            updateWhoMove();
        } else {
            throw new Exception("You can't move unselected piece ;-)");
        }
    }

    private int heuristic(PieceKind[][] table, PieceKind kind) {
        int blacks = 0, whites = 0, neutron = 0;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (table[row][col] == BLACK) {
                    blacks += moves(row, col).size();
                } else if (table[row][col] == WHITE) {
                    whites += moves(row, col).size();
                } else if (table[row][col] == NEUTRON) {
                    neutron += moves(row, col).size();
                }
            }
        }

        return whites - blacks;
    }



    enum Direction {NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST}
}
