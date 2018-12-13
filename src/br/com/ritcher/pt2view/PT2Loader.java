package br.com.ritcher.pt2view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

import br.com.ritcher.pt2view.data.AudioReleaseInfo;
import br.com.ritcher.pt2view.data.MusicSystem;
import br.com.ritcher.pt2view.data.Note;
import br.com.ritcher.pt2view.data.Position;
import br.com.ritcher.pt2view.data.Score;
import br.com.ritcher.pt2view.data.ScoreInfo;
import br.com.ritcher.pt2view.data.SongData;
import br.com.ritcher.pt2view.data.Stave;
import br.com.ritcher.pt2view.data.Voice;

public class PT2Loader {

	private File file;
	
	
	public Score loadFile(File file) throws JsonParseException, FileNotFoundException, IOException{
		this.file = file;
		return process();
	}
	
	private Score process() throws JsonParseException, FileNotFoundException, IOException {
		JsonFactory f = new MappingJsonFactory();
		JsonParser jp = f.createJsonParser(new GZIPInputStream(new FileInputStream(file)));
		JsonToken current;
		current = jp.nextToken();
		
		if (current != JsonToken.START_OBJECT) {
			throw new RuntimeException("Error: root should be object: quiting.");
		}
		
		JsonNode node = jp.readValueAsTree();
		//System.out.println(node);
		Score score = new Score();
		JsonNode scoren = node.get("score");
		transfer(score, scoren);
		
		
		ScoreInfo scoreInfo = new ScoreInfo();
		score.score_info = scoreInfo;
		JsonNode scoreInfon = node.get("score").get("score_info");
		transfer(scoreInfo, scoreInfon);
		
		JsonNode songDatan = scoreInfon.get("song_data");
		SongData songData = new SongData();
		scoreInfo.songData = songData;
		transfer(songData, songDatan);
		
		
		JsonNode audioReleaseIn = songDatan.get("audio_release_info");
		AudioReleaseInfo audioReleaseI = new AudioReleaseInfo();
		songData.audio_release_info = audioReleaseI;
		transfer(audioReleaseI, audioReleaseIn);
		
		
		ArrayList<MusicSystem> systemList = new ArrayList<MusicSystem>();
		score.systems = systemList;
		Iterator<JsonNode> it = node.get("score").get("systems").getElements();
		//System.out.println(node.get("score"));
		while(it.hasNext()){
			JsonNode systemn = it.next();
			MusicSystem system = new MusicSystem();
			systemList.add(system);
			transfer(system, systemn);
			
			Iterator<JsonNode> stv = systemn.get("staves").getElements();
			ArrayList<Stave> staveList = new ArrayList<Stave>();
			system.staves = staveList;
			while (stv.hasNext()) {
				JsonNode staven = (JsonNode) stv.next();
				
				Stave stave = new Stave();
				staveList.add(stave);
				transfer(stave, staven);
				
				ArrayList<Voice> voiceList = new ArrayList<Voice>();
				stave.voices = voiceList;
				
				Iterator<String> voices = staven.get("voices").getFieldNames();
				while (voices.hasNext()) {
					String voiceName = (String) voices.next();
					
					Voice voice = new Voice();
					voiceList.add(voice);
					JsonNode voicen = staven.get("voices").get(voiceName);
					transfer(voice, voicen);
					
					ArrayList<Position> positionList = new ArrayList<Position>();
					voice.positions = positionList;
					
					Iterator<JsonNode> pos = staven.get("voices").get(voiceName).get("positions").getElements();
					while (pos.hasNext()) {
						JsonNode positionn = (JsonNode) pos.next();
						Position position = new Position();
						positionList.add(position);
						transfer(position, positionn);
						
						ArrayList<Note> noteList = new ArrayList<Note>();
						position.notes = noteList;
						
						Iterator<JsonNode> notes = positionn.get("notes").getElements();
						while(notes.hasNext()){
							JsonNode noten  = notes.next();
							Note note = new Note();
							noteList.add(note);
							transfer(note, noten);
							
						}
					}
				}
			}
		}
		return score;
	}

	private void transfer(Object to, JsonNode from) {
		Iterator<String> fields = from.getFieldNames();
		while (fields.hasNext()) {
			String f = (String) fields.next();
			Field field;
			try {
				JsonNode value = from.get(f);
				if(value.isArray() || value.isObject() || value.isNull()){
					continue;
				}
				
				field = to.getClass().getField(f);
				if(value.isInt()){
					field.setInt(to, value.getIntValue());
				}
				else if(value.isTextual()){
					field.set(to, value.getTextValue());
				}
				else if(value.isBoolean()){
					field.setBoolean(to, value.getBooleanValue());
				}
				else {
					throw new RuntimeException("Field type not defined: "+ f +  to);
				}
				
			} catch (SecurityException e) {
				e.printStackTrace();
				
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				System.out.println(to);
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			catch (AbstractMethodError e) {
				throw new RuntimeException("Problema setando " + f + " em " + to, e);
			}
		}
		
	}

	/*
	private Map<String, String> noteexpmap = new HashMap<String, String>();
	int currentnoteex = 1;
	
	Random random = new Random();
	private String getNoteExp(String notetxt) {
		if(noteexpmap.containsKey(notetxt)){
			return noteexpmap.get(notetxt);
		}
		else {
			currentnoteex++;
			
			String key;
			do {
				key = "";
				key += (char)(random.nextInt(26) + 'a');
				key += (char)(random.nextInt(26) + 'a');
				key += (char)(random.nextInt(26) + 'a');
				
			} while(noteexpmap.containsValue(key));
			
			noteexpmap.put(notetxt, key);
			return key;
		}
	}
	*/
}
