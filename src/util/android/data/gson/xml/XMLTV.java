package util.android.data.gson.xml;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import util.android.util.FileUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

public class XMLTV {

	@SerializedName("channel")
	public List<Channel> channels;
	@SerializedName("programme")
	public List<Programme> programmes;
	
	public class Programme {
		@SerializedName("@channel")
		public String channel;
		@SerializedName("@start")
		public Date start;
		@SerializedName("@stop")
		public Date stop;
		
		public Title title;
		
		@SerializedName("sub-title")
		public Title subtitle = null;
		
		@SerializedName("desc")
		public Title description = null;
		
//		@SerializedName("previously-shown")
//		public PreviousShow previousShow = new PreviousShow();
		
		@SerializedName("episode-num")
		public EpisodeNumber episodeNumber;
		
		@SerializedName("category")
		public List<Category> categories;
	}
	
	public class Category {
		@SerializedName("$")
		public String name;
	}
	
	public class Title {
		@SerializedName("$")
		public String title;
	}
	
	public class PreviousShow {
		public PreviousShow() {
			super();
			this.start = null;
		}

		@SerializedName("@start")
		public String start = null;
	}
	
	public class EpisodeNumber {
		@SerializedName("@system")
		public String system;
		
		@SerializedName("$")
		public String value;
		
	}
	
	public class Channel {
		@SerializedName("@id")
		public String id;
		@SerializedName("display-name")
		public List<DisplayName> name;
		@SerializedName("icon")
		public Icon icon;
	}

	public class DisplayName {
		@SerializedName("@lang")
		public String lang;

		@SerializedName("$")
		public String name;
	}

	public class Icon {
		@SerializedName("@src")
		public String src;

		@SerializedName("@width")
		public int width;

		@SerializedName("@height")
		public int height;
	}

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

	public static final String DATE_FORMAT = "yyyyMMddHHmmss z";

	public static final GsonXml gsonXml = new GsonXmlBuilder()
			.wrap(new GsonBuilder().setDateFormat(DATE_FORMAT).registerTypeAdapter(Date.class,
					new JsonDeserializer<Date>() {
						DateFormat df = new SimpleDateFormat(DATE_FORMAT);

						@Override
						public Date deserialize(final JsonElement json, final Type typeOfT,
								final JsonDeserializationContext context) throws JsonParseException {
							try {
								return df.parse(json.getAsString());
							} catch (ParseException e) {
								return null;
							}
						}
					})).setXmlParserCreator(parserCreator).setSameNameLists(true).create();

	public static final XMLTV readFile(String filename) throws JsonSyntaxException, IOException {
		XMLTV p = XMLTV.gsonXml.fromXml(FileUtils.readFile(filename), XMLTV.class);
		return p;
	}
}
