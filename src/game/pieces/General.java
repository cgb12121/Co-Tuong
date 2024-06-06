package game.pieces;

import game.board.Board;
import game.board.Point;
import game.Team;

/**
 * Lớp General đại diện cho quân cờ Tướng trong trò chơi.
 */
public class General extends Piece {

    /**
     * Hàm khởi tạo cho lớp General.
     *
     * @param team đội của quân Tướng.
     */
    public General(Team team) {
        super(PieceType.GENERAL, team);
    }

    /**
     * Kiểm tra xem quân Tướng có thể di chuyển từ điểm bắt đầu đến điểm kết thúc hay không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu quân Tướng có thể di chuyển, ngược lại false.
     */
    @Override
    public boolean canMove(Board board, Point start, Point end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Kiểm tra xem nước đi nằm trong một bước di chuyển theo chiều dọc hoặc ngang
        boolean validStep = (Math.abs(startRow - endRow) == 1 && startCol == endCol) ||
                (Math.abs(startCol - endCol) == 1 && startRow == endRow);

        // Kiểm tra xem nước đi có nằm trong cung điện và điểm kết thúc trống hoặc bị chiếm bởi quân địch
        if (validStep && isWithinPalace(end)) {
            return !end.isOccupied() || end.getPiece().getTeam() != this.getTeam();
        }
        return false;
    }

    /**
     * Kiểm tra xem điểm có nằm trong cung điện không.
     *
     * @param point điểm cần kiểm tra.
     * @return true nếu điểm nằm trong cung điện, ngược lại false.
     */
    private boolean isWithinPalace(Point point) {
        int row = point.getRow();
        int col = point.getCol();

        if (this.getTeam() == Team.BLACK) {
            return row >= 0 && row <= 2 && col >= 3 && col <= 5;
        } else {
            return row >= 7 && row <= 9 && col >= 3 && col <= 5;
        }
    }

    /**
     * Trả về tên tệp biểu tượng của quân Tướng.
     *
     * @return tên tệp biểu tượng của quân Tướng.
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.RED){
            return "src/resources/red/7.png";
        } else {
            return "src/resources/black/7.png";
        }
    }
}
