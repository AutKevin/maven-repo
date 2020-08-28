package com.autumn.monitor;

import com.sun.management.OperatingSystemMXBean;

import java.io.*;
import java.lang.management.*;
import java.net.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 硬件信息
 */
public class HardwareInfo {

    /*注意这里一定要强转为com.sun.management下的OperatingSystemMXBean,默认返回的是java.lang.management下的类*/
    private static OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();

    /**
     * 判断是服务器的系统类型是windows还是linux
     * @return windows或者linux
     */
    public static String isWindowsOrLinux() {
        String osName = System.getProperty("os.name");
        String sysName = "";
        if (osName.toLowerCase().startsWith("windows")) {
            sysName = "windows";
        } else if (osName.toLowerCase().startsWith("linux")) {
            sysName = "linux";
        }
        return sysName;
    }

    /**
     * 获取系统全称
     * @return
     */
    public static String getOSname(){
        return System.getProperty("os.name");
    }

    /**
     * 获取CPU信息
     * @return CPU占用百分比
     * @throws InterruptedException
     */
    public static int getCpuLoad() throws InterruptedException {
        double cpuLoad = osmxb.getSystemCpuLoad();    /*windows下第一次调用返回-1,因为JVM需要运行几秒钟才能收集CPU信息*/
        int count = 0;
        while(cpuLoad <= 0.0){    /*如果第一次没有获取成功*/
            Thread.sleep(3000);    /*等待几秒,让JVM收集CPU信息*/
            cpuLoad = osmxb.getSystemCpuLoad();   /*再次获取CPU信息*/
            if (count > 5){
                break;
            }
            count++;
        }
        int percentCpuLoad = (int) (cpuLoad * 100);
        return percentCpuLoad;
    }

    /**
     * 获取内存信息
     * @return 内存占用百分比
     */
    public static int getMemoryLoad() {
        double totalMemory = osmxb.getTotalPhysicalMemorySize();
        double freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        double value = freePhysicalMemorySize/totalMemory;
        int percentMemoryLoad = (int) ((1-value)*100);
        return percentMemoryLoad;
    }

    /**
     * 获取总内存
     * @return 内存总大小,单位G,保留两位小数
     */
    public static String getMemoryTotal() {
        double totalMemory = osmxb.getTotalPhysicalMemorySize();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        String totalSize = nf.format(totalMemory/(double)1024/(double)1024/(double)1024);
        return totalSize;
    }

    /**
     * 获取空闲内存
     * @return 空闲大小,单位G,保留两位小数
     */
    public static String getMemoryFree() {
        double freeMemory = osmxb.getFreePhysicalMemorySize();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        String freeSize = nf.format(freeMemory/(double)1024/(double)1024/(double)1024);
        return freeSize;
    }

