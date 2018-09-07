package com.blogspot.applications4android.comicreader.comics;

import android.test.InstrumentationTestCase;
import com.blogspot.applications4android.comicreader.core.Strip;

import java.io.File;

public class Xkcd2Test extends InstrumentationTestCase {

    public void test() throws Exception {
        Xkcd comic = new Xkcd();
        Strip strip = comic.getLatestStrip();
        assertNotNull(strip);
        assertTrue(comic.getCurrentId() > 2040);
        String image = strip.getImage(comic);
        assertNotNull(image);
        File f = new File(image);
        assertTrue(f.exists());
        assertTrue(f.length() > 0);
    }

}
