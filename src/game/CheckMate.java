package game;

import game.board.Board;
import game.board.Point;
import game.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp CheckMate chứa các phương thức để kiểm tra vua có thể thoát khỏi chiếu hay không và các tình huống chiếu.
 */
public class CheckMate {

    /**
     * Phương thức kiểm tra xem vua có thể thoát khỏi chiếu không.
     *
     * @param board Bàn cờ hiện tại.
     * @param genPos Vị trí của vua.
     * @return true nếu vua có thể thoát khỏi chiếu, ngược lại trả về false.
     */
    public static boolean canGeneralEscape(Board board, Point genPos) {
        int genRow = genPos.getRow();
        int genCol = genPos.getCol();
        Team genTeam = genPos.getPiece().getTeam();
        int[][] possibleMoves = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        for (int[] move : possibleMoves) {
            int newRow = genRow + move[0];
            int newCol = genCol + move[1];

            // Kiểm tra xem vị trí mới có nằm trong cung hay không
            boolean isWithinPalace = (genTeam == Team.RED && newRow >= 0 && newRow <= 2 && newCol >= 3 && newCol <= 5) ||
                    (genTeam == Team.BLACK && newRow >= 7 && newRow <= 9 && newCol >= 3 && newCol <= 5);

            if (isWithinPalace) {
                Point newPoint = board.getPoints()[newRow][newCol];
                if (!newPoint.isOccupied() || newPoint.getPiece().getTeam() != genTeam) {
                    if (!willBeCheckedBySoldier(board, genPos, newPoint) &&
                            !willBeCheckedByHorse(board, genPos, newPoint) &&
                            !willBeCheckedByChariot(board, genPos, newPoint) &&
                            !willBeCheckedByCannon(board, genPos, newPoint) &&
                            !willBeCheckedByGeneral(board, genPos, newPoint)) {
                        return true; // Tìm thấy vị trí an toàn để thoát
                    }
                }
            }
        }
        return false; // Không tìm thấy vị trí an toàn để thoát
    }

    /**
     * Phương thức kiểm tra xem vua có bị chiếu không.
     *
     * @param board Bàn cờ hiện tại.
     * @param genPos Vị trí của vua.
     * @return true nếu vua bị chiếu, ngược lại trả về false.
     */
    public static boolean generalInCheck(Board board, Point genPos) {
        return willBeCheckedBySoldier(board, genPos, genPos) ||
                willBeCheckedByHorse(board, genPos, genPos) ||
                willBeCheckedByChariot(board, genPos, genPos) ||
                willBeCheckedByCannon(board, genPos, genPos) ||
                willBeCheckedByGeneral(board, genPos, genPos);
    }

