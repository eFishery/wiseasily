package wiseasily.util;



import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 29/03/18.
 */
public class WifiUtilTest {

    WifiUtil wifiUtil = new WifiUtil();

    @Test
    public void isSsidWifiConfigEqualsSsidConfigTest() throws Exception {
        assertFalse(wifiUtil.isSsidWifiConfigEqualsSsidConfig(null, null));
        assertFalse(wifiUtil.isSsidWifiConfigEqualsSsidConfig("", null));
        assertFalse(wifiUtil.isSsidWifiConfigEqualsSsidConfig(null, ""));
        assertFalse(wifiUtil.isSsidWifiConfigEqualsSsidConfig("", ""));
        assertFalse(wifiUtil.isSsidWifiConfigEqualsSsidConfig("Efishery_0001C", "efishery_0001C"));
        assertFalse(wifiUtil.isSsidWifiConfigEqualsSsidConfig("efishery_0001C", "EFISHERY_0001C"));
        assertTrue(wifiUtil.isSsidWifiConfigEqualsSsidConfig("efishery_0001C", "efishery_0001C"));
    }

    @Test
    public void getConfigFormatSSIDTest() throws Exception {
        assertEquals("\"\"", wifiUtil.getConfigFormatSSID(null));
        assertEquals("\"\"", wifiUtil.getConfigFormatSSID(""));
        assertEquals("\"efishery_18\"", wifiUtil.getConfigFormatSSID("efishery_18"));
    }

    @Test
    public void removeQuotesTest() throws Exception {
        assertEquals("", wifiUtil.removeQuotes(null));
        assertEquals("", wifiUtil.removeQuotes(""));
        assertEquals("efishery_18", wifiUtil.removeQuotes("\"efishery_18\""));
    }

    @Test
    public void getNetIdOfScanResultInWifiConfigTest() throws Exception {
        assertEquals(-1, wifiUtil.getNetIdOfScanResultInWifiConfig(null, null));
        assertEquals(-1, wifiUtil.getNetIdOfScanResultInWifiConfig(null, new ArrayList<>()));
        assertEquals(-1, wifiUtil.getNetIdOfScanResultInWifiConfig("", null));
        assertEquals(-1, wifiUtil.getNetIdOfScanResultInWifiConfig("", new ArrayList<>()));
    }

    @Test
    public void getNetIdTest() throws Exception {
        assertEquals(-1, wifiUtil.getNetId(null, null));
        assertEquals(-1, wifiUtil.getNetId("", null));
    }

    @Test
    public void addNetworkTest() throws Exception {
        assertEquals(-1, wifiUtil.addNetwork(null, null));
        assertEquals(-1, wifiUtil.addNetwork("", null));
    }

    @Test
    public void isScanResultsContainsSsidTest() throws Exception {
        assertFalse(wifiUtil.isScanResultsContainsSsid(null, null));
        assertFalse(wifiUtil.isScanResultsContainsSsid("", null));
    }

    @Test
    public void isWifiConnectedToAPTest() throws Exception {
        assertFalse(wifiUtil.isWifiConnectedToAP(null, null));
        assertFalse(wifiUtil.isWifiConnectedToAP("", null));
    }

    @Test
    public void forgetCurrentNetworkTest() throws Exception {
        assertFalse(wifiUtil.forgetCurrentNetwork(null));
    }

    @Test
    public void getCurrentWifiTest() throws Exception {
        assertEquals("", wifiUtil.getCurrentWifi(null));
    }
}