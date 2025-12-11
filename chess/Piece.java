package chess;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
	PlayerColor color; //BLACK, WHITE
	PieceType type; 
	boolean hasMoved = false;
	
	public Piece(PlayerColor color, PieceType type) {
		this.color = color;
		this.type = type;
	}
	
	abstract List<Move> getLegalMoves(int r, int c, Piece[][] board);
	
	public PlayerColor getColor() {
		return this.color;
	}
	
	public PieceType getType() {
		return this.type;
	}
	
	public void setHasMoved(boolean moved) {
		this.hasMoved = moved;
	}
	
	//check that our row and column are within 0-7
	public boolean checkRange(int row, int column) {
		if (row < 8 && row > -1 && column < 8 && column > -1) {
			return true;
		} else {
			return false;
		}
	}
	
	public static class King extends Piece {
		private static final int[][] KING_MOVES = {{0,1}, {0,-1}, {1,0}, {1,1}, {1,-1}, {-1,0}, {-1,1}, {-1,-1}};
		
		public King(PlayerColor color) {
			super(color, PieceType.KING);
		}
		
		//r is current row, c is current column, board is the board we're on, currently there is only one board but it might be useful later to have this open to other boards, for simulating moves etc.
		@Override
		public List<Move> getLegalMoves(int r, int c, Piece[][] board) {
			//list to hold moves
			List<Move> legalMoves = new ArrayList<>();
			
			//return a list of legal moves based on current location and other pieces on board
			for (int[] move:KING_MOVES) {
				int targetRow = r+move[0];
				int targetColumn = c+move[1];
				//check if we are off the board and skip to next step in the for loop if we are
				if (!checkRange(targetRow, targetColumn)) continue; 
				//make a variable for the target space so we can safely check if its empty
				Piece targetPiece = board[targetRow][targetColumn];
				
				//check if there is a piece in the space, if there isn't add it to the list for now, we'll check if it leaves our king open later
				if (targetPiece == null) 
				{
					legalMoves.add(new Move(r, c, targetRow, targetColumn, this.getType()));
				//if there is, check if it is the opponent's piece, if it is, add it to the list, we'll check if this is legal wrt to moving our king into check later
				} else if (board[targetRow][targetColumn].getColor() != this.getColor()) {
					legalMoves.add(new Move(r, c, targetRow, targetColumn, this.getType()));
				}
				
			}
			return legalMoves;
		}
	}
	
	public static class Knight extends Piece {
		private static final int[][] KNIGHT_MOVES = {{-2,-1},{-2,1},{2,-1},{2,1},{-1,-2},{-1,2},{1,-2},{1,2}};
		
		public Knight(PlayerColor color) {
			super(color, PieceType.KNIGHT);
		}
		
		public List<Move> getLegalMoves(int r, int c, Piece[][] board) {
			//list to hold moves
			List<Move> legalMoves = new ArrayList<>();
			
			//return a list of legal moves based on current location and other pieces on board
			for (int[] move:KNIGHT_MOVES) {
				int targetRow = r+move[0];
				int targetColumn = c+move[1];
				//check if we are off the board and skip to next step in the for loop if we are
				if (!checkRange(targetRow, targetColumn)) continue; 
				//make a variable for the target space so we can safely check if its empty
				Piece targetPiece = board[targetRow][targetColumn];
				//check if there is a piece in the space, if there isn't add this move to the list
				if (targetPiece == null) 
				{
					legalMoves.add(new Move(r, c, targetRow, targetColumn, this.getType()));
				//if there is, check if it is the opponent's piece, if it is, add this move to the list
				} else if (board[targetRow][targetColumn].getColor() != this.getColor()) {
					legalMoves.add(new Move(r, c, targetRow, targetColumn, this.getType()));
				}
				
			}
			return legalMoves;
		}
	}
	
	public static class Pawn extends Piece {
		int[][] pawnMoves;
		int[][] pawnCaptures;
		private static final int[][] BLACK_PAWN_MOVES = {{1,0},{2,0}};
		private static final int[][] WHITE_PAWN_MOVES = {{-1,0},{-2,0}};
		private static final int[][] BLACK_PAWN_CAPTURES = {{1,-1},{1,1}};
		private static final int[][] WHITE_PAWN_CAPTURES = {{-1,-1},{-1,1}};
		
		
		
		public Pawn(PlayerColor color) {
			super(color, PieceType.PAWN);
			this.pawnMoves = (color == PlayerColor.WHITE) ? WHITE_PAWN_MOVES : BLACK_PAWN_MOVES;
			this.pawnCaptures = (color == PlayerColor.WHITE) ? WHITE_PAWN_CAPTURES : BLACK_PAWN_CAPTURES;
		}
		
		@Override
		public List<Move> getLegalMoves(int r, int c, Piece[][] board) {
			//list to hold moves
			List<Move> legalMoves = new ArrayList<>();
			//return a list of legal moves based on current location and other pieces on board
			for (int[] move:pawnMoves) {
				int targetRow = r+move[0];
				int targetColumn = c;//column won't change on a move for pawn
				//check range
				if (!checkRange(targetRow, targetColumn)) continue;
				Piece targetPiece = board[targetRow][targetColumn];
				
				if (targetPiece != null) {
					continue;
				}
				//disallow two moves once the piece has moved at all, must set hasMoved to true in game for pawns
				if (this.hasMoved && Math.abs(move[0]) == 2) {
					continue;
				}
				legalMoves.add(new Move(r, c, targetRow, targetColumn, this.getType()));
			}
			
			
			for (int[] capture:pawnCaptures) {
				int targetRow = r+capture[0];
				int targetColumn = c+capture[1];
				//check if we are off the board and skip to next step in the for loop if we are
				if (!checkRange(targetRow, targetColumn)) continue; 
				Piece targetPiece = board[targetRow][targetColumn];
				//check if there is a piece in the space, if there isn't skip this move
				if (targetPiece == null) 
				{
					continue;
				//if there is, check if it is the opponent's piece, if it is, add it to the list
				} else if (board[targetRow][targetColumn].getColor() != this.getColor()) {
					legalMoves.add(new Move(r, c, targetRow, targetColumn, this.getType()));
				}
				
			}
			return legalMoves;
			}
		}
}


