import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Knight extends Piece {

    BufferedImage blackKnight;
    BufferedImage whiteKnight;

    public Knight(int col, int row, int id) {
        super(col, row, id, 'k');

        try {
            blackKnight = ImageIO.read(new File("Photos/black-rook.png"));
            whiteKnight = ImageIO.read(new File("Photos/white-rook.png"));
        } catch (IOException e) {
        }
    }

    public Knight(int col, int row, int id, boolean isMoving) {
        super(col, row, id, 'k');
        try {
            blackKnight = ImageIO.read(new File("Photos/black-rook.png"));
            whiteKnight = ImageIO.read(new File("Photos/white-rook.png"));
        } catch (IOException e) {
        }
    }

    @Override
    public ArrayList<MoveOption> getMoves() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMoves'");
    }

    @Override
    public void draw(Graphics g) {
        switch (id) {
            case 27, 28 -> {
                if (blackKnight != null) {
                    g.drawImage(blackKnight, super.x, super.y, width, height, null);
                    isWhite = false;
                }
            }
            case 29, 30 -> {
                if (whiteKnight != null) {
                    g.drawImage(whiteKnight, super.x, super.y, width, height, null);
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
