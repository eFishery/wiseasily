package wiseasily;

import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 10/29/17.
 */

@RunWith(AndroidJUnit4.class)
public class BaseWisEasilyTest {

    ConnectivityManager mMockConnectivityManager;

    protected WifiManager mMockWiFiManager;

    @Before
    public void setUp() {
        mMockWiFiManager = mock(WifiManager.class);
        mMockConnectivityManager = mock(ConnectivityManager.class);

        mMockWiFiManager = mock(WifiManager.class);
        mMockConnectivityManager = mock(ConnectivityManager.class);

    }

}
