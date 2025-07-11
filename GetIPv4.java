package ef;

import java.net.*;
import java.io.*;
import java.util.*;

public class GetIPv4
{
    public static String  GetHostName()
    {
        String HostName = "";
        try
        {
            InetAddress IA = InetAddress.getLocalHost();
            HostName = IA.getHostName();
        }
        catch(UnknownHostException E)
        {
            E.printStackTrace();
        }
        return HostName;
    }

    public static String GetAddressIP()
    {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        String ip = addr.getHostAddress();
                        if (ip.startsWith("10.")) return ip;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[])
    {
        System.out.println("-  Hostname:  " + GetHostName());
        System.out.println("- IP Address_  " + GetAddressIP());
    }
}