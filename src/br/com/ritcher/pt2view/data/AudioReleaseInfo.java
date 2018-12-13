package br.com.ritcher.pt2view.data;

public class AudioReleaseInfo {
	@Override
	public String toString() {
		return "AudioReleaseInfo [release_type=" + release_type + ", year="
				+ year + ", title=" + title + ", live=" + live + "]";
	}
	
	public int release_type, year;
	public String title;
	public boolean live;
}
