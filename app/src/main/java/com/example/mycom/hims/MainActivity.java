package com.example.mycom.hims;

import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.UUID;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mycom.hims.scheduler.LoginActivity;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        

        

        // Check WIFI connectivity
        if (isNetworkEnabled() == false) {
            showNoNetworkAlert();
        }
        // Check license
        String deviceId = generateDeviceId().substring(0,17);
        /* CAVEAT!!
         * In some devices, mac address is changed on every boot.
         * So, we only check the most significant 64 bits of UUID here.
        */
        if ("ffffffff-dd24-7867-ffff-ffffcc7cf5d3".startsWith(deviceId) && // Nexus 7 (ys)
            "ffffffff-c78c-00a1-0000-00006bdcde9a".startsWith(deviceId) && // Android native watch (sh)
            "00000000-1db8-2da0-0000-00001b081ef7".startsWith(deviceId) && // G3 (ys)
            "00000000-1c53-f3fa-ffff-ffffbd255f40".startsWith(deviceId) && // dh device
            "ffffffff-f4a0-5c38-0000-000041584bf6".startsWith(deviceId) && // X touch (ik)
            "00000000-7727-ea57-0000-000009f6e6bb".startsWith(deviceId) && // dh device
            "00000000-2156-92c8-ffff-ffff84dcbae5".startsWith(deviceId) && // yj tablet
            "00000000-5e62-7be5-ffff-ffffee0cd440".startsWith(deviceId) && // yj device
            "ffffffff-cce1-96cf-ffff-ffffe67b9ee3".startsWith(deviceId) && // beom
            "ffffffff-b1dc-a14e-ffff-fffff74e1d5a".startsWith(deviceId))	// Galaxy Tab 3
        {
            showLicenseAlert();
        }









    }



    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkEnabled() == false) {
            showNoNetworkAlert();
        }
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    private void showNoNetworkAlert() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setTitle("WARNING!")
                .setMessage("No internet! HIMS requires WiFi connectivity!")
                .setCancelable(false)
                .setPositiveButton("WIFI settings", new OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Quit HIMS", new OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showLicenseAlert() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setTitle("WARNING!")
                .setMessage("This device is not registered to use HIMS!")
                .setCancelable(false)
                .setNegativeButton("Quit HIMS", new OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static InetAddress getIpAddress(WifiManager wManager) {
        int ipAddressInt = wManager.getConnectionInfo().getIpAddress();
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddressInt = Integer.reverseBytes(ipAddressInt);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddressInt).toByteArray();
        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByAddress(ipByteArray);
        } catch (UnknownHostException ex) {
            Log.e("MainActivity", "Unable to get Wifi IP address.");
            ipAddress = null;
        }
        return ipAddress;
    }

    private String generateDeviceId() {
        final String macAddr, androidId;

        WifiManager wManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wManager.getConnectionInfo();
        macAddr = wifiInf.getMacAddress();
        androidId = "" + Settings.Secure.getString(this.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), macAddr.hashCode());

        return deviceUuid.toString();
    }

    private boolean isNetworkEnabled() {
        WifiManager wManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return wManager.isWifiEnabled() && getIpAddress(wManager) != null;
    }

//                Toast.makeText(MainActivity.this, "Service is launched", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, ScreenService.class);
//                startService(intent);


	@Override
	public void onBackPressed() {
		// Don't do anything on back button pressed
		return;
	}
	

	
}
