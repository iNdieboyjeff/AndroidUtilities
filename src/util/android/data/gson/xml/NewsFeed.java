package util.android.data.gson.xml;

import java.util.Date;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import util.android.data.gson.xml.KCPodcast.KCPodcastEpisode;

import com.google.gson.annotations.SerializedName;

public class NewsFeed {

	public NewsChannel channel;

	public class NewsChannel {
		public String title;
		public String link;
		public String description;
		public String language;
		public String copyright;
		public Date pubDate;

		@SerializedName("item")
		@Nullable
		public List<NewsItem> items;

	}

	public class NewsItem {
		public String title;
		public String description;
		public String link;
		public Date pubDate;
		public String image;

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
