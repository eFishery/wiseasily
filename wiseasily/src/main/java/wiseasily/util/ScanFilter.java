package wiseasily.util;

import android.net.wifi.ScanResult;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wiseasily.util.util.ProximityUtils;
import wiseasily.util.util.WifiUtils;

/**
 * @author Jonas Sevcik
 */
public class ScanFilter {

    //private static final Pattern BSSID_PATTERN = Pattern.compile("([\\p{XDigit}]{2}:){5}[\\p{XDigit}]{2}");

    @Nullable
    private String mac;
    @Nullable
    private String ssid;
    @Nullable
    private Set<String> ssids;
    @Nullable
    private Set<Integer> channels;
    @Nullable
    private Proximity proximity;

    /**
     * Filter parameters. Enter null for no filtering.
     *
     * @param mac      Ethernet MAC address, e.g., XX:XX:XX:XX:XX:XX where each X is a hex digit
     * @param ssid     service set identifier of the 802.11 network
     * @param channels Set of channel numbers
     * @throws IllegalArgumentException if {@code mac}'s length > 17 chars or {@code ssid}'s length > 32 chars
     */
    public ScanFilter(@Nullable String mac, @Nullable String ssid, @Nullable Set<String> ssids, @Nullable Set<Integer> channels, @Nullable Proximity proximity) {
        if (mac != null && mac.length() > 17) {
            throw new IllegalArgumentException("Mac longer than 17 chars");
        }
        if (ssid != null && ssid.length() > 32) {
            throw new IllegalArgumentException("SSID longer than 32 chars");
        }

        if (mac != null) {
            this.mac = mac.toLowerCase();
        }
        if (ssid != null) {
            this.ssid = ssid.toLowerCase();
        }
        this.channels = channels;

        if(ssids!=null){
            Set<String> ssidsAssign = new HashSet<>();
            for(String id : ssids){
                ssidsAssign.add(id.toLowerCase());
            }
            this.ssids = ssidsAssign;
        }
        this.proximity = proximity;
    }

    public boolean matchesStart(ScanResult scanResult) {
        return scanResult != null && !(mac != null && !scanResult.BSSID.toLowerCase().startsWith(mac)) && !(ssid != null && !scanResult.SSID.toLowerCase().startsWith(ssid)) && !(ssids != null && !ssids.contains(scanResult.SSID.toLowerCase())) && !(channels != null && !channels.contains(WifiUtils.toChannel(scanResult.frequency))) && !(proximity != null && proximity != ProximityUtils.getProximity(scanResult.level, scanResult.frequency));

    }
}
