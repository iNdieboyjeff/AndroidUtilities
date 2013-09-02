package util.android.data.gson.xml;

import java.util.Date;
import java.util.List;

import util.android.data.gson.xml.KCPodcast.KCPodcastEpisode;

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

		@SuppressWarnings("deprecation")
		public boolean equalTo(KCPodcastEpisode episode) {

			boolean titleOK = false;
			boolean descriptionOK = false;
			boolean dateOK = false;

			if (!episode.Title.equalsIgnoreCase(title)) {

				return false;
			} else {
				titleOK = true;
			}

			if (!episode.Description.equalsIgnoreCase(description)) {

				return false;
			} else {
				descriptionOK = true;
			}

			if (episode.PublishDate == null)
				return false;
			if (pubDate == null)
				return false;
			if (episode.PublishDate.compareTo(pubDate) != 0) {

				episode.PublishDate.setHours(pubDate.getHours());
				episode.PublishDate.setMinutes(pubDate.getMinutes());
				episode.PublishDate.setSeconds(pubDate.getSeconds());

				if (episode.PublishDate.compareTo(pubDate) != 0 && (!titleOK || !descriptionOK)) {
					return false;
				}

				episode.PublishDate = pubDate;
				return true;
			}
			return true;
		}
	}
}
