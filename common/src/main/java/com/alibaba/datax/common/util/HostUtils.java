package com.alibaba.datax.common.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by liqiang on 15/8/25.
 */
public class HostUtils {

    public static final String IP;
    public static final String HOSTNAME;
    private static final Logger log = LoggerFactory.getLogger(HostUtils.class);

    static {
        String ip="";
        String hostname="";
        try {
            //这里是由于,部分机器存在双网卡的情况,有可能ip反馈不全
            StringBuffer host=new StringBuffer();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    //过滤回路地址，如127.0.0.1 和 物理地址
                    if (!inetAddress.isLoopbackAddress()&&!inetAddress.isLinkLocalAddress()) {
                        host.append(inetAddress.getHostAddress()).append(",");
                    }
                }
            }
            ip = host.toString();
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException e) {
            log.error("Can't find out address: " + e.getMessage());
            ip = "UNKNOWN";
            hostname = "UNKNOWN";
        } catch (SocketException e) {
            log.error("Can't find out address: " + e.getMessage());
            ip = "UNKNOWN";
            hostname = "UNKNOWN";
        }
        if (ip.equals("127.0.0.1") || ip.equals("::1") || ip.equals("UNKNOWN")) {
            try {
                Process process = Runtime.getRuntime().exec("hostname -i");
                if (process.waitFor() == 0) {
                    ip = new String(IOUtils.toByteArray(process.getInputStream()), "UTF8");
                }
                process = Runtime.getRuntime().exec("hostname");
                if (process.waitFor() == 0) {
                    hostname = (new String(IOUtils.toByteArray(process.getInputStream()), "UTF8")).trim();
                }
            } catch (Exception e) {
                log.warn("get hostname failed {}", e.getMessage());
            }
        }
        IP = ip;
        HOSTNAME = hostname;
        log.info("IP {} HOSTNAME {}", IP, HOSTNAME);
    }
}
