package wiseasily.pair;

import android.net.wifi.SupplicantState;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 17/04/18.
 */
public class UtilPairTest {
    UtilPair utilPair = new UtilPair();

    @Test
    public void getStateSsidPairTest() throws Exception {
        assertFalse(utilPair.containsPair(null, null));
        assertFalse(utilPair.containsPair(new ArrayList<>(), null));

        GenerateStateMachineWifi generateStateMachineWifi = new GenerateStateMachineWifi("efishery");

        generateStateMachineWifi.setStateMachine(SupplicantState.FOUR_WAY_HANDSHAKE, "efishery");

        Pair<SupplicantState, String> pairScanningUnkown = utilPair.getStateSsidPair(SupplicantState.SCANNING, "<unknown ssid>");

        assertFalse(utilPair.containsPair(generateStateMachineWifi.getStateMachine(), pairScanningUnkown));

        Pair<SupplicantState, String> pairScanning0x = utilPair.getStateSsidPair(SupplicantState.SCANNING, "0x");

        assertFalse(utilPair.containsPair(generateStateMachineWifi.getStateMachine(), pairScanning0x));

        Pair<SupplicantState, String> pairDisconnectedSsid = utilPair.getStateSsidPair(SupplicantState.DISCONNECTED, "efishery");

        assertFalse(utilPair.containsPair(generateStateMachineWifi.getStateMachine(), pairDisconnectedSsid));

        Pair<SupplicantState, String> pairGroupHandshakeSsid = utilPair.getStateSsidPair(SupplicantState.GROUP_HANDSHAKE, "efishery");

        assertFalse(utilPair.containsPair(generateStateMachineWifi.getStateMachine(), pairGroupHandshakeSsid));
    }
}