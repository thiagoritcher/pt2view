package br.com.ritcher.pt2view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;

import br.com.ritcher.pt2view.data.MusicSystem;
import br.com.ritcher.pt2view.data.Note;
import br.com.ritcher.pt2view.data.Position;
import br.com.ritcher.pt2view.data.Score;
import br.com.ritcher.pt2view.data.Stave;
import br.com.ritcher.pt2view.data.Voice;

public class ScorePanel extends JPanel {
	
	private Score score;
	private int timeFactor = 100;
	
	public void setTimeFactor(int timeFactor) {
		this.timeFactor = timeFactor;
		this.repaint();
		this.getParent().invalidate();
	}
	
	public int getTimeFactor() {
		return timeFactor;
	}
	
	public void setScore(Score score) {
		this.score = score;
		this.repaint();
		this.getParent().invalidate();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		
		if(score == null){
			return;
		}
		
		int maxx = 0;
		
		Graphics2D g = (Graphics2D) g0;
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Color lineColor = Color.LIGHT_GRAY, systemColor = Color.red, textColor = Color.gray, timeColor=Color.gray,  backColor = Color.white;
		ColorTable colorTable = new ColorTable();
		
		int y = 30;
		int width = getWidth();
		int line_h = 10;
		int stave_h = 40;
		int system_h = 50;
		int pos_w = 10;
		int x_start = 20;
		int x = x_start;
		
		
		
		Font fontDefault = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		Font fontTime = new Font(Font.SANS_SERIF, Font.PLAIN, 9);
		Font fontTitle = new Font(Font.SANS_SERIF, Font.BOLD, 14);
		
		pos_w =(int)(g.getFontMetrics(fontDefault).stringWidth("24") * (timeFactor/100.0));
		
		
		
		x-= 10;
		g.setFont(fontTitle);
		g.setColor(textColor);
		try {
			if(score.score_info.songData.title != null){
				g.drawString(score.score_info.songData.title, x, y);
				y += g.getFontMetrics().getHeight();
				
				if(score.score_info.songData.audio_release_info != null){
					g.setFont(fontDefault);
					g.drawString(Integer.toString(score.score_info.songData.audio_release_info.year), x, y);
					y += g.getFontMetrics().getHeight()*.8;
				}
				g.setColor(lineColor);
				g.drawLine(0, y, width, y);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		y += 40;
		
		
		g.setFont(fontDefault);
		
		FontMetrics fm = g.getFontMetrics();
		int font_h = fm.getHeight();
		
		int systemIndex = 1;
		Iterator<MusicSystem> systems = score.systems.iterator();
		while (systems.hasNext()) {
			MusicSystem system = (MusicSystem) systems.next();
			Iterator<Stave> staves = system.staves.iterator();
			int staveIndex = 1;
			
			x = x_start;
			
			
			
			while (staves.hasNext()) {
				int y0 = y;
				
				x = x_start;
				g.setColor(textColor);
				g.setFont(fontDefault);
				g.drawString("Score " + staveIndex, x, y - line_h);
				
				
				Stave stave = (Stave) staves.next();
				for (int i = 0; i < stave.string_count; i++) {
					y += line_h;
					g.setColor(lineColor);
					g.drawLine(0, y, width, y);	
				}
				
				Iterator<Voice> voices = stave.voices.iterator();
				while (voices.hasNext()) {
					Voice voice = (Voice) voices.next();
					
					g.setColor(lineColor);
					g.drawLine(x, y0, x, y);
					
					x += pos_w;
					
					for (int i = 0; i < voice.positions.size(); i++) {
						Position position = (Position) voice.positions.get(i);
						Position nextPosition = null;
						if(i < voice.positions.size() -2){
							nextPosition = voice.positions.get(i + 1);
						}
						
						String key = "";
							
						
						ArrayList<StringBox> stringBoxList = new ArrayList<StringBox>();
						Iterator<Note> notes = position.notes.iterator();
						while (notes.hasNext()) {
							Note note = (Note) notes.next();
							String fret = Integer.toString(note.fret);
							//w = Math.max(w, fm.stringWidth(fret));
							
							key += "" + note.string + note.fret;
							
							int yfret = y0 + (note.string * line_h) + (int) (font_h * .9);
							Note nextNote = null;
							if(nextPosition != null){
								nextNote = nextPosition.getNoteOnString(note.string);
							}
							
							fret += getNoteProperties(note, nextNote);
							
							StringBox sb = new StringBox();
							sb.string = fret;
							sb.x = x;
							sb.y = yfret;
							
							
							
							sb.xb = x - 1;
							sb.yb = yfret - (int)(.7 *font_h);
							sb.wb = fm.stringWidth(fret) + 2;
							sb.hb = (int)(font_h * .85);
							stringBoxList.add(sb);
						}
						
						Iterator<StringBox> stringBoxIterator = stringBoxList.iterator();
						ColorBox  colorBox = colorTable.colorFor(key);
						
						while (stringBoxIterator.hasNext()) {
							StringBox sb = (StringBox) stringBoxIterator.next();
							
							g.setColor(colorBox.bg);
							g.fillRect(sb.xb, sb.yb, sb.wb, sb.hb);
						}

						g.setFont(fontDefault);
						stringBoxIterator = stringBoxList.iterator();
						g.setColor(colorBox.text);
						while (stringBoxIterator.hasNext()) {
							StringBox stringBox = (StringBox) stringBoxIterator.next();
							g.drawString(stringBox.string, stringBox.x, stringBox.y);
						}
						
						int x0 = x;
						
						
						
						x += pos_w * 16 / position.duration;
						
						g.setColor(backColor);
						double fac = .1;
						int xline = (int)(x0 - pos_w * fac);
						
						g.drawLine(xline, y0,xline, y);
						
						g.setColor(timeColor);
						g.setFont(fontTime);
						fac = 0;
						g.drawString(getDuration(position.duration), x0,(int)( y0 + line_h *fac));
						
						
						
						
						
					}
					x += pos_w;
					
					
				}
				y += stave_h;
				
				staveIndex++;
			}
			g.setColor(systemColor);

			g.setFont(fontDefault);
			g.drawString("System " + Integer.toString(systemIndex), getWidth() - 60, y);
			systemIndex++;
			y += font_h * .8;
			
			g.setColor(systemColor);
			g.drawLine(0, y, width, y);
			y += system_h;
			
			maxx = Math.max(maxx, x);
		}
		setPreferredSize(new Dimension(maxx, y));
	}

	private String getNoteProperties(Note note, Note nextNote) {
		String props = "";
		//00010000000000000
		if(note.properties.charAt(3) == '1'){
			//Slide
			if(nextNote == null || nextNote.fret > note.fret){
				props += "/";
			}
			else {
				props += "\\";
			}
		}
		//00000000000000100
		if(note.properties.charAt(14) == '1'){
			//Slide
			if(nextNote == null || nextNote.fret > note.fret){
				props += "h";
			}
			else {
				props += "p";
			}
		}
		return props;
	}

	private String getDuration(int duration) {
		switch(duration){
			case 1:
				return "1";
			case 2:
				return "2";
			case 4:
				return "4";
			case 8:
				return "8";
			case 16:
				return "16";
			case 32:
				return "32";
			case 64:
				return "64";
		}
		return null;
	}
}

class StringBox {
	String string;
	int x, y, xb, yb, wb, hb;
}

class ColorBox {
	Color text, bg;
}

class ColorTable {
	
	HashMap<String, ColorBox> colorMap = new HashMap<String, ColorBox>();
	
	String[][] colors = {{"Acid","#B0BF1A","38"},
			{"Aero","#7CB9E8","70"},
			{"Aero blue","#C9FFE5","89"},
			{"Air superiority blue","#72A0C1","60"},
			{"Alabama crimson","#AF002A","34"},
			{"Alabaster","#EDEAE0","90"},
			{"Alice blue","#F0F8FF","97"},
			{"Almond","#EFDECD","87"},
			{"Amazon","#3B7A57","36"},
			{"Amethyst","#9966CC","60"},
			{"Anti-flash white","#F2F3F4","95"},
			{"Antique brass","#CD9575","63"},
			{"Antique bronze","#665D1E","26"},
			{"Antique ruby","#841B2D","31"},
			{"Antique white","#FAEBD7","91"},
			{"Ao (English)","#008000","25"},
			{"Apple green","#8DB600","36"},
			{"Apricot","#FBCEB1","84"},
			{"Aquamarine","#7FFFD4","75"},
			{"Army green","#4B5320","23"},
			{"Arylide yellow","#E9D66B","67"},
			{"Ash gray","#B2BEB5","72"},
			{"Atomic tangerine","#FF9966","70"},
			{"Avocado","#568203","26"},
			{"Azure mist","#F0FFFF","97"},
			{"Baby blue","#89CFF0","74"},
			{"Baby blue eyes","#A1CAF1","79"},
			{"Baby pink","#F4C2C2","86"},
			{"Baby powder","#FEFEFA","99"},
			{"Baker-Miller pink","#FF91AF","78"},
			{"Banana Mania","#FAE7B5","85"},
			{"Barn red","#7C0A02","25"},
			{"Beau blue","#BCD4E6","82"},
			{"Beige","#F5F5DC","91"},
			{"B'dazzled blue","#2E5894","38"},
			{"Big dip o’ruby","#9C2542","38"},
			{"Big Foot Feet","#E88E5A","63"},
			{"Bisque","#FFE4C4","88"},
			{"Bistre","#3D2B1F","18"},
			{"Bistre brown","#967117","34"},
			{"Bittersweet","#FE6F5E","68"},
			{"Black","#000000","0"},
			{"Black bean","#3D0C02","12"},
			{"Black chocolate","#1B1811","9"},
			{"Black coffee","#3B2F2F","21"},
			{"Black coral","#54626F","38"},
			{"Black olive","#3B3C36","22"},
			{"Black Shadows","#BFAFB2","72"},
			{"Blanched almond","#FFEBCD","90"},
			{"Blizzard blue","#ACE5EE","80"},
			{"Blond","#FAF0BE","86"},
			{"Blood red","#660000","20"},
			{"Blue (Munsell)","#0093AF","34"},
			{"Blue (NCS)","#0087BD","37"},
			{"Blue (Pantone)","#0018A8","33"},
			{"Blue (pigment)","#333399","40"},
			{"Blue bell","#A2A2D0","73"},
			{"Blue-gray","#6699CC","60"},
			{"Blue-green","#0D98BA","39"},
			{"Blue-green (color wheel)","#064E40","17"},
			{"Blue jeans","#5DADEC","65"},
			{"Blue sapphire","#126180","29"},
			{"Blue-violet (color wheel)","#4D1A7F","30"},
			{"Blush","#DE5D83","62"},
			{"Bole","#79443B","35"},
			{"Bondi blue","#0095B6","36"},
			{"Bone","#E3DAC9","84"},
			{"Bottle green","#006A4E","21"},
			{"Brandy","#87413F","39"},
			{"Bright lilac","#D891EF","75"},
			{"Brilliant rose","#FF55A3","67"},
			{"Brink pink","#FB607F","68"},
			{"British racing green","#004225","13"},
			{"Brown","#88540B","29"},
			{"Brunswick green","#1B4D3E","20"},
			{"Bubbles","#E7FEFF","95"},
			{"Buff","#F0DC82","73"},
			{"Burgundy","#800020","25"},
			{"Burlywood","#DEB887","70"},
			{"Burnt orange","#CC5500","40"},
			{"Burnt sienna","#E97451","62"},
			{"Burnt umber","#8A3324","34"},
			{"Byzantium","#702963","30"},
			{"Cadet","#536872","39"},
			{"Cadet blue (Crayola)","#A9B2C3","71"},
			{"Cadet grey","#91A3B0","63"},
			{"Cadmium green","#006B3C","21"},
			{"Café noir","#4B3621","21"},
			{"Cambridge blue","#A3C1AD","70"},
			{"Cameo pink","#EFBBCC","84"},
			{"Canary","#FFFF99","80"},
			{"Candy pink","#E4717A","67"},
			{"Caput mortuum","#592720","24"},
			{"Caribbean green","#00CC99","40"},
			{"Carmine","#960018","29"},
			{"Carnation pink","#FFA6C9","83"},
			{"Carnelian","#B31B1B","40"},
			{"Castleton green","#00563F","17"},
			{"Catawba","#703642","33"},
			{"Celadon","#ACE1AF","78"},
			{"Celadon blue","#007BA7","33"},
			{"Celadon green","#2F847C","35"},
			{"Celeste","#B2FFFF","85"},
			{"Cerulean","#007BA7","33"},
			{"Cerulean frost","#6D9BC3","60"},
			{"CG blue","#007AA5","32"},
			{"Champagne","#F7E7CE","89"},
			{"Champagne pink","#F1DDCF","88"},
			{"Charcoal","#36454F","26"},
			{"Charleston green","#232B2B","15"},
			{"Charm pink","#E68FAC","73"},
			{"Cherry blossom pink","#FFB7C5","86"},
			{"Chestnut","#954535","40"},
			{"China pink","#DE6FA1","65"},
			{"Chinese red","#AA381E","39"},
			{"Chocolate (traditional)","#7B3F00","24"},
			{"Citron","#9FA91F","39"},
			{"Claret","#7F1734","29"},
			{"Cobalt blue","#0047AB","34"},
			{"Coffee","#6F4E37","33"},
			{"Columbia Blue","#B9D9EB","82"},
			{"Congo pink","#F88379","72"},
			{"Cool grey","#8C92AC","61"},
			{"Copper (Crayola)","#DA8A67","63"},
			{"Coral","#FF7F50","66"},
			{"Coral pink","#F88379","72"},
			{"Cordovan","#893F45","39"},
			{"Corn","#FBEC5D","67"},
			{"Cornflower blue","#6495ED","66"},
			{"Cornsilk","#FFF8DC","93"},
			{"Cosmic cobalt","#2E2D88","36"},
			{"Cosmic latte","#FFF8E7","95"},
			{"Cosmos pink","#FEBCFF","87"},
			{"Coyote brown","#81613C","37"},
			{"Cotton candy","#FFBCD9","87"},
			{"Cream","#FFFDD0","91"},
			{"Cultured","#F5F5F5","96"},
			{"Cyber grape","#58427C","37"},
			{"Cyclamen","#F56FA1","70"},
			{"Dark brown","#654321","26"},
			{"Dark byzantium","#5D3954","29"},
			{"Dark cornflower blue","#26428B","35"},
			{"Dark cyan","#008B8B","27"},
			{"Dark electric blue","#536878","40"},
			{"Dark goldenrod","#B8860B","38"},
			{"Dark green","#013220","10"},
			{"Dark green (X11)","#006400","20"},
			{"Dark jungle green","#1A2421","12"},
			{"Dark lava","#483C32","24"},
			{"Dark liver","#534B4F","31"},
			{"Dark liver (horses)","#543D37","27"},
			{"Dark magenta","#8B008B","27"},
			{"Dark moss green","#4A5D23","25"},
			{"Dark olive green","#556B2F","30"},
			{"Dark pastel green","#03C03C","38"},
			{"Dark purple","#301934","15"},
			{"Dark red","#8B0000","27"},
			{"Dark salmon","#E9967A","70"},
			{"Dark sea green","#8FBC8F","65"},
			{"Dark sienna","#3C1414","16"},
			{"Dark sky blue","#8CBED6","69"},
			{"Dark slate blue","#483D8B","39"},
			{"Dark slate gray","#2F4F4F","25"},
			{"Dark spring green","#177245","27"},
			{"Dartmouth green","#00703C","22"},
			{"Davy's grey","#555555","33"},
			{"Deep champagne","#FAD6A5","81"},
			{"Deep jungle green","#004B49","15"},
			{"Deep saffron","#FF9933","60"},
			{"Deep Space Sparkle","#4A646C","36"},
			{"Desert sand","#EDC9AF","81"},
			{"Drab","#967117","34"},
			{"Duke blue","#00009C","31"},
			{"Dutch white","#EFDFBB","84"},
			{"Earth yellow","#E1A95F","63"},
			{"Ebony","#555D50","34"},
			{"Ecru","#C2B280","63"},
			{"Eerie black","#1B1B1B","11"},
			{"Eggplant","#614051","32"},
			{"Eggshell","#F0EAD6","89"},
			{"Egyptian blue","#1034A6","36"},
			{"Electric blue","#7DF9FF","75"},
			{"Eminence","#6C3082","35"},
			{"English green","#1B4D3E","20"},
			{"English lavender","#B48395","61"},
			{"English violet","#563C5C","30"},
			{"Eton blue","#96C8A2","69"},
			{"Falu red","#801818","30"},
			{"Fandango pink","#DE5285","60"},
			{"Fawn","#E5AA70","67"},
			{"Feldgrau","#4D5D53","33"},
			{"Fern green","#4F7942","37"},
			{"Field drab","#6C541E","27"},
			{"Fiery rose","#FF5470","67"},
			{"Fire opal","#E95C4B","60"},
			{"Flax","#EEDC82","72"},
			{"Flesh","#FFE9D1","91"},
			{"Flirt","#A2006D","32"},
			{"Floral white","#FFFAF0","97"},
			{"Forest green (traditional)","#014421","14"},
			{"Forest green (web)","#228B22","34"},
			{"French blue","#0072BB","37"},
			{"French fuchsia","#FD3F92","62"},
			{"French lime","#9EFD38","61"},
			{"French mauve","#D473D4","64"},
			{"French pink","#FD6C9E","71"},
			{"French rose","#F64A8A","63"},
			{"French sky blue","#77B5FE","73"},
			{"Fuzzy Wuzzy","#CC6666","60"}
};
	
	int selected = -1;
	public ColorBox colorFor(String key){
		if(colorMap.containsKey(key)){
			return colorMap.get(key);
		}
		else {
			
			selected++;
			ColorBox cb = new ColorBox();
			
			int current = selected % colors.length;
			cb.bg = Color.decode(colors[current][1]);
			
			int light = Integer.parseInt(colors[current][2]);
			
			if(light > 70){
				cb.text = Color.black;
			}
			else {
				cb.text = Color.white;
			}
			colorMap.put(key, cb);
			return cb;
		}
	}
	
}
