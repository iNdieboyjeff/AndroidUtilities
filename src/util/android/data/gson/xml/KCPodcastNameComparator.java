package util.android.data.gson.xml;

import java.util.Comparator;

public class KCPodcastNameComparator implements Comparator<KCPodcast> {

	@Override
	public int compare(KCPodcast lhs, KCPodcast rhs) {
		return lhs.Title.compareToIgnoreCase(rhs.Title);
	}

}