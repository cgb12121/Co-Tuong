package game.pieces;

import game.board.Board;
import game.board.Point;
import game.Team;

/**
 * Lớp Cannon đại diện cho quân cờ Pháo trong trò chơi.
 */
public class Cannon extends Piece {

    /**
     * Hàm khởi tạo cho lớp Cannon.
     *
     * @param team đội của quân Pháo.
     */
    public Cannon(Team team) {
        super(PieceType.CANNON, team);
    }

    /**
     * Kiểm tra xem quân Pháo có thể di chuyển từ điểm bắt đầu đến điểm kết thúc hay không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu quân Pháo có thể di chuyển, ngược lại false.
     */
    public boolean canMove(Board board, Point start, Point end) {
        if (start.equals(end)) {
            return false; // Không thể di chuyển đến cùng một vị trí
        }

        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        // Đảm bảo các điểm nằm cùng một hàng hoặc cùng một cột
        if (startX != endX && startY != endY) {
            return false;
        }

        // Kiểm tra xem đường đi có trống cho quân Pháo không
        if (isPathClearForCannon(board, start, end)) {
            // Di chuyển thông thường (không bắt)
            return !board.getPoints()[endX][endY].isOccupied();
        } else if (board.getPoints()[endX][endY].isOccupied() && board.getPoints()[endX][endY].getPiece().getTeam() != this.getTeam()) {
            // Di chuyển bắt (phải nhảy qua chính xác một quân cờ)
            return isPathClearForCannonCapture(board, start, end);
        }

        return false;
    }

    /**
     * Kiểm tra xem đường đi có trống cho quân Pháo không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu đường đi trống, ngược lại false.
     */
    private static boolean isPathClearForCannon(Board board, Point start, Point end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        // Đảm bảo các điểm nằm cùng một hàng hoặc cùng một cột
        if (startX != endX && startY != endY) {
            return false;
        }

        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);

        int x = startX + dx;
        int y = startY + dy;

        while (x != endX || y != endY) {
            Point square = board.getPoints()[x][y];
            if (square.isOccupied()) {
                return false;
            }
            x += dx;
            y += dy;
        }

        return true;
    }

    /**
     * Kiểm tra xem đường đi có trống cho quân Pháo để bắt không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu đường đi trống cho quân Pháo để bắt, ngược lại false.
     */
    private static boolean isPathClearForCannonCapture(Board board, Point start, Point end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        // Đảm bảo các điểm nằm cùng một hàng hoặc cùng một cột
        if (startX != endX && startY != endY) {
            return false;
        }

        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);

        int x = startX + dx;
        int y = startY + dy;

        boolean jumped = false;

        while (x != endX || y != endY) {
            Point square = board.getPoints()[x][y];
            if (square.isOccupied()) {
                if (jumped) {
                    return false; // Có hơn một quân cờ giữa điểm bắt đầu và kết thúc
                }
                jumped = true;
            }
            x += dx;
            y += dy;
        }

        return jumped; // Trả về true nếu chính xác một quân cờ đã bị nhảy qua
    }

    /**
     * Trả về tên tệp biểu tượng của quân Pháo.
     *
     * @return tên tệp biểu tượng của quân Pháo.
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.RED){
            return "src/resources/red/2.png";
        } else {
            return "src/resources/black/2.png";
        }
    }
}
