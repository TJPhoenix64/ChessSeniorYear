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
            blackKnight = ImageIO.read(new File("Photos/black-knight.png"));
            whiteKnight = ImageIO.read(new File("Photos/white-knight.png"));
        } catch (IOException e) {
        }
    }

    public Knight(int col, int row, int id, boolean isMoving) {
        super(col, row, id, 'k');
        try {
            blackKnight = ImageIO.read(new File("Photos/black-knight.png"));
            whiteKnight = ImageIO.read(new File("Photos/white-knight.png"));
        } catch (IOException e) {
        }
    }

    @Override
    public ArrayList<MoveOption> getPsuedoMoves() {
        ArrayList<MoveOption> answer = new ArrayList<>();
        int checkX = this.col + 1;
        int checkY = this.row - 2;

        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }
        checkX -= 2;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }

        checkX -= 1;
        checkY += 1;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }

        checkY += 2;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }

        checkX += 1;
        checkY += 1;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }

        checkX += 2;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }

        checkX += 1;
        checkY -= 1;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }

        checkY -= 2;
        if (checkMove(checkX, checkY)) {
            answer.add(new MoveOption(checkX, checkY));
        }
        return answer;
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
