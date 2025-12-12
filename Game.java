//Submission by: Andrew McMullan
//Date: 12/11/2025 
//assignment: module 13, assignment 20
//Short description: primary class for the chess game, controls UI and also the main type

package chess;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Game extends Application{
		//make a grid layout 
		GridPane boardGrid = new GridPane();
		//label to show whose turn it is and the round number
		static Label turnLabel = new Label("Welcome to Chess\n The Game of Kings");
		
		static StackPane[][] square = new StackPane[8][8];
		static Board board = new Board();
		static Round round;
		static List<Move> history = new ArrayList<>();
		static String playerOne = "Gary Fisher";
		static String playerTwo = "Paul Morphy";
		static Button startButton = new Button("Start");
		static VBox root;
	public void startGame() {
		//clear the board just in case another match has already happened.
		for (int r = 0; r < 8; r++) {
		    for (int c = 0; c < 8; c++) {
		        square[r][c].getChildren().clear();
		        board.getArray()[r][c] = null;
		    }
		}

		//put pieces on the board, and in location array
		//black pawns row 1, white row 6
		for (int column = 0; column < 8; column++) {
			board.setPiece(1, column, new Piece.Pawn(PlayerColor.BLACK));
			square[1][column].getChildren().add(ImageAssets.outlineImage(board.getPiece(1,column))); 
			board.setPiece(6, column, new Piece.Pawn(PlayerColor.WHITE));
			square[6][column].getChildren().add(ImageAssets.outlineImage(board.getPiece(6,column)));
		}
		//kings
		board.setPiece(0, 3, new Piece.King(PlayerColor.BLACK));
		square[0][3].getChildren().add(ImageAssets.outlineImage(board.getPiece(0,3)));
		board.setPiece(7, 3, new Piece.King(PlayerColor.WHITE));
		square[7][3].getChildren().add(ImageAssets.outlineImage(board.getPiece(7,3)));
		//knights
		board.setPiece(0, 1, new Piece.Knight(PlayerColor.BLACK));
		square[0][1].getChildren().add(ImageAssets.outlineImage(board.getPiece(0,1)));
		board.setPiece(0, 6, new Piece.Knight(PlayerColor.BLACK));
		square[0][6].getChildren().add(ImageAssets.outlineImage(board.getPiece(0,6)));
		board.setPiece(7, 1, new Piece.Knight(PlayerColor.WHITE));
		square[7][1].getChildren().add(ImageAssets.outlineImage(board.getPiece(7,1)));
		board.setPiece(7, 6, new Piece.Knight(PlayerColor.WHITE));
		square[7][6].getChildren().add(ImageAssets.outlineImage(board.getPiece(7,6)));
		//bishops
		board.setPiece(0, 2, new Piece.Bishop(PlayerColor.BLACK));
		square[0][2].getChildren().add(ImageAssets.outlineImage(board.getPiece(0,2)));
		board.setPiece(0, 5, new Piece.Bishop(PlayerColor.BLACK));
		square[0][5].getChildren().add(ImageAssets.outlineImage(board.getPiece(0,5)));
		board.setPiece(7, 2, new Piece.Bishop(PlayerColor.WHITE));
		square[7][2].getChildren().add(ImageAssets.outlineImage(board.getPiece(7,2)));
		board.setPiece(7, 5, new Piece.Bishop(PlayerColor.WHITE));
		square[7][5].getChildren().add(ImageAssets.outlineImage(board.getPiece(7,5)));
		//queens
		board.setPiece(0, 4, new Piece.Queen(PlayerColor.BLACK));
		square[0][4].getChildren().add(ImageAssets.outlineImage(board.getPiece(0,4)));
		board.setPiece(7, 4, new Piece.Queen(PlayerColor.WHITE));
		square[7][4].getChildren().add(ImageAssets.outlineImage(board.getPiece(7,4)));
		//rooks
		board.setPiece(0, 0, new Piece.Rook(PlayerColor.BLACK));
		square[0][0].getChildren().add(ImageAssets.outlineImage(board.getPiece(0,0)));
		board.setPiece(0, 7, new Piece.Rook(PlayerColor.BLACK));
		square[0][7].getChildren().add(ImageAssets.outlineImage(board.getPiece(0,7)));
		board.setPiece(7, 0, new Piece.Rook(PlayerColor.WHITE));
		square[7][0].getChildren().add(ImageAssets.outlineImage(board.getPiece(7,0)));
		board.setPiece(7, 7, new Piece.Rook(PlayerColor.WHITE));
		square[7][7].getChildren().add(ImageAssets.outlineImage(board.getPiece(7,7)));
		
		round = new Round();
		turnLabel.setText(String.format("Current Round: %d\n%s's turn.", round.getTurnCount(), getPlayer(round.getSideToMove()) ));
	}
	//for checking if a king is in check while validating a given move and for checkmate
	
	
	
	public void gameOver() {
		root.getChildren().addAll(startButton);
		//turnLabel.setText("Welcome to Chess\nThe Game of King's");
		
	}
	
	
	public static boolean isKingInCheck(PlayerColor side, Piece[][] board) {
		int kingsRow = -1;
		int kingsColumn = -1;
		
		outer:
		for (int r = 0; r < 8; r++) {
			for (int c = 0;c<8; c++) {
				Piece p = board[r][c];
				if (p != null && p.getType() == PieceType.KING && p.getColor() == side) {
					kingsRow = r;
					kingsColumn = c;
					break outer;//saves some work, we exit the loop once king is found
				}
			}
		}
		PlayerColor opponent = side.opposite();
		
		for (int r = 0; r < 8; r++) {
			for (int c = 0;c<8; c++) {
				Piece p = board[r][c];
				if (p != null && p.getColor() == opponent) {
					for (Move m : p.getPotentialMoves(r, c, board)) {
						if (m.toRow == kingsRow && m.toColumn == kingsColumn) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	//check for checkmate
	public boolean hasEscape(PlayerColor side, Piece[][] board) {
		 for (int r = 0; r < 8; r++) {
			 for (int c = 0; c < 8; c++) {
		       	Piece p = board[r][c];
		       	if (p == null || p.getColor() != side) continue;
		       	
		       	for (Move m : p.getPotentialMoves(r,  c, board)) {
		       		Piece captured = board[m.toRow][m.toColumn];
		       		board[m.toRow][m.toColumn] = p;
		       		board[r][c] = null;
		        		
		       		boolean stillInCheck = isKingInCheck(side, board);
		        		
		       		//undo move
		       		board[r][c] = p;
		       		board[m.toRow][m.toColumn] = captured;
		        		
		       		if (!stillInCheck) {
		       			return true;//found at least one way out.
		        	}
		       	}
			 }
		 }
		
		
		return false;
	}
	//return name of PlayerColor player
	public String getPlayer(PlayerColor color) {
		return color == PlayerColor.WHITE ? playerOne : playerTwo;
	}
	
	public static int selectedRow = -1;
	public static int selectedColumn = -1;
	
	public void selectSquare(int row, int column) {
		square[row][column].getStyleClass().add("selected-square");

	}
	public static void showLegalMoves(List<Move> moves) {
		for (Move move:moves) {
			int r = move.toRow;
			int c = move.toColumn;
			
			Circle dot = new Circle(8);
			dot.setFill(Color.rgb(0, 0, 0, 0.3));
			dot.setMouseTransparent(true);
			square[r][c].getChildren().add(dot);
			
			}
		}
	
	
	//clear selection UI
	public void clearSelection() {
		selectedRow = -1;
		selectedColumn = -1;
		for (int row = 0;row<8;row++) {
			for (int col = 0;col<8;col++) {
				//remove dots from showlegalmoves
				square[row][col].getChildren().removeIf(node -> node instanceof Circle);
				//remove highlighting
				square[row][col].getStyleClass().removeAll(
						"selected-square",
						"highlight-square");
			}
		}
	}
	
	//for performing the move everywhere and incrementing round
	 
	public void executeMove(Move move) {
		Piece[][] boardState = board.getArray();
		
		Piece moving = boardState[move.fromRow][move.fromColumn];
		Piece captured = boardState[move.toRow][move.toColumn];
		
		boardState[move.toRow][move.toColumn] = moving;
		square[move.toRow][move.toColumn].getChildren().clear();
		square[move.toRow][move.toColumn].getChildren().add(ImageAssets.outlineImage(board.getPiece(move.toRow,move.toColumn)));
		boardState[move.fromRow][move.fromColumn] = null;
		square[move.fromRow][move.fromColumn].getChildren().clear();
		
		moving.setHasMoved(true);
		
		round.next();
		turnLabel.setText(String.format("Current Round: %d\n%s's turn.", round.getTurnCount(), getPlayer(round.getSideToMove()) ));
		//record a Move() object with all the info we have on this move.
		history.add(new Move(move.fromRow, 
							move.fromColumn, 
							move.toRow, 
							move.toColumn, 
							moving.getType(), 
							captured == null ? null : captured.getType(),//so we don't crash if there is no captured piece. 
							round.getTurnCount(), 
							round.getSideToMove()));
							
		clearSelection();
		
		//checkmate detection
		boolean inCheck = isKingInCheck(round.getSideToMove(),board.getArray());
		boolean escape = hasEscape(round.getSideToMove(), board.getArray());
		
		if (inCheck && !escape) {
			//checkmate
			turnLabel.setText(String.format("Current Round: %d\nCheckmate! %s wins!", round.getTurnCount(), getPlayer(round.getSideToMove().opposite())));
			gameOver();
		} else if (!inCheck && !escape) {
			//stalemate
			turnLabel.setText(String.format("Current Round: %d\nStalemate! We're all winners!", round.getTurnCount(), getPlayer(round.getSideToMove().opposite())));
			gameOver();
		} else if (inCheck) {
			//just in check
			turnLabel.setText(String.format("Current Round: %d\n%s's turn, watch out, you are in Check!", round.getTurnCount(), getPlayer(round.getSideToMove()) ));
		}
	}
	
	
	//this is for handling any clicks on the board, 
	public void handleClickOnSquare(int row, int column) {
		Piece clicked = board.getPiece(row, column);
		
		//if nothing selected yet
		if (selectedRow == -1) {
			if (clicked == null) return;
			//below is for when we have rounds and we want only one color of pieces selectable
			if (clicked.getColor() != round.getSideToMove()) return;
			clearSelection();
			selectSquare(row, column);
			selectedRow = row;
			selectedColumn = column;
			//also show legal moves here based on what is selected
			showLegalMoves(board.getPiece(row,column).getLegalMoves(selectedRow, selectedColumn, board.board, board.getPiece(row, column).getColor()));
			return;
		}
		
		//something is already selected, 
		
		Piece selectedPiece = board.getPiece(selectedRow, selectedColumn);
		for (Move move : selectedPiece.getLegalMoves(selectedRow, selectedColumn, board.board, selectedPiece.getColor())) {
			if (move.toRow == row && move.toColumn == column) {
				//do the move, need a method for this
				executeMove(move);
				clearSelection();
				return;
			}
		}
		
		
		//something is selected and the thing we click on is another of our own piece
		//change selection to the new piece
		
		if (clicked != null && clicked.getColor() == round.getSideToMove()) {
			clearSelection();
			selectSquare(row, column);
			selectedRow = row;
			selectedColumn = column;
			showLegalMoves(board.getPiece(row,column).getLegalMoves(row, column, board.board, board.getPiece(row, column).getColor()));
			return;
		}
		
		
		//last case, empty square is clicked while one of sideToMove's pieces is selected, deselect all
		clearSelection();
	}
	
	//UI STUFF
	
	
	
	@Override
	public void start(Stage primaryStage) {
		
		
		//load images, to be viewed later, now in ImageAssets
		
		//populate the gridpane with stackPanes of alternating colors
		for (int row=0;row<8;row++) {
			for (int col=0;col<8;col++) {
				square[row][col] = new StackPane();
				square[row][col].setPrefSize(60,60);
				//need to reset these values of row and col because the compiler doesn't like lambda working on values that may change later 
				int finalRow = row;
				int finalColumn = col;
				square[row][col].setOnMouseClicked(e -> handleClickOnSquare(finalRow, finalColumn));
				//color the squares
				boolean light = (row+col) % 2 == 0;
				square[row][col].getStyleClass().add(light ? "light-square" : "dark_square");
				//add them to the grid
				boardGrid.add(square[row][col],col,row);
				
			}
		}
		//put a border on the board of squares
		boardGrid.setStyle("-fx-border-color: black; -fx-border-width: 3;");
		//restrict our board to only be as big as it needs to be, otherwise it extends to the window edge
		boardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		
	
		
		//Label to show whose turn it is and the current round
		//defined at class level so other methods and classes can write to it.
		
		//text boxes for player one and two
		TextField p1NameField = new TextField();
		TextField p2NameField = new TextField();
		
		//button to start game
		
		
		
		HBox nameBox = new HBox(100,p1NameField,p2NameField); 
		nameBox.setAlignment(Pos.CENTER);
		root = new VBox(turnLabel,boardGrid,nameBox, startButton);
		root.setAlignment(Pos.CENTER);
		
		startButton.setOnAction(e -> {
			if (p1NameField.getText() != "") {
				playerOne = p1NameField.getText();
			}
			if (p1NameField.getText() != "") {
				playerTwo = p2NameField.getText();
			}
			startGame();
			root.getChildren().removeAll(nameBox,startButton);
		});
		
		
		
		Scene scene = new Scene(root, 800, 640);
		scene.getStylesheets().add("file:ChessStyle.css");
		primaryStage.setTitle("Chess");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
