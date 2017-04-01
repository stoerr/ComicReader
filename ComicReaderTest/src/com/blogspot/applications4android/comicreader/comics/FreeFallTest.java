package com.blogspot.applications4android.comicreader.comics;

import android.provider.Settings;
import android.test.InstrumentationTestCase;
import com.blogspot.applications4android.comicreader.core.Strip;
import junit.framework.TestCase;

/**
 * @author <a href="http://www.stoerr.net/">Hans-Peter Stoerr</a>
 * @since 05.04.2016
 */
public class FreeFallTest extends FreeFall {

    public static void main(String[] args) {
        new FreeFallTest().run();
    }

    public void run() {
        System.out.println("Hallo!");
        fetchAllComicUrls();
    }
}
