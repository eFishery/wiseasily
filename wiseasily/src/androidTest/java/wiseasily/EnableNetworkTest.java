package wiseasily;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 10/29/17.
 */
public class EnableNetworkTest extends BaseWisEasilyTest{


    private EnableNetwork enableNetwork;

    @Before
    public void setUp() {
        enableNetwork = new EnableNetwork(InstrumentationRegistry.getContext());

    }

    @Test
    public void enable() throws Exception {
    }

    @Test
    public void isScanContainsSsid() throws Exception {
    }

}