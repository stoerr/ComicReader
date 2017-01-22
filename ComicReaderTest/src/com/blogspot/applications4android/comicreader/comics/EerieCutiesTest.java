package com.blogspot.applications4android.comicreader.comics;

import junit.framework.TestCase;

/**
 * Created by hps on 08.06.2016.
 */
public class EerieCutiesTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("http.proxyHost", "proxy");
        System.getProperty("http.proxyPort", "8080");
    }

    public void test() {

        EerieCuties ec = new EerieCuties();
        String ls = ec.getLatestStripUrl();
        assertNotNull(ls);
    }

}
