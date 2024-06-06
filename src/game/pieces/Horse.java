package game.pieces;

import game.board.Board;
import game.board.Point;
import game.Team;

/**
 * Lớp Horse đại diện cho quân cờ Mã trong trò chơi.
 */
public class Horse extends Piece {

    /**
     * Hàm khởi tạo cho lớp Horse.
     *
     * @param team đội của quân Mã.
     */
    public Horse(Team team){
        super(PieceType.HORSE, team);
    }

    /**
     * Kiểm tra xem quân Mã có thể di chuyển từ điểm bắt đầu đến điểm kết thúc hay không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu quân Mã có thể di chuyển, ngược lại false.
     */
    @Override
    public boolean canMove(Board board, Point start, Point end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        boolean validStep = (Math.abs(startRow - endRow) == 2 && Math.abs(startCol - endCol) == 1) ||
                (Math.abs(startCol - endCol) == 2 && Math.abs(startRow - endRow) == 1);

        if (validStep && !isBlocked(board, start, end)) {
            return !end.isOccupied() || end.getPiece().getTeam() != this.getTeam();
        }
        return false;
    }

    /**
     * Kiểm tra xem quân Mã có bị chặn hay không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu quân Mã bị chặn, ngược lại false.
     */
    private boolean isBlocked(Board board, Point start, Point end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Xác định vị trí "chân" cần kiểm tra có bị chặn không
        if (Math.abs(startRow - endRow) == 2) {
            int legRow = startRow + (endRow - startRow) / 2;
            return board.getPoints()[legRow][startCol].isOccupied();
        } else if (Math.abs(startCol - endCol) == 2) {
            int legCol = startCol + (endCol - startCol) / 2;
            return board.getPoints()[startRow][legCol].isOccupied();
        }
        return false;
    }

    /**
     * Trả về tên tệp biểu tượng của quân Mã.
     *
     * @return tên tệp biểu tượng của quân Mã.
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.RED){
            return "src/resources/red/4.png";
        } else {
            return "src/resources/black/4.png";
        }
    }

}
