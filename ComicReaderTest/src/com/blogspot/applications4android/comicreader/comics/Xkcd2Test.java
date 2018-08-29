package com.blogspot.applications4android.comicreader.comics;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import com.blogspot.applications4android.comicreader.comics.GoComics.CalvinandHobbes;
import com.blogspot.applications4android.comicreader.core.Strip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Xkcd2Test extends InstrumentationTestCase {

    public void test() throws Exception {
        Xkcd comic = new Xkcd();
        Strip strip = comic.getLatestStrip();
        assertNotNull(strip);
        String image = strip.getImage(comic);
        assertNotNull(image);
    }

}
