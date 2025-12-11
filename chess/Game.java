

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
		
	public void startGame() {
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
		
		//queens
		
		//rooks
		
		round = new Round();
		turnLabel.setText(String.format("Current Round: %d\n%s's turn.", round.getTurnCount(), getPlayer(round.getSideToMove()) ));
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
			showLegalMoves(board.getPiece(row,column).getLegalMoves(selectedRow, selectedColumn, board.board));
			return;
		}
		
		//something is already selected, 
		
		Piece selectedPiece = board.getPiece(selectedRow, selectedColumn);
		for (Move move : selectedPiece.getLegalMoves(selectedRow, selectedColumn, board.board)) {
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
			showLegalMoves(board.getPiece(row,column).getLegalMoves(row, column, board.board));
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
		Button startButton = new Button("Start");
		
		
		HBox nameBox = new HBox(100,p1NameField,p2NameField); 
		nameBox.setAlignment(Pos.CENTER);
		VBox root = new VBox(turnLabel,boardGrid,nameBox, startButton);
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

