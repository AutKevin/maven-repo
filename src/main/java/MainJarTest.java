import com.autumn.monitor.HardwareInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.util.Arrays;

/**
 * 打包测试用
 * 打包命令 mvn assembly:assembly
 * 运行脚本
 * java -classpath aeo-tool-1.0.4-jar-with-dependencies.jar MainJarTest
 * pause
 *
 * @author: 秋雨
 * 2020-09-22 10:50
 **/
public class MainJarTest {
    public static void main(String[] args) {
        Sigar sigar = new Sigar();
        try {
            System.out.println("获取CPU"+sigar.getCpuInfoList());
            System.out.println("获取内存信息"+sigar.getMem().getTotal());
            System.out.println("获取所有磁盘"+sigar.getFileSystemList());
            System.out.println("------------------------------------------------");
            String info = "cpuload="+ HardwareInfo.getCpuLoad()
                    +"&memoryload="+HardwareInfo.getMemoryLoad()
                    +"&diskload="+HardwareInfo.getDiskLoad()
                    +"&memorytotal="+HardwareInfo.getMemoryTotal()
                    +"&memoryfree="+HardwareInfo.getMemoryFree()
                    +"&disktotal="+HardwareInfo.getDiskTotal()
                    +"&diskfree="+HardwareInfo.getDiskFree();
            System.out.println(info);
        } catch (SigarException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
