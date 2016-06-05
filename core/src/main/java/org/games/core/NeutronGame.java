package org.games.core;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
    @SerializedName("table")
    public final PieceKind[][] table;

    @SerializedName("movements")
    public final List<FullMove> movements;
    private final PieceKind[] rotation = {NEUTRON, WHITE};
    @SerializedName("depth")
    public int depth = 4;
    private int whoMove = 0;
    private Move selected = null;
    private Move neutronFrom = null;
    private Move neutronTo = null;

    public NeutronGame() {
        table = new PieceKind[5][5];
        movements = new ArrayList<>();

        Arrays.setAll(table[0], i -> BLACK);
        Arrays.setAll(table[1], i -> CELL);
        Arrays.setAll(table[2], i -> i == 2 ? NEUTRON : CELL);
        Arrays.setAll(table[3], i -> CELL);
        Arrays.setAll(table[4], i -> WHITE);
    }

    public List<Move> moves(Move startPoint) {
        selected = null;
        selected = startPoint;

        return moves(startPoint.clone(SCELL), table);
    }

    public Move applyMove(Move to) {
        Move neutronDestination = null;
        applyMove(selected, to, table);

        if (getWhoMove() == WHITE) {
            movements.add(new FullMove(neutronFrom, neutronTo, selected, to));
            final FullMove machineFullMove = maxValue(table, depth, -Integer.MAX_VALUE, Integer.MAX_VALUE, BLACK);
//            final FullMove machineFullMove = alphaBetaMiniMax(table, 4, BLACK, WHITE, -Integer.MAX_VALUE, Integer.MAX_VALUE);

            if (machineFullMove != null) {
                movements.add(machineFullMove);
                neutronDestination = machineFullMove.moves.get(1);
                applyMove(machineFullMove, table);
            }
        } else {
            neutronFrom = selected;
            neutronTo = to;
            neutronDestination = to;
        }

        updateWhoMove();

        return neutronDestination;
    }

    public PieceKind getSelectedKind() {
        return selected.kind;
    }

    public PieceKind getWhoMove() {
        return rotation[whoMove];
    }

    private List<Move> moves(Move startPoint, PieceKind[][] table) {
        Direction[] directions = {NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST};

        return Arrays.stream(directions)
                .map(direction -> checkMove(startPoint, direction, table))
                .filter(move -> move != null)
                .collect(Collectors.toList());
    }

    private Move checkMove(Move move, Direction direction, PieceKind[][] table) {
        int incRow = getRowMove(direction);
        int incCol = getColMove(direction);
        int newRow = move.row;
        int newCol = move.col;

        while (inBounds(newRow, incRow) && inBounds(newCol, incCol) && table[newRow + incRow][newCol + incCol] == CELL) {
            newRow += incRow;
            newCol += incCol;
        }

        return newRow == move.row && newCol == move.col ? null : new Move(newRow, newCol, move.kind);
    }

    private boolean inBounds(int value, int inc) {
        return value + inc >= 0 && value + inc < 5;
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
        whoMove = (whoMove + 1) % 2;
    }

    private void applyMove(FullMove fullMove, PieceKind[][] table) {
        applyMove(fullMove, table, true);
    }

    private void applyMove(FullMove fullMove, PieceKind[][] table, boolean apply) {
        applyMove(fullMove.moves.get(apply ? 0 : 3), fullMove.moves.get(apply ? 1 : 2), table);
        applyMove(fullMove.moves.get(apply ? 2 : 1), fullMove.moves.get(apply ? 3 : 0), table);
    }

    private void applyMove(Move from, Move to, PieceKind[][] table) {
        table[to.row][to.col] = to.kind;
        if (from != to) table[from.row][from.col] = CELL;
    }

    private int heuristic(PieceKind[][] table, PieceKind player) {
        int blacks = 0, whites = 0;
        int goalBlacks = 0, goalWhites = 0;

        final Move neutron = findNeutron(table);
        if (neutron.row == 0) return 1000;
        if (neutron.row == 4) return -1000;

        final List<Move> moves = moves(neutron, table);
        for (Move move : moves) {
            if (move.row == 0) goalBlacks += 100;
            if (move.row == 4) goalWhites += 100;
        }

        if (player == WHITE && goalWhites != 0) return -goalWhites;
        if (player == BLACK && goalBlacks != 0) return goalBlacks;
        if (player == BLACK && goalWhites != 0) whites += 100;

        final int emptySpacesInBlacks = (int) Arrays.stream(table[0]).map(cell -> cell == CELL ? 1 : 0).count();
        final int emptySpacesInWhites = (int) Arrays.stream(table[4]).map(cell -> cell == CELL ? 1 : 0).count();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (table[row][col] == BLACK) {
                    blacks += moves(new Move(row, col, BLACK), table).size();
                } else if (table[row][col] == WHITE) {
                    whites += moves(new Move(row, col, WHITE), table).size();
                }
            }
        }

        return (blacks * emptySpacesInBlacks) - (whites * emptySpacesInWhites);
    }

    private List<FullMove> allMoves(PieceKind player, PieceKind[][] table) {
        List<FullMove> moves = new ArrayList<>();
        final Move neutron = findNeutron(table);
        final AtomicReference<Move> lastNeutronMove = new AtomicReference<>(neutron);
        int playerHome = player == BLACK ? 0 : 4;
        int opponentHome = player == BLACK ? 4 : 0;
        final List<Move> neutronMoves = moves(neutron, table);
        neutronMoves.sort((o1, o2) -> {
            if (o1.row == playerHome || o2.row == opponentHome) return -1;
            if (o2.row == playerHome || o1.row == opponentHome) return 1;
            return 0;
        });
        final List<Move> pieces = findPieces(player, table);

        neutronMoves.stream()
                .forEach(neutronMove -> {
                    applyMove(lastNeutronMove.get(), neutron, table);
                    applyMove(neutron, neutronMove, table);
                    lastNeutronMove.set(neutronMove);
                    pieces.stream().forEach(piece -> moves.addAll(moves(piece, table)
                            .stream()
                            .map(pieceMove -> new FullMove(neutron, neutronMove, piece, pieceMove))
                            .collect(Collectors.toList())));
                });

        applyMove(lastNeutronMove.get(), neutron, table);

        return moves;
    }

    private FullMove minValue(PieceKind[][] table, int depth, int alpha, int beta, PieceKind kind) {
        if (depth == 0) return new FullMove(heuristic(table, kind));

        FullMove minFullMove = null;
        final List<FullMove> fullMoves = allMoves(kind, table);
        int newBeta = beta;

        for (FullMove fullMove : fullMoves) {
            applyMove(fullMove, table);
            FullMove maxFullMove;

            if (fullMove.moves.get(1).row == 0 || fullMove.moves.get(1).row == 4) {
                maxFullMove = new FullMove(heuristic(table, kind));
            } else {
                maxFullMove = maxValue(table, depth - 1, alpha, newBeta, kind == BLACK ? WHITE : BLACK);
            }

            applyMove(fullMove, table, false);

            if (maxFullMove != null && maxFullMove.score < newBeta) {
                newBeta = maxFullMove.score;
                minFullMove = fullMove;
                minFullMove.score = newBeta;
            }

            if (alpha >= newBeta) {
                fullMove.score = newBeta;
                return fullMove;
            }
        }

        return minFullMove;
    }

    private FullMove maxValue(PieceKind[][] table, int depth, int alpha, int beta, PieceKind player) {
        if (depth == 0) return new FullMove(heuristic(table, player));

        FullMove maxFullMove = null;
        final List<FullMove> fullMoves = allMoves(player, table);
        int newAlpha = alpha;

        for (FullMove fullMove : fullMoves) {
            applyMove(fullMove, table);
            FullMove minFullMove;

            if (fullMove.moves.get(1).row == 0 || fullMove.moves.get(1).row == 4) {
                minFullMove = new FullMove(heuristic(table, player));
            } else {
                minFullMove = minValue(table, depth - 1, newAlpha, beta, player == BLACK ? WHITE : BLACK);
            }

            applyMove(fullMove, table, false);

            if (minFullMove != null && minFullMove.score > newAlpha) {
                newAlpha = minFullMove.score;
                maxFullMove = fullMove;
                maxFullMove.score = newAlpha;
            }

            if (newAlpha >= beta) {
                fullMove.score = newAlpha;
                return fullMove;
            }
        }

        return maxFullMove;
    }

    private List<Move> findPieces(PieceKind kind, PieceKind[][] table) {
        List<Move> pieces = new ArrayList<>();

        for (int row = 0; row < 5; row++)
            for (int col = 0; col < 5; col++)
                if (table[row][col] == kind) pieces.add(new Move(row, col, kind));

        return pieces;
    }

    private Move findNeutron(PieceKind[][] table) {
        for (int row = 0; row < 5; row++)
            for (int col = 0; col < 5; col++)
                if (table[row][col] == NEUTRON) return new Move(row, col, NEUTRON);

        return null;
    }

    private PieceKind[][] copyTable(PieceKind[][] table) {
        PieceKind[][] newTable = new PieceKind[5][5];

        System.arraycopy(table[0], 0, newTable[0], 0, 5);
        System.arraycopy(table[1], 0, newTable[1], 0, 5);
        System.arraycopy(table[2], 0, newTable[2], 0, 5);
        System.arraycopy(table[3], 0, newTable[3], 0, 5);
        System.arraycopy(table[4], 0, newTable[4], 0, 5);

        return newTable;
    }

    private FullMove alphaBetaMiniMax(PieceKind[][] table, int depth, PieceKind player, PieceKind opponent, int low, int high) {
        if (depth == 0) return new FullMove(heuristic(table, player));

        FullMove best = new FullMove();
        final List<FullMove> fullMoves = allMoves(player, table);

        for (FullMove fullMove : fullMoves) {
            final PieceKind[][] newTable = copyTable(table);
            applyMove(fullMove, newTable);

            final FullMove newFullMove = alphaBetaMiniMax(newTable, depth - 1, opponent, player, -high, -low);

            if (best.isEmpty() || -newFullMove.score > best.score) {
                low = -newFullMove.score;
                best.moves = fullMove.moves;
                best.score = low;
            }

            if (low >= high) return best;
        }

        return best;
    }

    private void tableToString(PieceKind[][] table) {
        final String tableString = Arrays.stream(table)
                .map(tb -> Arrays.stream(tb)
                        .map(this::pieceToString)
                        .collect(Collectors.joining("|", "||", "||")))
                .collect(Collectors.joining("\n"));
        System.out.println(tableString);
    }

    private String pieceToString(PieceKind kind) {
        String str;
        switch (kind) {
            case BLACK:
                str = "B";
                break;
            case WHITE:
                str = "W";
                break;
            case NEUTRON:
                str = "N";
                break;
            default:
                str = " ";
        }
        return str;
    }

    enum Direction {
        NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST
    }
}
