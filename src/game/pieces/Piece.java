package game.pieces;

import game.board.Board;
import game.board.Point;
import game.Team;

/**
 * Lớp trừu tượng Piece đại diện cho một quân cờ trong trò chơi.
 */
public abstract class Piece {
    private PieceType pieceType;
    private Team team;

    /**
     * Hàm khởi tạo cho lớp Piece.
     *
     * @param pieceType loại quân cờ.
     * @param team đội của quân cờ.
     */
    public Piece(PieceType pieceType, Team team) {
        this.pieceType = pieceType;
        this.team = team;
    }

    /**
     * Trả về tên tệp biểu tượng của quân cờ.
     *
     * @return tên tệp biểu tượng của quân cờ.
     */
    public abstract String getIconFileName();

    /**
     * Kiểm tra xem quân cờ có thể di chuyển từ điểm bắt đầu đến điểm kết thúc hay không.
     *
     * @param board bàn cờ hiện tại.
     * @param start điểm bắt đầu.
     * @param end điểm kết thúc.
     * @return true nếu quân cờ có thể di chuyển, ngược lại false.
     */
    public abstract boolean canMove(Board board, Point start, Point end);

    /**
     * Trả về đội của quân cờ.
     *
     * @return đội của quân cờ.
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Thiết lập đội cho quân cờ.
     *
     * @param team đội mới của quân cờ.
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Trả về loại của quân cờ.
     *
     * @return loại của quân cờ.
     */
    public PieceType getType() {
        return pieceType;
    }

    /**
     * Thiết lập loại cho quân cờ.
     *
     * @param pieceType loại mới của quân cờ.
     */
    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }
}
