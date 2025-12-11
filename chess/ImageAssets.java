package chess;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ImageAssets {
	
		private ImageAssets() {}//disallow construction
		
		
			//load images, to be viewed later
			static final Image WHITE_PAWN  = new Image("file:images/WHITE_PAWN.png");
			static final Image BLACK_PAWN  = new Image("file:images/BLACK_PAWN.png");
			static final Image WHITE_ROOK  = new Image("file:images/WHITE_ROOK.png");
			static final Image BLACK_ROOK  = new Image("file:images/BLACK_ROOK.png");
			static final Image WHITE_KNIGHT = new Image("file:images/WHITE_KNIGHT.png");
			static final Image BLACK_KNIGHT = new Image("file:images/BLACK_KNIGHT.png");
			static final Image WHITE_BISHOP = new Image("file:images/WHITE_BISHOP.png");
			static final Image BLACK_BISHOP = new Image("file:images/BLACK_BISHOP.png");
			static final Image WHITE_QUEEN = new Image("file:images/WHITE_QUEEN.png");
			static final Image BLACK_QUEEN = new Image("file:images/BLACK_QUEEN.png");
			static final Image WHITE_KING = new Image("file:images/WHITE_KING.png");
			static final Image BLACK_KING = new Image("file:images/BLACK_KING.png");
			
			//for making pieces outlined on same color squares, not really needed since the color change, but I'll leave it.
			public static ImageView outlineImage(Piece p) {
				Image img = getImageFor(p);
				ImageView iv = new ImageView(img);
				
				DropShadow outline = new DropShadow();
				outline.setColor(Color.WHITE);   // outline color
				outline.setRadius(2);            // thickness
				outline.setSpread(0.1);          // important: makes it a hard outline
				outline.setOffsetX(0);           // center the effect
				outline.setOffsetY(0);
				iv.setEffect(outline);
				
				return iv;
			}

			//for supplying the correct Image to our outline method above
			public static Image getImageFor(Piece piece) {
			    return switch (piece.getType()) {
			        case PAWN   -> (piece.getColor() == PlayerColor.WHITE ? WHITE_PAWN  : BLACK_PAWN);
			        case ROOK   -> (piece.getColor() == PlayerColor.WHITE ? WHITE_ROOK  : BLACK_ROOK);
			        case KNIGHT -> (piece.getColor() == PlayerColor.WHITE ? WHITE_KNIGHT: BLACK_KNIGHT);
			        case BISHOP -> (piece.getColor() == PlayerColor.WHITE ? WHITE_BISHOP: BLACK_BISHOP);
			        case QUEEN  -> (piece.getColor() == PlayerColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN);
			        case KING   -> (piece.getColor() == PlayerColor.WHITE ? WHITE_KING  : BLACK_KING);
			    };
			}
			
}
