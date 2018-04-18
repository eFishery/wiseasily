package wiseasily.pair;

import android.net.wifi.SupplicantState;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import wiseasily.util.WifiUtil;


/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 13/02/18.
 */

public class GenerateStateMachineWifi {

    private String ssidToConnect = "";
    private ArrayList<Pair> stateMachine = new ArrayList<>();

    public ArrayList<Pair> getStateMachine() {
        return stateMachine;
    }

    public GenerateStateMachineWifi(@NonNull String ssidToConnect) {
        if(ssidToConnect!=null && !ssidToConnect.isEmpty()){
            this.ssidToConnect = ssidToConnect;
        }
    }

    public void setStateMachine(@NonNull SupplicantState supplicantState, @NonNull String ssid) {
        if(supplicantState!=null && ssid!=null && !ssid.isEmpty()){
            String ssidConfig = new WifiUtil().getConfigFormatSSID(ssidToConnect);
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
            if(supplicantState == SupplicantState.SCANNING && (ssid.contains("<unknown ssid>") || ssid.contains("0x"))){
                stateMachine = new ArrayList<>();
                stateMachine.addAll(Arrays.asList(disconnectedSsid, associatingSsid, associatedSsid));
            }else if(supplicantState == SupplicantState.SCANNING && (!ssid.contains("<unknown ssid>") && !ssid.contains("0x"))){
                stateMachine = new ArrayList<>();
                stateMachine.addAll(Arrays.asList(disconnectedUnkown, disconnectedOx, disconnectedSsid, associatingSsid, associatedSsid));
            }else if(supplicantState == SupplicantState.DISCONNECTED && (ssid.contains("<unknown ssid>") || ssid.contains("0x"))){
                stateMachine = new ArrayList<>();
                stateMachine.addAll(Arrays.asList(scanningUnknown, scanningOx, associatingUnkown, associatingOx, associatedSsid));
            }else if(supplicantState == SupplicantState.DISCONNECTED && (!ssid.contains("<unknown ssid>") && !ssid.contains("0x"))){
                stateMachine = new ArrayList<>();
                stateMachine.addAll(Arrays.asList(scanningUnknown, scanningOx, scanningSsid, associatingSsid, associatedSsid));
            }else if(supplicantState == SupplicantState.ASSOCIATING && (!ssid.contains("<unknown ssid>") && !ssid.contains("0x"))){
                stateMachine = new ArrayList<>();
                stateMachine.addAll(Arrays.asList(disconnectedSsid, associatedSsid));
            }else if(supplicantState == SupplicantState.ASSOCIATED && (!ssid.contains("<unknown ssid>") && !ssid.contains("0x"))){
                stateMachine = new ArrayList<>();
                stateMachine.addAll(Arrays.asList(fourWayHandshakeSsid, groupHandshakeSsid, completedSsid));
            }else if(supplicantState == SupplicantState.FOUR_WAY_HANDSHAKE && (!ssid.contains("<unknown ssid>") && !ssid.contains("0x"))){
                stateMachine = new ArrayList<>();
                stateMachine.addAll(Arrays.asList(groupHandshakeSsid, completedSsid));
            }else if(supplicantState == SupplicantState.GROUP_HANDSHAKE && (!ssid.contains("<unknown ssid>") && !ssid.contains("0x"))){
                stateMachine = new ArrayList<>();
                stateMachine.addAll(Collections.singletonList(completedSsid));
            }else {
                stateMachine = new ArrayList<>();
            }
        }
    }
}
