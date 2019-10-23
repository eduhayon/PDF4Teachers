package fr.themsou.document.render;

import fr.themsou.document.editions.elements.Element;
import fr.themsou.main.Main;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PageRenderer extends Pane {

    private Image render;
    private ImageView renderView;
    private int page;
    private ArrayList<Element> elements = new ArrayList<>();
    public double mouseX = 0;
    public double mouseY = 0;

    public PageRenderer(java.awt.Image render, int page) {
        this.render = SwingFXUtils.toFXImage((BufferedImage) render, null);
        this.page = page;
        renderView = new ImageView(this.render);
        final double ratio = this.render.getHeight() / this.render.getWidth();

        setWidth(Main.mainScreen.pageWidthProperty().get());
        setHeight(Main.mainScreen.pageWidthProperty().get() * ratio);

        prefWidthProperty().bind(Main.mainScreen.pageWidthProperty());
        prefHeightProperty().bind(widthProperty().multiply(ratio));

        renderView.fitHeightProperty().bind(heightProperty());
        renderView.fitWidthProperty().bind(widthProperty());

        getChildren().add(renderView);

        setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                Main.mainScreen.document.setCurrentPage(page);
            }
        });

    }

    public void clearElements(){
        getChildren().clear();
        getChildren().add(renderView);
        elements = new ArrayList<>();
    }

    public void addElement(Element element){

        if(element != null){

            elements.add(element);
            getChildren().add((Control) element);

        }
    }
    public void removeElement(Element element){

        if(element != null){
            elements.remove(element);
            getChildren().remove((Control) element);
        }
    }

    public Image getRender() {
        return render;
    }
    public void setRender(Image render) {
        this.render = render;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public ArrayList<Element> getElements() {
        return elements;
    }
}