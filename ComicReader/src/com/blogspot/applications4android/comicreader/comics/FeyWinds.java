package com.blogspot.applications4android.comicreader.comics;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.blogspot.applications4android.comicreader.comictypes.ArchivedComic;
import com.blogspot.applications4android.comicreader.core.Strip;


public class FeyWinds extends ArchivedComic {

	@Override
	public String getComicWebPageUrl() {
		return "http://www.feywinds.com/";
	}

	@Override
	protected String[] getAllComicUrls(BufferedReader reader) throws IOException {
			int idx = 0;
			ArrayList<String> m_com = new ArrayList<String>();
			String str, str_temp;
			String urlpart = "href=\"http://www.feywinds.com/feywinds/";
			while ((str = reader.readLine()) != null) {
				int i = -1;
				while ((i = str.indexOf(urlpart, i+1)) >= 0) {
					int j = str.indexOf("\"", i + urlpart.length());
					str_temp = str.substring(i + urlpart.length(), j-1);
					str_temp = "http://www.feywinds.com/wp-content/uploads/" + str_temp + ".jpg";
					m_com.add(str_temp);
					idx++;
				}
			}
			String[] m_com_urls = new String[idx];
			m_com.toArray(m_com_urls);
			return m_com_urls;
	}

	@Override
	protected String getArchiveUrl() {
		return "http://www.feywinds.com/archive/";
	}

	@Override
	protected boolean htmlNeeded() {
		return false;
	}

	@Override
	protected String parse(String url, BufferedReader reader, Strip strip)
			throws IOException {
		String val = url;
		String index = val.substring(val.length()-4,val.length()-1);
		strip.setTitle("Fey Winds" + ": " + index);
		strip.setText("-NA-");
		return url;
	}
}
