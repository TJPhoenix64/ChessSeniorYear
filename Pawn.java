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

    public boolean canMoveForwardOne(int moveX, int moveY1) {
        if (moveX >= 0 && moveX <= GamePanel.NUM_TILES - 1) {
            if (moveY1 >= 0 && moveY1 <= GamePanel.NUM_TILES - 1) {
                return (!Piece.spaceIsOccupied(moveX, moveY1, this.id));
            }
        }
        return false;
    }

    public boolean canMoveForwardTwo(int moveX, int moveY2) {
        return (!Piece.spaceIsOccupied(moveX, moveY2, this.id));
    }

    public void addForwardMoves(int moveX, int moveY1, int moveY2, ArrayList<MoveOption> moves) {
        if (canMoveForwardOne(moveX, moveY1)) {
            moves.add(new MoveOption(moveX, moveY1));
            if (canMoveForwardTwo(moveX, moveY2)) {
                moves.add(new MoveOption(moveX, moveY2));
            }
        }
    }

    public boolean canMoveDiagLeft(int moveX, int moveY1) {
        if (moveX - 1 >= 0 && moveX - 1 <= GamePanel.NUM_TILES - 1) {
            if (Piece.spaceIsOccupied(moveX - 1, moveY1, this.id)) {
                return (Piece.getPiece(moveX - 1, moveY1).isWhite != this.isWhite);
            }
        }
        return false;
    }

    public boolean canMoveDiagRight(int moveX, int moveY) {
        if (moveX + 1 >= 0 && moveX + 1 <= GamePanel.NUM_TILES - 1) {
            if (Piece.spaceIsOccupied(moveX + 1, moveY, this.id)) {
                return (Piece.getPiece(moveX + 1, moveY).isWhite != this.isWhite);
            }
        }
        return false;
    }

    public void addDiagMoves(int moveX, int moveY, ArrayList<MoveOption> moves) {
        if (moveY >= 0 && moveY <= GamePanel.NUM_TILES - 1) {
            if (canMoveDiagLeft(moveX, moveY)) {
                moves.add(new MoveOption(moveX - 1, moveY));
            }
            if (canMoveDiagRight(moveX, moveY)) {
                moves.add(new MoveOption(moveX + 1, moveY));
            }
        }
    }

    public void addEnpassantMoves(int moveX, int moveY, ArrayList<MoveOption> moves) {
        if (row == 3 && this.isWhite) {
            if (Piece.getPiece(col - 1, row) != null) {
                Piece piece = Piece.getPiece(col - 1, row);
                if (piece.type == 'p' && !piece.isWhite && piece.numPreviousMoves == 1
                        && GamePanel.lastPieceThatMoved == piece) {
                    moves.add(new MoveOption(moveX - 1, moveY));
                }
            }

            if (Piece.getPiece(col + 1, row) != null) {
                Piece piece = Piece.getPiece(col + 1, row);
                if (piece.type == 'p' && !piece.isWhite && piece.numPreviousMoves == 1
                        && GamePanel.lastPieceThatMoved == piece) {
                    moves.add(new MoveOption(moveX + 1, moveY));
                }
            }
        }

        if (row == 4 && !this.isWhite) {
            if (Piece.getPiece(col - 1, row) != null) {
                Piece piece = Piece.getPiece(col - 1, row);
                if (piece.type == 'p' && piece.isWhite && piece.numPreviousMoves == 1
                        && GamePanel.lastPieceThatMoved == piece) {
                    moves.add(new MoveOption(moveX - 1, piece.row + 1));
                }
            }

            if (Piece.getPiece(col - 1, row) != null) {
                Piece piece = Piece.getPiece(col - 1, row);
                if (piece.type == 'p' && piece.isWhite && piece.numPreviousMoves == 1
                        && GamePanel.lastPieceThatMoved == piece) {
                    moves.add(new MoveOption(moveX - 1, piece.row + 1));
                }
            }
        }
    }

    @Override
    public ArrayList<MoveOption> getPsuedoMoves() {
        // System.out.println("This method is being run");
        ArrayList<MoveOption> answer = new ArrayList<>();
        int multiplier = -1;

        if (isWhite) {
            multiplier = 1;
        }

        int moveX = col;
        int moveY1 = row - (1 * multiplier);
        int moveY2 = row - (2 * multiplier);

        // moving forward
        addForwardMoves(moveX, moveY1, moveY2, answer);

        // capturing diagonally
        addDiagMoves(moveX, moveY2, answer);

        // en passant
        addEnpassantMoves(moveX, moveY1, answer);

        return answer;
    }

    @Override
    public ArrayList<MoveOption> getAttackingMoves() {
        return getPsuedoMoves();
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
