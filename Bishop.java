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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMoves'");
    }

    @Override
    public void draw(Graphics g) {
        switch (id) {
            case 21:
            case 22:
                if (blackBishop != null) {
                    g.drawImage(blackBishop, super.x, super.y, width, height, null);
                }
                break;
            case 23:
            case 24:
                if (whiteBishop != null) {
                    g.drawImage(whiteBishop, super.x, super.y, width, height, null);
                }
                break;
            default:
                g.setColor(Color.BLACK);
                g.fillRect(super.x, super.y, width, height);
                break;
        }
    }

}
