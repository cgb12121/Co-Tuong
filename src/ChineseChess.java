import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import game.CheckMate;
import game.board.Board;
import game.board.Point;
import game.pieces.Piece;
import game.Team;

/**
 * Lớp ChineseChess đại diện cho trò chơi Cờ Tướng.
 */
public class ChineseChess extends JFrame {
    private Board board;
    private BufferedImage boardImage;
    private Point selectedPoint;
    private List<Point> validMoves;
    private Team currentTurn;
    private Point lastMoveStart;
    private Point lastMoveEnd;

    /**
     * Khởi tạo trò chơi Cờ Tướng.
     */
    public ChineseChess() {
        setTitle("Chinese Chess");
        setSize(900, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        board = new Board();
        validMoves = new ArrayList<>();
        currentTurn = Team.RED; // Đội Đỏ đi trước

        // Tải ảnh bàn cờ
        try {
            boardImage = ImageIO.read(new File("src/resources/board.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Tạo và thiết lập panel cho bàn cờ
        BoardPanel boardPanel = new BoardPanel();
        add(boardPanel);

        setVisible(true);
    }

    /**
     * Lớp BoardPanel đại diện cho panel của bàn cờ.
     */
    private class BoardPanel extends JPanel {
        public BoardPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int cellWidth = getWidth() / 9;
                    int cellHeight = getHeight() / 10;
                    int col = e.getX() / cellWidth;
                    int row = e.getY() / cellHeight;

                    Point clickedPoint = board.getPoints()[row][col];

                    if (selectedPoint == null) {
                        if (clickedPoint.getPiece() != null && clickedPoint.getPiece().getTeam() == currentTurn) {
                            selectedPoint = clickedPoint;
                            validMoves = getValidMoves(selectedPoint);
                        }
                    } else {
                        if (validMoves.contains(clickedPoint)) {
                            lastMoveStart = selectedPoint;
                            lastMoveEnd = clickedPoint;
                            if (board.movePiece(selectedPoint, clickedPoint)) {
                                selectedPoint = null;
                                validMoves.clear();
                                switchTurn();

                                // Kiểm tra chiếu tướng sau khi di chuyển
                                checkForCheckMate();
                            }
                        } else if (clickedPoint.getPiece() != null && clickedPoint.getPiece().getTeam() == currentTurn) {
                            selectedPoint = clickedPoint;
                            validMoves = getValidMoves(selectedPoint);
                        } else {
                            selectedPoint = null;
                            validMoves.clear();
                        }
                    }
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (boardImage != null) {
                g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), this);
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ các quân cờ
            Point[][] points = board.getPoints();
            for (int row = 0; row < points.length; row++) {
                for (int col = 0; col < points[row].length; col++) {
                    Piece piece = points[row][col].getPiece();
                    if (piece != null) {
                        String iconFileName = piece.getIconFileName();
                        BufferedImage pieceImage = loadImage(iconFileName);
                        if (pieceImage != null) {
                            int cellWidth = getWidth() / 9;
                            int cellHeight = getHeight() / 10;
                            g.drawImage(pieceImage, col * cellWidth, row * cellHeight, cellWidth, cellHeight, this);
                        }
                    }
                }
            }

            // Làm nổi bật quân cờ được chọn với vòng tròn dày hơn
            if (selectedPoint != null) {
                g2d.setColor(Color.GREEN);
                int cellWidth = getWidth() / 9;
                int cellHeight = getHeight() / 10;
                int x = selectedPoint.getCol() * cellWidth;
                int y = selectedPoint.getRow() * cellHeight;
                g2d.setStroke(new BasicStroke(5));
                g2d.drawOval(x, y, cellWidth, cellHeight);
            }

            // Làm nổi bật các nước đi hợp lệ
            g2d.setStroke(new BasicStroke(1));
            for (Point move : validMoves) {
                int cellWidth = getWidth() / 9;
                int cellHeight = getHeight() / 10;
                int x = move.getCol() * cellWidth + cellWidth / 3;
                int y = move.getRow() * cellHeight + cellHeight / 3;

                if (move.getPiece() != null && move.getPiece().getTeam() != selectedPoint.getPiece().getTeam()) {
                    // Làm nổi bật các quân địch trong nước đi hợp lệ bằng dấu chấm đỏ
                    g2d.setColor(Color.RED);
                    g2d.fillOval(x, y, cellWidth / 3, cellHeight / 3);
                } else {
                    g2d.setColor(Color.GREEN);
                    g2d.fillOval(x, y, cellWidth / 3, cellHeight / 3);
                }
            }

            // Làm nổi bật nước đi cuối cùng
            if (lastMoveStart != null && lastMoveEnd != null) {
                int cellWidth = getWidth() / 9;
                int cellHeight = getHeight() / 10;

                // Vẽ dấu chấm tại vị trí bắt đầu của nước đi cuối
                g2d.setColor(Color.BLUE);
                int xStart = lastMoveStart.getCol() * cellWidth + cellWidth / 3;
                int yStart = lastMoveStart.getRow() * cellHeight + cellHeight / 3;
                g2d.fillOval(xStart, yStart, cellWidth / 3, cellHeight / 3);

                // Vẽ vòng tròn xung quanh vị trí kết thúc của nước đi cuối
                int xEnd = lastMoveEnd.getCol() * cellWidth;
                int yEnd = lastMoveEnd.getRow() * cellHeight;
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(xEnd, yEnd, cellWidth, cellHeight);
            }

            // Làm nổi bật tướng đang bị chiếu bằng vòng tròn đỏ
            highlightGeneralInCheck(g2d, Team.RED);
            highlightGeneralInCheck(g2d, Team.BLACK);
        }

        /**
         * Tải ảnh từ tên file.
         *
         * @param fileName tên file của ảnh
         * @return đối tượng BufferedImage hoặc null nếu không thể tải ảnh
         */
        private BufferedImage loadImage(String fileName) {
            try {
                return ImageIO.read(new File(fileName));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Làm nổi bật tướng bị chiếu bằng vòng tròn đỏ.
         *
         * @param g2d đối tượng Graphics2D để vẽ
         * @param team đội của tướng
         */
        private void highlightGeneralInCheck(Graphics2D g2d, Team team) {
            Point generalPoint = board.findGeneralPoint(team);
            if (generalPoint != null && board.isGeneralInCheck(team)) {
                int cellWidth = getWidth() / 9;
                int cellHeight = getHeight() / 10;
                int x = generalPoint.getCol() * cellWidth;
                int y = generalPoint.getRow() * cellHeight;
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(7));
                g2d.drawOval(x + cellWidth / 2 - 100 / 2, y + cellHeight / 2 - 100 / 2, 100, 100);
            }
        }
    }

    /**
     * Lấy các nước đi hợp lệ cho quân cờ tại điểm đã chọn.
     *
     * @param point điểm đã chọn
     * @return danh sách các nước đi hợp lệ
     */
    private List<Point> getValidMoves(Point point) {
        List<Point> moves = new ArrayList<>();
        Piece piece = point.getPiece();
        if (piece != null) {
            Point[][] points = board.getPoints();
            for (Point[] value : points) {
                for (Point destination : value) {
                    if (piece.canMove(board, point, destination)) {
                        // Kiểm tra nếu nước đi để lại tướng bị chiếu mà không thực sự di chuyển quân cờ
                        if (!leavesGeneralInCheck(piece, point, destination)) {
                            moves.add(destination);
                        }
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Kiểm tra nếu nước đi làm tướng bị chiếu.
     *
     * @param piece quân cờ
     * @param start điểm bắt đầu
     * @param end điểm kết thúc
     * @return true nếu nước đi làm tướng bị chiếu, ngược lại false
     */
    private boolean leavesGeneralInCheck(Piece piece, Point start, Point end) {
        Piece originalEndPiece = end.getPiece();

        // Giả lập nước đi
        start.setPiece(null);
        end.setPiece(piece);

        // Kiểm tra nếu tướng bị chiếu sau nước đi
        boolean inCheck = board.isGeneralInCheck(piece.getTeam());

        // Khôi phục nước đi
        start.setPiece(piece);
        end.setPiece(originalEndPiece);

        return inCheck;
    }

    /**
     * Chuyển lượt chơi.
     */
    private void switchTurn() {
        currentTurn = (currentTurn == Team.RED) ? Team.BLACK : Team.RED;
    }

    /**
     * Kiểm tra chiếu tướng.
     */
    private void checkForCheckMate() {
        Point redGeneralPosition = board.findGeneralPoint(Team.RED);
        Point blackGeneralPosition = board.findGeneralPoint(Team.BLACK);

        if (isCheckMate(board, redGeneralPosition)) {
            JOptionPane.showMessageDialog(this, "Black wins! Red is in checkmate.");
        } else if (isCheckMate(board, blackGeneralPosition)) {
            JOptionPane.showMessageDialog(this, "Red wins! Black is in checkmate.");
        }
    }

    /**
     * Kiểm tra nếu có chiếu tướng.
     *
     * @param board bàn cờ
     * @param currentGeneralPosition vị trí hiện tại của tướng
     * @return true nếu có chiếu tướng, ngược lại false
     */
    private static boolean isCheckMate(Board board, Point currentGeneralPosition) {
        Team team = currentGeneralPosition.getPiece().getTeam();

        // Kiểm tra nếu tướng có thể thoát
        if (!CheckMate.canGeneralEscape(board, currentGeneralPosition)) {
            // Kiểm tra nếu bất kỳ quân cờ nào có thể chặn hoặc bắt quân cờ đe dọa
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 9; col++) {
                    Point currentPoint = board.getPoints()[row][col];
                    Piece piece = currentPoint.getPiece();

                    // Nếu quân cờ thuộc đội hiện tại
                    if (piece != null && piece.getTeam() == team) {
                        // Thử di chuyển quân cờ này để chặn hoặc bắt quân cờ chiếu
                        for (Point move : getValidMoves(board, currentPoint)) {
                            Piece originalPiece = move.getPiece();

                            // Giả lập nước đi
                            currentPoint.setPiece(null);
                            move.setPiece(piece);

                            // Kiểm tra nếu tướng vẫn bị chiếu
                            boolean stillInCheck = board.isGeneralInCheck(team);

                            // Khôi phục nước đi
                            currentPoint.setPiece(piece);
                            move.setPiece(originalPiece);

                            if (!stillInCheck) {
                                return false; // Không chiếu tướng vì đã tìm thấy một nước đi hợp lệ để chặn chiếu
                            }
                        }
                    }
                }
            }
            // Nếu không có nước đi nào có thể chặn hoặc bắt quân cờ chiếu
            return true;
        }
        return false;
    }

    /**
     * Lấy các nước đi hợp lệ cho quân cờ tại một điểm trên bàn cờ.
     *
     * @param board bàn cờ
     * @param point điểm
     * @return danh sách các nước đi hợp lệ
     */
    private static List<Point> getValidMoves(Board board, Point point) {
        List<Point> moves = new ArrayList<>();
        Piece piece = point.getPiece();
        if (piece != null) {
            Point[][] points = board.getPoints();
            for (Point[] value : points) {
                for (Point destination : value) {
                    if (piece.canMove(board, point, destination)) {
                        moves.add(destination);
                    }
                }
            }
        }
        return moves;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChineseChess::new);
    }
}