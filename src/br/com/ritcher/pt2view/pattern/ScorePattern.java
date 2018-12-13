package br.com.ritcher.pt2view.pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import br.com.ritcher.pt2view.data.MusicSystem;
import br.com.ritcher.pt2view.data.Note;
import br.com.ritcher.pt2view.data.Position;
import br.com.ritcher.pt2view.data.Score;
import br.com.ritcher.pt2view.data.Stave;
import br.com.ritcher.pt2view.data.Voice;

public class ScorePattern {
	private Score score;
	
	public void setScore(Score score) {
		this.score = score;
	}
	
	
	public void process(){
		Iterator<MusicSystem> systems = score.systems.iterator();
		
		ArrayList<ArrayList<Position>> list = new ArrayList<ArrayList<Position>>();
		for (int i = 0; i < 20; i++) {
			list.add(new ArrayList<Position>());
		}
		
		while (systems.hasNext()) {
			MusicSystem musicSystem = (MusicSystem) systems.next();
			
			Iterator<Stave> staves = musicSystem.staves.iterator();
			int i = 0;
			while (staves.hasNext()) {
				Stave stave = (Stave) staves.next();
				
				Iterator<Voice> voices = stave.voices.iterator();
				while (voices.hasNext()) {
					Voice voice = (Voice) voices.next();
					list.get(i).addAll(voice.positions);
				}
				i++;
			}
		}
		
		ArrayList<StringBuffer> strings = new ArrayList<StringBuffer>();
		Iterator<ArrayList<Position>> positions = list.iterator();
		while (positions.hasNext()) {
			ArrayList<Position> posList = (ArrayList<Position>) positions.next();
			if(posList.size() < 1){
				continue;
			}
			StringBuffer current = new StringBuffer();
			Iterator<Position> posListIter = posList.iterator();
			while (posListIter.hasNext()) {
				Position position = (Position) posListIter.next();
				positionToBuffer(current, position);
			}
			strings.add(current);
		}
		
		int startSize = 3, maxsize = 60; 
		
		int size = startSize;
		
		ArrayList<Pattern> patterns = new  ArrayList<Pattern>();
		for (int i = 0; i < strings.size(); i++) {
			String current = strings.get(i).toString();
			ArrayList<Position> currentList = list.get(i);
			HashMap<String, Boolean> hasProcessed = new HashMap<String, Boolean>();
			
			int index = 0;
			while(size < maxsize){
				while(index < currentList.size() - size - 1){
					StringBuffer currentKeySb = new StringBuffer();
					for (int j = 0; j < size; j++) {
						positionToBuffer(currentKeySb, currentList.get(index + j));	
					}
					
					String currentKey = currentKeySb.toString();
					
					if(current.length() < 1){
						break;
					}
					
					if(hasProcessed.containsKey(currentKey)){
						index++;
						continue;
					}
					
					int found = -1, foundquant = 0;
					while((found = current.indexOf(currentKey, found)) != -1){
						foundquant++;
						found += currentKey.length();
					}
					
					if(foundquant > 1){
						Pattern p = new Pattern();
						p.key = currentKey;
						p.positionList = i;
						p.found = foundquant;
						p.size = size;
						System.out.println(p);
						//patterns.add(p);
					}
					hasProcessed.put(currentKey, true);
					index ++;
				}
				index = 0;
				size ++;
			}
			size = startSize;
		}
		System.out.println(patterns);
	}


	private void positionToBuffer(StringBuffer current,
			Position position) {
		current.append("[");
		current.append(position.duration);
		current.append(";");
		current.append("(");
		Iterator<Note> notesit = position.notes.iterator();
		while (notesit.hasNext()) {
			Note note = (Note) notesit.next();
			current.append(note.string);
			current.append(";");
			current.append(note.fret);
			current.append(";");
		}
		current.append(")]||");
	}
}

class Pattern {
	public int size;
	String key;
	int positionList;
	int found;
	
	@Override
	public String toString() {
		return "Pattern [size=" + size + ", key=" + key + ", positionList="
				+ positionList + ", found=" + found + "]\n";
	}
	
	
}
