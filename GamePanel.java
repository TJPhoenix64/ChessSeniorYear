import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

    static final int NUM_TILES = 8;
    static final int PIECE_SIZE = 100;
    static final int GAME_SIZE = PIECE_SIZE * NUM_TILES;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_SIZE, GAME_SIZE);
    static final Dimension PIECE_DIMENSION = new Dimension(PIECE_SIZE, PIECE_SIZE);

    static boolean canClick = true;
    static boolean mouseReleased = false;
    static boolean teleportPiece = false;

    public static Piece selectedPiece = null;

    static ArrayList<Piece> pieceList = new ArrayList<>();
    static ArrayList<MoveOption> movesList = new ArrayList<>();

    Thread gameThread;
    Image image;
    Graphics graphics;

    BufferedImage background;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public GamePanel() {
        newPieces();
        this.setFocusable(true);
        AL listener = new AL();
        this.addKeyListener(listener);
        addMouseListener(listener);
        addMouseMotionListener(listener);
        this.setPreferredSize(SCREEN_SIZE);
        gameThread = new Thread(this);
        gameThread.start();

        try {
            background = ImageIO.read(new File("Photos/chessBoard.png"));
        } catch (IOException e) {
        }

    }

    public void newPieces() {
        newPawns();
        newRooks();
        newBishops();
        newQueens();
    }

    public void newPawns() {
        for (int i = 0; i < NUM_TILES; i++) {
            int rank = 1;
            pieceList.add(new Pawn(i, rank, i + 1));
        }

        for (int i = 0; i < NUM_TILES; i++) {
            int rank = 6;
            pieceList.add(new Pawn(i, rank, i + 9));
        }
    }

    public void newRooks() {
        pieceList.add(new Rook(0, 0, 17));
        pieceList.add(new Rook(7, 0, 18));
        pieceList.add(new Rook(0, 7, 19));
        pieceList.add(new Rook(7, 7, 20));
    }

    public void newBishops() {
        pieceList.add(new Bishop(2, 0, 21));
        pieceList.add(new Bishop(5, 0, 22));
        pieceList.add(new Bishop(2, 7, 23));
        pieceList.add(new Bishop(5, 7, 24));
    }

    public void newQueens() {
        pieceList.add(new Queen(4, 0, 25));
        pieceList.add(new Queen(4, 7, 26));
    }

    @Override
    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {

        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }

        for (Piece elem : pieceList) {
            elem.draw(g);
        }
        for (MoveOption elem : movesList) {
            elem.draw(g);
        }

    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0.0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                repaint();
                delta--;
            }
        }
    }

    public class AL implements KeyListener, MouseListener, MouseMotionListener {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // print all pieces when 9 pressed
            if (key == KeyEvent.VK_9) {
                for (Piece elem : GamePanel.pieceList) {
                    System.out.println(elem);
                }
            }
            if (selectedPiece != null) {
                // move pieces in direction of arrow

                if (key == KeyEvent.VK_UP
                        && movesList.contains(new MoveOption(selectedPiece.col, selectedPiece.row - 1))) {
                    selectedPiece.row--;
                }
                if (key == KeyEvent.VK_DOWN) {
                    selectedPiece.row++;
                }
                if (key == KeyEvent.VK_LEFT) {
                    selectedPiece.col--;
                }
                if (key == KeyEvent.VK_RIGHT) {
                    selectedPiece.col++;
                }

                // delete the current piece
                if (key == KeyEvent.VK_D) {
                    pieceList.remove(selectedPiece);
                    selectedPiece = null;
                }

                if (key == KeyEvent.VK_0) {
                    System.out.println("\nNumMoves: " + movesList.size());
                    System.out.println("Moves: " + movesList);
                }
            }

            if (key == KeyEvent.VK_C) {
                int index = 0;
                System.out.println("index: " + pieceList.indexOf(selectedPiece) + " current: " + selectedPiece);
                if (selectedPiece != null && pieceList.size() > 1) {
                    index = pieceList.indexOf(selectedPiece) + 1;
                }
                if (!pieceList.isEmpty()) {
                    if (index == pieceList.size()) {
                        index = 0;
                    }
                    selectedPiece = pieceList.get(index);
                    System.out.println(index + " " + pieceList.get(index));
                }

                // cycle through the pieces on the board and select them
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        // MouseListener methods
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (selectedPiece != null) {
                for (MoveOption moves : movesList) {
                    Point p = new Point(moves.centerX - 30, moves.centerY - 30);
                    Rectangle rect = new Rectangle(p, PIECE_DIMENSION);
                    if (rect.contains(e.getPoint())) {
                        teleportPiece = true;
                        selectedPiece.teleportPiece(moves);
                        selectedPiece.hadFirstTurn = true;

                        // captures the piece it goes to
                        for (int i = 0; i < pieceList.size(); i++) {
                            Piece elem = pieceList.get(i);
                            if (elem.contains(e.getPoint()) && elem != selectedPiece) {
                                pieceList.remove(elem);
                                break;
                            }
                        }
                        // deselects piece and clears moves
                        selectedPiece = null;
                        movesList.clear();
                        break;
                    }
                }
                if (teleportPiece) {
                    // System.out.println("TELEPORT");
                    teleportPiece = false;
                } else if (!selectedPiece.contains(e.getPoint())) {
                    movesList.clear();
                    selectedPiece = null;
                    System.out.println("Clicked on air1");
                } else {
                    selectedPiece.mousePressed(e);
                }
            } else {
                // if no preselected piece
                for (int i = 0; i < pieceList.size(); i++) {
                    Piece elem = pieceList.get(i);
                    if (elem.contains(e.getPoint())) {
                        elem.mousePressed(e);
                        // System.out.println("Mouse Pressed!!!");
                        break;
                    }
                    // if it didnt click a piece
                    if (i + 1 == pieceList.size()) {
                        movesList.clear();
                        selectedPiece = null;
                        System.out.println("Clicked on air2");
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            for (Piece elem : pieceList) {
                if (elem.contains(e.getPoint())) {
                    elem.mouseReleased(e);
                    break;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        // MouseMotionListener methods
        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            for (Piece elem : pieceList) {
                if (elem.pieceIsSelected) {
                    elem.mouseDragged(e);
                }
            }
        }
    }
}
