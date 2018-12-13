package br.com.ritcher.pt2view.data;

public class SongData {
		public String title, artist, arranger, transcriber, copyright, lyrics, performance_notes;
		public AudioReleaseInfo audio_release_info;
		public VideoReleaseInfo video_release_info;
		@Override
		public String toString() {
			return "SongData [title=" + title + ", artist=" + artist
					+ ", arranger=" + arranger + ", transcriber=" + transcriber
					+ ", copyright=" + copyright + ", lyrics=" + lyrics
					+ ", performance_notes=" + performance_notes
					+ ", audio_release_info=" + audio_release_info
					+ ", video_release_info=" + video_release_info + "]";
		}
		
		
}
