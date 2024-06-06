package game.pieces;

import game.board.Board;
import game.board.Point;
import game.Team;

/**
 * Lớp Elephant đại diện cho quân cờ Tượng trong trò chơi.
 */
public class Elephant extends Piece {

    /**
     * Hàm khởi tạo cho lớp Elephant.
     *
     * @param team đội của quân Tượng.
     */
    public Elephant(Team team){
        super(PieceType.ELEPHANT, team);
    }

    /**
     * Kiểm tra xem quân Tượng có thể di chuyển từ điểm bắt đầu đến điểm kết thúc hay không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu quân Tượng có thể di chuyển, ngược lại false.
     */
    @Override
    public boolean canMove(Board board, Point start, Point end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Kiểm tra xem nước đi có đúng hai ô theo đường chéo không
        if (Math.abs(startRow - endRow) == 2 && Math.abs(startCol - endCol) == 2) {
            // Đảm bảo quân Tượng không vượt sông
            if (!isWithinTerritory(end)) {
                return false;
            }

            // Kiểm tra xem mắt của Tượng (trung tâm của nước đi chéo) có bị chặn không
            int eyeRow = (startRow + endRow) / 2;
            int eyeCol = (startCol + endCol) / 2;
            if (board.getPoints()[eyeRow][eyeCol].isOccupied()) {
                return false;
            }

            // Kiểm tra xem điểm kết thúc có trống hoặc bị chiếm bởi quân địch không
            if (!board.getPoints()[endRow][endCol].isOccupied() || board.getPoints()[endRow][endCol].getPiece().getTeam() != this.getTeam()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem điểm có nằm trong lãnh thổ của quân Tượng không.
     *
     * @param point điểm cần kiểm tra.
     * @return true nếu điểm nằm trong lãnh thổ, ngược lại false.
     */
    private boolean isWithinTerritory(Point point) {
        int row = point.getRow();
        int col = point.getCol();

        if (this.getTeam() == Team.BLACK) {
            return (row >= 0 && row <= 4) && (col >= 0 && col <= 8);
        } else {
            return (row >= 5 && row <= 9) && (col >= 0 && col <= 8);
        }
    }

    /**
     * Trả về tên tệp biểu tượng của quân Tượng.
     *
     * @return tên tệp biểu tượng của quân Tượng.
     */
    @Override
    public String getIconFileName() {
        if (this.getTeam() == Team.RED){
            return "src/resources/red/5.png";
        } else {
            return "src/resources/black/5.png";
        }
    }
}
