package br.com.ritcher.pt2view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageExport {
	private ScorePanel panel;
	private File outputfile;
	
	

	public ImageExport(ScorePanel panel, File outputfile) {
		super();
		this.panel = panel;
		this.outputfile = outputfile;
	}


	public void export() throws IOException {
	    int w = panel.getWidth();
	    int h = panel.getHeight();
	    
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    panel.paint(g);
	    g.dispose();
	    ImageIO.write(bi, "png", outputfile);
	}
	
	
	public void setOutputfile(File outputfile) {
		this.outputfile = outputfile;
	}
	
	public void setPanel(ScorePanel panel) {
		this.panel = panel;
	}
}
