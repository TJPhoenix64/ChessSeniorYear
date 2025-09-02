import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class King extends Piece {

    BufferedImage blackKing;
    BufferedImage whiteKing;

    public King(int displayX, int displayY, int id) {
        super(displayX, displayY, id, 'K');

        try {
            blackKing = ImageIO.read(new File("Photos/black-knight.png"));
            whiteKing = ImageIO.read(new File("Photos/white-knight.png"));
        } catch (IOException e) {
        }
    }

    public King(int displayX, int displayY, int id, boolean isMoving) {
        super(displayX, displayY, id, 'K', isMoving);

        try {
            blackKing = ImageIO.read(new File("Photos/black-knight.png"));
            whiteKing = ImageIO.read(new File("Photos/white-knight.png"));
        } catch (IOException e) {
        }
    }

    @Override
    public ArrayList<MoveOption> getMoves() {
        ArrayList<MoveOption> answer = new ArrayList<>();
        int checkX = this.col + 1;
        int checkY = this.row + 1;

        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }
        checkX--;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }
        checkX--;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }
        checkY--;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }
        checkY--;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }
        checkX++;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }
        checkX++;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }
        checkY++;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }

        return answer;

    }

    @Override
    public void draw(Graphics g) {
        switch (id) {
            case 31 -> {
                if (blackKing != null) {
                    g.drawImage(blackKing, super.x, super.y, width, height, null);
                    isWhite = false;
                }
            }
            case 32 -> {
                if (whiteKing != null) {
                    g.drawImage(whiteKing, super.x, super.y, width, height, null);
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
