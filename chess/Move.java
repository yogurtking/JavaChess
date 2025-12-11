package chess;

public class Move {
	public int fromRow;
	public int fromColumn;
	public int toRow;
	public int toColumn;
	public PieceType movingPiece;
	public PieceType capturedPiece;
	public int round;
	public PlayerColor side;
	
	
	
	//simpler constructor for making lists of possible moves with getLegalMoves
	public Move(int fromRow, int fromColumn, int toRow, int toColumn, PieceType movingPiece) {
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
		this.movingPiece = movingPiece;
		
	}
	
	public Move(int fromRow, int fromColumn, int toRow, int toColumn, PieceType movingPiece,
				PieceType capturedPiece, int currentTurn, PlayerColor side) {
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
		this.movingPiece = movingPiece;
		this.capturedPiece = capturedPiece;
		this.round = currentTurn;
		this.side = side;
	}
}
