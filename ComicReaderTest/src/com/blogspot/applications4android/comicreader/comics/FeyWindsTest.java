package com.blogspot.applications4android.comicreader.comics;

import com.blogspot.applications4android.comicreader.core.Strip;
import junit.framework.TestCase;

import java.net.URL;

/**
 * @author <a href="http://www.stoerr.net/">Hans-Peter Stoerr</a>
 * @since 05.04.2016
 */
public class FeyWindsTest extends TestCase {

    public void test() throws Exception {
        new FeyWinds() {
            public void testit() throws Exception {
                super.fetchAllComicUrls();
                assertTrue(mComicUrls.length > 10);
                String ls = getLatestStripUrl();
                assertNotNull(ls);
                System.out.println(ls);
                Strip strip = getLatestStrip();
                assertNotNull(strip);
                // this fails with exception in FileUtils stub. How to actually test this?
                String image = strip.getImage(this);
                assertNotNull(image);
                System.out.println(image);
            }
        }.testit();

    }
}
