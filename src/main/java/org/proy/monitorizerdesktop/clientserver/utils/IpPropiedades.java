package org.proy.monitorizerdesktop.clientserver.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IpPropiedades {

    public String obtenerIP(){

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (!ni.isUp() || ni.isLoopback()) continue;

                String displayName = ni.getDisplayName().toLowerCase();
                if (!displayName.contains("wlan") && !displayName.contains("wi-fi")) continue;

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {

                        return addr.getHostAddress();
                    }
                }
            }

            System.out.println("No se encontró una interfaz inalámbrica activa.");
        } catch (SocketException e) {
            e.printStackTrace();
        }


        return "";
    }
}
