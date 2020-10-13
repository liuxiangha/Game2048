package com.example.game2048;

import android.annotation.SuppressLint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Board {
    private static final int BLANK = 0;
    private final int n;
    private int[][] blocks;

    public Board(int[][] inBlocks) {
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        n = inBlocks.length;
        blocks = new int[n][n];
        copy(blocks, inBlocks);
    }

    private void copy(int[][] toBlocks, int[][] fromBlocks) {
        for (int row = 0; row < n; row++)
            System.arraycopy(fromBlocks[row], 0, toBlocks[row], 0, n);
    }

    public int dimension() {
        // board dimension n
        return n;
    }

    private int getRow(int value) {
        return (value - 1) / n;
    }

    private int getCol(int value) {
        return (value - 1) % n;
    }

    private int getValue(int row, int col) {
        return row * n + col + 1;
    }

    public int hamming() {
        // number of blocks out of place
        int hamming = 0;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (blocks[row][col] != BLANK && blocks[row][col] != getValue(row, col))
                    hamming++;
        return hamming;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        int manhattan = 0;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (blocks[row][col] != BLANK && blocks[row][col] != getValue(row, col))
                    manhattan += Math.abs(getRow(blocks[row][col]) - row) + Math.abs(getCol(blocks[row][col]) - col);
        return manhattan;
    }

    public boolean isGoal() {
        // is this board the goal board?
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (blocks[row][col] != BLANK && blocks[row][col] != getValue(row, col))
                    return false;
        return true;
    }

    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        Board twinBoard = new Board(blocks);
        int firRow = 0;
        int firCol = 0;
        if (blocks[firRow][firCol] == BLANK)
            firCol++;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (blocks[row][col] != blocks[firRow][firCol] && blocks[row][col] != BLANK) {
                    twinBoard.swap(firRow, firCol, row, col);
                    return twinBoard;
                }
            }
        }
        return twinBoard;
    }

    private void swap(int vRow, int vCol, int wRow, int wCol) {
        int t = blocks[vRow][vCol];
        blocks[vRow][vCol] = blocks[wRow][wCol];
        blocks[wRow][wCol] = t;
    }

    public boolean equals(Object y) {
        // does this board equal y?
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (y.getClass().isInstance(this)) {
            Board yb = (Board) y;
            if (yb.n != this.n)
                return false;
            else {
                for (int row = 0; row < n; row++)
                    for (int col = 0; col < n; col++)
                        if (yb.blocks[row][col] != blocks[row][col])
                            return false;
                return true;
            }
        } else
            return false;
    }

    public Iterable<Board> neighbors() {
        // all neighboring boards
        ArrayList<Board> neighbors = new ArrayList<Board>();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (blocks[row][col] == BLANK) {
                    if (row > 0) {
                        Board neighborT = new Board(blocks);
                        neighborT.swap(row, col, row - 1, col);
                        neighbors.add(neighborT);
                    }
                    if (row < n - 1) {
                        Board neighborB = new Board(blocks);
                        neighborB.swap(row, col, row + 1, col);
                        neighbors.add(neighborB);
                    }
                    if (col > 0) {
                        Board neighborL = new Board(blocks);
                        neighborL.swap(row, col, row, col - 1);
                        neighbors.add(neighborL);
                    }
                    if (col < n - 1) {
                        Board neighborR = new Board(blocks);
                        neighborR.swap(row, col, row, col + 1);
                        neighbors.add(neighborR);
                    }
                }
            }
        }
        return neighbors;
    }

    public int[][] getBlocks() {
        return this.blocks;
    }

    @SuppressLint("DefaultLocale")
    @NotNull
    public String toString() {
        // string representation of this board (in the output format specified
        // below)
        StringBuilder sb = new StringBuilder();
        sb.append(n).append("\n");
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                sb.append(String.format("%2d ", blocks[row][col]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
