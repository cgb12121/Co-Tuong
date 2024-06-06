package game.pieces;

import game.board.Board;
import game.board.Point;
import game.Team;

/**
 * Lớp Soldier đại diện cho quân cờ Tốt trong trò chơi.
 */
public class Soldier extends Piece {

    /**
     * Hàm khởi tạo cho lớp Soldier.
     *
     * @param team đội của quân Tốt.
     */
    public Soldier(Team team) {
        super(PieceType.SOLDIER, team);
    }

    /**
     * Kiểm tra xem quân Tốt có thể di chuyển từ điểm bắt đầu đến điểm kết thúc hay không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu quân Tốt có thể di chuyển, ngược lại false.
     */
    public boolean canMove(Board board, Point start, Point end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Sau khi qua sông
        if (hasCrossedRiver(start)) {
            // Cho phép di chuyển về phía trước hoặc ngang (trái/phải)
            if ((endRow == startRow && Math.abs(startCol - endCol) == 1) ||
                    (getTeam() == Team.RED && endRow == startRow - 1 && startCol == endCol) ||
                    (getTeam() == Team.BLACK && endRow == startRow + 1 && startCol == endCol)) {
                return !end.isOccupied() || end.getPiece().getTeam() != this.getTeam();
            }
        }

        // Trước khi qua sông
        if (!hasCrossedRiver(start)) {
            if (getTeam() == Team.RED) {
                // Chỉ cho phép di chuyển về phía trước cho đội đỏ trước khi qua sông
                if (endRow == startRow - 1 && startCol == endCol) {
                    return !end.isOccupied() || end.getPiece().getTeam() != this.getTeam();
                }
            } else {
                // Chỉ cho phép di chuyển về phía trước cho đội đen trước khi qua sông
                if (endRow == startRow + 1 && startCol == endCol) {
                    return !end.isOccupied() || end.getPiece().getTeam() != this.getTeam();
                }
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem quân Tốt đã qua sông hay chưa.
     *
     * @param currentPosition vị trí hiện tại của quân Tốt.
     * @return true nếu quân Tốt đã qua sông, ngược lại false.
     */
    private boolean hasCrossedRiver(Point currentPosition) {
        int currentRow = currentPosition.getRow();
        return (this.getTeam() == Team.RED && currentRow < 5) ||
                (this.getTeam() == Team.BLACK && currentRow > 4);
    }

    /**
     * Trả về tên tệp biểu tượng của quân Tốt.
     *
     * @return tên tệp biểu tượng của quân Tốt.
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.RED){
            return "src/resources/red/1.png";
        } else {
            return "src/resources/black/1.png";
        }
    }
}
