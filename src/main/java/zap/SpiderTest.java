package zap;

import net.continuumsecurity.proxy.ZAProxyScanner;
import net.continuumsecurity.proxy.model.Context;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class SpiderTest {
    static ZAProxyScanner zaproxy;
    static String HOST = "127.0.0.1";
    static int PORT = 8090;
    static String BASEURL = "http://localhost:3000/#/search";
    static String DEFAULT_CONTEXT = "Default Context";
    public static String  APIKEY=null;

    public static void testSpider(String URL) {
        zaproxy = new ZAProxyScanner(HOST, PORT, APIKEY);
        zaproxy.spider(URL);
        int progress = 0;
        while (progress < 100) {
            progress = zaproxy.getSpiderProgress(zaproxy.getLastSpiderScanId());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        List<String> results = zaproxy.getSpiderResults(zaproxy.getLastSpiderScanId());

        assertThat(results.size(),equalTo(63));
        assert results.contains(BASEURL);
        for (String url : results) {
            System.out.println(url);
        }
    }
}