/**
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                    O\ = /O
 *                ____/`---'\____
 *              .   ' \\| |// `.
 *               / \\||| : |||// \
 *             / _||||| -:- |||||- \
 *               | | \\\ - /// | |
 *             | \_| ''\---/'' | |
 *              \ .-\__ `-` ___/-. /
 *           ___`. .' /--.--\ `. . __
 *        ."" '< `.___\_<|>_/___.' >'"".
 *       | | : `- \`.;`\ _ /`;.`/ - ` : | |
 *         \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 *                    `=---='
 *
 * .............................................
 *          佛祖保佑             永无BUG
 */

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The public class that runs the Gomoku game. User can use this program to play
 * Gomoku in the terminal.
 */
public class Gomoku {
    /**
     * The scanner object is an instance of the java standard library Scanner. The
     * object will be used in dependent methods for accepting input.
     */
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Board board = new Board();

        /**
         * Two player objects with ID 1 and 2.
         */
        Players player1 = new Players(1);
        Players player2 = new Players(2);

        // Set player1 to move first.
        player1.setTurn(true);

        // Prints the blank game board in the terminal.
        board.printBoard();

        try {

            // Starts the game in the while loop.
            while (true) {

                // Asks player one to input.
                player1.input(board);
                board.printBoard();

                // Determines if the game is finished. If finished, it will break out from the
                // loop.
                if (board.isFinished()) {
                    break;
                }

                // Asks player two to input.
                player2.input(board);
                board.printBoard();

                // Determines if the game is finished. If finished, it will break out from the
                // loop.
                if (board.isFinished()) {
                    break;
                }
            }

            /**
             * Sends announcement for the winner.
             */
            if (board.isFinished()) {
                if (player1.onTurn) {
                    player1.announcements(board);
                } else if (player2.onTurn) {
                    player2.announcements(board);
                } else {
                    System.out.println("All positions are filled. No one wins. ");
                }
            }
        } finally {
            scanner.close();
        }
    }

    /**
     * 
     * @param one   The first character to be compared.
     * @param two   The second character to be compared.
     * @param three The third character to be compared.
     * @param four  The fourth character to be compared.
     * @param five  The fifth character to be compared.
     * @return true If the five parameters are the same, false otherwise. Compares
     *         if three arguments have the same value.
     */
    public static boolean isFiveSame(char one, char two, char three, char four, char five) {
        if (one == two) {
            if (two == three) {
                if (three == four) {
                    if (four == five) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

/**
 * The Board class represents the game board.
 */
class Board {

    /**
     * An arraylist that consists 225 Cells objects. Represents the cells, where
     * players can place marks on.
     */
    private ArrayList<Cells> cellList = new ArrayList<Cells>();

    /**
     * A pointer that counts from index 0 to 255.
     */
    static int countID = 0;

    /**
     * Fields represent whether the game is finished.
     */
    private boolean isHoriComplete;
    private boolean isVertComplete;
    private boolean isDiagComplete;
    private boolean isAllFilled;
    private boolean isFinished;

    /**
     * Default constructor. Creats 225 Cells objects and adds them into the
     * cellList. Each Cells object have the value of ' '.
     */
    public Board() {
        for (int i = 0; i < 15 * 15; i++) {
            cellList.add(new Cells('+', (i / 15 + 1), (char) ('a' + i % 15)));
        }
    }

    /**
     * print the board in the terminal.
     */
    public void printBoard() {
        countID = 0;

        printColumnHeader();
        for (int i = 0; i < 15 - 1; i ++) {
            printRows1();
            printRows2();
        }
        printRows1();
    }

    /**
     * Deters if the game on the board is finished.
     * 
     * @return true if the game is finished, false otherwise.
     */
    public boolean isFinished() {
        this.isAllFilled = deterAllFilled();
        this.isHoriComplete = deterHorizontal();
        this.isVertComplete = deterVertical();
        this.isDiagComplete = deterDiagonal();
        this.isFinished = isAllFilled || isHoriComplete || isVertComplete || isDiagComplete;
        return isFinished;
    }

    boolean deterAllFilled() {
        for (int i = 0; i < 224; i++) {
            if (this.cellList.get(i).getSelectStatus()) {
                return false;
            }
        }
        return true;
    }

    boolean getHoriComplete() {
        return isHoriComplete;
    }

    boolean getVertComplete() {
        return isVertComplete;
    }

    boolean getDiagComplete() {
        return isDiagComplete;
    }

    ArrayList<Cells> getCellList() {
        return this.cellList;
    }

    /**
     * Get a Cells object from the cellList that have the matched row and column.
     * 
     * @param row    An integer that represents the row position of the cell.
     * @param column An integer that represents the column position of the cell.
     * @return the Cells object on the argument row and column.
     */
    Cells getCellByPosition(int row, char column) {
        for (Cells c : cellList) {
            if (c.getColumn() == column && c.getRow() == row) {
                return c;
            }
        }
        // System.out.println("Cell not found. ");
        return (new Cells('-', -1, '-'));
    }

    private void printColumnHeader() {
        for (int i = 0; i < 15; i++) {
            System.out.print((char) ('a' + i) + "    ");
        }
        System.out.println();
    }

    private void printRowHeader() {
        System.out.println(" " + cellList.get(countID - 1).getRow());
    }

    private void printRows1() {
        for (int i = 0; i < 15 - 1; i ++) {
            System.out.print(cellList.get(countID).getValue() + "----");
            if (countID != 15 * 15 - 1) {
                countID ++;
            }
        }
        System.out.print(cellList.get(countID).getValue());
        if (countID != 15 * 15 - 1) {
            countID ++;
        }
        printRowHeader();
    }

    private void printRows2() {
        for (int i = 0; i < 15 - 1; i++) {
            System.out.print("|    ");
        }
        System.out.println("|");
    }


    private boolean deterHorizontal() {
        for (Cells c : this.cellList) {
            int thisRow = c.getRow();
            char thisColumn = c.getColumn();
            if (c.getColumn() <= 'k') {
                if (c.getSelectStatus()) {
                    boolean res = Gomoku.isFiveSame(c.getValue(),
                            this.getCellByPosition(thisRow, (char) (thisColumn + 1)).getValue(),
                            this.getCellByPosition(thisRow, (char) (thisColumn + 2)).getValue(),
                            this.getCellByPosition(thisRow, (char) (thisColumn + 3)).getValue(),
                            this.getCellByPosition(thisRow, (char) (thisColumn + 4)).getValue());
                    if (res) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean deterVertical() {
        for (Cells c : this.cellList) {
            int thisRow = c.getRow();
            char thisColumn = c.getColumn();
            if (c.getRow() <= 11) {
                if (c.getSelectStatus()) {
                    boolean res = Gomoku.isFiveSame(c.getValue(),
                            this.getCellByPosition(thisRow + 1, thisColumn).getValue(),
                            this.getCellByPosition(thisRow + 2, thisColumn).getValue(),
                            this.getCellByPosition(thisRow + 3, thisColumn).getValue(),
                            this.getCellByPosition(thisRow + 4, thisColumn).getValue());
                    if (res) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean deterDiagonal() {
        for (Cells c : this.cellList) {
            int thisRow = c.getRow();
            char thisColumn = c.getColumn();
            if (c.getRow() <= 11 && c.getValue() <= 'k') {
                if (c.getSelectStatus()) {
                    boolean res = Gomoku.isFiveSame(c.getValue(),
                            this.getCellByPosition(thisRow + 1, (char) (thisColumn + 1)).getValue(),
                            this.getCellByPosition(thisRow + 2, (char) (thisColumn + 2)).getValue(),
                            this.getCellByPosition(thisRow + 3, (char) (thisColumn + 3)).getValue(),
                            this.getCellByPosition(thisRow + 4, (char) (thisColumn + 4)).getValue());
                    if (res) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

/**
 * Represents the cells on the board. The position of each cell is represented
 * by an int row and a char column.
 */
class Cells {

    /**
     * The face value of the cell. ' ' for unselecte, 'X' for player 1, and 'O' for
     * player 2.
     */
    private char value;

    /**
     * Represents the position of the cell.
     */
    private int row;
    private char column;

    /**
     * Represents if the cell has been selected by either players.
     */
    private boolean selected = false;

    /**
     * Default constructor for Cells.
     * 
     * @param value  The face value will be assigned to the object.
     * @param row    The row number of the object.
     * @param column The column number of the object.
     */
    public Cells(char value, int row, char column) {
        this.value = value;
        this.row = row;
        this.column = column;
    }

    boolean getSelectStatus() {
        return selected;
    }

    char getValue() {
        return value;
    }

    void setValue(char value) {
        this.value = value;
    }

    void setSelectStatus(boolean b) {
        this.selected = b;
    }

    int getRow() {
        return row;
    }

    char getColumn() {
        return column;
    }
}

class Players {

    /**
     * True if it is this player's turn to move.
     */
    boolean onTurn = false;

    /**
     * The identical number of the player.
     */
    private int playerNum;

    /**
     * Set the ID number of the player.
     * 
     * @param playerNum
     */
    public Players(int playerNum) {
        this.playerNum = playerNum;
    }

    /**
     * Ask the user to input the row and column of the cell they prefer to choose.
     * The row and column should be seperated by a blank.
     * 
     * @param board The board which the game is playing on.
     */
    public void input(Board board) {
        int chosenRow;
        char chosenColumn;
        String choseString;
        int[] possibleRows = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        char[] possibleColumns = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o'};
        while (true) {
            System.out.print("Player " + playerNum + " input: ");
            choseString = Gomoku.scanner.nextLine();
            
            // Seperates the input String to row and column.
            String[] str = choseString.split(" ");
            chosenColumn = str[0].charAt(0);
            chosenRow = Integer.valueOf(str[1]);

            if (isInclude(chosenColumn, possibleColumns) && isInclude(chosenRow, possibleRows)) {
                Cells cellChosenByPosition = board.getCellByPosition(chosenRow, chosenColumn);
                if (!cellChosenByPosition.getSelectStatus()) {
                    if (playerNum == 1) {
                        cellChosenByPosition.setValue('X');
                    } else if (playerNum == 2) {
                        cellChosenByPosition.setValue('O');
                    }
                    cellChosenByPosition.setSelectStatus(true);
                    break;
                } else {
                    System.out.println("The position has been used. Please choose another one. ");
                }
            } else {
                System.out.println("Wrong format. Please inpute as: ROW COLUMN");
            }
            
        }
    }

    void setTurn(boolean b) {
        this.onTurn = b;
    }

    private boolean isInclude(char c, char[] charArr) {
        for (int i = 0; i < charArr.length; i ++) {
            if (c == charArr[i]) {
                return true;
            }
        }
        return false;
    }

    private boolean isInclude(int in, int[] intArr) {
        for (int i = 0; i < intArr.length; i ++) {
            if (in == intArr[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Announces the winner of the game. The winner can be player 1, player 2, or no
     * winner but all the cells on the board are filled.
     * 
     * @param board The board that the game is playing on.
     */
    public void announcements(Board board) {
        String winningMethod;
        if (board.getHoriComplete()) {
            winningMethod = "horizontally";
        } else if (board.getVertComplete()) {
            winningMethod = "vertically";
        } else if (board.getDiagComplete()) {
            winningMethod = "on the diagonal";
        } else {
            winningMethod = "";
        }
        System.out.print("Congratulations. \nPlayer" + playerNum
                + " wins the game. \nThe player wins by placing five consecutiver marks " + winningMethod + ". \n");
    }
}
