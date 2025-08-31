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

        Piece newPiece = getPiece(x, y);

        int pieceId = newPiece.getId();
        int newX = x;
        int newY = y;
        boolean hasHitPiece = false;
        System.out.println("Getting rook moves");
        for (int i = GamePanel.NUM_TILES; i >= 0; i--) {
            if (!hasHitPiece) {
                System.out.println("current rook has not hit a piece");
                if (!Piece.spaceIsOccupied(newX, newY - (i * 100), pieceId)) {
                    System.out.println(newX + " " + (newY - (i * 100)));
                    System.out.println("MoveOptions: " + (newX / 100) + " " + (newY / 100 - i));
                    answer.add(new MoveOption(newX / 100, newY / 100 - i));
                } else {
                    hasHitPiece = true;
                }
            }
        }

        return answer;
    }
}
