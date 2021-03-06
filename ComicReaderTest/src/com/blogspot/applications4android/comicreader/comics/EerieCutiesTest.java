package com.blogspot.applications4android.comicreader.comics;

import com.blogspot.applications4android.comicreader.core.Strip;
import junit.framework.TestCase;

/**
 * Created by hps on 08.06.2016.
 */
public class EerieCutiesTest extends TestCase {

    public void test() throws Exception {

        EerieCuties comic = new EerieCuties();
        String ls = comic.getLatestStripUrl();
        assertNotNull(ls);
        System.out.println(ls);
        Strip strip = comic.getLatestStrip();
        assertNotNull(strip);
        String image = strip.getImage(comic);
        assertNotNull(image);
        System.out.println(image);
    }

}
