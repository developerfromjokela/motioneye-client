package com.developerfromjokela.motioneyeclient.ui.utils;

import android.os.AsyncTask;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;

public class ConnectionUtils {

    /**
     * Check if host is reachable.
     *
     * @param host    The host to check for availability. Can either be a machine name, such as "google.com",
     *                or a textual representation of its IP address, such as "8.8.8.8".
     * @param port    The port number.
     * @param timeout The timeout in milliseconds.
     * @return True if the host is reachable. False otherwise.
     */
    private static boolean isHostAvailable(final String host, final int port, final int timeout) {
        try (final Socket socket = new Socket()) {
            final InetAddress inetAddress = InetAddress.getByName(host);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);

            socket.connect(inetSocketAddress, timeout);
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface HostAvailabilityCheck {
        void onResult(boolean available);
    }

    public static void isHostAvailable(final String host, final int port, final int timeout, HostAvailabilityCheck check) {
        new HostCheckAsync(host, port, timeout, check).execute();
    }

    private static class HostCheckAsync extends AsyncTask<String, String, Boolean> {

        private String host;
        private int port, timeout;
        private HostAvailabilityCheck listener;

        public HostCheckAsync(String host, int port, int timeout, HostAvailabilityCheck listener) {
            this.host = host;
            this.port = port;
            this.timeout = timeout;
            this.listener = listener;
        }


        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                return isHostAvailable(URLUtils.getDomainName(host), port, timeout);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            listener.onResult(aBoolean);
        }
    }
}
