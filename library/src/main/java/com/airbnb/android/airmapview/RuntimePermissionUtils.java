package com.airbnb.android.airmapview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Utility class that handles runtime permissions
 */
final class RuntimePermissionUtils {

  private RuntimePermissionUtils() {
  }

  private static final byte LOCATION_PERMISSION_REQUEST_CODE = 1;
  private static final String[] LOCATION_PERMISSIONS =
          new String[]{"android.permission.ACCESS_FINE_LOCATION",
                  "android.permission.ACCESS_COARSE_LOCATION"};

  /**
   * Verifies that any of the given permissions have been granted.
   *
   * @return returns true if all permissions have been granted.
   */
  private static boolean verifyPermissions(int... grantResults) {
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check the permissions are the requested permissions
   *
   * @param permissions
   * @return true if the permissions are matched perfectly
   */
  static boolean isRequestedPermission(String[] permissions) {
    if (permissions.length != LOCATION_PERMISSIONS.length) {
      return false;
    }

    final Set<String> requestPermissions = new HashSet<>(Arrays.asList(LOCATION_PERMISSIONS));
    for (String permission : permissions) {
      if (!requestPermissions.contains(permission)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns true if the context has access to any given permissions.
   */
  private static boolean hasSelfPermissions(Context context, String... permissions) {
    for (String permission : permissions) {
      if (checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks given permissions are needed to show rationale.
   *
   * @return returns true if one of the permission is needed to show rationale.
   */
  static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
    for (String permission : permissions) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if any location permissions are granted, and invoke onLocationPermissionsGranted() in the
   * callback if granted. It will ask users for location permissions and invoke the same callback if
   * no permission was granted and got granted at runtime.
   *
   * @param airMapInterface the callback interface if permission is granted.
   */
  static void checkLocationPermissions(Activity targetActivity, AirMapInterface airMapInterface) {
    if (hasSelfPermissions(targetActivity, LOCATION_PERMISSIONS)) {
      airMapInterface.onLocationPermissionsGranted();
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      targetActivity.requestPermissions(LOCATION_PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
    } else {
      airMapInterface.onLocationPermissionsDenied();
    }
  }

  /**
   * Dispatch actions based off requested permission results.
   * The activity or fragment must call this function in onRequestPermissionsResult
   * {@link android.support.v4.app.Fragment#onRequestPermissionsResult(int, String[], int[])}
   * {@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])}<br />
   * Further actions like
   * 1> Rationale: showing a snack bar to explain why the permissions are needed and
   * 2> Denied: adding airMapInterface.onLocationPermissionsDenied()
   * should be added here if needed.
   *
   */
  static void onRequestPermissionsResult(Activity activity, AirMapInterface airMapInterface, int requestCode,
          String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case LOCATION_PERMISSION_REQUEST_CODE:
        if (!isRequestedPermission(permissions)) {
          break;
        }

        if (verifyPermissions(grantResults)) {
          airMapInterface.onLocationPermissionsGranted();
        } else if (!shouldShowRequestPermissionRationale(activity, LOCATION_PERMISSIONS)) {
            airMapInterface.onLocationPermissionsNeverAskAgain();
        } else {
            airMapInterface.onLocationPermissionsDenied();
        }
        break;
      default:
        break;
    }
  }
}
