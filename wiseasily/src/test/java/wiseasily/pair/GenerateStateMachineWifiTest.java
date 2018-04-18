package wiseasily.pair;

import android.net.wifi.SupplicantState;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 31/03/18.
 */
public class GenerateStateMachineWifiTest {

    @Test
    public void isSsidWifiConfigEqualsSsidConfigTest() throws Exception {
        ArrayList<Pair> stateMachineEmpty = new ArrayList<>();
        GenerateStateMachineWifi generateStateMachineWifi = new GenerateStateMachineWifi("efishery");
        generateStateMachineWifi.setStateMachine(null, null);
        assertEquals(stateMachineEmpty.toString(), generateStateMachineWifi.getStateMachine().toString());
        generateStateMachineWifi.setStateMachine(null, "");
        assertEquals(stateMachineEmpty.toString(), generateStateMachineWifi.getStateMachine().toString());
        generateStateMachineWifi.setStateMachine(null, "efishery");
        assertEquals(stateMachineEmpty.toString(), generateStateMachineWifi.getStateMachine().toString());

        generateStateMachineWifi.setStateMachine(SupplicantState.SCANNING, "0x");
        String actualScanning0x = generateStateMachineWifi.getStateMachine().toString();
        String expectedScanning0x = "" +
                "[Pair{supplicantState=DISCONNECTED, ssid=\"efishery\"}, " +
                "Pair{supplicantState=ASSOCIATING, ssid=\"efishery\"}, " +
                "Pair{supplicantState=ASSOCIATED, ssid=\"efishery\"}]";
        assertEquals(expectedScanning0x, actualScanning0x);

        generateStateMachineWifi.setStateMachine(SupplicantState.SCANNING, "<unknown ssid>");
        String actualScanningUnkown = generateStateMachineWifi.getStateMachine().toString();
        String expectedScanningUnkown = "" +
                "[Pair{supplicantState=DISCONNECTED, ssid=\"efishery\"}, " +
                "Pair{supplicantState=ASSOCIATING, ssid=\"efishery\"}, " +
                "Pair{supplicantState=ASSOCIATED, ssid=\"efishery\"}]";
        assertEquals(expectedScanningUnkown, actualScanningUnkown);

        generateStateMachineWifi.setStateMachine(SupplicantState.SCANNING, "efishery");
        String actualScanning = generateStateMachineWifi.getStateMachine().toString();
        String expectedScanning = "" +
                "[Pair{supplicantState=DISCONNECTED, ssid=<unknown ssid>}, " +
                "Pair{supplicantState=DISCONNECTED, ssid=0x}, " +
                "Pair{supplicantState=DISCONNECTED, ssid=\"efishery\"}, " +
                "Pair{supplicantState=ASSOCIATING, ssid=\"efishery\"}, " +
                "Pair{supplicantState=ASSOCIATED, ssid=\"efishery\"}]";
        assertEquals(expectedScanning, actualScanning);

        generateStateMachineWifi.setStateMachine(SupplicantState.DISCONNECTED, "0x");
        String actualDisconnected0x = generateStateMachineWifi.getStateMachine().toString();
        String expectedlDisconnected0x = "" +
                "[Pair{supplicantState=SCANNING, ssid=<unknown ssid>}, " +
                "Pair{supplicantState=SCANNING, ssid=0x}, " +
                "Pair{supplicantState=ASSOCIATING, ssid=<unknown ssid>}, " +
                "Pair{supplicantState=ASSOCIATING, ssid=0x}, " +
                "Pair{supplicantState=ASSOCIATED, ssid=\"efishery\"}]";
        assertEquals(expectedlDisconnected0x, actualDisconnected0x);

        generateStateMachineWifi.setStateMachine(SupplicantState.DISCONNECTED, "<unknown ssid>");
        String actualDisconnectedUnkown = generateStateMachineWifi.getStateMachine().toString();
        String expectedlDisconnectedUnkown = "" +
                "[Pair{supplicantState=SCANNING, ssid=<unknown ssid>}, " +
                "Pair{supplicantState=SCANNING, ssid=0x}, " +
                "Pair{supplicantState=ASSOCIATING, ssid=<unknown ssid>}, " +
                "Pair{supplicantState=ASSOCIATING, ssid=0x}, " +
                "Pair{supplicantState=ASSOCIATED, ssid=\"efishery\"}]";
        assertEquals(expectedlDisconnectedUnkown, actualDisconnectedUnkown);

        generateStateMachineWifi.setStateMachine(SupplicantState.DISCONNECTED, "efishery");
        String actualDisconnected = generateStateMachineWifi.getStateMachine().toString();
        String expectedlDisconnected = "" +
                "[Pair{supplicantState=SCANNING, ssid=<unknown ssid>}, " +
                "Pair{supplicantState=SCANNING, ssid=0x}, " +
                "Pair{supplicantState=SCANNING, ssid=\"efishery\"}, " +
                "Pair{supplicantState=ASSOCIATING, ssid=\"efishery\"}, " +
                "Pair{supplicantState=ASSOCIATED, ssid=\"efishery\"}]";
        assertEquals(expectedlDisconnected, actualDisconnected);

        generateStateMachineWifi.setStateMachine(SupplicantState.ASSOCIATING, "0x");
        String actualAssociating0x = generateStateMachineWifi.getStateMachine().toString();
        assertEquals(stateMachineEmpty.toString(), actualAssociating0x);

        generateStateMachineWifi.setStateMachine(SupplicantState.ASSOCIATING, "<unknown ssid>");
        String actualAssociatingUnkown = generateStateMachineWifi.getStateMachine().toString();
        assertEquals(stateMachineEmpty.toString(), actualAssociatingUnkown);

        generateStateMachineWifi.setStateMachine(SupplicantState.ASSOCIATING, "efishery");
        String actualAssociating = generateStateMachineWifi.getStateMachine().toString();
        String expectedlAssociating = "" +
                "[Pair{supplicantState=DISCONNECTED, ssid=\"efishery\"}, " +
                "Pair{supplicantState=ASSOCIATED, ssid=\"efishery\"}]";
        assertEquals(expectedlAssociating, actualAssociating);

        generateStateMachineWifi.setStateMachine(SupplicantState.ASSOCIATED, "0x");
        String actualAssociated0x = generateStateMachineWifi.getStateMachine().toString();
        assertEquals(stateMachineEmpty.toString(), actualAssociated0x);

        generateStateMachineWifi.setStateMachine(SupplicantState.ASSOCIATED, "<unknown ssid>");
        String actualAssociatedUnkown = generateStateMachineWifi.getStateMachine().toString();
        assertEquals(stateMachineEmpty.toString(), actualAssociatedUnkown);

        generateStateMachineWifi.setStateMachine(SupplicantState.ASSOCIATED, "efishery");
        String actualAssociated = generateStateMachineWifi.getStateMachine().toString();
        String expectedlAssociated = "" +
                "[Pair{supplicantState=FOUR_WAY_HANDSHAKE, ssid=\"efishery\"}, " +
                "Pair{supplicantState=GROUP_HANDSHAKE, ssid=\"efishery\"}, " +
                "Pair{supplicantState=COMPLETED, ssid=\"efishery\"}]";
        assertEquals(expectedlAssociated, actualAssociated);

        generateStateMachineWifi.setStateMachine(SupplicantState.FOUR_WAY_HANDSHAKE, "0x");
        String actualFourWayHandshake0x = generateStateMachineWifi.getStateMachine().toString();
        assertEquals(stateMachineEmpty.toString(), actualFourWayHandshake0x);

        generateStateMachineWifi.setStateMachine(SupplicantState.FOUR_WAY_HANDSHAKE, "<unknown ssid>");
        String actualFourWayHandshakeUnkown = generateStateMachineWifi.getStateMachine().toString();
        assertEquals(stateMachineEmpty.toString(), actualFourWayHandshakeUnkown);

        generateStateMachineWifi.setStateMachine(SupplicantState.FOUR_WAY_HANDSHAKE, "efishery");
        String actualFourWayHandshake = generateStateMachineWifi.getStateMachine().toString();
        String expectedlFourWayHandshake = "" +
                "[Pair{supplicantState=GROUP_HANDSHAKE, ssid=\"efishery\"}, " +
                "Pair{supplicantState=COMPLETED, ssid=\"efishery\"}]";
        assertEquals(expectedlFourWayHandshake, actualFourWayHandshake);

        generateStateMachineWifi.setStateMachine(SupplicantState.GROUP_HANDSHAKE, "0x");
        String actualGroupHandshake0x = generateStateMachineWifi.getStateMachine().toString();
        assertEquals(stateMachineEmpty.toString(), actualGroupHandshake0x);

        generateStateMachineWifi.setStateMachine(SupplicantState.GROUP_HANDSHAKE, "<unknown ssid>");
        String actualGroupHandshakeUnkown = generateStateMachineWifi.getStateMachine().toString();
        assertEquals(stateMachineEmpty.toString(), actualGroupHandshakeUnkown);

        generateStateMachineWifi.setStateMachine(SupplicantState.GROUP_HANDSHAKE, "efishery");
        String actualGroupHandshake = generateStateMachineWifi.getStateMachine().toString();
        String expectedGroupHandshake = "" +
                "[Pair{supplicantState=COMPLETED, ssid=\"efishery\"}]";
        assertEquals(expectedGroupHandshake, actualGroupHandshake);
    }
}