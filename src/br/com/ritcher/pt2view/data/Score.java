package br.com.ritcher.pt2view.data;

import java.util.List;

public class Score {
	public List<MusicSystem> systems;
	public ScoreInfo score_info;
	public int line_spacing;
	@Override
	public String toString() {
		return "Score [systems=" + systems + ", score_info=" + score_info
				+ ", line_spacing=" + line_spacing + "]";
	}
	
	
}
