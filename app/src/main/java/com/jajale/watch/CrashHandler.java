package com.jajale.watch;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;

import com.jajale.watch.utils.FileUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {


    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
//    private Properties mDeviceCrashInfo = new Properties();
    private StringBuilder sb = new StringBuilder();
    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = "date.cr";
    private String currentTime;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return (INSTANCE == null) ? (INSTANCE = new CrashHandler()) : INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // Sleep一会后结束程序
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Log.e(AppConstants.TAG_CRASH_HANDLER, "Error : ", e);
            }
            try {
                MobclickAgent.onKillProcess(mContext.getApplicationContext());
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        final String msg = ex.getLocalizedMessage();
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                // Toast 显示需要出现在一个线程的消息队列中
                Looper.prepare();
                if (BuildConfig.DEBUG) {
//                    Toast.makeText(mContext, "e:" + msg, Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(mContext, "程序异常退出！", Toast.LENGTH_LONG).show();
                }
                Looper.loop();
            }
        }.start();
        // 收集设备信息
        collectCrashDeviceInfo(mContext);
        // 保存错误报告文件
        String crashFileName = saveCrashInfoToFile(ex);
        // 发送错误报告到服务器
//        sendCrashReportsToServer(mContext);
        return true;
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx) {
        try {
            // Class for retrieving various kinds of information related to the
            // application packages that are currently installed on the device.
            // You can find this class through getPackageManager().
            PackageManager pm = ctx.getPackageManager();
            // getPackageInfo(String packageName, int flags)
            // Retrieve overall information about an application package that is installed on the system.
            // public static final int GET_ACTIVITIES
            // Since: API Level 1 PackageInfo flag: return information about activities in the package in activities.
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentTime = simpleDateFormat.format(new Date());
                // public String versionName The version name of this package,
                // as specified by the <manifest> tag's versionName attribute.
                sb.append(Thread.currentThread().getId());
                sb.append("-----start-----");
                sb.append(currentTime);
                sb.append("-----\n");

                sb.append(VERSION_NAME);
                sb.append(":");
                sb.append((pi.versionName == null ? "not set" : pi.versionName));
                sb.append("\n");
//                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                // public int versionCode The version number of this package,
                // as specified by the <manifest> tag's versionCode attribute.
                sb.append(VERSION_CODE);
                sb.append(":");
                sb.append(pi.versionCode);
                sb.append("\n");


                sb.append("crash-time:");
                sb.append(currentTime);
                sb.append("\n");
//                mDeviceCrashInfo.put(VERSION_CODE, "v:" + pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(AppConstants.TAG_CRASH_HANDLER, "Error while collect package info", e);
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
//        Field[] fields = Build.class.getDeclaredFields();
//        for (Field field : fields) {
//            try {
//                // setAccessible(boolean flag)
//                // 将此对象的 accessible 标志设置为指示的布尔值。
//                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
//                field.setAccessible(true);
////                mDeviceCrashInfo.put(field.getName(), field.get(null));
//                sb.append(field.getName() + ":" + field.get(null));
//                sb.append("\n");
//                L.d(AppConstants.TAG_CRASH_HANDLER, field.getName() + " : " + field.get(null));
//            } catch (Exception e) {
//                L.e(AppConstants.TAG_CRASH_HANDLER, "Error while collect crash info", e);
//            }
//        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        // printStackTrace(PrintWriter s)
        // 将此 throwable 及其追踪输出到指定的 PrintWriter
        ex.printStackTrace(printWriter);

        // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        // toString() 以字符串的形式返回该缓冲区的当前值。
        String result = info.toString();
        printWriter.close();
//        mDeviceCrashInfo.put(STACK_TRACE, result);
        sb.append(result);
        sb.append("\n");
        sb.append("------end------");
        sb.append(currentTime);
        sb.append("-----\n\n\n");

        String fileName = "crash-" + CRASH_REPORTER_EXTENSION;
        // 保存文件
        FileUtils.writeString2File(mContext, fileName, sb.toString(), false);
        return fileName;
    }

//    /**
//     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
//     *
//     * @param ctx
//     */
//    private void sendCrashReportsToServer(Context ctx) {
//        String[] crFiles = getCrashReportFiles(ctx);
//        if (crFiles != null && crFiles.length > 0) {
//            TreeSet<String> sortedFiles = new TreeSet<String>();
//            sortedFiles.addAll(Arrays.asList(crFiles));
//
//            for (String fileName : sortedFiles) {
//                File cr = new File(ctx.getFilesDir(), fileName);
//                postReport(cr);
//                cr.delete();// 删除已发送的报告
//            }
//        }
//    }

//    /**
//     * 获取错误报告文件名
//     *
//     * @param ctx
//     * @return
//     */
//    private String[] getCrashReportFiles(Context ctx) {
//        File filesDir = ctx.getFilesDir();
//        // 实现FilenameFilter接口的类实例可用于过滤器文件名
//        FilenameFilter filter = new FilenameFilter() {
//            // accept(File dir, String name)
//            // 测试指定文件是否应该包含在某一文件列表中。
//            public boolean accept(File dir, String name) {
//                return name.endsWith(CRASH_REPORTER_EXTENSION);
//            }
//        };
//        // list(FilenameFilter filter)
//        // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录
//        return filesDir.list(filter);
//    }

//    private void postReport(File file) {
//        // TODO 使用HTTP Post 发送错误报告到服务器
//        // 这里不再详述,开发者可以根据OPhoneSDN上的其他网络操作
//        // 教程来提交错误报告
//    }
//
//    /**
//     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
//     */
//    public void sendPreviousReportsToServer() {
//        sendCrashReportsToServer(mContext);
//    }


}