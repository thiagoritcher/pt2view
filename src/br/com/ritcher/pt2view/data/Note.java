package br.com.ritcher.pt2view.data;

public class Note {
	public static final int C4 = 46;
	
	public int string, fret, trill, tapped_harmonic, artificial_harmonic;
	public String properties, bend;
	@Override
	public String toString() {
		return "Note [string=" + string + ", fret=" + fret + ", trill=" + trill
				+ ", tapped_harmonic=" + tapped_harmonic
				+ ", artificial_harmonic=" + artificial_harmonic
				+ ", properties=" + properties + ", bend=" + bend + "]";
	}
	
	public static int getNote(String name){
		name = name.toUpperCase();
		
		char note = name.charAt(0);
		String level = name.substring(1, 2);
		
		int diff = 'A' - note;
		return Integer.valueOf(level) * 12 + diff;
	}
	
}
