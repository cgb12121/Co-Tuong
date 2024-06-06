package game.board;

import game.CheckMate;
import game.pieces.*;
import game.Team;

public class Board {
    private Point[][] points;

    public Board(){
        this.points = new Point[10][9];
        initializePoints();
        setBoard();
    }

    private void initializePoints() {
        for (int row = 0; row < points.length; row++){
            for (int col = 0; col < points[0].length; col++){
                points[row][col] = new Point(row, col, null);
            }
        }
    }

    private void setBoard() {
        // Black pieces
        for (int i = 0; i < points[0].length; i += 2) {
            points[3][i] = new Point(3, i, new Soldier(Team.BLACK));
        }

        points[2][1] = new Point(2, 1, new Cannon(Team.BLACK));
        points[2][7] = new Point(2, 7, new Cannon(Team.BLACK));

        points[0][0] = new Point(0, 0, new Chariot(Team.BLACK));
        points[0][8] = new Point(0, 8, new Chariot(Team.BLACK));

        points[0][1] = new Point(0, 1, new Horse(Team.BLACK));
        points[0][7] = new Point(0, 7, new Horse(Team.BLACK));

        points[0][2] = new Point(0, 2, new Elephant(Team.BLACK));
        points[0][6] = new Point(0, 6, new Elephant(Team.BLACK));

        points[0][3] = new Point(0, 3, new Advisor(Team.BLACK));
        points[0][5] = new Point(0, 5, new Advisor(Team.BLACK));

        points[0][4] = new Point(0, 4, new General(Team.BLACK));

        // Black pieces
        for (int i = 0; i < points[0].length; i += 2) {
            points[6][i] = new Point(6, i, new Soldier(Team.RED));
        }

        points[7][1] = new Point(7, 1, new Cannon(Team.RED));
        points[7][7] = new Point(7, 7, new Cannon(Team.RED));

        points[9][0] = new Point(9, 0, new Chariot(Team.RED));
        points[9][8] = new Point(9, 8, new Chariot(Team.RED));

        points[9][1] = new Point(9, 1, new Horse(Team.RED));
        points[9][7] = new Point(9, 7, new Horse(Team.RED));

        points[9][2] = new Point(9, 2, new Elephant(Team.RED));
        points[9][6] = new Point(9, 6, new Elephant(Team.RED));

        points[9][3] = new Point(9, 3, new Advisor(Team.RED));
        points[9][5] = new Point(9, 5, new Advisor(Team.RED));

        points[9][4] = new Point(9, 4, new General(Team.RED));
    }

    public Point[][] getPoints() {
        return points;
    }public boolean movePiece(Point start, Point end) {
        if (start == null || end == null || start == end || !start.isOccupied() || !start.getPiece().canMove(this, start, end)) {
            return false;
        }

        Piece piece = start.getPiece();
        Piece capturedPiece = end.getPiece();

        // Temporarily move the piece
        end.setPiece(piece);
        start.setPiece(null);

        // Check if the move puts the general in check
        Point generalPoint = findGeneralPoint(piece.getTeam());
        if (isGeneralInCheck(piece.getTeam())) {
            // Restore the original positions if the general is in check
            start.setPiece(piece);
            end.setPiece(capturedPiece);
            return false;
        }

        return true;
    }

    public boolean isGeneralInCheck(Team team) {
        Point kingSquare = findGeneralPoint(team);
        return CheckMate.generalInCheck(this, kingSquare);
    }


    public Point findGeneralPoint(Team team) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                Point generalPoint = getPoints()[i][j];
                if (generalPoint != null && generalPoint.getPiece() != null &&
                        generalPoint.getPiece().getType() == PieceType.GENERAL &&
                        generalPoint.getPiece().getTeam() == team) {
                    return generalPoint;
                }
            }
        }
        return null;
    }
}
