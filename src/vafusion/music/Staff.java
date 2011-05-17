package vafusion.music;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Staff {
	private Line2D.Double[] lines;
	private Graphics2D g2d;
	private List<Measure> measures;
	private int width, height, x, y;
	private vafusion.data.Line staffData;
	private BufferedImage clef;
	private static BufferedImage bass;
	private static BufferedImage treble;
	
	public Staff(int x, int y, int width, int height, int clef){
		this.lines = new Line2D.Double[5];
		this.width = width;
		this.height = height;
		staffData = new vafusion.data.Line(x, y, width, height, clef);
		this.x = x;
		this.y = y;
		
		for(int i = 0; i< 5; i ++){
			this.lines[i] = new Line2D.Double(x, y + (height/5)*i, x + width, y + (height/5)*i);
		}
		
		if(bass == null){
			try {
				bass = loadImage(new File("img/Notes/bassclef.gif"));
				treble = loadImage(new File("img/Notes/treble-clef.gif"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(clef == 1){
			this.clef = treble;
		}else{
			this.clef = bass;
		}
		
		measures = new ArrayList<Measure>();
		
	}
	
	public void paint(Graphics g){
		this.g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		
		for(Line2D.Double line : this.lines){
			g2d.draw(line);
		}
		
		g2d.drawImage(clef, null, x, y);
		
		for(Measure m : measures)
			m.paint(g2d);
		
		measures.clear();
		
	}
	
	public boolean addMeasure(Measure m) {
		
		int measureWidthTotal = 0;
		for(Measure i : measures)
			measureWidthTotal += i.getWidth();
		
		if(m.getWidth() + measureWidthTotal > width)
			return false;
		else {
			
			measures.add(m);
			return true;
		
		}
			
		
	}
	
	public void update() {
		
		int currX = this.staffData.getX() + this.clef.getWidth() + 10;
		//System.out.println(measures.size());
		for(Measure m : measures) {
			
			m.update(currX, staffData.getY(), this.height);
			currX += m.getWidth();
			
		}
		
	}
	
	public int getLineSeparation() {
		
		return height / 5;
		
	}
	
	public static BufferedImage loadImage(File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		return image;
	}
	
	public int getX() {
		
		return this.x;
		
	}
	
	public int getY() {
		
		return this.y;
		
	}
	
	public int getWidth() {
		
		return width;
		
	}
	
	public int getHeight() {
		
		return height;
		
	}
	
	public vafusion.music.Note getNote(int x) {
		
		System.out.println("Staff.getNote x: " + x);
		//figure out which measure the note is in
		for(Measure m : measures)
			if(x >= m.getX() && x <= m.getX() + m.getWidth())
				return m.getNote(x);
		
		return null;
		
	}

	public void clearMeasures() {
		
		measures.clear();
		
	}
}

