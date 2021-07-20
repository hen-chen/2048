/*  Name: Henry Chen
  *  PennKey: henchen
  *  Recitation: 212
  *
  *  A class representing the main game of 2048.
  *  Execution: Proj2048.java
  */

public class Proj2048 {
    private static final int TILE_2048 = 2048;
    private static final int SIDE = 4;
    private Number[][] tiles;
    private int highestTile;
    private boolean checkingPossibleMoves;
    private boolean wonGame;
    private boolean loseGame;
    private static int numberOfMoves;
    
    // Constructor
    public Proj2048() {
        highestTile = 0;
        numberOfMoves = 0;
        tiles = new Number[SIDE][SIDE];
        for (int i = 0; i < 2; i++) {
            addNewTile();
        }
        updateGrid();
        while (true) {
            getKey();
            checkMovesPossible();
            if (loseGame) {
                PennDraw.setPenColor(PennDraw.BLACK);
                PennDraw.text(0.5, 0.51, "You Lost!");
                String numberMoves = "";
                numberMoves += numberOfMoves;
                PennDraw.text(0.5, 0.55, "Number of Moves: " + numberMoves);
                break;
            }
            if (wonGame) {
                updateGrid();
                PennDraw.setPenColor(PennDraw.BLACK);
                PennDraw.text(0.5, 0.51, "You Won!");
                String numberMoves = "";
                numberMoves += numberOfMoves;
                PennDraw.text(0.5, 0.55, "Number of Moves: " + numberMoves);
                break;
            }
        }
    }
    /**
     * Input: N/A
     * Output: Void
     * Description: updates the grid with new/merged Tiles
     */
    public void updateGrid() {
        PennDraw.clear();
        PennDraw.setPenColor(PennDraw.BLACK);
        PennDraw.line(0.25, 0, 0.25, 1);
        PennDraw.line(0.5, 0, 0.5, 1);
        PennDraw.line(0.75, 0, 0.75, 1);
        PennDraw.line(0, 0.25, 1, 0.25);
        PennDraw.line(0, 0.50, 1, 0.50);
        PennDraw.line(0, 0.75, 1, 0.75);
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                if (tiles[row][column] != null) {
                    drawTiles(row, column);
                }
            }
        }
    }
    /**
     * Input: N/A
     * Output: Void
     * Description: if the highest current tile is 2048, player wins.
     */
    public void didPlayerWin() {
        if (TILE_2048 == highestTile) {
            wonGame = true;
        }
    }
    /**
     * Input: int row, int column
     * Output: Void
     * Description: does the drawing of the tiles and their respective values.
     */
    public void drawTiles(int row, int column) {
        String number = "";
        int value = tiles[row][column].getValue();
        number += value;
        double tempRow;
        if (row == 0) {
            tempRow = 0.125;
        }
        else if (row == 1) {
            tempRow = 0.375;
        }
        else if (row == 2) {
            tempRow = 0.625;
        }
        else if (row == 3) {
            tempRow = 0.75 + 0.125;
        }
        else {
            throw new RuntimeException("Error with drawTiles, Row");
        }
        double tempColumn;
        if (column == 0) {
            tempColumn = 0.125;
        }
        else if (column == 1) {
            tempColumn = 0.375;
        }
        else if (column == 2) {
            tempColumn = 0.625;
        }
        else if (column == 3) {
            tempColumn = 0.75 + 0.125;
        }
        else {
            throw new RuntimeException("Error with drawTiles, Column");
        }
        PennDraw.text(tempColumn, 1 - tempRow, number);
    }
    /**
     * Input: N/A
     * Output: Void
     * Description: Check if there any moves that can be done. If there aren't,
     * the player loses the game.
     */
    public void checkMovesPossible() {
        checkingPossibleMoves = true;
        boolean moves = moveUp() || moveDown() || moveRight() || moveLeft();
        if (!moves) {
            loseGame = true;
        }
        checkingPossibleMoves = false;
    }
    /**
     * Input: N/A
     * Output: Void
     * Description: Once a tile has been merged with another tile, that 
     * singular, merged tile cannot be moved again during that move. For example,
     * 4 0 0 0         8 0 0 0
     * 4 0 0 0   ->    8 0 0 0
     * 4 0 0 0   ->    0 0 0 0
     * 4 0 0 0         0 0 0 0
     * After hitting up, the four 4's combine into two eights, but the two eights
     * aren't converted to one 16.
     * Helper function for moveUp, moveDown, moveLeft, moveRight.
     */
    private void updateMerge() {
        for (int row = 0; row < tiles.length; row++) {
            for (int c = 0; c < tiles[row].length; c++) {
                if (tiles[row][c] != null) {
                     tiles[row][c].merged(false);
                }
            }
        }         
    }
    /**
     * Input: N/A
     * Output: Void
     * Description: adds a new tile with value 2 or 4 at random at a blank tile
     * after one successful move.
     */
    public void addNewTile() {
        int newTile;
        if (Math.random() < 0.5) {
            newTile = 2;
        }
        else {
            newTile = 4;
        }
        int row = randomValue();
        int column = randomValue();
        while (tiles[row][column] != null) {
            row = randomValue();
            column = randomValue();
        }
        tiles[row][column] = new Number(newTile);
    }
    // helper function for addNewTile
    private int randomValue() {
        return (int) (Math.random() * 4);
    }
    /**
     * Input: N/A
     * Output: Void
     * Description: gets the player's input. The controls essentially.
     */
    public void getKey() {
        if (PennDraw.hasNextKeyTyped()) {
            char key = PennDraw.nextKeyTyped();
            if (key == 'd' || key == 'D') {
                moveRight();
            }
            else if (key == 'a' || key == 'A') {
                moveLeft();
            }
            else if (key == 's' || key == 'S') {
                moveDown();
            }
            else if (key == 'w' || key == 'W') {
                moveUp();
            }
        }
    }
 /**
     * Input: N/A
     * Output: boolean
     * Description: moves all the Tiles up. If testing for possible moves, returns
     * true if it is possible to move at least one tile up.
     */
    public boolean moveUp() {
        boolean upSuccessful = false;
        for (int row = 1; row < tiles.length; row++) {
            for (int c = 0; c < tiles[row].length; c++) { 
                if (tiles[row][c] != null) {
                    int newR = row - 1;
                    while (newR >= 0) {
                        Number newTile = tiles[newR][c];
                        Number currentTile = tiles[row][c];
                        if (newTile == null) {
                            if (checkingPossibleMoves) {
                                return true;
                            }
                            upSuccessful = true;
                            tiles[newR][c] = currentTile;
                            tiles[row][c] = null;
                            row = newR;
                            newR = newR - 1;
                        }
                        else if (newTile.mergeWith(currentTile)) {
                            if (checkingPossibleMoves) {
                                return true;
                            }
                            upSuccessful = true;
                            int newValue = newTile.mergeTogether(currentTile);
                            if (newValue > highestTile) {
                                highestTile = newValue;
                            }
                            tiles[row][c] = null;
                        }
                        else {
                            break;
                        }
                    }
                }
            }
        }
        if (upSuccessful) {
            numberOfMoves++;
            didPlayerWin();
            updateMerge();
            addNewTile();
            updateGrid();
        }
        return upSuccessful;
    }
    /**
     * Input: N/A
     * Output: boolean
     * Description: moves all the Tiles left. If testing for possible moves, returns
     * true if it is possible to move at least one tile to the left.
     */
    public boolean moveLeft() {
        boolean leftSuccessful = false;
        for (int row = 0; row < tiles.length; row++) {
            for (int c = 1; c < tiles[row].length; c++) { 
                if (tiles[row][c] != null) {
                    int newC = c - 1;
                    while (newC >= 0) {
                        Number newTile = tiles[row][newC];
                        Number currentTile = tiles[row][c];
                        if (newTile == null) {
                            if (checkingPossibleMoves) {
                                return true;
                            }
                            leftSuccessful = true;
                            tiles[row][newC] = currentTile;
                            tiles[row][c] = null;
                            c = newC;
                            newC = newC - 1;
                        }
                        else if (newTile.mergeWith(currentTile)) {
                            if (checkingPossibleMoves) {
                                return true;
                            }
                            leftSuccessful = true;
                            int newValue = newTile.mergeTogether(currentTile);
                            if (newValue > highestTile) {
                                highestTile = newValue;
                            }
                            tiles[row][c] = null;
                        }
                        else {
                            break;
                        }
                    }
                }
            }
        }
        if (leftSuccessful) {
            numberOfMoves++;
            didPlayerWin();
            updateMerge();
            addNewTile();
            updateGrid();
        }
        return leftSuccessful;
    }
    /**
     * Input: N/A
     * Output: boolean
     * Description: moves all the Tiles right. If testing for possible moves, returns
     * true if it is possible to move at least one tile to the right.
     */
    public boolean moveRight() {
        boolean rightSuccessful = false;
        for (int row = 0; row < tiles.length; row++) {
            for (int c = 2; c >= 0; c--) { 
                if (tiles[row][c] != null) {
                    int newC = c + 1;
                    while (newC < 4) {
                        Number newTile = tiles[row][newC];
                        Number currentTile = tiles[row][c];
                        if (newTile == null) {
                            if (checkingPossibleMoves) {
                                return true;
                            }
                            rightSuccessful = true;
                            tiles[row][newC] = currentTile;
                            tiles[row][c] = null;
                            c = newC;
                            newC = newC + 1;
                        }
                        else if (newTile.mergeWith(currentTile)) {
                            if (checkingPossibleMoves) {
                                return true;
                            }
                            rightSuccessful = true;
                            int newValue = newTile.mergeTogether(currentTile);
                            if (newValue > highestTile) {
                                highestTile = newValue;
                            }
                            tiles[row][c] = null;
                        }
                        else {
                            break;
                        }
                    }
                }
            }
        }
        if (rightSuccessful) {
            numberOfMoves++;
            didPlayerWin();
            updateMerge();
            addNewTile();
            updateGrid();
        }
        return rightSuccessful;
    }
    /**
     * Input: N/A
     * Output: boolean
     * Description: moves all the Tiles down. If testing for possible moves, returns
     * true if it is possible to move at least one tile down.
     */
    public boolean moveDown() {
        boolean downSuccessful = false;
        for (int row = 2; row >= 0; row--) {
            for (int c = 0; c < tiles[row].length; c++) { 
                if (tiles[row][c] != null) {
                    int newR = row + 1;
                    while (newR < SIDE) {
                        Number newTile = tiles[newR][c];
                        Number currentTile = tiles[row][c];
                        if (newTile == null) {
                            if (checkingPossibleMoves) {
                                return true;
                            }
                            downSuccessful = true;
                            tiles[newR][c] = currentTile;
                            tiles[row][c] = null;
                            row = newR;
                            newR = newR + 1;
                        }
                        else if (newTile.mergeWith(currentTile)) {
                            if (checkingPossibleMoves) {
                                return true;
                            }
                            downSuccessful = true;
                            int newValue = newTile.mergeTogether(currentTile);
                            if (newValue > highestTile) {
                                highestTile = newValue;
                            }
                            tiles[row][c] = null;
                        }
                        else {
                            break;
                        }
                    }
                }
            }
        }
        if (downSuccessful) {
            numberOfMoves++;
            didPlayerWin();
            updateMerge();
            addNewTile();
            updateGrid();
            
        }
        return downSuccessful;
    }
    public static void main(String[] args) {
        Proj2048 game = new Proj2048();
    }
}