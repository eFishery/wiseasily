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
 * last update = 7 June 2018
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
            Pair<SupplicantState, String> inactiveUnknown = new Pair<>(SupplicantState.INACTIVE, "<unknown ssid>");
            Pair<SupplicantState, String> inactiveOx = new Pair<>(SupplicantState.INACTIVE, "0x");
            Pair<SupplicantState, String> scanningUnknown = new Pair<>(SupplicantState.SCANNING, "<unknown ssid>");
            Pair<SupplicantState, String> scanningOx = new Pair<>(SupplicantState.SCANNING, "0x");
            Pair<SupplicantState, String> scanningSsid = new Pair<>(SupplicantState.SCANNING, ssidConfig);
            Pair<SupplicantState, String> disconnectedUnkown = new Pair<>(SupplicantState.DISCONNECTED, "<unknown ssid>");
            Pair<SupplicantState, String> disconnectedOx = new Pair<>(SupplicantState.DISCONNECTED, "0x");
            Pair<SupplicantState, String> disconnectedSsid = new Pair<>(SupplicantState.DISCONNECTED, ssidConfig);
            Pair<SupplicantState, String> associatingUnkown = new Pair<>(SupplicantState.ASSOCIATING, "<unknown ssid>");
            Pair<SupplicantState, String> associatingOx = new Pair<>(SupplicantState.ASSOCIATING, "0x");
            Pair<SupplicantState, String> associatingSsid = new Pair<>(SupplicantState.ASSOCIATING, ssidConfig);
            Pair<SupplicantState, String> associatedUnkown = new Pair<>(SupplicantState.ASSOCIATED, "<unknown ssid>");
            Pair<SupplicantState, String> associatedOx = new Pair<>(SupplicantState.ASSOCIATED, "0x");
            Pair<SupplicantState, String> associatedSsid = new Pair<>(SupplicantState.ASSOCIATED, ssidConfig);
            Pair<SupplicantState, String> fourWayHandshakeSsid = new Pair<>(SupplicantState.FOUR_WAY_HANDSHAKE, ssidConfig);
            Pair<SupplicantState, String> groupHandshakeSsid = new Pair<>(SupplicantState.GROUP_HANDSHAKE, ssidConfig);
            Pair<SupplicantState, String> completedSsid = new Pair<>(SupplicantState.COMPLETED, ssidConfig);

            ArrayList<PairStateMachine<Pair<SupplicantState, String>, ArrayList<Pair>>>
                listStateMachine = new ArrayList<>(
                        Arrays.asList(
                                new PairStateMachine<>(inactiveUnknown,
                                        new ArrayList<>(Arrays.asList(inactiveUnknown, inactiveOx, disconnectedSsid))),
                                new PairStateMachine<>(inactiveOx,
                                        new ArrayList<>(Arrays.asList(inactiveUnknown, inactiveOx, disconnectedSsid))),
                                new PairStateMachine<>(scanningUnknown,
                                        new ArrayList<>(Arrays.asList(scanningOx, scanningUnknown, disconnectedUnkown, disconnectedOx, disconnectedSsid, associatingSsid, associatedSsid))),
                                new PairStateMachine<>(scanningOx,
                                        new ArrayList<>(Arrays.asList(scanningOx, scanningUnknown, disconnectedUnkown, disconnectedOx, disconnectedSsid, associatingSsid, associatedSsid))),
                                new PairStateMachine<>(scanningSsid,
                                        new ArrayList<>(Arrays.asList(disconnectedUnkown, disconnectedOx, disconnectedSsid, associatingUnkown, associatingOx, associatingSsid, associatedSsid))),
                                new PairStateMachine<>(disconnectedUnkown,
                                        new ArrayList<>(Arrays.asList(scanningSsid, disconnectedUnkown, disconnectedOx, associatingUnkown, associatingOx, associatingSsid, associatedUnkown, associatedOx, associatedSsid))),
                                new PairStateMachine<>(disconnectedOx,
                                        new ArrayList<>(Arrays.asList(scanningSsid, disconnectedUnkown, disconnectedOx, associatingUnkown, associatingOx, associatingSsid, associatedUnkown, associatedOx, associatedSsid))),
                                new PairStateMachine<>(disconnectedSsid,
                                        new ArrayList<>(Arrays.asList(scanningUnknown, scanningOx, scanningSsid, disconnectedUnkown, disconnectedOx, disconnectedSsid, associatingUnkown, associatingOx, associatingSsid, associatedUnkown, associatedOx, associatedSsid))),
                                new PairStateMachine<>(associatingUnkown,
                                        new ArrayList<>(Arrays.asList(disconnectedUnkown, disconnectedOx, disconnectedSsid, associatedUnkown, associatedOx, associatedSsid))),
                                new PairStateMachine<>(associatingOx,
                                        new ArrayList<>(Arrays.asList(disconnectedUnkown, disconnectedOx, disconnectedSsid, associatedUnkown, associatedOx, associatedSsid))),
                                new PairStateMachine<>(associatingSsid,
                                        new ArrayList<>(Arrays.asList(disconnectedUnkown, disconnectedOx, disconnectedSsid, associatedSsid))),
                                new PairStateMachine<>(associatedUnkown,
                                        new ArrayList<>(Arrays.asList(disconnectedUnkown, disconnectedOx))),
                                new PairStateMachine<>(associatedOx,
                                        new ArrayList<>(Arrays.asList(disconnectedUnkown, disconnectedOx))),
                                new PairStateMachine<>(associatedSsid,
                                        new ArrayList<>(Arrays.asList(disconnectedUnkown, disconnectedOx, fourWayHandshakeSsid, groupHandshakeSsid, completedSsid))),
                                new PairStateMachine<>(fourWayHandshakeSsid,
                                        new ArrayList<>(Arrays.asList(disconnectedSsid, groupHandshakeSsid, completedSsid))),
                                new PairStateMachine<>(groupHandshakeSsid,
                                        new ArrayList<>(Arrays.asList(disconnectedSsid, completedSsid)))
                        )
            );

            Pair<SupplicantState, String> pairCurrent = new Pair<>(supplicantState, ssid);

            for(PairStateMachine<
                    Pair<SupplicantState, String>,
                    ArrayList<Pair>> machine : listStateMachine){
                if(machine.getPairState().equals(pairCurrent)){
                    stateMachine = machine.getPairStateArrayList();
                }
            }

        }
    }
}
