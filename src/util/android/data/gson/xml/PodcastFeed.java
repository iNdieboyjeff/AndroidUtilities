package util.android.data.gson.xml;

import java.util.Date;
import java.util.List;

import util.android.data.gson.xml.KCPodcast.KCPodcastEpisode;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class PodcastFeed {

	public PodcastChannel channel;

	public class PodcastChannel {
		public String title;
		public String link;
		public String description;
		public String language;
		public PodcastImage image;
		public String copyright;
		public Date pubDate;

		@SerializedName("item")
		public List<PodcastItem> items;

	}

	public class PodcastImage {
		public String url;
		public String title;
		public String link;
	}

	public class PodcastItem {
		public String title;
		public String description;
		public String link;
		public Date pubDate;

		public boolean equalTo(KCPodcastEpisode episode) {
			if (!episode.Title.equalsIgnoreCase(title)) {
				Log.i("Podcast Compare", "Fails on title");
				return false;
			}
			if (!episode.Description.equalsIgnoreCase(description)) {
				Log.i("Podcast Compare", "Fails on description");
				return false;
			}
			if (episode.PublishDate.compareTo(pubDate) != 0) {
				Log.i("Podcast Compare", "Fails on publication date");
				return false;
			}
			return true;
		}
	}
}
