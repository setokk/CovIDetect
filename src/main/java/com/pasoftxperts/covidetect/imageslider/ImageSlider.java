package com.pasoftxperts.covidetect.imageslider;

import com.pasoftxperts.covidetect.RunApplication;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ImageSlider
{
    int j = 0;
    double orgCliskSceneX, orgReleaseSceneX;
    Button lbutton, rButton;
    ImageView imageView;

    public GridPane createImageSlider()
    {
        // images in src folder.
        try {
            GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);

            lbutton = new Button("<");
            rButton = new Button(">");

            lbutton.setStyle("-fx-background-color: #323232; -fx-text-fill: white;");
            rButton.setStyle("-fx-background-color: #323232; -fx-text-fill: white;");

            Image[] images = new Image[2];
            images[0] = new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectLogo.jpg"));
            images[1] = new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectLogoTransparent.png"));


            imageView = new ImageView(images[j]);
            imageView.setCursor(Cursor.CLOSED_HAND);

            EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgCliskSceneX = t.getSceneX();
                }
            };

            imageView.setOnMousePressed(circleOnMousePressedEventHandler);

            imageView.setOnMouseReleased(e -> {
                orgReleaseSceneX = e.getSceneX();
                if (orgCliskSceneX > orgReleaseSceneX) {
                    lbutton.fire();
                } else {
                    rButton.fire();
                }
            });

            rButton.setOnAction(e -> {
                j = j + 1;
                if (j == images.length) {
                    j = 0;
                }
                imageView.setImage(images[j]);

            });
            lbutton.setOnAction(e -> {
                j = j - 1;
                if (j == 0 || j > images.length + 1 || j == -1) {
                    j = images.length - 1;
                }
                imageView.setImage(images[j]);

            });

            imageView.setFitHeight(300);
            imageView.setFitWidth(500);

            HBox hBox = new HBox();
            hBox.setSpacing(15);
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(lbutton, imageView, rButton);

            root.add(hBox, 1, 1);

            return root;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
