import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Queen extends Piece {

    BufferedImage blackQueen;
    BufferedImage whiteQueen;

    public Queen(int col, int row, int id) {
        super(col, row, id, 'q');

        try {
            blackQueen = ImageIO.read(new File("Photos/black-queen.png"));
            whiteQueen = ImageIO.read(new File("Photos/white-queen.png"));
        } catch (IOException e) {
        }
    }

    public Queen(int col, int row, int id, boolean isMoving) {
        super(col * 100, row * 100, id, 'q', true);

        try {
            blackQueen = ImageIO.read(new File("Photos/black-queen.png"));
            whiteQueen = ImageIO.read(new File("Photos/white-queen.png"));
        } catch (IOException e) {
        }
    }

    public Queen(int col, int row, int id, boolean isMoving, boolean isWhite) {
        super(col, row, id, 'q', true, isWhite);

        try {
            blackQueen = ImageIO.read(new File("Photos/black-queen.png"));
            whiteQueen = ImageIO.read(new File("Photos/white-queen.png"));
        } catch (IOException e) {
        }
    }

    @Override
    public ArrayList<MoveOption> getPsuedoMoves() {
        ArrayList<MoveOption> answer = new ArrayList<>();

        Piece newPiece = getPiece(col, row);

        int pieceId = newPiece.getId();
        int pieceCol = newPiece.col;
        int pieceRow = newPiece.row;

        // diagonals
        addMovesInDirection(answer, pieceCol, pieceRow, -1, -1, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, -1, 1, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 1, 1, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 1, -1, pieceId);

        // straights
        addMovesInDirection(answer, pieceCol, pieceRow, -1, 0, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 1, 0, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 0, 1, pieceId);
        addMovesInDirection(answer, pieceCol, pieceRow, 0, -1, pieceId);

        return answer;

    }

    @Override
    public ArrayList<MoveOption> getAttackingMoves() {
        return getPsuedoMoves();
    }

    @Override
    public void draw(Graphics g) {
        if (blackQueen != null && id == 25) {
            g.drawImage(blackQueen, super.x, super.y, width, height, null);
            isWhite = false;
        } else if (whiteQueen != null && id == 26) {
            g.drawImage(whiteQueen, super.x, super.y, width, height, null);
            isWhite = true;
        } else if (id > 50 && this.isWhite) {
            g.drawImage(whiteQueen, super.x, super.y, width, height, null);
            isWhite = true;
        } else if (id > 50 && !this.isWhite) {
            g.drawImage(blackQueen, super.x, super.y, width, height, null);
            isWhite = false;
        } else {
            g.drawImage(blackQueen, super.x, super.y, width, height, null);
            isWhite = false;
        }
    }
}
