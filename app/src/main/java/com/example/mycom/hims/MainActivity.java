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

    private ImageView onBtn, offBtn;
    private PendingIntent restartIntent;
    private UncaughtExceptionHandler uncaughtHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onBtn= (ImageView)findViewById(R.id.on_btn);
        offBtn= (ImageView)findViewById(R.id.off_btn);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
//        uncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
//        Thread.setDefaultUncaughtExceptionHandler(new AppRestarter());
        
        restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getIntent()), getIntent().getFlags());
        
//        // REST API Test
//    	LoginResponse loginResponse = SchedulerServerAPI.login("admin", "1");
//    	String token = loginResponse.getToken();
//        if (!TextUtils.isEmpty(token)) {
//        	QueryHimsServer.setToken(token);
//        	GetUsersResponse response1 = SchedulerServerAPI.getUsers(null, null, null, null);
//            GetMyInfoResponse response2 = SchedulerServerAPI.getMyInfo();
//            GetRoomsResponse response3 = SchedulerServerAPI.getRooms(null, null, null);
//            GetFormsResponse response4 = SchedulerServerAPI.getForms(null, null, null, null);
//            GetFormsResponse response5 = SchedulerServerAPI.getFormById("1");
//            GetEvalResponse response6 = SchedulerServerAPI.getEvaluation(null, null, null);
//            LogoutResponse response7 = SchedulerServerAPI.logout();
//        }

        // Check WIFI connectivity
        if (isNetworkEnabled() == false) {
            showNoNetworkAlert();
        }
        // Check license
        String deviceId = generateDeviceId();
        /* CAVEAT!!
         * In some devices, mac address is changed on every boot.
         * So, we only check the most significant 64 bits of UUID here.
        */
        if (deviceId.substring(0, 17).compareToIgnoreCase("ffffffff-dd24-7867-ffff-ffffcc7cf5d3".substring(0, 17)) != 0 && // Nexus 7 (ys)
            deviceId.substring(0, 17).compareToIgnoreCase("ffffffff-c78c-00a1-0000-00006bdcde9a".substring(0, 17)) != 0 && // Android native watch (sh)
            deviceId.substring(0, 17).compareToIgnoreCase("00000000-1db8-2da0-0000-00001b081ef7".substring(0, 17)) != 0 && // G3 (ys)
            deviceId.substring(0, 17).compareToIgnoreCase("00000000-1c53-f3fa-ffff-ffffbd255f40".substring(0, 17)) != 0 && // dh device
            deviceId.substring(0, 17).compareToIgnoreCase("ffffffff-f4a0-5c38-0000-000041584bf6".substring(0, 17)) != 0 && // X touch (ik)
            deviceId.substring(0, 17).compareToIgnoreCase("00000000-7727-ea57-0000-000009f6e6bb".substring(0, 17)) != 0 && // dh device
            deviceId.substring(0, 17).compareToIgnoreCase("00000000-2156-92c8-ffff-ffff84dcbae5".substring(0, 17)) != 0 && // yj tablet
            deviceId.substring(0, 17).compareToIgnoreCase("00000000-5e62-7be5-ffff-ffffee0cd440".substring(0, 17)) != 0 && // yj device
            deviceId.substring(0, 17).compareToIgnoreCase("ffffffff-cce1-96cf-ffff-ffffe67b9ee3".substring(0, 17)) != 0 && // beom
            deviceId.substring(0, 17).compareToIgnoreCase("ffffffff-b1dc-a14e-ffff-fffff74e1d5a".substring(0, 17)) != 0)	// Galaxy Tab 3
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

    public void onClick(View v){
        switch(v.getId()){
            case R.id.on_btn:
                Toast.makeText(MainActivity.this, "Service is launched", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ScreenService.class);
                startService(intent);
                Intent intent3 = new Intent(this, RegistrationIntentService.class);
                startService(intent3);
                //편하게하기위해서
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);

                break;
            case R.id.off_btn:
                Toast.makeText(MainActivity.this, "Service is terminated", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, ScreenService.class);
                stopService(intent2);
                break;
        }
    }

	@Override
	public void onBackPressed() {
		// Don't do anything on back button pressed
		Log.d("MainActivity", "Back button pressed!");
		return;
	}
	
	private class AppRestarter implements Thread.UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restartIntent);
			System.exit(2);
		}
	}
	
}
