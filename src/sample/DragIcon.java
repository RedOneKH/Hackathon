package sample;

import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.CubicCurve;

public class DragIcon extends AnchorPane{
	
	@FXML AnchorPane root_pane;
	@FXML
	Label title;

	@FXML
	Label img;

	private DragIconType mType = null;
	public String filepath;

	public DragIcon() {
		
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/DragIcon.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
	}
	
	@FXML
	private void initialize() {}
	
	public void relocateToPoint (Point2D p) {

		//relocates the object to a point that has been converted to
		//scene coordinates
		Point2D localCoords = getParent().sceneToLocal(p);
		
		relocate ( 
				(int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
				(int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
			);

	}
	
	public DragIconType getType () { return mType; }
	
	public void setType (DragIconType type) {
		mType = type;
		
		getStyleClass().clear();
		getStyleClass().add("dragicon");
		getStyleClass().add("icon-grey");
		
		switch (mType) {
			case select:
				title.setText("Select");
                img.setGraphic(getImage("check-box"));
                break;
			case combine:
				title.setText("Combine");
                img.setGraphic(getImage("link"));

                break;
			case groupeby:
				title.setText("Groupe by");
                img.setGraphic(getImage("sigma"));

                break;
			case file:
				title.setText("File");
                img.setGraphic(getImage("file"));
                break;
		
		default:
		break;
		}
	}

	public void setLabel(String s) {
		title.setText(s);
	}

	public String getFile() {
		return title.getText();
	}

	public ImageView getImage(String name){
		InputStream input =getClass().getResourceAsStream("/" +name+".png");

		Image image = new Image(input);
		return new ImageView(image);

	}
}
