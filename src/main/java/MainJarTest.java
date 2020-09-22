import com.autumn.tool.ArrayUtil;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.util.Arrays;

/**
 * MainJarTest
 * 打包测试用
 * @author: 秋雨
 * 2020-09-22 10:50
 **/
public class MainJarTest {
    public static void main(String[] args) {
        Sigar sigar = new Sigar();
        try {
            System.out.println("获取内存信息"+sigar.getMem().getTotal());
            System.out.println("获取CPU"+sigar.getCpuInfoList());
            System.out.println("获取所有磁盘"+sigar.getFileSystemList());
            System.out.println("getNetInterfaceList返回值:"+sigar.getNetInterfaceList());
            String[] netInterfaceList = sigar.getNetInterfaceList();
            System.out.println(Arrays.toString(netInterfaceList));
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }
}
