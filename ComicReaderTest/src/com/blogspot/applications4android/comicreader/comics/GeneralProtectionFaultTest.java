package com.blogspot.applications4android.comicreader.comics;

import com.blogspot.applications4android.comicreader.core.Strip;
import junit.framework.TestCase;

/**
 * Created by hps on 01.02.2017.
 */
public class GeneralProtectionFaultTest extends TestCase {

    public void test() throws Exception {

        GeneralProtectionFault comic = new GeneralProtectionFault();
        Strip strip = comic.getLatestStrip();
        assertNotNull(strip);
        String image = strip.getImage(comic);
        assertNotNull(image);
        System.out.println(image);
    }
}
