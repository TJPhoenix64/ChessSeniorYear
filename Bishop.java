import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Bishop extends Piece {

    BufferedImage blackBishop;
    BufferedImage whiteBishop;

    public Bishop(int col, int row, int id) {
        super(col, row, id, 'b');

        try {
            blackBishop = ImageIO.read(new File("Photos/black-rook.png"));
            whiteBishop = ImageIO.read(new File("Photos/white-rook.png"));
        } catch (IOException e) {
        }
    }

    public Bishop(int col, int row, int id, boolean isMoving) {
        super(col, row, id, 'b', true);

        try {
            blackBishop = ImageIO.read(new File("Photos/black-rook.png"));
            whiteBishop = ImageIO.read(new File("Photos/white-rook.png"));
        } catch (IOException e) {
        }
    }

    @Override
    public ArrayList<MoveOption> getMoves() {
        ArrayList<MoveOption> answer = new ArrayList<>();

        Piece newPiece = getPiece(col, row);

        int pieceId = newPiece.getId();
        int pieceCol = newPiece.col;
        int pieceRow = newPiece.row;

        addMovesInDirection(answer, pieceCol, pieceRow, -1, -1, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, -1, 1, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 1, 1, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 1, -1, pieceId);

        return answer;

    }

    @Override
    public void draw(Graphics g) {
        switch (id) {
            case 21, 22 -> {
                if (blackBishop != null) {
                    g.drawImage(blackBishop, super.x, super.y, width, height, null);
                    isWhite = false;
                }
            }
            case 23, 24 -> {
                if (whiteBishop != null) {
                    g.drawImage(whiteBishop, super.x, super.y, width, height, null);
                    isWhite = true;
                }
            }
            default -> {
                g.setColor(Color.BLACK);
                g.fillRect(super.x, super.y, width, height);
            }
        }
    }

}
