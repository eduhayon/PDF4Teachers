package fr.themsou.document.render;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import fr.themsou.document.editions.Edition;
import fr.themsou.document.editions.elements.Element;
import fr.themsou.document.editions.elements.TextElement;
import fr.themsou.main.Main;
import fr.themsou.utils.Hand;
import fr.themsou.utils.Location;

public class EditRender {
	
	private int width;
	private int height;
	public Element current = null;
	public Hand hand = null;

	private Edition edition;




	public EditRender(Edition edition, int width, int height) {
		this.edition = edition;
		this.width = width;
		this.height = height;
	}

	public Image render(Image img, int page, int mouseX, int mouseY){
		
		BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);

		if(Main.leftBarText.elementToEdit != null && Main.leftBarText.elementToEdit.getPage() == page){
			if(Main.leftBarText.elementToEdit.getMargin() == null){
				Main.leftBarText.elementToEdit.paint(g, mouseX, mouseY);
			}
			verifyLoc(this, Main.leftBarText.elementToEdit);
		}

		for(int i = 0; i < edition.elements.size(); i++){
			if(edition.elements.get(i).getPage() == page){
				
				boolean on = edition.elements.get(i).paint(g, mouseX, mouseY);
				if(on){
					current = edition.elements.get(i);
				}
			}
		}
		if(!((mouseY > img.getHeight(null) && (page+1) != edition.document.rendered.length) || (mouseY < 0 && page != 0))){ // mouse on

			mouse(this, current, g, page, mouseX, mouseY);
			if(hand != null)
				hand.getElement().paint(g, mouseX, mouseY);
			if(edition.document.currentPage != page){
				edition.document.currentPage = page;
				Main.footerBar.repaint();
			}
		}
		
		g.dispose();
		return bimg;
		
	}


	public void mouse(EditRender page, Element element, Graphics2D g, int pageNumber, int mouseX, int mouseY){

		if(hand == null && Main.click && element != null){ // Ajouter

			hand = new Hand(element, element.getLocation().substractValues(new Location(mouseX, mouseY)), element.getPage());
			edition.removeElement(element);
			if(element instanceof TextElement)
				Main.leftBarText.selectTextElement(element);

		}else if(hand != null){ // Déposer - Déplacer

			if(hand.getElement().getMargin() == null){
				hand.getElement().paint(g, mouseX, mouseY);
			}

			hand.setPage(pageNumber);
			hand.setLoc(new Location(mouseX, mouseY).additionValues(hand.getShift()));
			verifyLoc(page, hand.getElement());

			if(!Main.click){ // Déposer

				edition.addElement(hand.getElement());
				hand.getElement().paint(g, mouseX, mouseY);
				hand = null;
			}
		}
	}
	public void verifyLoc(EditRender page, Element element){

		Location minLoc = element.getLocation().substractValues(element.getMargin());
		Location maxLoc = element.getLocation().additionValues(element.getMargin());

		if(minLoc.getX() < 0) element.setX(element.getMargin().getX());
		if(maxLoc.getX() > page.getWidth()) element.setX(page.getWidth() - element.getMargin().getX());

		if(minLoc.getY() < 0) element.setY(element.getMargin().getY());
		if(maxLoc.getY() > page.getHeight()) element.setY(page.getHeight() - element.getMargin().getY());

	}


	public int getHeight(){
		return height;
	}
	public int getWidth(){
		return width;
	}

}
