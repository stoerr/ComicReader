package com.blogspot.applications4android.comicreader.comics;

import com.blogspot.applications4android.comicreader.comics.GoComics.CalvinandHobbes;
import com.blogspot.applications4android.comicreader.core.Strip;
import junit.framework.TestCase;

/**
 * Created by hps on 22.01.2017.
 */
public class CalvinAndHobbesTest extends TestCase {

    public void test() throws Exception {
        CalvinandHobbes comic = new CalvinandHobbes();
        Strip strip = comic.getLatestStrip();
        assertNotNull(strip);
        String image = strip.getImage(comic);
        assertNotNull(image);
    }

}
