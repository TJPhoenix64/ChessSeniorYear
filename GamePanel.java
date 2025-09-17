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
    static boolean showPsuedoMoves = false;

    public static King kingW = null;
    public static King kingB = null;

    public static Piece selectedPiece = null;
    public static Piece lastPieceThatMoved = null;

    public static int numTurns = 1;

    static ArrayList<Piece> pieceList = new ArrayList<>();
    static ArrayList<MoveOption> selectedPieceMovesList = new ArrayList<>();
    static ArrayList<MoveOption> psuedoLegalMovesList = new ArrayList<>();
    static ArrayList<MoveOption> legalMovesList = new ArrayList<>();

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
            background = ImageIO.read(new File("Photos/chess-board.png"));
        } catch (IOException e) {
        }

    }

    public static void updatePsuedoMovesList() {
        psuedoLegalMovesList.clear();
        System.out.println("UPDATING MOVES LIST");
        boolean isWhiteTurn = numTurns % 2 == 1;
        for (Piece elem : pieceList) {
            psuedoLegalMovesList.addAll(elem.getMoves());

            if (elem.type == 'K') {
                if (!elem.isWhite) {
                    kingB = (King) elem;
                } else {
                    kingW = (King) elem;
                }
            }

            if (kingW != null) {
                if (elem.getMoves().contains(new MoveOption(kingW.col, kingW.row))) {
                    System.out.println("Checking white king: " + elem);
                }
            }

            if (kingB != null) {
                if (elem.getMoves().contains(new MoveOption(kingB.col, kingB.row))) {
                    System.out.println("Checking black king: " + elem);
                }
            }
        }
        if (psuedoLegalMovesList.contains(new MoveOption(kingW.col, kingW.row))) {
            System.out.println("White King in check");
            kingW.isInCheck = true;
        } else {
            kingW.isInCheck = false;
        }

        if (psuedoLegalMovesList.contains(new MoveOption(kingB.col, kingB.row))) {
            System.out.println("Black King in check");
            kingB.isInCheck = true;
        } else {
            kingB.isInCheck = false;
        }
        if (psuedoLegalMovesList.isEmpty()) {
            if (isWhiteTurn && !kingW.isInCheck) {
                System.out.println("STALEMATE1");
            }
            if (!isWhiteTurn && !kingB.isInCheck) {
                System.out.println("STALEMATE2");
            }

        }
    }

    public void newPieces() {
        newPawns();
        newRooks();
        newBishops();
        newQueens();
        newKnights();
        newKings();
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
        pieceList.add(new Queen(3, 0, 25));
        pieceList.add(new Queen(3, 7, 26));
    }

    public void newKnights() {
        pieceList.add(new Knight(6, 0, 27));
        pieceList.add(new Knight(1, 0, 28));
        pieceList.add(new Knight(6, 7, 29));
        pieceList.add(new Knight(1, 7, 30));
    }

    public void newKings() {
        pieceList.add(new King(4, 0, 31));
        pieceList.add(new King(4, 7, 32));
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
        for (MoveOption elem : selectedPieceMovesList) {
            elem.draw(g);
        }
        if (showPsuedoMoves) {
            for (MoveOption elem : psuedoLegalMovesList) {
                elem.draw(g);
            }
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
                        && selectedPieceMovesList.contains(new MoveOption(selectedPiece.col, selectedPiece.row - 1))) {
                    selectedPiece.row--;
                }
                if (key == KeyEvent.VK_DOWN
                        && selectedPieceMovesList.contains(new MoveOption(selectedPiece.col, selectedPiece.row + 1))) {
                    selectedPiece.row++;
                }
                if (key == KeyEvent.VK_LEFT
                        && selectedPieceMovesList.contains(new MoveOption(selectedPiece.col - 1, selectedPiece.row))) {
                    selectedPiece.col--;
                }
                if (key == KeyEvent.VK_RIGHT
                        && selectedPieceMovesList.contains(new MoveOption(selectedPiece.col + 1, selectedPiece.row))) {
                    selectedPiece.col++;
                }

                // delete the current piece
                if (key == KeyEvent.VK_D) {
                    pieceList.remove(selectedPiece);
                    selectedPiece = null;
                }

                if (key == KeyEvent.VK_0) {
                    System.out.println("\nNumMoves: " + selectedPieceMovesList.size());
                    System.out.println("SelectedMoves: " + selectedPieceMovesList);
                }

                if (key == KeyEvent.VK_1) {
                    System.out.println("\nNumMoves: " + psuedoLegalMovesList.size());
                    System.out.println("PsuedoMoves: " + psuedoLegalMovesList);
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

            // prints the rectangles of any promoted pawns
            if (key == KeyEvent.VK_3) {
                for (int i = 50; i < 100; i++) {
                    if (Piece.getPiece(i) != null) {
                        System.out.println("Has piece " + i);
                        System.out.println(Piece.getPiece(i).getBounds());
                    }
                }
            }
            // toggles if you want to see psuedo moves
            if (key == KeyEvent.VK_2) {
                showPsuedoMoves = !showPsuedoMoves;
                System.out.println(psuedoLegalMovesList.contains(new MoveOption(4, 6)));
            }

            if (key == KeyEvent.VK_4) {
                for (Piece elem : pieceList) {
                    System.out.println(elem + " " + elem.getMoves() + " " + elem.getMoves().size());
                    for (MoveOption move : elem.getMoves()) {
                        if (move == new MoveOption(4, 6)) {
                            System.out.println("Can go to 4, 6: " + elem);
                        }
                    }
                }
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
            boolean isWhiteTurn = numTurns % 2 == 1;
            System.out.println("SelectedPiece: " + selectedPiece);

            if (selectedPiece != null) {
                System.out.println("THIS IS BEING RUN");
                for (MoveOption moves : selectedPieceMovesList) {
                    Point p = new Point(moves.centerX - 30, moves.centerY - 30);
                    Rectangle rect = new Rectangle(p, PIECE_DIMENSION);
                    if (rect.contains(e.getPoint())) {
                        teleportPiece = true;
                        // handles castling
                        if (selectedPiece.type == 'K') {
                            if (moves.col == selectedPiece.col + 2) {
                                Piece rook = Piece.getPiece(moves.col + 1, moves.row);
                                rook.teleportPiece(rook, new MoveOption(moves.col - 1, moves.row));
                            } else if (moves.col == selectedPiece.col - 2) {
                                Piece rook = Piece.getPiece(moves.col - 2, moves.row);
                                rook.teleportPiece(rook, new MoveOption(moves.col + 1, moves.row));
                            }
                        }
                        // white en passant

                        if (selectedPiece.type == 'p') {
                            if (selectedPiece.row == 3 && selectedPiece.isWhite) {
                                if (Piece.getPiece(selectedPiece.col - 1, selectedPiece.row) != null) {

                                    Piece piece = Piece.getPiece(selectedPiece.col - 1, selectedPiece.row);
                                    if (piece.type == 'p' && !piece.isWhite && piece.numPreviousMoves == 1
                                            && GamePanel.lastPieceThatMoved == piece) {
                                        selectedPiece.teleportPiece(
                                                new MoveOption(selectedPiece.col - 1, selectedPiece.row - 1));
                                        pieceList.remove(piece);
                                    }
                                }
                                if (Piece.getPiece(selectedPiece.col + 1, selectedPiece.row) != null) {
                                    Piece piece = Piece.getPiece(selectedPiece.col + 1, selectedPiece.row);
                                    if (piece.type == 'p' && !piece.isWhite && piece.numPreviousMoves == 1
                                            && GamePanel.lastPieceThatMoved == piece) {
                                        selectedPiece.teleportPiece(
                                                new MoveOption(selectedPiece.col + 1, selectedPiece.row - 1));
                                        pieceList.remove(piece);
                                    }
                                }
                            }

                            if (selectedPiece.row == 4 && !selectedPiece.isWhite) {
                                if (Piece.getPiece(selectedPiece.col + 1, selectedPiece.row) != null) {
                                    Piece piece = Piece.getPiece(selectedPiece.col + 1, selectedPiece.row);
                                    if (piece.type == 'p' && piece.isWhite && piece.numPreviousMoves == 1
                                            && GamePanel.lastPieceThatMoved == piece) {
                                        selectedPiece.teleportPiece(
                                                new MoveOption(selectedPiece.col + 1, selectedPiece.row + 1));
                                        pieceList.remove(piece);
                                    }
                                }
                                if (Piece.getPiece(selectedPiece.col - 1, selectedPiece.row) != null) {
                                    Piece piece = Piece.getPiece(selectedPiece.col - 1, selectedPiece.row);
                                    if (piece.type == 'p' && piece.isWhite && piece.numPreviousMoves == 1
                                            && GamePanel.lastPieceThatMoved == piece) {
                                        selectedPiece.teleportPiece(
                                                new MoveOption(selectedPiece.col - 1, selectedPiece.row + 1));
                                        pieceList.remove(piece);
                                    }
                                }
                            }
                        }

                        // captures the piece it goes to
                        for (Piece elem : pieceList) {
                            // System.out.println("elem: " + elem + " " + i);
                            if (elem.contains(e.getPoint()) && selectedPiece != elem) {
                                pieceList.remove(elem);
                                break;
                            }
                        }
                        selectedPiece.teleportPiece(moves);

                        // deselects piece and clears moves
                        selectedPiece = null;
                        selectedPieceMovesList.clear();
                        break;
                    }
                }
                if (teleportPiece) {
                    // System.out.println("TELEPORT");
                    teleportPiece = false;
                } else if (!selectedPiece.contains(e.getPoint())) {
                    selectedPieceMovesList.clear();
                    selectedPiece = null;
                    System.out.println("Clicked on air1");
                } else {
                    selectedPiece.mousePressed(e);
                }
            } else {
                // if no preselected piece
                boolean clickedPiece = false;
                for (int i = 0; i < pieceList.size(); i++) {
                    Piece elem = pieceList.get(i);
                    if (elem.contains(e.getPoint())) {
                        clickedPiece = true;

                        if (isWhiteTurn == elem.isWhite) {
                            elem.mousePressed(e);
                            // System.out.println("Mouse Pressed!!!");
                            break;
                        } else {
                            System.out.println("Not your turn");
                            break;
                        }
                    }
                }
                // if it didnt click a piece
                if (!clickedPiece) {
                    selectedPieceMovesList.clear();
                    selectedPiece = null;
                    System.out.println("Clicked on air2");
                }

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (selectedPiece != null) {
                for (Piece elem : pieceList) {
                    if (elem.contains(e.getPoint()) && selectedPiece != elem) {
                        pieceList.remove(elem);
                        break;
                    }
                }
                selectedPiece.mouseReleased(e);

            } else {
                for (Piece elem : pieceList) {
                    if (elem.contains(e.getPoint())) {
                        elem.mouseReleased(e);
                        break;
                    }
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
