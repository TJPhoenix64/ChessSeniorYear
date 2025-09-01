import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Rook extends Piece {

    BufferedImage blackRook;
    BufferedImage whiteRook;

    public Rook(int x, int y, int id) {
        super(x, y, id, 'r');

        try {
            blackRook = ImageIO.read(new File("Photos/black-rook.png"));
            whiteRook = ImageIO.read(new File("Photos/white-rook.png"));
        } catch (IOException e) {
        }
    }

    public void draw(Graphics g) {

        switch (id) {
            case 17:
            case 18:
                if (blackRook != null) {
                    g.drawImage(blackRook, super.x, super.y, width, height, null);
                }
                break;
            case 19:
            case 20:
                if (whiteRook != null) {
                    g.drawImage(whiteRook, super.x, super.y, width, height, null);
                }
                break;
            default:
                g.setColor(Color.BLACK);
                g.fillRect(super.x, super.y, width, height);
                break;
        }
    }

    @Override
    public ArrayList<MoveOption> getMoves() {
        ArrayList<MoveOption> answer = new ArrayList<>();

        Piece newPiece = getPiece(col, row);

        int pieceId = newPiece.getId();
        int pieceCol = newPiece.col;
        int pieceRow = newPiece.row;
        System.out.println("Col: " + pieceCol + " Row: " + pieceRow);

        // System.out.println("Getting rook moves");

        boolean hasHitPiece = false;
        int checkX = pieceCol;
        int checkY = pieceRow - 1;
        // System.out.println("Up");
        while (!hasHitPiece) {
            if (!Piece.spaceIsOccupied(checkX, checkY, pieceId) && spaceIsInBounds(checkX, checkY)) {
                answer.add(new MoveOption(checkX, checkY));
                checkY--;
            } else {
                hasHitPiece = true;

            }
        }
        hasHitPiece = false;
        checkY = newPiece.row + 1;
        // System.out.println("Down");
        while (!hasHitPiece) {
            if (!Piece.spaceIsOccupied(checkX, checkY, pieceId) && spaceIsInBounds(checkX, checkY)) {
                answer.add(new MoveOption(checkX, checkY));
                checkY++;
            } else {
                hasHitPiece = true;
            }
        }
        hasHitPiece = false;
        checkY = newPiece.row;
        checkX--;
        // System.out.println("Left");
        while (!hasHitPiece) {
            if (!Piece.spaceIsOccupied(checkX, checkY, pieceId) && spaceIsInBounds(checkX, checkY)) {
                answer.add(new MoveOption(checkX, checkY));
                checkX--;
            } else {
                hasHitPiece = true;
            }
        }
        hasHitPiece = false;
        checkX = newPiece.col + 1;
        // System.out.println("Right");
        while (!hasHitPiece) {
            if (!Piece.spaceIsOccupied(checkX, checkY, pieceId) && spaceIsInBounds(checkX, checkY)) {
                answer.add(new MoveOption(checkX, checkY));
                checkX++;
            } else {
                hasHitPiece = true;
            }
        }

        // 5,5
        // 4,5;3,5;2,5;1,5;0,5
        // 6,5;7,5;8,5
        // 5,6;5,7;5,8;5,8
        // 5,4;5,3;5,2;5,1;5,0

        return answer;
    }
}
