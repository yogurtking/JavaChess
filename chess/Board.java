package chess;

public class Board {
	
	//array to contain Piece objects, the location will be consistent with our StackPane array
	public final Piece[][] board = new Piece[8][8];
	
	public Board() {
		//maybe this is where start game is called? not sure.
	}
	
	public Piece[][] getArray() {
		return this.board;
	}
	
	public void setPiece(int row, int column, Piece piece) {
		board[row][column] = piece;
	}
	
	public Piece getPiece(int row, int column) {
		return board[row][column];
	}
	
	
	
	//I think this is redundant, its already in Piece
	public static PieceType getPieceType(int row, int column, Piece[][] board) {
		//this returns the piecetype of the piece in that square, maybe also color?
		return board[row][column].getType();
	}
	
	
	
	
}
