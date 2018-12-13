package br.com.ritcher.pt2view.data;

import java.util.List;

public class Stave {
	public List<Voice> voices;
	public int string_count ,clef_type;
	@Override
	public String toString() {
		return "Stave [voices=" + voices + ", string_count=" + string_count
				+ ", clef_type=" + clef_type + "]\n";
	}
}