    /**
     * Phương thức kiểm tra xem vua sẽ bị chiếu bởi một vua khác.
     *
     * @param board Bàn cờ hiện tại.
     * @param kingPos Vị trí của vua.
     * @param kingNewPos Vị trí mới của vua.
     * @return true nếu vua sẽ bị chiếu, ngược lại trả về false.
     */
    public static boolean willBeCheckedByGeneral(Board board, Point kingPos, Point kingNewPos) {
        int kingCol = kingNewPos.getCol();
        int numRows = 10;

        for (int row = 0; row < numRows; row++) {
            if (row != kingNewPos.getRow()) {
                Point targetSquare = board.getPoints()[row][kingCol];
                if (targetSquare.isOccupied() && targetSquare.getPiece().getType() == PieceType.GENERAL &&
                        targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam()) {
                    if (isPathClear(board, kingNewPos, targetSquare)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Phương thức kiểm tra xem vua sẽ bị chiếu bởi tốt.
     *
     * @param board Bàn cờ hiện tại.
     * @param kingPos Vị trí của vua.
     * @param kingNewPos Vị trí mới của vua.
     * @return true nếu vua sẽ bị chiếu, ngược lại trả về false.
     */
    public static boolean willBeCheckedBySoldier(Board board, Point kingPos, Point kingNewPos) {
        int endX = kingNewPos.getRow();
        int endY = kingNewPos.getCol();
        Team kingTeam = kingPos.getPiece().getTeam();

        // Định nghĩa các nước đi có thể của tốt tùy thuộc vào đội.
        int[][] possibleMoves;
        if (kingTeam == Team.BLACK) {
            // Tốt đen di chuyển xuống
            possibleMoves = new int[][]{{1, 0}, {0, -1}, {0, 1}};
        } else {
            // Tốt đỏ di chuyển lên
            possibleMoves = new int[][]{{-1, 0}, {0, -1}, {0, 1}};
        }

        for (int[] move : possibleMoves) {
            int newX = endX + move[0];
            int newY = endY + move[1];

            // Đảm bảo nước đi nằm trong giới hạn của bàn cờ
            if (newX >= 0 && newX < 10 && newY >= 0 && newY < 9) {
                Point targetPoint = board.getPoints()[newX][newY];
                if (targetPoint.isOccupied() && targetPoint.getPiece().getType() == PieceType.SOLDIER &&
                        targetPoint.getPiece().getTeam() != kingTeam) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Phương thức kiểm tra xem vua sẽ bị chiếu bởi pháo.
     *
     * @param board Bàn cờ hiện tại.
     * @param kingPos Vị trí của vua.
     * @param kingNewPos Vị trí mới của vua.
     * @return true nếu vua sẽ bị chiếu, ngược lại trả về false.
     */
    public static boolean willBeCheckedByCannon(Board board, Point kingPos, Point kingNewPos) {
        int newRow = kingNewPos.getRow();
        int newCol = kingNewPos.getCol();
        int numRows = 10;
        int numCols = 9;

        // Kiểm tra pháo trong cùng một cột
        for (int row = 0; row < numRows; row++) {
            if (row != newRow) {
                Point targetSquare = board.getPoints()[row][newCol];
                if (targetSquare != null && targetSquare.getPiece() != null &&
                        targetSquare.getPiece().getType() == PieceType.CANNON &&
                        targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam() &&
                        isPathClearForCannon(board, targetSquare, kingNewPos)) {
                    return true;
                }
            }
        }

        // Kiểm tra pháo trong cùng một hàng
        for (int col = 0; col < numCols; col++) {
            if (col != newCol) {
                Point targetSquare = board.getPoints()[newRow][col];
                if (targetSquare != null && targetSquare.getPiece() != null &&
                        targetSquare.getPiece().getType() == PieceType.CANNON &&
                        targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam() &&
                        isPathClearForCannon(board, targetSquare, kingNewPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Phương thức kiểm tra xem vua sẽ bị chiếu bởi quân mã.
     *
     * @param board Bàn cờ hiện tại.
     * @param kingPos Vị trí của vua.
     * @param kingNewPos Vị trí mới của vua.
     * @return true nếu vua sẽ bị chiếu, ngược lại trả về false.
     */
    public static boolean willBeCheckedByHorse(Board board, Point kingPos, Point kingNewPos) {
        int newRow = kingNewPos.getRow();
        int newCol = kingNewPos.getCol();
        Team kingTeam = kingPos.getPiece().getTeam();

        // Các nước đi tiềm năng của quân mã so với vị trí mới của vua
        int[][] horseMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] move : horseMoves) {
            int potentialRow = newRow + move[0];
            int potentialCol = newCol + move[1];

            // Kiểm tra nếu vị trí tiềm năng nằm trong giới hạn của bàn cờ
            if (potentialRow >= 0 && potentialRow < 10 && potentialCol >= 0 && potentialCol < 9) {
                Point potentialPosition = board.getPoints()[potentialRow][potentialCol];
                if (potentialPosition.isOccupied() &&
                        potentialPosition.getPiece().getType() == PieceType.HORSE &&
                        potentialPosition.getPiece().getTeam() != kingTeam &&
                        isPathClearForHorse(board, potentialPosition, kingNewPos)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Phương thức kiểm tra xem vua sẽ bị chiếu bởi xe.
     *
     * @param board Bàn cờ hiện tại.
     * @param kingPos Vị trí của vua.
     * @param kingNewPos Vị trí mới của vua.
     * @return true nếu vua sẽ bị chiếu, ngược lại trả về false.
     */
    public static boolean willBeCheckedByChariot(Board board, Point kingPos, Point kingNewPos) {
        int newRow = kingNewPos.getRow();
        int newCol = kingNewPos.getCol();
        int numRows = 10;
        int numCols = 9;

        // Kiểm tra các xe ở cùng cột
        for (int row = 0; row < numRows; row++) {
            if (row != newRow) {
                Point targetSquare = board.getPoints()[row][newCol];
                if (targetSquare != null && targetSquare.getPiece() != null &&
                        targetSquare.getPiece().getType() == PieceType.CHARIOT &&
                        targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam() &&
                        isPathClear(board, targetSquare, kingNewPos)) {
                    return true;
                }
            }
        }

        // Kiểm tra các xe ở cùng hàng
        for (int col = 0; col < numCols; col++) {
            if (col != newCol) {
                Point targetSquare = board.getPoints()[newRow][col];
                if (targetSquare != null && targetSquare.getPiece() != null &&
                        targetSquare.getPiece().getType() == PieceType.CHARIOT &&
                        targetSquare.getPiece().getTeam() != kingPos.getPiece().getTeam() &&
                        isPathClear(board, targetSquare, kingNewPos)) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Kiểm tra xem đường đi giữa hai điểm có rỗng không (không bị chặn bởi các quân cờ khác).
     *
     * @param board Bàn cờ hiện tại.
     * @param start Điểm bắt đầu.
     * @param end Điểm kết thúc.
     * @return true nếu đường đi rỗng, ngược lại trả về false.
     */
    private static boolean isPathClear(Board board, Point start, Point end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        // Đảm bảo các điểm nằm trên cùng một hàng hoặc cột
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
     * Kiểm tra xem đường đi giữa hai điểm có rỗng không (không bị chặn bởi quân cờ nào trừ quân cờ ngắn sau).
     *
     * @param board Bàn cờ hiện tại.
     * @param start Điểm bắt đầu.
     * @param end Điểm kết thúc.
     * @return true nếu đường đi rỗng, ngược lại trả về false.
     */
    private static boolean isPathClearForCannon(Board board, Point start, Point end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        // Đảm bảo các điểm nằm trên cùng một hàng hoặc cột
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
                    return false; // Hơn một quân cờ nằm giữa điểm bắt đầu và điểm kết thúc
                }
                jumped = true;
            }
            x += dx;
            y += dy;
        }

        return jumped; // Trả về true nếu chỉ có một quân cờ được bắt qua
    }

    /**
     * Kiểm tra xem đường đi cho con ngựa có rỗng không (không bị chặn bởi các quân cờ khác).
     *
     * @param board Bàn cờ hiện tại.
     * @param start Điểm bắt đầu.
     * @param end Điểm kết thúc.
     * @return true nếu đường đi rỗng, ngược lại trả về false.
     */
    public static boolean isPathClearForHorse(Board board, Point start, Point end) {
        int startX = start.getRow();
        int startY = start.getCol();
        int endX = end.getRow();
        int endY = end.getCol();

        // Tính sự khác biệt về hàng và cột
        int dx = endX - startX;
        int dy = endY - startY;

        // Đảm bảo di chuyển là nước đi hợp lệ cho con ngựa
        if (!((Math.abs(dx) == 2 && Math.abs(dy) == 1) || (Math.abs(dx) == 1 && Math.abs(dy) == 2))) {
            return false;
        }

        // Xác định vị trí "chân" có thể chặn nước đi
        int legX = startX + Integer.signum(dx);
        int legY = startY + Integer.signum(dy);

        // Kiểm tra xem vị trí "chân" có được chiếm bởi quân cờ không
        if (Math.abs(dx) == 2) {
            // Nước đi chủ yếu theo chiều dọc, vì vậy kiểm tra chân ngang
            legY = startY;
        } else {
            // Nước đi chủ yếu theo chiều ngang, vì vậy kiểm tra chân dọc
            legX = startX;
        }

        // Trả về true nếu vị trí "chân" không được chiếm
        return !board.getPoints()[legX][legY].isOccupied();
    }
}
