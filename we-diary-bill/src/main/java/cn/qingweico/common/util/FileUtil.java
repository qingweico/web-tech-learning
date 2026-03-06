package cn.qingweico.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Calendar;
import java.util.Random;

@Slf4j
public class FileUtil {

    /**
     * 创建目录
     *
     * @param destDirName 目标目录名
     * @return 目录创建成功返回true, 否则返回false
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        // 创建单个目录
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建文件夹
     *
     * @param dirpath 文件件路径
     * @return
     */
    public static boolean mkdir(String dirpath) {
        File f = new File(dirpath);
        boolean rtv = false;
        if (!f.exists()) {
            rtv = f.mkdir();
        }
        return rtv;
    }

    /**
     * 创建文件夹, 可带子文件夹
     *
     * @param dirpath
     * @return
     */
    public static boolean mkdirs(String dirpath) {
        File f = new File(dirpath);
        return f.mkdirs();
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return boolean
     */
    public static boolean isExsit(String path) {
        boolean b = false;
        File f = new File(path);
        if (f.exists()) {
            b = true;
        }
        return b;
    }

    /**
     * 删除文件
     *
     * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
     * @return boolean
     */
    public static void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myDelFile = new File(filePath);
            myDelFile.delete();

        } catch (Exception e) {
            log.error("删除文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 读取到字节数组0
     *
     * @param filePath //路径
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            log.error("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

    /**
     * 读取到字节数组1
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filePath) throws IOException {

        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 读取到字节数组2
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray2(String filePath) throws IOException {

        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mapped File way MappedByteBuffer 可以在处理大文件时, 提升性能
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray3(String filePath) throws IOException {

        FileChannel fc = null;
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(filePath, "r");
            fc = rf.getChannel();
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
                    fc.size()).load();
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rf.close();
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 参数是文件的扩展名, 生成一个当前日期为名字的文件名+扩展名
     *
     * @名称 generateFileName
     * @描述 上传文件, 生成文件名
     * @参数 @param fileExpandedName
     * @参数 @return
     * @返回值 String
     * @作者 wulimo
     * @时间 2015-11-27 下午1:01:14
     */
    public static String generateFileName(String fileExpandedName) {
        Calendar c = Calendar.getInstance();
        StringBuilder str = new StringBuilder();
        str.append(c.get(Calendar.YEAR));
        int month = c.get(Calendar.MONTH) + 1;
        str.append(month < 10 ? "0" + month : month);
        int day = c.get(Calendar.DAY_OF_MONTH);
        str.append(day < 10 ? "0" + day : day);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        str.append(hour < 10 ? "0" + hour : hour);
        int minute = c.get(Calendar.MINUTE);
        str.append(minute < 10 ? "0" + minute : minute);
        int second = c.get(Calendar.SECOND);
        str.append(second < 10 ? "0" + second : second);
        str.append(c.get(Calendar.MILLISECOND));
        str.append(new Random().nextInt(10000));
        if (null == fileExpandedName || "".equals(fileExpandedName)) {

        } else {
            str.append(".");
            str.append(fileExpandedName);
        }
        return str.toString();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return true;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空, 可以删除
        return dir.delete();
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如: c:/fqf.txt
     * @param newPath String 复制后路径 如: f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                //文件存在时
                //读入原文件
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    //字节数 文件大小
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean readUrlAndSaveFile(String urlStr, String filePath) throws Exception {
        //new一个URL对象
        URL url = new URL(urlStr);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据, 以二进制封装得到数据, 具有通用性
        byte[] data = readInputStream(inStream);
        //new一个文件对象用来保存图片, 默认保存当前工程根目录
        File imageFile = new File(filePath);

        if (!imageFile.getParentFile().exists()) {
            imageFile.getParentFile().mkdirs();
        }
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(imageFile);
        //写入数据
        outStream.write(data);
        //关闭输出流
        outStream.close();
        return true;
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度, 如果为-1, 代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据, 中间参数代表从哪个位置开始读, len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}
