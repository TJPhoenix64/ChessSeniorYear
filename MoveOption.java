import java.awt.*;
import java.util.*;

public class MoveOption {
    int col;
    int row;
    int widthHeight = 40;
    int centerX;
    int centerY;

    /**
     * 
     * @param col - the coloum/x position
     * @param row - the row/y position
     */
    public MoveOption(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Color tylerGray = new Color(0, 0, 0, 160);
        g2.setColor(tylerGray);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        centerX = col * 100 + 50 - widthHeight / 2;
        centerY = row * 100 + 50 - widthHeight / 2;
        g2.fillOval(centerX, centerY, widthHeight, widthHeight);
    }

    @Override
    public String toString() {
        String answer = "";
        answer += "MoveX: " + col;
        answer += " MoveY: " + row;
        return answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        // makes sure that object exists and is right class before typecasting to
        // Moveoption
        if (obj == null || getClass() != obj.getClass())
            return false;
        MoveOption other = (MoveOption) obj; // the new move option
        // System.out.println("Equals Method: ");
        // System.out.println(this.x + " " + other.x + " " + this.y + " " + other.y);
        return this.col == other.col && this.row == other.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

}
