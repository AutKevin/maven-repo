/**
 * 线程join及Intercept测试
 * @author: 秋雨
 * 2020-09-15 11:40
**/

public class ThreadInterrupt extends Thread
{
    public void run()
    {
        //线程处于RUNNABLE状态时,isInterrupted()方法判断当前线程是否被中断
        while(!Thread.currentThread().isInterrupted()){
            try
            {
                System.out.println(Thread.currentThread().getName()+"正在执行");
                sleep(5000);  // 延迟5秒,线程处于阻塞状态
            }
            catch (InterruptedException e)    //
            {
                System.out.println(Thread.currentThread().getName()+"线程"+e.getMessage());
                break;   //捕获阻塞时线程被interrupt的异常
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        Thread thread = new ThreadInterrupt();
        //子线程运行
        thread.start();

        System.out.println("在5秒之内按任意键中断子线程!");
        System.in.read();
        thread.interrupt();   //执行中断
        thread.join();   //主线程等待子线程执行完毕
        System.out.println("主线程退出!");
    }
}
