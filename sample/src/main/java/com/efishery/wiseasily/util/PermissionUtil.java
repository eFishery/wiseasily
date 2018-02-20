package com.efishery.wiseasily.util;

import android.content.pm.PackageManager;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 25/01/18.
 */

public abstract class PermissionUtil {

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
