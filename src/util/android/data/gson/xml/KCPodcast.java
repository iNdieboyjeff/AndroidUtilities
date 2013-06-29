package util.android.data.gson.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import util.android.util.FileUtils;

import android.annotation.SuppressLint;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

/**
 * Class to represent a podcast feed as used in KiesCast and similar podcast
 * apps.
 * 
 * A typical file consists of information about the feed, and a variable length
 * list of podcast episodes.
 * 
 * @author Jeff Sutton
 * @version 1.0
 * 
 */
public class KCPodcast {
	/**
	 * Comparator for sorting episodes in reverse date order.
	 * 
	 * @author Jeff Sutton
	 * @since 1.0
	 * 
	 */
	public class KCEpisodeByReleaseDescComparator implements
			Comparator<KCPodcastEpisode> {

		@Override
		public int compare(KCPodcastEpisode lhs, KCPodcastEpisode rhs) {
			return rhs.PublishDate.compareTo(lhs.PublishDate);
		}

	}

	public class KCEpisodeFilenameComparator implements
			Comparator<KCPodcastEpisode> {

		@Override
		public int compare(KCPodcastEpisode lhs, KCPodcastEpisode rhs) {
			return rhs.FilePath.compareToIgnoreCase(lhs.FilePath);
		}

	}

	/**
	 * Represents an individual podcast episode.
	 * 
	 * This data is found within <Post></Post> tags;
	 * 
	 * @author Jeff Sutton
	 * @since 1.0
	 * 
	 */
	public class KCPodcastEpisode {
		public String Title = "";
		public String SubTitle = "";
		public String Description = "";
		public String Author = "";
		public String Summary = "";
		public String Guid = "";
		public Date PublishDate = new Date();
		public Date DownloadDate = new Date();
		public int Rating;
		public long Duration;
		public long Length;
		public String Type = "AUDIO";
		public String FilePath = "";
		public int BitRate;
		public int SamplingRate;
		public URL Url;
		public String Link = "";
		public String DetailMediaTypeString = "";
		public String DRMProperty = "";
		public String AudioCodec = "";
		public String VideoCodec = "";
	}

	public URL ChannelURL;
	public String Title;
	public String SubTitle;
	public String Description;
	public String Author;
	public String Summary;
	public String Link;
	public URL ImageURL;
	public String Category;
	public String MediaType;
	public String WebMaster;
	public int JsonId;

	public String Subscribed;

	public String subscribedFrom;

	@SerializedName("Post")
	public List<KCPodcastEpisode> Posts;

