package ftptest;

import com.autumn.ftp.FtpUtil;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FtpUtilTest {
    public static void main(String[] args) throws FileNotFoundException {

        //初始化ftpclient
        FTPClient ftpClient = FtpUtil.initFtpClient("10.77.198.133", 21, "95533", "95533");

        //上传文件
        InputStream is = new FileInputStream("D:\\1.txt");  //输入文件流
        FtpUtil.uploadFile(ftpClient,"/home/YH04/qy","2.txt",is);

        //下载文件
        FtpUtil.downloadFile(ftpClient,"/home/YH04/qy","2.txt","D:\\迅雷下载");

        //删除文件
        FtpUtil.deleteFile(ftpClient,"/home/YH04/qy","2.txt");

        //登出
        FtpUtil.logout(ftpClient);
    }
}
