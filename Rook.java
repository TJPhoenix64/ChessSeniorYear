import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Rook extends Piece {

    BufferedImage blackRook;
    BufferedImage whiteRook;

    public Rook(int col, int row, int id) {
        super(col, row, id, 'r');

        try {
            blackRook = ImageIO.read(new File("Photos/black-rook.png"));
            whiteRook = ImageIO.read(new File("Photos/white-rook.png"));
        } catch (IOException e) {
        }
    }

    public Rook(int displayX, int displayY, int id, boolean isMoving) {
        super(displayX, displayY, id, 'r', isMoving);
        try {
            blackRook = ImageIO.read(new File("Photos/black-rook.png"));
            whiteRook = ImageIO.read(new File("Photos/white-rook.png"));
        } catch (IOException e) {
        }
    }

    @Override
    public void draw(Graphics g) {

        switch (id) {
            case 17:
            case 18:
                if (blackRook != null) {
                    g.drawImage(blackRook, super.x, super.y, width, height, null);
                    isWhite = false;
                }
                break;
            case 19:
            case 20:
                if (whiteRook != null) {
                    g.drawImage(whiteRook, super.x, super.y, width, height, null);
                    isWhite = true;
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

        addMovesInDirection(answer, pieceCol, pieceRow, -1, 0, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 1, 0, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 0, 1, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 0, -1, pieceId);

        return answer;
    }
}