	static XmlParserCreator parserCreator = new XmlParserCreator() {
		@Override
		public XmlPullParser createParser() {
			try {
				return XmlPullParserFactory.newInstance().newPullParser();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	};

	/**
	 * Output date format for use with SimpleDateFormat.
	 * 
	 * Dates are similar to: 19 Jun 1979 11:12:00 GMT
	 * 
	 * @since 1.0
	 */
	public static final String DATE_FORMAT = "d MMM yyyy HH:mm:ss zzz";

	public static final GsonXml gsonXml = new GsonXmlBuilder()
			.wrap(new GsonBuilder().setDateFormat(DATE_FORMAT))
			.setXmlParserCreator(parserCreator).setSameNameLists(true).create();

	/**
	 * Sorts list of podcast episodes. By default episodes are sorted newest to
	 * oldest.
	 * 
	 * @param oldestFirst
	 *            - if true, episodes will be sorted with the oldest first in
	 *            the list
	 * 
	 * @since 1.0
	 */
	public void sortByRelease(boolean oldestFirst) {
		if (oldestFirst) {
			Collections.sort(Posts, Collections
					.reverseOrder(new KCEpisodeByReleaseDescComparator()));
		} else {
			Collections.sort(Posts, (new KCEpisodeByReleaseDescComparator()));
		}
	}

	/**
	 * Generate object representation of XML file.
	 * 
	 * @param filename
	 * @return KCPodcast - representation of XML file
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @since 1.0
	 */
	public static final KCPodcast readFile(String filename)
			throws JsonSyntaxException, IOException {
		return KCPodcast.gsonXml.fromXml(FileUtils.readFile(filename),
				KCPodcast.class);
	}

	/**
	 * Convert this KCPodcast into an XML string representation.
	 * 
	 * @return String - XML document
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @since 1.0
	 */
	@SuppressLint("SimpleDateFormat")
	public String toXML() throws ParserConfigurationException,
			TransformerException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element rootElement = doc.createElement("channel");
		doc.appendChild(rootElement);

		Element staff = doc.createElement("ChannelURL");
		rootElement.appendChild(staff);
		staff.appendChild(doc.createTextNode(ChannelURL.toExternalForm()));

		Element title = doc.createElement("Title");
		rootElement.appendChild(title);
		title.appendChild(doc.createTextNode(Title));

		Element subtitle = doc.createElement("SubTitle");
		rootElement.appendChild(subtitle);
		subtitle.appendChild(doc.createTextNode(SubTitle));

		Element description = doc.createElement("Description");
		rootElement.appendChild(description);
		description.appendChild(doc.createTextNode(Description));

		Element author = doc.createElement("Author");
		rootElement.appendChild(author);
		author.appendChild(doc.createTextNode(Author));

		Element summary = doc.createElement("Summary");
		rootElement.appendChild(summary);
		summary.appendChild(doc.createTextNode(Summary));

		Element link = doc.createElement("Link");
		rootElement.appendChild(link);
		link.appendChild(doc.createTextNode(Link));

		Element imageURL = doc.createElement("ImageURL");
		rootElement.appendChild(imageURL);
		imageURL.appendChild(doc.createTextNode(ImageURL.toExternalForm()));

		Element mediaType = doc.createElement("MediaType");
		rootElement.appendChild(mediaType);
		mediaType.appendChild(doc.createTextNode(MediaType));

		Element category = doc.createElement("Category");
		rootElement.appendChild(category);
		category.appendChild(doc.createTextNode(Category));

		Element jsonId = doc.createElement("JsonId");
		rootElement.appendChild(jsonId);
		jsonId.appendChild(doc.createTextNode(String.valueOf(JsonId)));

		Element webmaster = doc.createElement("WebMaster");
		rootElement.appendChild(webmaster);
		webmaster.appendChild(doc.createTextNode(WebMaster));

		Element subscribed = doc.createElement("Subscribed");
		rootElement.appendChild(subscribed);
		subscribed.appendChild(doc.createTextNode(Subscribed));

		Element subscribedFm = doc.createElement("subscribedFrom");
		rootElement.appendChild(subscribedFm);
		subscribedFm.appendChild(doc.createTextNode(subscribedFrom));

		// process posts
		for (KCPodcastEpisode ep : Posts) {
			Element post = doc.createElement("Post");
			rootElement.appendChild(post);

			Element pTitle = doc.createElement("Title");
			post.appendChild(pTitle);
			pTitle.appendChild(doc.createTextNode(ep.Title));

			Element pSubTitle = doc.createElement("SubTitle");
			post.appendChild(pSubTitle);
			pSubTitle.appendChild(doc.createTextNode(ep.SubTitle));

			Element pDescription = doc.createElement("Description");
			post.appendChild(pDescription);
			pDescription.appendChild(doc.createTextNode(ep.Description));

			Element pAuthor = doc.createElement("Author");
			post.appendChild(pAuthor);
			pAuthor.appendChild(doc.createTextNode(ep.Author));

			Element pSummary = doc.createElement("Summary");
			post.appendChild(pSummary);
			pSummary.appendChild(doc.createTextNode(ep.Summary));

			Element pGuid = doc.createElement("Guid");
			post.appendChild(pGuid);
			pGuid.appendChild(doc.createTextNode(ep.Guid));

			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

			Element pPubDate = doc.createElement("PublishDate");
			post.appendChild(pPubDate);
			pPubDate.appendChild(doc.createTextNode(sdf.format(ep.PublishDate)));

			Element pDownDate = doc.createElement("DownloadDate");
			post.appendChild(pDownDate);
			pDownDate.appendChild(doc.createTextNode(sdf
					.format(ep.DownloadDate)));

			Element pRate = doc.createElement("Rating");
			post.appendChild(pRate);
			pRate.appendChild(doc.createTextNode(String.valueOf(ep.Rating)));

			Element pDuration = doc.createElement("Duration");
			post.appendChild(pDuration);
			pDuration.appendChild(doc.createTextNode(String
					.valueOf(ep.Duration)));

			Element pLength = doc.createElement("Length");
			post.appendChild(pLength);
			pLength.appendChild(doc.createTextNode(String.valueOf(ep.Length)));

			Element pBitRate = doc.createElement("BitRate");
			post.appendChild(pBitRate);
			pBitRate.appendChild(doc.createTextNode(String.valueOf(ep.BitRate)));

			Element pSamplingRate = doc.createElement("SamplingRate");
			post.appendChild(pSamplingRate);
			pSamplingRate.appendChild(doc.createTextNode(String
					.valueOf(ep.SamplingRate)));

			Element pType = doc.createElement("Type");
			post.appendChild(pType);
			pType.appendChild(doc.createTextNode(ep.Type));

			Element pPath = doc.createElement("FilePath");
			post.appendChild(pPath);
			pPath.appendChild(doc.createTextNode(ep.FilePath));

			try {
				Element pURL = doc.createElement("Url");
				post.appendChild(pURL);
				pURL.appendChild(doc.createTextNode(ep.Url.toString()));
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Element pLink = doc.createElement("Link");
			post.appendChild(pLink);
			pLink.appendChild(doc.createTextNode(ep.Link));

			Element pDetail = doc.createElement("DetailMediaTypeString");
			post.appendChild(pDetail);
			pDetail.appendChild(doc.createTextNode(ep.DetailMediaTypeString));

			Element pDRM = doc.createElement("DRMProperty");
			post.appendChild(pDRM);
			pDRM.appendChild(doc.createTextNode(ep.DRMProperty));

			Element pAudioCodec = doc.createElement("AudioCodec");
			post.appendChild(pAudioCodec);
			pAudioCodec.appendChild(doc.createTextNode(ep.AudioCodec));

			Element pVideoCodec = doc.createElement("VideoCodec");
			post.appendChild(pVideoCodec);
			pVideoCodec.appendChild(doc.createTextNode(ep.VideoCodec));

		}

		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource source = new DOMSource(doc);
		StringWriter writer = new StringWriter();

		transformer.transform(source, new StreamResult(writer));
		String output = writer.getBuffer().toString();
		return output;
	}
}
