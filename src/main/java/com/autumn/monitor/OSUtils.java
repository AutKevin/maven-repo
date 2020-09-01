package com.autumn.monitor;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Who;

import static com.autumn.monitor.PathUtil.setSystemPathVariable;

/**
 * OSUtils
 * 使用Sigar获取系统环境
 * @author: 秋雨
 * 2020-08-28 14:32
 **/
public class OSUtils {

    /**
     * 获取CPU负载
     * @return
     * @throws SigarException
     */
    public static String getCpuLoad() throws SigarException {
        Sigar sigar = new Sigar();
        CpuPerc cpu = sigar.getCpuPerc();
        double cpuUsedPerc = cpu.getCombined();
        String result = String.format("%.1f", cpuUsedPerc * 100);
        return result;
    }

    /**
     * 获取内存负载
     * @return
     * @throws SigarException
     */
    public static String getMemoryLoad() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        double memUsedPerc = mem.getUsedPercent();
        String result = String.format("%.2f", memUsedPerc);
        return result;
    }

    /**
     * 获取磁盘负载
     * @return 磁盘使用率百分比
     * @throws SigarException
     */
    public static String getDiskLoad() throws SigarException, InterruptedException {
        Sigar sigar = new Sigar();
        FileSystemUsage fileSystemUsage = null;

        //获取所有盘符
        List<FileSystem> list = Arrays.asList(sigar.getFileSystemList());
        double usePercent_total = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                fileSystemUsage = sigar.getFileSystemUsage(String.valueOf(list.get(i)));
            } catch (SigarException e) {   //当fileSystem.getType()为5时会出现该异常——此时文件系统类型为光驱
                continue;
            }
            //四个磁盘占用率总和
            usePercent_total += fileSystemUsage.getUsePercent();
        }

        // 磁盘使用率
        String usePercents = String.format("%.2f", usePercent_total*100/list.size()) + " %";

        // 关闭sigar
        sigar.close();

        return usePercents;
    }

    /**
     * 获取磁盘总大小
     * @return 磁盘大小
     * @throws SigarException
     */
    public static String getDiskTotal() throws SigarException, InterruptedException {
        Sigar sigar = new Sigar();
        FileSystemUsage fileSystemUsage = null;

        List<FileSystem> list = Arrays.asList(sigar.getFileSystemList());
        double total = 0;


        for (int i = 0; i < list.size(); i++) {
            try {
                fileSystemUsage = sigar.getFileSystemUsage(String.valueOf(list.get(i)));
            } catch (SigarException e) {// 当fileSystem.getType()为5时会出现该异常——此时文件系统类型为光驱
                continue;
            }
            total += fileSystemUsage.getTotal();
        }

        // 磁盘容量
        String totals="";
        totals = String.format("%.1f", total / 1024 / 1024) + " GB";

        /*if(total / 1024 / 1024 / 1024>1) {
            totals = String.format("%.1f", total / 1024 / 1024 / 1024) + " TB";
        }else if(total / 1024 / 1024>1){
            totals = String.format("%.1f", total / 1024 / 1024) + " GB";
        }else if(total / 1024>1) {
            totals = String.format("%.1f", total / 1024) + " MB";
        }else if(total <=1) {
            totals = String.format("%.1f", total) + " KB";
        }*/

        //System.out.println("总容量======="+totals);

        // 关闭sigar
        sigar.close();

        return totals;
    }

    /**
     * 数据不准,勿用
     * 获取磁盘IO读取速率
     * @return 磁盘读取速率单位: KB/s
     * @throws SigarException
     * @throws InterruptedException
     */
    @Deprecated
    public static String getDiskReadIO() throws SigarException, InterruptedException {
        Sigar sigar = new Sigar();
        FileSystemUsage sfileSystemUsage = null;
        FileSystemUsage efileSystemUsage = null;

        List<FileSystem> list = Arrays.asList(sigar.getFileSystemList());

        double startreads = 0;
        double endreads = 0;

        double reads = 0;

        long start = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            try {
                sfileSystemUsage = sigar.getFileSystemUsage(String.valueOf(list.get(i)));
            } catch (SigarException e) {// 当fileSystem.getType()为5时会出现该异常——此时文件系统类型为光驱
                continue;
            }
            startreads += sfileSystemUsage.getDiskReads();
        }

        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            try {
                efileSystemUsage = sigar.getFileSystemUsage(String.valueOf(list.get(i)));
            } catch (SigarException e) {// 当fileSystem.getType()为5时会出现该异常——此时文件系统类型为光驱
                continue;
            }

            endreads += efileSystemUsage.getDiskReads();
        }

        reads = ((endreads - startreads)*8/(end-start)*1000);

        // 读
        String readss = String.format("%.1f", reads/1024d/8);

        // 关闭sigar
        sigar.close();

        //System.out.println("读取速度======="+readss);
        return readss;

    }

    /**
     * 数据不准,勿用
     * 获取磁盘IO写速率
     * @return 磁盘写入速率单位: KB/s
     * @throws SigarException
     * @throws InterruptedException
     */
    @Deprecated
    public static String getDiskWriteIO() throws SigarException, InterruptedException {
        Sigar sigar = new Sigar();
        FileSystemUsage sfileSystemUsage = null;
        FileSystemUsage efileSystemUsage = null;

        List<FileSystem> list = Arrays.asList(sigar.getFileSystemList());

        double startwrites = 0;
        double startwrites2 = 0;

        double endwrites = 0;
        double endwrites2 = 0;

        double writes = 0;

        long start = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            try {
                sfileSystemUsage = sigar.getFileSystemUsage(String.valueOf(list.get(i)));
            } catch (SigarException e) {// 当fileSystem.getType()为5时会出现该异常——此时文件系统类型为光驱
                continue;
            }
            startwrites += sfileSystemUsage.getDiskWrites();
            startwrites2 += sfileSystemUsage.getDiskWriteBytes();
            //System.out.println(sfileSystemUsage.getDiskWriteBytes());
        }

        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            try {
                efileSystemUsage = sigar.getFileSystemUsage(String.valueOf(list.get(i)));
            } catch (SigarException e) {  // 当fileSystem.getType()为5时会出现该异常——此时文件系统类型为光驱
                continue;
            }

            endwrites += efileSystemUsage.getDiskWrites();
            endwrites2 += efileSystemUsage.getDiskWriteBytes();
        }
        System.out.println(endwrites - startwrites);
        System.out.println((endwrites2 - startwrites2)/1000);
        writes = ((endwrites - startwrites)/(end-start)*1000);

        // 写
        String writess = String.format("%.1f", writes/1024d);
        // 磁盘容量
        String totals="";

        //System.out.println("写入速度======="+writess);

        // 关闭sigar
        sigar.close();

        return writess;

    }

    /**
     * 网络IO - 上行数据监控
     * @return 上行,单位: MB/s
     * @throws SigarException
     * @throws InterruptedException
     */
    public static String getNETIO_Tx() throws SigarException, InterruptedException {
        Sigar sigar = new Sigar();

        String ip = "";
        /*筛选虚拟机的一些IP,只监听物理网卡IP*/
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                String displayName = intf.getDisplayName();
                if (!name.contains("docker") && !name.contains("lo") && !displayName.contains("Virtual")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            ex.printStackTrace();
        }

        String[] netInterfaceList = sigar.getNetInterfaceList();

        double txBytes = 0;
        // 一些其它的信息
        for (int i = 0; i < netInterfaceList.length; i++) {
            String netInterface = netInterfaceList[i];// 网络接口
            NetInterfaceConfig netInterfaceConfig = sigar.getNetInterfaceConfig(netInterface);

            if (netInterfaceConfig.getAddress().equals(ip)) {
                double start = System.currentTimeMillis();
                NetInterfaceStat statStart = sigar.getNetInterfaceStat(netInterface);
                double txBytesStart = statStart.getTxBytes();

                Thread.sleep(1000);
                double end = System.currentTimeMillis();
                NetInterfaceStat statEnd = sigar.getNetInterfaceStat(netInterface);
                double txBytesEnd = statEnd.getTxBytes();

                txBytes = ((txBytesEnd - txBytesStart)*8/(end-start)*1000)/1024d;
                break;
            }


        }
        // 发送字节
        String txBytess = String.format("%.1f", txBytes/1024/8);

        // 关闭sigar
        sigar.close();
        return txBytess;
    }

    /**
     * 网络IO - 下行数据监控
     * @return 下行,单位: MB/s
     * @throws SigarException
     * @throws InterruptedException
     */
    public static String getNETIO_Rx() throws SigarException, InterruptedException {
        Sigar sigar = new Sigar();

        String ip = "";
        /*筛选虚拟机的一些IP,只监听物理网卡IP*/
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                String displayName = intf.getDisplayName();
                if (!name.contains("docker") && !name.contains("lo") && !displayName.contains("Virtual")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            ex.printStackTrace();
        }

        String[] netInterfaceList = sigar.getNetInterfaceList();

        double rxBytes = 0;
        // 一些其它的信息
        for (int i = 0; i < netInterfaceList.length; i++) {
            String netInterface = netInterfaceList[i];// 网络接口
            NetInterfaceConfig netInterfaceConfig = sigar.getNetInterfaceConfig(netInterface);

            if (netInterfaceConfig.getAddress().equals(ip)) {
                double start = System.currentTimeMillis();
                NetInterfaceStat statStart = sigar.getNetInterfaceStat(netInterface);
                double rxBytesStart = statStart.getRxBytes();

                Thread.sleep(1000);
                double end = System.currentTimeMillis();
                NetInterfaceStat statEnd = sigar.getNetInterfaceStat(netInterface);
                double rxBytesEnd = statEnd.getRxBytes();

                rxBytes = ((rxBytesEnd - rxBytesStart)*8/(end-start)*1000)/1024d;

                break;
            }

            // 判断网卡信息中是否包含VMware即虚拟机，不存在则设置为返回值
            //System.out.println("网卡MAC地址 ======="+netInterfaceConfig.getHwaddr());

        }
        // 接收字节,除以8把Mps单位转为MB/s
        String rxBytess = String.format("%.1f", rxBytes/1024/8);

        // 关闭sigar
        sigar.close();
        return rxBytess;
    }

    /**
     * 获取网络状态
     * @return IP :ip地址;
     *         TX: 上行网速,单位: Mbps;
     *         RX: 下行网速,单位: Mbps;
     * @throws SigarException
     * @throws InterruptedException
     */
    public static Map getNETIO(String ipStr) throws SigarException, InterruptedException {
        Sigar sigar = new Sigar();

        String ip = "";
        /*筛选虚拟机的一些IP,只监听物理网卡IP*/
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                String displayName = intf.getDisplayName();
                if (!name.contains("docker") && !name.contains("lo") && !displayName.contains("Virtual")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            ex.printStackTrace();
        }

        String[] netInterfaceList = sigar.getNetInterfaceList();

        double rxBytes = 0;
        double txBytes = 0;
        String description = null;
        // 一些其它的信息
        for (int i = 0; i < netInterfaceList.length; i++) {
            String netInterface = netInterfaceList[i];// 网络接口
            NetInterfaceConfig netInterfaceConfig = sigar.getNetInterfaceConfig(netInterface);
            if (netInterfaceConfig.getAddress().equals(ip)) {
                /**如果传入IP了则用传入IP的值,否则取物理网卡*/
                if(ipStr!=null && !ipStr.isEmpty()){
                    ip = ipStr;
                }

                double start = System.currentTimeMillis();
                NetInterfaceStat statStart = sigar.getNetInterfaceStat(netInterface);
                double rxBytesStart = statStart.getRxBytes();
                double txBytesStart = statStart.getTxBytes();

                Thread.sleep(1000);
                double end = System.currentTimeMillis();
                NetInterfaceStat statEnd = sigar.getNetInterfaceStat(netInterface);
                double rxBytesEnd = statEnd.getRxBytes();
                double txBytesEnd = statEnd.getTxBytes();

                /**乘以8是为了计算bps*/
                rxBytes = ((rxBytesEnd - rxBytesStart)*8d/(end-start)*1000d)/1024d;
                txBytes = ((txBytesEnd - txBytesStart)*8d/(end-start)*1000d)/1024d;
                break;
            }
        }
        // 接收字节,单位MB/s
        String rxBytess = String.format("%.1f", rxBytes/1024d);
        // 发送字节,单位MB/s
        String txBytess = String.format("%.1f", txBytes/1024d);

        /*if(rxBytes>1024) {
            rxBytess = String.format("%.1f", rxBytes/1024)+" Mbps";
        }else {
            rxBytess = String.format("%.0f", rxBytes)+" Kbps";
        }
        if(txBytes>1024) {
            txBytess = String.format("%.1f", txBytes/1024)+" Mbps" ;
        }else {
            txBytess=String.format("%.0f", txBytes)+" Kbps";
        }
        System.out.println("发送======="+txBytess);
        System.out.println("接收======="+rxBytess);
        System.out.println("IP======="+ip);
        */

        Map<String,String> resultMap = new HashMap<String, String>();
        resultMap.put("IP",ip);
        resultMap.put("TX",txBytess);
        resultMap.put("RX",rxBytess);

        // 关闭sigar
        sigar.close();
        return resultMap;
    }

    public static void main(String[] args) throws SigarException, InterruptedException {
        while(true){
            /*getCpuLoad();
            int r = HardwareInfo.getCpuLoad();
            System.out.println("Hd:::"+r);
            */
            /*System.out.println("--------------getCpuLoad-----------------");
            System.out.println(getCpuLoad());
            System.out.println(":::"+HardwareInfo.getCpuLoad());
            System.out.println("--------------getMemoryLoad-----------------");
            System.out.println(getMemoryLoad());
            System.out.println(":::"+HardwareInfo.getMemoryLoad());
            System.out.println("--------------DiskLoad-----------------");
            System.out.println(getDiskLoad());
            System.out.println(":::"+HardwareInfo.getDiskLoad());
            System.out.println("--------------getDiskTotal-----------------");
            System.out.println(getDiskTotal()+"GB");
            System.out.println(":::"+HardwareInfo.getDiskTotal());*/
            //System.out.println("--------------getDiskReadIO-----------------");
            //System.out.println(getDiskReadIO()+"KB/s");
            //System.out.println("--------------getDiskWriteIO-----------------");
            //System.out.println(getDiskWriteIO()+"KB/s");
            //System.out.println(getDiskReadIO()+"KB/s");

            //System.out.println("上行:"+getNETIO_Tx());
            //System.out.println("下行:"+getNETIO_Rx());

            //System.out.println("磁盘读取:"+getDiskReadIO());
            //System.out.println("磁盘写入:"+getDiskWriteIO());
            Map netio = getNETIO("");
            System.out.println(netio.toString());
            /*OSUtils io = new OSUtils();
            long tempTotal = io.getTotalByte();
            double start = System.currentTimeMillis();
            for (int i = 0; i < 200; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long total = io.getTotalByte();
                double end = System.currentTimeMillis();
                System.out.println( Integer.valueOf((int) ((total - tempTotal) /(end - start) * 1000/1024d/1024d)) + "Mb/s");
                tempTotal = total;
            }*/
        }


    }

    public long getTotalByte(){
        Sigar sigar = new Sigar();
        long totalByte = 0;
        try {
            FileSystem[] fslist = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++){
                if (fslist[i].getType() == 2){
                    FileSystemUsage usage = sigar.getFileSystemUsage(fslist[i].getDirName());
                    totalByte += usage.getDiskReadBytes();
                    totalByte += usage.getDiskWriteBytes();
                }
            }
        } catch (SigarException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return totalByte;
    }
}