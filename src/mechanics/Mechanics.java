package mechanics;


import World.Board;
import World.Piece;
import World.Space;


public class Mechanics {
    public Mechanics(Board board) {
        this.board = board;
    }

    Board board;

    public void move(int moves, Piece piece, Space from, Space to) {
        if (canMoveThere(from, to, piece, moves)) {
            if (to.getSize() == 1) {
                to.movePiece(board.getEatenSpace());
            }
            from.movePiece(to);
        }
    }

    private boolean canMoveThere(Space from, Space to, Piece piece, int moves) {
        int distance = Math.abs(from.getId() - to.getId());
        if (distance != moves) return false;
        else {
            int pieceId = piece.getId();
            return checkIfCanDominate(pieceId, to);
        }
    }

    private boolean checkIfCanDominate(int pieceId, Space to) {
        int size = to.getSize();
        if (to.getDominantId() != pieceId) {
            return size <= 1;
        }
        return true;
    }

    public int isWin() {
        int p1 = 0;
        int p2 = 0;
        for (int i = 18; i < 24; i++) {
            Space current = board.getSpaces()[i];
            for (Piece p : current.getPieces()) {
                if (p.getId() == 1) {
                    p1++;
                }
            }
        }
        if (p1 >= 15) return 1;
        else {
            for (int i = 0; i < 6; i++) {
                Space current = board.getSpaces()[i];
                for (Piece p : current.getPieces()) {
                    if (p.getId() == 2) {
                        p2++;
                    }
                }
            }
            if (p2 >= 15) {
                return p2;
            } else {
                return 0;
            }
        }
    }


}
