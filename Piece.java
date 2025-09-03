
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public abstract class Piece extends Rectangle {
    int id;
    boolean pieceIsSelected;
    boolean pieceIsPressed;
    boolean pieceIsDragged;
    int startMouseX;
    int startMouseY;
    int startPieceX;
    int startPieceY;
    int row;
    int col;
    char type;
    boolean isWhite;
    boolean hadFirstTurn = false;

    public Piece(int col, int row, int id, char type) {
        super(col * GamePanel.PIECE_SIZE, row * GamePanel.PIECE_SIZE, GamePanel.PIECE_SIZE, GamePanel.PIECE_SIZE);
        this.x = col * GamePanel.PIECE_SIZE;
        this.y = row * GamePanel.PIECE_SIZE;
        this.id = id;
        this.col = col;
        this.row = row;
        this.type = type;
    }

    public Piece(int displayX, int displayY, int id, char type, boolean isMoving) {
        super(displayX, displayY, GamePanel.PIECE_SIZE, GamePanel.PIECE_SIZE);
        this.x = displayX;
        this.y = displayY;
        this.id = id;
        this.col = displayX / 100;
        this.row = displayY / 100;
        this.type = type;

    }

    public int getId() {
        return id;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean hadFirstTurn() {
        return hadFirstTurn;
    }

    // public void keyPressed(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        System.out.println("\nMousePressed");
        // System.out.println("CurrentX: " + x + " CurrentY: " + y);

        if (GamePanel.canClick) {

            GamePanel.movesList.clear();
            GamePanel.mouseReleased = false;

            startMouseX = e.getX();
            startMouseY = e.getY();

            // no piece was selected
            if (GamePanel.selectedPiece == null) {
                System.out.println("Previous selection was null");
                // System.out.println("CurrentX: " + x + " CurrentY: " + y);
                GamePanel.selectedPiece = this;
                pieceIsSelected = true;
                pieceIsPressed = true;
                startPieceX = x;
                startPieceY = y;
                System.out.println(GamePanel.selectedPiece);
                // System.out.println("HELLOW WRLD");
                GamePanel.selectedPiece.addMoves();
                // clicked piece again
            } else if (GamePanel.selectedPiece == this) {
                System.out.println("clicked piece again");
                // System.out.println("CurrentX: " + x + " CurrentY: " + y);
                GamePanel.movesList.clear();
                GamePanel.selectedPiece = this;
                pieceIsSelected = true;
                pieceIsPressed = true;
                startPieceX = x;
                startPieceY = y;
                System.out.println(GamePanel.selectedPiece);
                GamePanel.selectedPiece.addMoves();
                // clicked a different piece
            } else {
                System.out.println("clicked on new piece");

                MoveOption move = new MoveOption(this.col, this.row);

                if (GamePanel.movesList.contains(move)) {
                    GamePanel.pieceList.remove(this);
                    System.out.println("Piece being removed: " + this);
                    GamePanel.selectedPiece.teleportPiece(move);
                } else {
                    System.out.println("THIS CODE IS BEING RUN - new piece selected");
                    GamePanel.selectedPiece = this;
                    GamePanel.selectedPiece.startPieceX = x;
                    GamePanel.selectedPiece.startPieceY = y;

                    pieceIsSelected = true;
                    pieceIsPressed = true;
                    GamePanel.movesList.clear();
                    System.out.println(GamePanel.selectedPiece);
                    GamePanel.selectedPiece.addMoves();
                }
            }
        }
    }

    public static Piece getPiece(int col, int row) {
        Piece checkPiece = null;
        // System.out.println("getPiece X: " + x + " Y: " + y);
        for (int i = 0; i < GamePanel.pieceList.size(); i++) {
            checkPiece = GamePanel.pieceList.get(i);

            if (checkPiece.col == col && checkPiece.row == row) {
                break;
            } else {
                checkPiece = null;
            }
        }

        return checkPiece;
    }

    public boolean checkMove(int x, int y) {
        if (spaceIsInBounds(x, y)) {
            if (!Piece.spaceIsOccupied(x, y, id)) {
                return true;
            } else
                return Piece.getPiece(x, y).isWhite != this.isWhite;
        }
        return false;
    }

    public void addMoves() {
        ArrayList<MoveOption> list = getMoves();
        // System.out.println("ADDING MOVES");
        for (MoveOption elem : list) {
            GamePanel.movesList.add(elem);
            System.out.println(elem);
        }
    }

    protected void addMovesInDirection(ArrayList<MoveOption> answer, int startX, int startY, int dx, int dy, int id) {
        int checkX = startX + dx;
        int checkY = startY + dy;

        while (true) {
            if (!spaceIsInBounds(checkX, checkY)) {
                break;
            }
            if (!Piece.spaceIsOccupied(checkX, checkY, id)) {
                answer.add(new MoveOption(checkX, checkY));
                checkX += dx;
                checkY += dy;
            } else if (Piece.getPiece(checkX, checkY).isWhite != this.isWhite) {
                answer.add(new MoveOption(checkX, checkY));
                break;
            } else {
                break;
            }
        }

    }

    public void teleportPiece(MoveOption move) {
        System.out.println("TELEPORTING PIECE");

        GamePanel.selectedPiece.x = move.col * GamePanel.PIECE_SIZE;
        GamePanel.selectedPiece.y = move.row * GamePanel.PIECE_SIZE;
        GamePanel.selectedPiece.col = move.col;
        GamePanel.selectedPiece.row = move.row;
    }

    public void teleportPiece(Piece piece, MoveOption move) {
        piece.x = move.col * GamePanel.PIECE_SIZE;
        piece.y = move.row * GamePanel.PIECE_SIZE;
        piece.col = move.col;
        piece.row = move.row;
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("MouseReleased");
        // not sure why this code is here
        if (pieceIsSelected && pieceIsDragged) {
            // GamePanel.movesList.clear();
        }

        GamePanel.canClick = true;

        GamePanel.mouseReleased = true;

        int[] closestSpaceArr = findClosestSpace(x, y);
        if (pieceIsSelected) {
            System.out.println("PieceIsSelected");
            col = closestSpaceArr[0];
            row = closestSpaceArr[1];
            // System.out.println("New Location X: " + basicX + " Y: " + basicY);

            // System.out.println("Available moves: " + GamePanel.movesList);

            if (GamePanel.selectedPiece != null && !GamePanel.movesList.isEmpty()) {

                System.out.println("there is a selected piece, and it has valid moves");
                // System.out.println("CurrentX: " + x + " CurrentY: " + y);
                // System.out.println("startPieceX: " + startPieceX + " startPieceY: " +
                // startPieceY);

                int mx = e.getX();
                int my = e.getY();

                int targetX = mx / GamePanel.PIECE_SIZE;
                int targetY = my / GamePanel.PIECE_SIZE;

                // System.out.println("Target square X: " + targetX + " Y: " + targetY);

                MoveOption attempt = new MoveOption(targetX, targetY);
                // System.out.println("attempt: " + attempt);
                /*
                 * for (MoveOption elem : GamePanel.movesList) {
                 * System.out.println(elem + " 5");
                 * }
                 */

                // if the move attempt is valid, move the piece
                if (GamePanel.movesList.contains(attempt)) {
                    System.out.println("TTHIS CODE RUNS");
                    teleportPiece(attempt);
                    // Clear selection and moves
                    GamePanel.movesList.clear();
                    GamePanel.selectedPiece = null;
                    GamePanel.canClick = true;
                } else {
                    GamePanel.selectedPiece.x = startPieceX;
                    GamePanel.selectedPiece.y = startPieceY;
                    System.out.println("invalid move location");
                }
            } else if (GamePanel.movesList.isEmpty()) {
                System.out.println("No valid moves");
            } else if (GamePanel.selectedPiece == null) {
                System.out.println("No selected piece");
            } else {
                System.out.println("IDK HOW I GOT HERE");
            }

            /*
             * else if (GamePanel.movesList.contains(new MoveOption(basicX, basicY))) {
             * System.out.println("took piece");
             * x = basicX * GamePanel.PIECE_SIZE;
             * y = basicY * GamePanel.PIECE_SIZE;
             * }
             */
            System.out.println("Piece Selected In loop: " + pieceIsSelected);

            pieceIsSelected = false;

            if (pieceIsDragged) {
                if (GamePanel.movesList.contains(new MoveOption(x / 100, y))) {

                }
                pieceIsDragged = false;
            }
            if (pieceIsPressed) {
                pieceIsPressed = false;
            }

        }
        System.out.println("Piece Selected out of loop: " + pieceIsSelected);

    }

    public int[] findClosestSpace(int x, int y) {
        int[] answer = new int[2];
        int newX = (x + 50) / GamePanel.PIECE_SIZE;
        int newY = (y + 50) / GamePanel.PIECE_SIZE;

        // make sure that the new space is on the board
        if (newX > GamePanel.NUM_TILES - 1) {
            newX = GamePanel.NUM_TILES - 1;
        } else if (newX < 0) {
            newX = 0;
        }
        if (newY > GamePanel.NUM_TILES - 1) {
            newY = GamePanel.NUM_TILES - 1;
        } else if (newY < 0) {
            newY = 0;
        }
        answer[0] = newX;
        answer[1] = newY;
        return answer;
    }

    /**
     * 
     * @param x  x-location of check
     * @param y  y-location of check
     * @param id current piece id
     * @return returns true if space is occupied, else false
     */
    public static boolean spaceIsOccupied(int col, int row, int id) {
        boolean answer = false;
        for (Piece elem : GamePanel.pieceList) {
            if (elem.id != id) {
                // System.out.println("Occupied Check:" + elem.col + " " + col + " " + elem.row
                // + " " + row);
                if (elem.col == col && elem.row == row) {
                    answer = true;
                    System.out.println("Piece was stopped by: " + elem.id);
                    break;
                }
            }
        }
        return answer;
    }

    public static boolean spaceIsInBounds(int col, int row) {
        System.out.println("Checking: " + col + " " + row);
        return (col >= 0 && col < GamePanel.NUM_TILES && row >= 0 && row < GamePanel.NUM_TILES);
    }

    public abstract ArrayList<MoveOption> getMoves();

    public abstract void draw(Graphics g);

    public void mouseDragged(MouseEvent e) {
        int dx;
        int dy;
        System.out.println("MOUSEDRAGGED");
        pieceIsDragged = true;
        dx = startMouseX - e.getX();
        dy = startMouseY - e.getY();
        int newX = startPieceX - dx;
        int newY = startPieceY - dy;
        // if its in bounds, change the location
        if (newX + this.width < GamePanel.GAME_SIZE && newX > 0) {
            if (newY + this.height < GamePanel.GAME_SIZE && newY > 0) {
                this.x = newX;
                this.y = newY;
            }

        }
    }

    @Override
    public String toString() {
        String answer = "";
        if (type == 'p') {
            answer += "Pawn, ";
        } else if (type == 'r') {
            answer += "Rook, ";
        }
        answer += "X: " + x + " Y: " + y + " ID: " + id;
        return answer;

    }
}
