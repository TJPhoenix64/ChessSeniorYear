import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Pawn extends Piece {

    BufferedImage blackPawn;
    BufferedImage whitePawn;

    public Pawn(int col, int row, int id) {
        super(col, row, id, 'p');

        try {
            blackPawn = ImageIO.read(new File("Photos/black-pawn.png"));
            whitePawn = ImageIO.read(new File("Photos/white-pawn.png"));
        } catch (IOException e) {
        }
    }

    public Pawn(int displayX, int displayY, int id, boolean isMoving) {
        super(displayX, displayY, id, 'p', isMoving);
        try {
            blackPawn = ImageIO.read(new File("Photos/black-pawn.png"));
            whitePawn = ImageIO.read(new File("Photos/white-pawn.png"));
        } catch (IOException e) {
        }
    }

    public char getType() {
        return type;
    }

    @Override
    public void draw(Graphics g) {

        if (id <= 8) {
            if (blackPawn != null) {
                g.drawImage(blackPawn, super.x, super.y, width, height, null);
                isWhite = false;
            }
        } else if (id <= 16) {
            if (whitePawn != null) {
                g.drawImage(whitePawn, super.x, super.y, width, height, null);
                isWhite = true;
            }

        } else {
            g.setColor(Color.BLACK);
            g.fillRect(super.x, super.y, width, height);
        }
    }

    @Override
    public ArrayList<MoveOption> getPsuedoMoves() {
        // System.out.println("This method is being run");
        ArrayList<MoveOption> answer = new ArrayList<>();
        int multiplier = -1;

        Piece newPiece = getPiece(col, row);

        int pieceId = newPiece.getId();
        // System.out.println("PieceId: " + pieceId);
        if (pieceId > 8 && pieceId <= 16) {
            multiplier = 1;
            // System.out.println("White Piece");
        } else {
            // System.out.println("Black Piece");
        }

        int moveX = col;
        int moveY1 = row - (1 * multiplier);
        int moveY2 = row - (2 * multiplier);

        // moving forward

        if (moveX >= 0 && moveX <= GamePanel.NUM_TILES - 1) {
            if (moveY1 >= 0 && moveY1 <= GamePanel.NUM_TILES - 1) {
                if (!Piece.spaceIsOccupied(moveX, moveY1, pieceId)) {
                    answer.add(new MoveOption(moveX, moveY1));
                }
            }
            if ((moveY2 >= 0 && moveY2 <= GamePanel.NUM_TILES - 1)
                    && (!Piece.spaceIsOccupied(moveX, moveY1, pieceId)) && !hadFirstTurn) {
                if (!Piece.spaceIsOccupied(moveX, moveY2, pieceId)) {
                    answer.add(new MoveOption(moveX, moveY2));
                }
            }
        }
        /*
         * // capturing diagonally
         * if (checkMove(moveX, moveY1)) {
         * answer.add(new MoveOption(moveX - 1, moveY1));
         * }
         * 
         * if (checkMove(moveX, moveY1)) {
         * answer.add(new MoveOption(moveX + 1, moveY1));
         * }
         */

        // capturing diagonally
        if (moveY1 >= 0 && moveY1 <= GamePanel.NUM_TILES - 1) {
            if (moveX - 1 >= 0 && moveX - 1 <= GamePanel.NUM_TILES - 1) {
                if (Piece.spaceIsOccupied(moveX - 1, moveY1, newPiece.getId())) {
                    if (Piece.getPiece(moveX - 1, moveY1).isWhite != this.isWhite) {
                        answer.add(new MoveOption(moveX - 1, moveY1));
                    }
                }
            }

            if (moveX + 1 >= 0 && moveX + 1 <= GamePanel.NUM_TILES - 1) {
                if (Piece.spaceIsOccupied(moveX + 1, moveY1, newPiece.getId())) {
                    if (Piece.getPiece(moveX + 1, moveY1).isWhite != this.isWhite) {
                        answer.add(new MoveOption(moveX + 1, moveY1));
                    }
                }
            }

        }

        // en passant
        if (row == 3 && this.isWhite) {
            if (Piece.getPiece(col - 1, row) != null) {
                Piece piece = Piece.getPiece(col - 1, row);
                if (piece.type == 'p' && !piece.isWhite && piece.numPreviousMoves == 1
                        && GamePanel.lastPieceThatMoved == piece) {
                    answer.add(new MoveOption(moveX - 1, moveY1));
                }
            }

            if (Piece.getPiece(col + 1, row) != null) {
                Piece piece = Piece.getPiece(col + 1, row);
                if (piece.type == 'p' && !piece.isWhite && piece.numPreviousMoves == 1
                        && GamePanel.lastPieceThatMoved == piece) {
                    answer.add(new MoveOption(moveX + 1, moveY1));
                }
            }
        }

        if (row == 4 && !this.isWhite) {
            if (Piece.getPiece(col - 1, row) != null) {
                Piece piece = Piece.getPiece(col - 1, row);
                if (piece.type == 'p' && piece.isWhite && piece.numPreviousMoves == 1
                        && GamePanel.lastPieceThatMoved == piece) {
                    answer.add(new MoveOption(moveX - 1, piece.row + 1));
                }
            }

            if (Piece.getPiece(col - 1, row) != null) {
                Piece piece = Piece.getPiece(col - 1, row);
                if (piece.type == 'p' && piece.isWhite && piece.numPreviousMoves == 1
                        && GamePanel.lastPieceThatMoved == piece) {
                    answer.add(new MoveOption(moveX - 1, piece.row + 1));
                }
            }
        }

        // System.out.println(answer);
        return answer;
    }

    public void promote() {
        Piece piece = Piece.getPiece(this.col, this.row);

        System.out.println("Promoting piece: " + piece);
        if (piece.type == 'p') {
            Queen queen = new Queen(piece.col, piece.row, piece.id + 50, true, piece.isWhite);
            System.out.println(queen);
            GamePanel.pieceList.remove(piece);
            GamePanel.pieceList.add(queen);
            GamePanel.selectedPiece = queen;
        }
    }

}
