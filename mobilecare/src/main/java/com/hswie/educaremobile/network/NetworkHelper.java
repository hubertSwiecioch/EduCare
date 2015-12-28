package com.hswie.educaremobile.network;



import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by hswie on 12/20/2015.
 */
public class NetworkHelper {
    public static int calculateSignalLevel(int rssi, int numLevels) {
        int MIN_RSSI = -100;
        int MAX_RSSI = -55;
        if (rssi <= MIN_RSSI) {
            return 0;
        } else if (rssi >= MAX_RSSI) {
            return numLevels - 1;
        } else {
            float inputRange = (MAX_RSSI - MIN_RSSI);
            float outputRange = (numLevels - 1);
            if (inputRange != 0)
                return (int) ((float) (rssi - MIN_RSSI) * outputRange / inputRange);
        }
        return 0;
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static boolean isConnectedWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }


    public static String getNetworkInfo(Context context) {
        final WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        int state = wifi.getWifiState();
        if (state == WifiManager.WIFI_STATE_ENABLED) {
            List<ScanResult> results = wifi.getScanResults();

            for (ScanResult result : results) {
                if (result.BSSID.equals(wifi.getConnectionInfo()
                        .getBSSID())) {
                    int signalStrength = NetworkHelper.calculateSignalLevel(wifi
                            .getConnectionInfo().getRssi(), 101);

                    String msg = "SignalStrength = "
                            + signalStrength
                            + "\nSSID = "
                            + result.SSID
                            + "\ncapabilities = "
                            + result.capabilities
                            + "\nfrequency = "
                            + result.frequency + " (" + getChannelFromFrequency(result.frequency) + ")"
                            + "\nlevel = "
                            + result.level
                            ;

                    msg += "\n"

                            + "\nLinkSpeed = "
                            + wifi.getConnectionInfo().getLinkSpeed()
                            + "\nNetworkId = "
                            + wifi.getConnectionInfo().getNetworkId()
                            + "\nRssi = "
                            + wifi.getConnectionInfo().getRssi()
                            + "\nSupplicantState = "
                            + wifi.getConnectionInfo().getSupplicantState();

                    Log.d("test",
                            "signalStrength = "
                                    + signalStrength + ", "
                                    + result.toString());

                    Log.d("test",
                            "HiddenSSID: "
                                    + wifi.getConnectionInfo().getHiddenSSID() + ", "
                                    + "IpAddress: "
                                    + wifi.getConnectionInfo().getIpAddress() + ", "
                                    + wifi.getConnectionInfo().toString());

                    return msg;
                }
            }
        }
        return null;
    }


    @SuppressWarnings("boxing")
    private final static ArrayList<Integer> channelsFrequency = new ArrayList<Integer>(
            Arrays.asList(0, 2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447,
                    2452, 2457, 2462, 2467, 2472, 2484));

    private static Integer getFrequencyFromChannel(int channel) {
        return channelsFrequency.get(channel);
    }

    private static int getChannelFromFrequency(int frequency) {
        return channelsFrequency.indexOf(Integer.valueOf(frequency));
    }

    public static void createDialog(Context context, String title, String msg) {
        AlertDialog alertDialog = (new AlertDialog.Builder(context)).setMessage(msg)
                .setPositiveButton(android.R.string.ok, null)
                .setTitle(title)
                .create();
        alertDialog.show();
    }

    public static String getIPAddress() {
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();
                Enumeration<InetAddress> addresses = network.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    String address = addresses.nextElement().getHostAddress();
                    if (!"127.0.0.1".equals(address) && !"0.0.0.0".equals(address)) {
                        return address;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return "127.0.0.1";
    }

    public static String getHostAddress(String domain) {
        String ip = null;

        try {
            InetAddress ia = InetAddress.getByName(domain);
            ip = ia.getHostAddress();
        } catch (UnknownHostException e) {
            Log.d("test", "getHostAddress e=" + e.getMessage());
        }

        return ip;
    }

    public static ArrayList<String> getAllHostAddresses(String domain) {
        ArrayList<String> addresses = new ArrayList<String>();

        try {
            InetAddress[] inetAddresses = InetAddress.getAllByName(domain);
            for (InetAddress inetAddress : inetAddresses) {
                String ip = inetAddress.getHostAddress();
                addresses.add(ip);
            }
        } catch (UnknownHostException e) {
        }

        return addresses;
    }
}
