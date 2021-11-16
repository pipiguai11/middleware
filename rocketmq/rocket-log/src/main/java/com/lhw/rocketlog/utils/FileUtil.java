package com.lhw.rocketlog.utils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author ：linhw
 * @date ：21.11.16 16:05
 * @description：文件处理
 * @modified By：
 */
public class FileUtil {

    private static final String BASE_DIR_PATH = "E:" + FileUtil.SEPARATOR + "temp" + FileUtil.SEPARATOR + "rocket";

    //如下两个常量可以根据系统自动识别文件路径中的符号
    public static final String PATH_SEPARATOR = File.pathSeparator;

    //如window中的路径拼接是“\”，而linux中是“/”，为了实现跨平台，所有的文件拼接最好都用这个进行拼接
    public static final String SEPARATOR = File.separator;

    public static void checkSystemSeparator(){
        System.out.println("pathSeparator:" + PATH_SEPARATOR);
        System.out.println("Separator:" + SEPARATOR);
    }


// ---------------------------------------------------文件操作【增删改文件】 start------------------------------------------------------------------------------------

    /**
     * 创建单个文件
     * @param filePath
     */
    public static void create(String filePath){
        File file = new File(filePath);
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else {
            System.out.println("文件【" + file.getName() + "】已存在");
        }
    }

