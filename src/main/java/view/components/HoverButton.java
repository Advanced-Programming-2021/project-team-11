package view.components;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.menus.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HoverButton extends StackPane implements Initializable {
    private static final int HOVER_DURATION = 200;
    private double initialHeight, initialWidth, hoverHeight, hoverWidth;
    private int fontSize = 44;
    @FXML
    private ImageView imageView;
    @FXML
    private Text text;

    public HoverButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hover_button.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.setClassLoader(getClass().getClassLoader());
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(double initialHeight) {
        imageView.setFitHeight(initialHeight);
        super.setMaxHeight(initialWidth);
        this.initialHeight = initialHeight;
    }

    public double getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(double initialWidth) {
        imageView.setFitWidth(initialWidth);
        super.setMaxWidth(initialWidth);
        this.initialWidth = initialWidth;
    }

    public double getHoverHeight() {
        return hoverHeight;
    }

    public void setHoverHeight(double hoverHeight) {
        this.hoverHeight = hoverHeight;
    }

    public double getHoverWidth() {
        return hoverWidth;
    }

    public void setHoverWidth(double hoverWidth) {
        this.hoverWidth = hoverWidth;
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public void setImage(Image image) {
        this.imageView.setImage(image);
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOnMouseEntered(x -> onMouseEntered());
        setOnMouseExited(x -> onMouseExited());
        imageView.setPreserveRatio(true);
        text.setFont(new Font("Times Roman", fontSize));
        text.setFill(Color.valueOf("#151b37"));
    }

    private void onMouseEntered() {
        final ImageView image = this.imageView;
        image.setImage(Assets.SELECTED_BUTTON_IMAGE);
        final double imageInitWidth = image.getFitWidth(), imageInitHeight = image.getFitHeight();
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(HOVER_DURATION));
            }

            protected void interpolate(double frac) {
                image.setFitHeight(imageInitHeight + frac * (hoverHeight - imageInitHeight));
                image.setFitWidth(imageInitWidth + frac * (hoverWidth - imageInitWidth));
            }
        };
        animation.play();
        SceneChanger.getScene().setCursor(Cursor.HAND);
    }

    private void onMouseExited() {
        final ImageView image = this.imageView;
        image.setImage(Assets.BUTTON_IMAGE);
        final double imageInitWidth = image.getFitWidth(), imageInitHeight = image.getFitHeight();
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(HOVER_DURATION));
            }

            protected void interpolate(double frac) {
                image.setFitHeight(imageInitHeight - frac * (imageInitHeight - initialHeight));
                image.setFitWidth(imageInitWidth - frac * (imageInitWidth - initialWidth));
            }
        };
        animation.play();
        SceneChanger.getScene().setCursor(Cursor.DEFAULT);
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        if (fontSize == 0)
            fontSize = 44;
        this.fontSize = fontSize;
        this.text.setFont(new Font("Times Roman", fontSize));
    }
}
