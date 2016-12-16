package team.code.effect.digitalbinder.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by huho on 2016-11-26.
 */

public class ZipCode {

    private static final int COMPRESSION_LEVEL = 8;

    private static final int BUFFER_SIZE = 1024 * 2;


    /**
     * 지정된 폴더를 Zip 파일로 압축한다.
     *
     * @param output     - 저장 zip 파일 이름
     * @throws Exception
     */
    public static void zip(ArrayList<File> sourceFile, String output) throws Exception {

        // 압축 대상(sourcePath)이 디렉토리나 파일이 아니면 리턴한다.
   /*     File sourceFile = new File(sourcePath);
        if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
            throw new Exception("압축 대상의 파일을 찾을 수가 없습니다.");
        }
*/
        // output 의 확장자가 zip이 아니면 리턴한다.
        //if (!(StringUtils.substringAfterLast(output, ".")).equalsIgnoreCase("zip")) {
        //    throw new Exception("압축 후 저장 파일명의 확장자를 확인하세요");
        //}

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;

        try {
            fos = new FileOutputStream(output); // FileOutputStream
            bos = new BufferedOutputStream(fos); // BufferedStream
            zos = new ZipOutputStream(bos); // ZipOutputStream
            zos.setLevel(COMPRESSION_LEVEL); // 압축 레벨 - 최대 압축률은 9, 디폴트 8

            zipEntry(sourceFile, zos); // Zip 파일 생성
            zos.finish(); // ZipOutputStream finish
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 압축
     *
     * @param sourceFile
     * @param zos
     * @throws Exception
     */
    private static void zipEntry(ArrayList<File> sourceFile, ZipOutputStream zos) throws Exception {
        // sourceFile 이 디렉토리인 경우 하위 파일 리스트 가져와 재귀호출
        /*if (sourceFile.isDirectory()) {
            if (sourceFile.getName().equalsIgnoreCase(".metadata")) { // .metadata 디렉토리 return
                return;
            }
            File[] fileArray = sourceFile.listFiles(); // sourceFile 의 하위 파일 리스트
            for (int i = 0; i < fileArray.length; i++) {
                zipEntry(fileArray[i], sourcePath, zos); // 재귀 호출
            }
        } else { // sourcehFile 이 디렉토리가 아닌 경우*/
        for(int i=0;i<sourceFile.size();i++) {
            File file=sourceFile.get(i);
            BufferedInputStream bis = null;
            try {
                String sFilePath = file.getPath();
                Log.i("aa", sFilePath);
                //String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());
                StringTokenizer tok = new StringTokenizer(sFilePath, "/");

                int tok_len = tok.countTokens();
                String zipEntryName = tok.toString();
                while (tok_len != 0) {
                    tok_len--;
                    zipEntryName = tok.nextToken();
                }
                bis = new BufferedInputStream(new FileInputStream(file));

                ZipEntry zentry = new ZipEntry(zipEntryName);
                zentry.setTime(file.lastModified());
                zos.putNextEntry(zentry);

                byte[] buffer = new byte[BUFFER_SIZE];
                int cnt = 0;

                while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zos.write(buffer, 0, cnt);
                }
                zos.closeEntry();
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }

        }
    }

    /**
     * Zip 파일의 압축을 푼다.
     *
     * @param zipFile             - 압축 풀 Zip 파일
     * @param targetDir           - 압축 푼 파일이 들어간 디렉토리
     * @param fileNameToLowerCase - 파일명을 소문자로 바꿀지 여부
     * @throws Exception
     */
    public static List unzip(String zipFile, String targetDir, boolean fileNameToLowerCase) throws Exception {
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry zentry = null;
        // ArrayList<File> fileList= new ArrayList<File>();
        ArrayList<Bitmap> isList = new ArrayList<Bitmap>();
        try {
            fis = new FileInputStream(zipFile); // FileInputStream
            zis = new ZipInputStream(fis); // ZipInputStream

            while ((zentry = zis.getNextEntry()) != null) {
                String fileNameToUnzip = zentry.getName();
                if (fileNameToLowerCase) { // fileName toLowerCase
                    fileNameToUnzip = fileNameToUnzip.toLowerCase();
                }

                File targetFile = new File(targetDir, fileNameToUnzip);

                if (zentry.isDirectory()) {// Directory 인 경우
                    //FileUtils.makeDir(targetFile.getAbsolutePath()); // 디렉토리 생성
                    File path = new File(targetFile.getAbsolutePath());
                    path.mkdirs();
                } else { // File 인 경우
                    // parent Directory 생성
                    //FileUtils.makeDir(targetFile.getParent());
                    File path = new File(targetFile.getParent());
                    path.mkdirs();
                    //  fileList.add(unzipEntry(zis, targetFile));
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    Bitmap bitmap = BitmapFactory.decodeStream(zis);

                    if (bitmap.getWidth() > 1000 && bitmap.getHeight() < bitmap.getWidth()) {
                        bitmap = bitmap.createScaledBitmap(bitmap, 1024, 768, true);
                    } else if (bitmap.getHeight() > 1000 && bitmap.getHeight() > bitmap.getWidth()) {
                        bitmap = bitmap.createScaledBitmap(bitmap, 768, 1024, true);
                    }
                    isList.add(bitmap);
                    //  isList.add(unzipBitmap(zis, targetFile));
                }

            }
        } finally {
            if (zis != null) {
                zis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return isList;
    }

    /**
     * Zip 파일의 한 개 엔트리의 압축을 푼다.
     *
     * @param zis - Zip Input Stream
     * @return
     * @throws Exception
     * @paramfilePath - 압축 풀린 파일의 경로
     */
    protected static File unzipEntry(ZipInputStream zis, File targetFile) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = zis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return targetFile;
    }

    //file생성후 비트맴 뽑기
    protected static Bitmap unzipBitmap(ZipInputStream zis, File targetFile) throws Exception {
        Bitmap bitmap = null;
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            fos = new FileOutputStream(targetFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = zis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is = new FileInputStream(targetFile);
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
            targetFile.delete();
        }

        return bitmap;
    }
}