    /**
     * 删除单个文件
     * @param filePath
     */
    public static void delete(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }else {
            System.out.println("文件【" + file.getName() + "】不存在");
        }
    }

    /**
     * 创建目录
     * @param filePath
     */
    public static void createDir(String filePath){
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 获取文件大小
     * @param filePath
     */
    public static long getFileSize(String filePath){
        File file = new File(filePath);
        System.out.println("文件【" + file.getName() + "】的大小为：" + file.length());
        return file.length();
    }

    /**
     * 获取当前目录下的所有文件名
     * @param filePath
     * @return
     */
    public static String[] getFileNames(String filePath){
        File file = new File(filePath);
        if (file.isDirectory()){
            String[] fileNames = file.list();
            if (fileNames.length != 0){
                for (String fileName : fileNames) {
                    System.out.println(fileName);
                }
            }
            return fileNames;
        }else {
            return new String[]{};
        }
    }

    /**
     * 输出一个目录下所有的文件名，并返回所有文件，其中包括所有的子目录
     */
    public static File[] getAllFile(String filePath){
        File file = new File(filePath);
        List<File> files = new LinkedList<>();

        handleFile(file,files,0);

        return files.toArray(new File[files.size()]);
    }

    /**
     * 处理目录，按照层级在控制台打印所有的文件名，并且把所有的文件都放到list中去
     * @param file
     * @param files
     * @param layer
     */
    private static void handleFile(File file, List<File> files, int layer){
        files.add(file);
        printFile(file,layer);
        if (file.isDirectory()){
            int temp = layer + 1;
            for (File listFile : Objects.requireNonNull(file.listFiles())) {
                if (listFile.isDirectory()){
                    handleFile(listFile,files,temp);
                }else {
                    printFile(listFile,temp);
                    files.add(listFile);
                }
            }
        }
    }

    /**
     * 控制台打印
     * @param file
     * @param layer
     */
    private static void printFile(File file, int layer){
        for (int i = 0; i <= layer; i++){
            System.out.print("|----");
        }
        System.out.println(file.getName());
    }

// ---------------------------------------------------文件操作【增删改文件】 end------------------------------------------------------------------------------------

//---------------------------------------------------------文件读取 start---------------------------------------------------------------------------------------------------


    /**
     * 【字节流】和【字符流】的区别
     *
     *      从后面的六个版本的文件读取方法可以看出来，字节流和字符流非常相似，只是处理的类的接收的对象不一致而已，其他的概念都是一样的，除此之外，两者还是有些不一样的地方的
     *
     *      1、字节流一般都是直接操作文件本身，而字符流一般操作的是缓冲区（内存），然后通过缓冲区再操作文件的。
     *          什么意思呢？我们可以通过流的关闭来看到效果（也就是流的close方法）
     *
     *          当我们使用输出流写出内容到文件时，且最后不关闭流（不调用close方法）
     *
     *              如果使用的是字节流（如OutputStream），当程序执行完之后就可以看到文件内容的变化了。
     *
     *              如果使用的是字符流（如Writer），当程序执行完之后既没有执行close方法也没有执行flush方法，我们是看不到文件的内容有任何变化的
     *                  这里提到了flush方法，该方法主要就是用来强制刷新缓冲区的。
     *                  如果我们没有调用close方法，调用了flush方法也是可以的。因为关闭字符流（close）的时候也会调用flush强制刷新缓冲区
     *                  也就是说，我们只需要保证缓冲区中的内容刷新到文件中就可以看到文件变化了。
     *
     *          什么是缓冲区？主要用于暂存数据的一个特殊的内存
     *
     *
     */




    /**
     * 【字节流】读取文件
     * 这个方法有些弊端
     * 首先是创建byte数组时的大小指定，如果文件大小超出了int的范围，则会出现精度缺失的问题
     * 因此，不推荐使用这种方式
     * @param path
     */
    public static void readFileV1(String path){
        File file = new File(path);
        try {
            InputStream in = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            //该方法传入一个字节数组，表示会从文件种读取固定数量字节并缓存到该数组中，最后返回所有的字节长度，如果没有读取到的话则返回-1
            //等价于read(b, 0, b.length);
            int length = in.read(bytes);
            in.close();
            System.out.println("文件内容长度：" + length);
            System.out.println("文件内容：" + new String(bytes));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 【字节流】读取文件
     * 此方法可以一个字节一个字节的读取，有需要的时候可以使用
     * @param path
     */
    public static void readFileV2(String path){
        File file = new File(path);
        try {
            InputStream in = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            //从输入流中读取下一个字节的数据。 值字节以0到255范围内的int形式返回。 如果由于已到达流末尾而没有可用字节，则返回值-1 。
            // 此方法会阻塞，直到输入数据可用、检测到流结束或抛出异常为止
            //相当于是一个字节一个字节的读取
            for (int i =0; i<bytes.length; i++){
                bytes[i] = (byte)in.read();
            }
            in.close();
            System.out.println("文件内容长度：" + bytes.length);
            System.out.println("文件内容：" + new String(bytes));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 【字节流】读取文件
     * 此方法每次读取固定数量的字节，直到文件读完为止，本方法设定为每次读取20个字节
     * @param path
     */
    public static void readFileV3(String path){
        File file = new File(path);
        try {
            InputStream in = new FileInputStream(file);
            byte[] bytes = new byte[20];
            int length = 0;
            //每次读取固定数量的字节，我这里设置是20个字节，具体的可以根据需求设定
            //知道读完文件或者出现异常为止（读完的时候会返回-1，然后终止循环）
            while ((length = in.read(bytes,0,bytes.length)) != -1){
                System.out.println("文件内容长度：" + bytes.length);
                System.out.println("文件内容：" + new String(bytes));
                System.out.println();
            }
            in.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 【字符流】读取文件 用法其实和V1是一样的，只是接收对象从byte数组变成了char数组
     * 一次性全部读取
     * @param path
     */
    public static void readFileV4(String path){
        File file = new File(path);
        try {
            Reader reader = new FileReader(file);
            char[] chars = new char[(int)file.length()];
            //该方法传入一个字节数组，表示会从文件种读取固定数量字节并缓存到该数组中，最后返回所有的字节长度，如果没有读取到的话则返回-1
            //等价于read(b, 0, b.length);
            int length = reader.read(chars);
            reader.close();
            System.out.println("文件内容长度：" + length);
            System.out.println("文件内容：" + new String(chars));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 【字符流】读取文件 用法其实和V2是一样的，只是接收对象从byte数组变成了char数组
     * 一个个字符读取
     * @param path
     */
    public static void readFileV5(String path){
        File file = new File(path);
        try {
            Reader reader = new FileReader(file);
            char[] chars = new char[(int)file.length()];
            //从输入流中读取下一个字节的数据。 值字节以0到255范围内的int形式返回。 如果由于已到达流末尾而没有可用字节，则返回值-1 。
            // 此方法会阻塞，直到输入数据可用、检测到流结束或抛出异常为止
            //相当于是一个字节一个字节的读取
            for (int i = 0; i<chars.length; i++){
                chars[i] = (char)reader.read();
            }
            reader.close();
            System.out.println("文件内容长度：" + chars.length);
            System.out.println("文件内容：" + new String(chars));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 【字符流】读取文件 用法其实和V3是一样的，只是接收对象从byte数组变成了char数组
     * 每次固定数量读取，知道文件读完
     * @param path
     */
    public static void readFileV6(String path){
        File file = new File(path);
        try {
            Reader reader = new FileReader(file);
            char[] chars = new char[20];
            int length = 0;
            //每次读取固定数量的字节，我这里设置是20个字节，具体的可以根据需求设定
            //知道读完文件或者出现异常为止（读完的时候会返回-1，然后终止循环）
            while ((length = reader.read(chars,0,chars.length)) != -1){
                System.out.println("文件内容长度：" + chars.length);
                System.out.println("文件内容：" + new String(chars));
                System.out.println();
            }
            reader.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

//---------------------------------------------------------文件读取 end---------------------------------------------------------------------------------------------------


//---------------------------------------------------------文件写出 start-------------------------------------------------------------------------------------------------

    /**
     * 【字节流】文件写出
     * 一次性将所有内容输出到文件中去，覆盖文件
     *
     * @param filePath
     * @param content
     */
    public static void writeFileV1(String filePath, String content){
        File file = new File(filePath);
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] bytes = content.getBytes();
            //将指定字节数组中的b.length个字节写入此输出流。 write(b)的一般约定是它应该与调用write(b, 0, b.length)具有完全相同的效果。
            out.write(bytes);
            out.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 【字节流】文件写出
     * 一个字节一个字节的输出，覆盖文件
     * @param filePath
     * @param content
     */
    public static void writeFileV2(String filePath, String content){
        File file = new File(filePath);
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] bytes = content.getBytes();
            //将指定的字节写入此输出流。 write的一般约定是将一个字节写入输出流
            for (int i=0; i<bytes.length; i++){
                out.write(bytes[i]);
            }
            out.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 【字节流】文件写出
     * 一个字节一个字节的输出，不会覆盖文件，会在文件最后追加内容
     * @param filePath
     * @param content
     */
    public static void writeFileV3(String filePath, String content){
        File file = new File(filePath);
        try {
            //其实要想实现不覆盖文件，而是追加文件，只需要调用不同的构造器即可，如下
            OutputStream out = new FileOutputStream(file,true);
            byte[] bytes = content.getBytes();
            //追加一个换行符，表示换行追加内容
            out.write("\r\n".getBytes());
            //效果和writeFileV1中的write(bytes)是一样的。
            out.write(bytes,0,bytes.length);
            out.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * 字节流和字符流的输出需要注意的点：
     *
     *  1、在字符流中，其实用法和字节流是一样的，只是做了一定的扩展
     *      也就是说，字符流也是可以直接将char或者char数组（一个一个或者一次性全部）输出到文件的，也就是和上面V1~V3一样，我这里就不再重复写方法了
     *      我这里就提供了字符输出流中扩展的两种方式（直接输出字符串），具体的可以看看Writer类
     *
     *  2、字符流在输出的时候，需要手动的调用flush或者close的，因为涉及到了缓冲区，而不是像字节流一样直接操作文件的
     *      如果程序执行完不调用flush强制刷新缓冲区，是看不到文件有任何变化的，具体的可以看到前面的注释（【字节流】和【字符流】的区别）
     *
     */


    /**
     * 【字符流】文件写出
     * 一次性将该字符串写出到文件，会覆盖文件
     * @param filePath
     * @param content
     */
    public static void writeFileV4(String filePath, String content){
        File file = new File(filePath);
        try {
            Writer writer = new FileWriter(file);
            writer.write(content);
            //如下这种写法和上面这个写法是一样的效果
//            writer.write(content,0,content.length());
            writer.flush();
            writer.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 字符流】文件写出
     * 一次性将字符串写出到文件，不会覆盖文件，会追加内容
     * @param filePath
     * @param content
     */
    public static void writeFileV5(String filePath, String content){
        File file = new File(filePath);
        try {
            Writer writer = new FileWriter(file,true);
            //追加换行符
            writer.write("\r\n");
            writer.write(content);
            //如下这种写法和上面这个写法是一样的效果
//            writer.write(content,0,content.length());
            writer.flush();
            writer.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

//---------------------------------------------------------文件写出 end---------------------------------------------------------------------------------------------------


//---------------------------------------------------------转换流 end---------------------------------------------------------------------------------------------------

    /**
     * 转换流原理：字节流+编码表。
     *      1、概述
     *          OutputStreamWriter：它是字符流通向字节流的桥梁，在内存中，是使用字符流进行操作的，因为字符流提供了操作字符串更加便捷的方式。
     *                              而当我们想要将内容写出到文件时，就是使用字节流操作的。我们可以指定输出的内容的编码格式，然后将其编码成字节，然后由创建OutputStreamWriter时传入的字节流写出去
     *                              我们可以调用它的getEncoding方法获取到它的编码格式，也可以在实例化的时候指定编码格式（构造器方式），如果不指定则为默认的（GBK）
     *          InputStreamReader：同上，只是流程反过来了，先使用字节流从文件中读取内容，然后转成字符流在内存中操作
     *
     *          OutputStreamWriter是Writer的子类，InputStreamReader是Reader的子类
     *
     *
     *
     */


    /**
     * 【转换流】OutputStreamWriter
     * 内存中：将字节输出流转成字符输出流，并使用字符流操作
     * 文件中：统一使用的是字节流处理文件
     * 上面两点如果不明白，可以看看【转换流原理】
     * @param path
     * @param content
     */
    public static void writeTransformStream(String path, String content){
        File file = new File(path);
        try {
//            Writer writer = new OutputStreamWriter(new FileOutputStream(file));
            //追加输出
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file,true));
            System.out.println("编码格式" + writer.getEncoding());
            writer.write("\r\n");
            writer.write(content);
            writer.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 【转换流】InputStreamReader
     * 内存中：将字节输入流转成字符输入流，并使用字符流操作
     * 文件中：统一使用的是字节流处理文件
     * 上面两点如果不明白，可以看看【转换流原理】
     * @param path
     */
    public static void readTransformStream(String path){
        File file = new File(path);
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            System.out.println("编码格式" + reader.getEncoding());
            char[] chars = new char[(int)file.length()];
            reader.read(chars);
            System.out.println(new String(chars));
            reader.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

//---------------------------------------------------------转换流 end---------------------------------------------------------------------------------------------------




//---------------------------------------------------------合并流 start---------------------------------------------------------------------------------------------------

    /**
     * 将两个文件合并成一个文件
     * @param inputStream1
     * @param inputStream2
     */
    public static void mergeStream(InputStream inputStream1, InputStream inputStream2){
        SequenceInputStream inputStream = new SequenceInputStream(inputStream1,inputStream2);
        try {
            OutputStream out = new FileOutputStream(BASE_DIR_PATH + FileUtil.SEPARATOR + "testMergeStream.txt");
            byte[] b = new byte[1024];
            while (inputStream.read(b) != -1){
                out.write(b);
            }
            out.close();
            inputStream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

//---------------------------------------------------------合并流 end---------------------------------------------------------------------------------------------------




//---------------------------------------------------------压缩流 start---------------------------------------------------------------------------------------------------

    private static final int  BUFFER_SIZE = 2 * 1024;
     /**
       * 压缩成ZIP 方法1
       * @param sourceFile 压缩文件夹路径
       * @param out    压缩文件输出流
       * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
       *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
       * @throws RuntimeException 压缩失败会抛出运行时异常
       */
     public static void toZip(File sourceFile, OutputStream out, boolean KeepDirStructure) throws RuntimeException{
         ZipOutputStream zos = null ;
         try {
             zos = new ZipOutputStream(out);
             compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
         } catch (Exception e) {
             throw new RuntimeException("zip error from ZipUtils",e);
         }finally{
             if(zos != null){
                 try {
                     zos.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
     }
     /**
      * 压缩成ZIP 方法2
      * @param srcFiles 需要压缩的文件列表
      * @param out           压缩文件输出流
      * @throws RuntimeException 压缩失败会抛出运行时异常
      */
     public static void toZip(List<File> srcFiles , OutputStream out)throws RuntimeException {
         long start = System.currentTimeMillis();
         ZipOutputStream zos = null ;
         try {
             zos = new ZipOutputStream(out);
             for (File srcFile : srcFiles) {
                 byte[] buf = new byte[BUFFER_SIZE];
                 zos.putNextEntry(new ZipEntry(srcFile.getName()));
                 int len;
                 FileInputStream in = new FileInputStream(srcFile);
                 while ((len = in.read(buf)) != -1){
                                    zos.write(buf, 0, len);
                 }
                 zos.closeEntry();
                 in.close();
             }
             long end = System.currentTimeMillis();
             System.out.println("压缩完成，耗时：" + (end - start) +" ms");
         } catch (Exception e) {
             throw new RuntimeException("zip error from ZipUtils",e);
         }finally{
             if(zos != null){
                 try {
                     zos.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
     }
     /**
      * 递归压缩方法
      * @param sourceFile 源文件
      * @param zos        zip输出流
      * @param name       压缩后的名称
      * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
      *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
      * @throws Exception
      */
     private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception{
         byte[] buf = new byte[BUFFER_SIZE];
         if(sourceFile.isFile()){
             // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
             zos.putNextEntry(new ZipEntry(name));
             // copy文件到zip输出流中
             int len;
             FileInputStream in = new FileInputStream(sourceFile);
             while ((len = in.read(buf)) != -1){
                 zos.write(buf, 0, len);
             }
             // Complete the entry
             zos.closeEntry();
             in.close();
         } else {
             File[] listFiles = sourceFile.listFiles();
             if(listFiles == null || listFiles.length == 0){
                 // 需要保留原来的文件结构时,需要对空文件夹进行处理
                 if(KeepDirStructure){
                     // 空文件夹的处理
                     zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                     zos.closeEntry();
                 }
             }else {
                 for (File file : listFiles) {
                     // 判断是否需要保留原来的文件结构
                     if (KeepDirStructure) {
                         // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                         // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                         compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                     } else {
                         compress(file, zos, file.getName(),KeepDirStructure);
                     }
                 }
             }
         }
     }


    /**
     * 解压缩zip文件
     * @param file
     * @throws Exception
     */
     public static void unzip(File file) throws Exception{
         String outPath = BASE_DIR_PATH + FileUtil.SEPARATOR + "unzip";
         File outFile = null;
         ZipFile zipFile = new ZipFile(file);
         ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
         ZipEntry zipEntry = null;
         InputStream inputStream = null;
         OutputStream outputStream = null;
         byte[] b = new byte[1024];
         while ((zipEntry = zipInputStream.getNextEntry()) != null){
             System.out.println("开始解压缩【" + zipEntry.getName() + "】");
             outFile = new File(outPath + FileUtil.SEPARATOR + zipEntry.getName());
             if (!outFile.getParentFile().exists()){
                 outFile.getParentFile().mkdirs();
             }
             if (!outFile.exists()){
                 outFile.createNewFile();
             }
             inputStream = zipFile.getInputStream(zipEntry);
             outputStream = new FileOutputStream(outFile);
             int temp = 0;
             while ((temp = inputStream.read(b)) != -1){
                 outputStream.write(b);
             }
             inputStream.close();
             outputStream.close();
         }
     }

//---------------------------------------------------------压缩流 end---------------------------------------------------------------------------------------------------



}
