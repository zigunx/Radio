package com.example.radio.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.radio.entity.ResponseInfo;
import com.example.radio.entity.Station;

import android.util.Log;
import android.util.Xml;

public class StationParser {
	
	private static final String TAG_RADIO = "radio";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_COUNTRY = "country";
	private static final String TAG_URL = "url";
	private static final String TAG_STATUS = "status";
	private static final String TAG_MESSAGE = "message";
	
	Boolean flag_station = false; 
	Boolean flag_response = false;
	
	ArrayList<Station> arrayStation = new ArrayList<Station>();
	Station station;
	ResponseInfo response;
	
	Object ob;
	
	public Object parse(InputStream stream){
		
		XmlPullParser parser = Xml.newPullParser();
		
		try {
			parser.setInput(stream, null);
			int eventType = parser.getEventType(); 
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String localName = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					/*if (TAG_STATUS.equals(localName)){
						
					}*/
					if (TAG_RADIO.equals(localName)){
						station = new Station();
						arrayStation.add(station);
						flag_station = true;
					} else if (TAG_ID.equals(localName) && flag_station){
						station.setId(toInt(readText(parser)));
					} else if (TAG_NAME.equals(localName)){
						station.setName(readText(parser));
						Log.i("DEBUG",station.getName());
					} else if (TAG_COUNTRY.equals(localName)){
						station.setCountry(readText(parser));
					} else if (TAG_URL.equals(localName)){
						station.setUrl(readText(parser));
					} else if(TAG_STATUS.equals(localName)){ // if response return expired token 
						response = new ResponseInfo();
						flag_response = true;
					} else if(TAG_ID.equals(localName) && flag_response){
						response.setCode(readText(parser));
					} else if(TAG_MESSAGE.equals(localName)){
						response.setMessage(readText(parser));
					}
					break;
				}
				
				try {
					eventType = parser.next();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		} catch (XmlPullParserException e) {

			e.printStackTrace();
		}
		
		// check for the message
		
		if (station != null) ob = arrayStation;
		else if (response != null) ob =  response;

		return ob;
		
	}
	
	
	private String readText(XmlPullParser parser)  {
		String result = "";
		try {
			if (parser.next() == XmlPullParser.TEXT) {
				result = parser.getText();
				parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
		
	private int toInt(String value) {
		int result = 0;

		try {
			result = Integer.parseInt(value);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
