package br.com.ritcher.pt2view.data;

import java.util.Iterator;
import java.util.List;

public class Position {
	public int position, duration, multibar_rest;
	public String group, properties;
	public List<Note> notes;
	@Override
	public String toString() {
		return "Position [position=" + position + ", duration=" + duration
				+ ", multibar_rest=" + multibar_rest + ", group=" + group
				+ ", properties=" + properties + ", notes=" + notes + "]";
	}
	
	public Note getNoteOnString(int string) {
		if(notes == null || notes.size() < 1){
			return null;
		}
		
		for (Iterator<Note> iterator = notes.iterator(); iterator.hasNext();) {
			Note note = (Note) iterator.next();
			if(note.string == string){
				return note;
			}
		}
		return null;
	}
	
	
}
