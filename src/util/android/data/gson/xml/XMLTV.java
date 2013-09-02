package util.android.data.gson.xml;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

	public class Channel {
		@SerializedName("@id")
		public String id;
		@SerializedName("display-name")
		public DisplayName name;
		@SerializedName("icon")
		public Icon icon;
	}

	public class DisplayName {
		@SerializedName("@lang")
		public String lang;

		@SerializedName("$")
		private String name;
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

	public static final String DATE_FORMAT = "yyyyMMddHHmm";

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
