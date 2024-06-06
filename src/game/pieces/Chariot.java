package game.pieces;

import game.board.Board;
import game.board.Point;
import game.Team;

/**
 * Lớp Chariot đại diện cho quân cờ Xe trong trò chơi.
 */
public class Chariot extends Piece {

    /**
     * Hàm khởi tạo cho lớp Chariot.
     *
     * @param team đội của quân Xe.
     */
    public Chariot(Team team) {
        super(PieceType.CHARIOT, team);
    }

    /**
     * Kiểm tra xem quân Xe có thể di chuyển từ điểm bắt đầu đến điểm kết thúc hay không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu quân Xe có thể di chuyển, ngược lại false.
     */
    @Override
    public boolean canMove(Board board, Point start, Point end) {
        if (start.equals(end)) {
            return false; // Không thể di chuyển đến cùng một vị trí
        }

        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Đảm bảo nước đi là một đường thẳng
        if (startRow != endRow && startCol != endCol) {
            return false;
        }

        // Kiểm tra xem đường đi có trống không
        if (!isPathClear(board, start, end)) {
            return false;
        }

        // Kiểm tra xem điểm kết thúc có bị chiếm bởi quân địch hay không hoặc trống
        return !board.getPoints()[endRow][endCol].isOccupied() || start.getPiece().getTeam() != end.getPiece().getTeam();
    }

    /**
     * Kiểm tra xem đường đi có trống không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu đường đi trống, ngược lại false.
     */
    private boolean isPathClear(Board board, Point start, Point end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int dx = Integer.compare(endRow, startRow);
        int dy = Integer.compare(endCol, startCol);

        int x = startRow + dx;
        int y = startCol + dy;

        while (x != endRow || y != endCol) {
            if (board.getPoints()[x][y].isOccupied()) {
                return false;
            }
            x += dx;
            y += dy;
        }

        return true;
    }

    /**
     * Trả về tên tệp biểu tượng của quân Xe.
     *
     * @return tên tệp biểu tượng của quân Xe.
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.RED){
            return "src/resources/red/3.png";
        } else {
            return "src/resources/black/3.png";
        }
    }
}
