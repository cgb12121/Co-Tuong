package game.pieces;

import game.board.Board;
import game.board.Point;
import game.Team;

/**
 * Lớp Advisor đại diện cho quân cờ Sĩ trong trò chơi.
 */
public class Advisor extends Piece {

    /**
     * Hàm khởi tạo cho lớp Advisor.
     *
     * @param team đội của quân Sĩ.
     */
    public Advisor(Team team){
        super(PieceType.ADVISOR, team);
    }

    /**
     * Kiểm tra xem quân Sĩ có thể di chuyển từ điểm bắt đầu đến điểm kết thúc hay không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu quân Sĩ có thể di chuyển, ngược lại false.
     */
    @Override
    public boolean canMove(Board board, Point start, Point end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Sĩ chỉ có thể di chuyển một bước chéo
        if (Math.abs(startRow - endRow) == 1 && Math.abs(startCol - endCol) == 1 && isWithinPalace(end)) {
            if (!board.getPoints()[endRow][endCol].isOccupied() ||
                    board.getPoints()[endRow][endCol].getPiece().getTeam() != this.getTeam()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem điểm có nằm trong cung hay không.
     *
     * @param point điểm cần kiểm tra.
     * @return true nếu điểm nằm trong cung, ngược lại false.
     */
    private boolean isWithinPalace(Point point) {
        int row = point.getRow();
        int col = point.getCol();

        if (this.getTeam() == Team.BLACK) {
            return (row >= 0 && row <= 2) && (col >= 3 && col <= 5);
        } else {
            return (row >= 7 && row <= 9) && (col >= 3 && col <= 5);
        }
    }

    /**
     * Trả về tên tệp biểu tượng của quân Sĩ.
     *
     * @return tên tệp biểu tượng của quân Sĩ.
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.RED){
            return "src/resources/red/6.png";
        } else {
            return "src/resources/black/6.png";
        }
    }
}
