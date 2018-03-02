package wiseasily.pair;

import android.net.wifi.SupplicantState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static wiseasily.util.WifiUtil.getConfigFormatSSID;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 13/02/18.
 */

public class ListOfStateMachineWifi {
    public ArrayList<Pair> getStateMachine() {
        return stateMachine;
    }

    private ArrayList<Pair> stateMachine = new ArrayList<>();

    public ListOfStateMachineWifi(SupplicantState supplicantState, String ssid) {
        String ssidConfig = getConfigFormatSSID(ssid);
        Pair<SupplicantState, String> scanningUnknown = new Pair<>(SupplicantState.SCANNING, "<unknown ssid>");
        Pair<SupplicantState, String> scanningOx = new Pair<>(SupplicantState.SCANNING, "0x");
        Pair<SupplicantState, String> scanningSsid = new Pair<>(SupplicantState.SCANNING, ssidConfig);
        Pair<SupplicantState, String> disconnectedUnkown = new Pair<>(SupplicantState.DISCONNECTED, "<unknown ssid>");
        Pair<SupplicantState, String> disconnectedOx = new Pair<>(SupplicantState.DISCONNECTED, "0x");
        Pair<SupplicantState, String> disconnectedSsid = new Pair<>(SupplicantState.DISCONNECTED, ssidConfig);
        Pair<SupplicantState, String> associatingUnkown = new Pair<>(SupplicantState.ASSOCIATING, "<unknown ssid>");
        Pair<SupplicantState, String> associatingOx = new Pair<>(SupplicantState.ASSOCIATING, "0x");
        Pair<SupplicantState, String> associatingSsid = new Pair<>(SupplicantState.ASSOCIATING, ssidConfig);
        Pair<SupplicantState, String> associatedSsid = new Pair<>(SupplicantState.ASSOCIATED, ssidConfig);
        Pair<SupplicantState, String> fourWayHandshakeSsid = new Pair<>(SupplicantState.FOUR_WAY_HANDSHAKE, ssidConfig);
        Pair<SupplicantState, String> groupHandshakeSsid = new Pair<>(SupplicantState.GROUP_HANDSHAKE, ssidConfig);
        Pair<SupplicantState, String> completedSsid = new Pair<>(SupplicantState.COMPLETED, ssidConfig);
        if(supplicantState == SupplicantState.SCANNING && (ssid.equals("<unknown ssid>") || ssid.equals("0x"))){
            stateMachine = new ArrayList<>();
            stateMachine.addAll(Arrays.asList(disconnectedSsid, associatingSsid, associatedSsid));
        }else if(supplicantState == SupplicantState.SCANNING && (!ssid.equals("<unknown ssid>") && !ssid.equals("0x"))){
            stateMachine = new ArrayList<>();
            stateMachine.addAll(Arrays.asList(disconnectedUnkown, disconnectedOx, disconnectedSsid, associatedSsid));
        }else if(supplicantState == SupplicantState.DISCONNECTED && (ssid.equals("<unknown ssid>") || ssid.equals("0x"))){
            stateMachine = new ArrayList<>();
            stateMachine.addAll(Arrays.asList(scanningUnknown, scanningOx, associatingUnkown, associatingOx, associatedSsid));
        }else if(supplicantState == SupplicantState.DISCONNECTED && (!ssid.equals("<unknown ssid>") && !ssid.equals("0x"))){
            stateMachine = new ArrayList<>();
            stateMachine.addAll(Arrays.asList(scanningSsid, associatingSsid, associatedSsid));
        }else if(supplicantState == SupplicantState.ASSOCIATING && (!ssid.equals("<unknown ssid>") && !ssid.equals("0x"))){
            stateMachine = new ArrayList<>();
            stateMachine.addAll(Arrays.asList(disconnectedSsid, associatedSsid));
        }else if(supplicantState == SupplicantState.ASSOCIATED && (!ssid.equals("<unknown ssid>") && !ssid.equals("0x"))){
            stateMachine = new ArrayList<>();
            stateMachine.addAll(Arrays.asList(fourWayHandshakeSsid, completedSsid));
        }else if(supplicantState == SupplicantState.FOUR_WAY_HANDSHAKE && (!ssid.equals("<unknown ssid>") && !ssid.equals("0x"))){
            stateMachine = new ArrayList<>();
            stateMachine.addAll(Arrays.asList(groupHandshakeSsid, completedSsid));
        }else if(supplicantState == SupplicantState.GROUP_HANDSHAKE && (!ssid.equals("<unknown ssid>") && !ssid.equals("0x"))){
            stateMachine = new ArrayList<>();
            stateMachine.addAll(Collections.singletonList(completedSsid));
        }else {
            stateMachine = new ArrayList<>();
        }
    }
}
