import java.awt.*;
import java.util.*;

public class MoveOption {
    int x;
    int y;
    int widthHeight = 40;
    int centerX;
    int centerY;

    /**
     * 
     * @param x basic version of x, eg 5 or 6, not 500 or 600
     * @param y basic y
     */
    public MoveOption(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Color tylerGray = new Color(0, 0, 0, 160);
        g2.setColor(tylerGray);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        centerX = x * 100 + 50 - widthHeight / 2;
        centerY = y * 100 + 50 - widthHeight / 2;
        g2.fillOval(centerX, centerY, widthHeight, widthHeight);
    }

    @Override
    public String toString() {
        String answer = "";
        answer += "MoveX: " + x;
        answer += " MoveY: " + y;
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
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
