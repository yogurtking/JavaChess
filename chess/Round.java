package chess;

public class Round {
	private PlayerColor sideToMove;
	private int ply;//basically half a full game round
	private int fullTurnCount;//each full turn both players have moved once
	
	public Round() {
		sideToMove = PlayerColor.WHITE;
		this.ply = 1;
		this.fullTurnCount = 1;
	}
	
	public void next() {
		
		sideToMove = sideToMove.opposite();
		ply++;
		if (sideToMove == PlayerColor.WHITE) {
			fullTurnCount++;
		}
		
	}
	
	public PlayerColor getSideToMove() {
		return sideToMove;
	}
	
	public int getTurnCount() {
		return this.fullTurnCount;
	}
	
}