    /**
     * 获取硬盘信息
     * return 硬盘使用率
     */
    public static int getDiskLoad()
    {
        File[] disks = File.listRoots();
        long total = 0;
        long free = 0;
        for(File file : disks)
        {
            total+=file.getTotalSpace();
            free+=file.getFreeSpace();
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        String freePercent = nf.format(((float)free/(float)total)*100);
        return 100-Integer.parseInt(freePercent);
    }

    /**
     * 获取磁盘总大小
     * @return 磁盘总大小,单位G,保留两位小数
     */
    public static String getDiskTotal()
    {
        File[] disks = File.listRoots();
        long total = 0;
        long free = 0;
        for(File file : disks)
        {
            total+=file.getTotalSpace();
            free+=file.getFreeSpace();
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        String totalSize = nf.format(total/(double)1024/(double)1024/(double)1024);
        return totalSize;
    }

    /**
     * 获取磁盘剩余空间
     * @return 空闲大小,单位G,保留两位小数
     */
    public static String getDiskFree()
    {
        File[] disks = File.listRoots();
        long free = 0;
        for(File file : disks)
        {
            free+=file.getFreeSpace();
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        String freeSize = nf.format(free/(double)1024/(double)1024/(double)1024);
        return freeSize;
    }

    /**
     * JVM堆内存
     * 1.JVM初始分配的堆内存由-Xms指定，默认是物理内存的1/64；
     * 2.JVM最大分配的堆内存由-Xmx指定，默认是物理内存的1/4。
     * 3.默认空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制；
     * 4.空余堆内存大于70%时，JVM会减少堆直到-Xms的最小限制。
     * 5.因此服务器一般设置-Xms、-Xmx 相等以避免在每次GC 后调整堆的大小。
     * 6.说明：如果-Xmx 不指定或者指定偏小，应用可能会导致java.lang.OutOfMemory错误，此错误来自JVM，不是Throwable的，无法用try…catch捕捉。
     */
    public static void getHeapMemory(){
        MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
        System.out.println("Max:" + mxb.getHeapMemoryUsage().getMax() / 1024 / 1024 + "MB");    //Max:1776MB
        System.out.println("Init:" + mxb.getHeapMemoryUsage().getInit() / 1024 / 1024 + "MB");  //Init:126MB
        System.out.println("Committed:" + mxb.getHeapMemoryUsage().getCommitted() / 1024 / 1024 + "MB");   //Committed:121MB
        System.out.println("Used:" + mxb.getHeapMemoryUsage().getUsed() / 1024 / 1024 + "MB");  //Used:7MB
        System.out.println(mxb.getHeapMemoryUsage().toString());    //init = 132120576(129024K) used = 8076528(7887K) committed = 126877696(123904K) max = 1862270976(1818624K)
    }

    /**
     * JVM非堆内存(方法区)
     * 1. JVM使用-XX:PermSize设置非堆内存初始值，默认是物理内存的1/64；
     * 2. 由XX:MaxPermSize设置最大非堆内存的大小，默认是物理内存的1/4。
     * * 还有一说：MaxPermSize缺省值和-server -client选项相关，-server选项下默认MaxPermSize为64m，-client选项下默认MaxPermSize为32m。
     * 3. XX:MaxPermSize设置过小会导致java.lang.OutOfMemoryError: PermGen space 就是内存益出。
     * 4. 内存益出：
     * * 这一部分内存用于存放Class和Meta的信息，Class在被 Load的时候被放入PermGen space区域，它和存放Instance的Heap区域不同。
     * * GC(Garbage Collection)不会在主程序运行期对PermGen space进行清理，所以如果你的APP会LOAD很多CLASS 的话,就很可能出现PermGen space错误。
     */
    public static void getNonHeapMemory(){
        MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
        System.out.println("Max:" + mxb.getNonHeapMemoryUsage().getMax() / 1024 / 1024 + "MB");    //Max:0MB
        System.out.println("Init:" + mxb.getNonHeapMemoryUsage().getInit() / 1024 / 1024 + "MB");  //Init:2MB
        System.out.println("Committed:" + mxb.getNonHeapMemoryUsage().getCommitted() / 1024 / 1024 + "MB");   //Committed:8MB
        System.out.println("Used:" + mxb.getNonHeapMemoryUsage().getUsed() / 1024 / 1024 + "MB");  //Used:7MB
        System.out.println(mxb.getNonHeapMemoryUsage().toString());    //init = 2555904(2496K) used = 7802056(7619K) committed = 9109504(8896K) max = -1(-1K)
    }

    /**
     * 获取本机IP
     * @return 返回本机IP,如果有多个网卡,返回第一个
     * @throws Exception
     */
    public static String getLocalIP() throws Exception {
        try {
            InetAddress ia=InetAddress.getLocalHost();
            String localip=ia.getHostAddress();
            /*String localname=ia.getHostName();
            System.out.println("本机名称是："+ localname);
            System.out.println("本机的ip是 ："+localip);*/
            return localip;
        } catch (UnknownHostException e) {
            throw new Exception("获取本机IP失败",e);
        }
    }

    /**
     * 获取本地IP列表 - 多个网卡时使用
     * @return ip地址列表
     * @throws Exception
     */
    public static List<String> getLocalIPList() throws Exception {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            throw new Exception("获取本机IP失败",e);
        }
        return ipList;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("系统类型:"+isWindowsOrLinux());
        System.out.println("--------------------------------");
        System.out.println("系统版本:"+getOSname());
        System.out.println("--------------------------------");
        System.out.println("CPU:"+ getCpuLoad()+"%");
        System.out.println("--------------------------------");
        System.out.println("内存:"+ getMemoryLoad()+"%");
        System.out.println("--------------------------------");
        System.out.println("内存总大小:"+ getMemoryTotal()+"G");
        System.out.println("--------------------------------");
        System.out.println("内存空闲:"+ getMemoryFree()+"G");
        System.out.println("--------------------------------");

        System.out.println("硬盘:"+getDiskLoad()+"%");
        System.out.println("--------------------------------");
        System.out.println("硬盘总大小:"+ getDiskTotal()+"G");
        System.out.println("--------------------------------");
        System.out.println("硬盘空闲:"+ getDiskFree()+"G");
        System.out.println("--------------------------------");

        getHeapMemory();
        getNonHeapMemory();

        String localIP = getLocalIP();
        System.out.println("本地IP:"+ localIP);

        List<String> localIPList = getLocalIPList();
        System.out.println("本地IP列表:"+localIPList);


    }
}
