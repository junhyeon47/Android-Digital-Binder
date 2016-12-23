package team.code.effect.digitalbinder.explorer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.MediaStorageHelper;
import team.code.effect.digitalbinder.common.Photobook;
import team.code.effect.digitalbinder.main.MainActivity;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by student on 2016-12-21.
 */

public class BookMaker extends AsyncTask<String, String, Photobook> {
    private Context context;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private int progressCount = 0;
    ArrayList<File> files;
    String title;

    public BookMaker(Context context, Dialog dialog, ArrayList<File> files) {
        this.context = context;
        this.dialog = dialog;
        this.files=files;
        progressDialog = new ProgressDialog(dialog.getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("준비");
        progressDialog.show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Photobook doInBackground(String... params) {
        title = params[0];
        int color = Integer.parseInt(params[1]);
        String filename = Long.toString(System.currentTimeMillis());
        File dataDir = new File(AppConstans.APP_PATH_DATA);


        progressDialog.setMax(files.size()*2);
        boolean isCreateDir;

        if(!dataDir.exists()) { //data 폴더가 존재하지 않으면 폴더 생성.
            isCreateDir = dataDir.mkdirs();
            if(!isCreateDir) {
                //폴더 생성 실패로 액티비티를 종료해야한다.
                return null;
            }
        }

        try {
            //파일로 저장된 이미지를 하나의 압축파일로 묶음.
            byte[] buff = new byte[16 * 1024];

            FileOutputStream fos = new FileOutputStream(AppConstans.APP_PATH_DATA + filename + AppConstans.EXT_DAT);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos);
            for (int i = 0; i < files.size(); ++i) {
                FileInputStream fis  = new FileInputStream(files.get(i));
                zos.putNextEntry(new ZipEntry((i + 1) + ".jpg"));
                int length;
                while ((length = fis.read(buff, 0, 16 * 1024)) > 0) {
                    zos.write(buff, 0, length);
                }
                publishProgress("progress", Integer.toString(++progressCount), "파일 변환하는 중...");
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();

            /*//temp 폴더에 저장된 파일 모두 삭제.
            for(int i=0; i<files.length; ++i){
                if(files[i].delete()){
                    publishProgress("progress", Integer.toString(++progressCount), "임시 파일을 삭제하는 중...");
                }
            }
            MediaStorageHelper.deleteAll(context, MediaStorageHelper.WHERE_TEMP);*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        //DB에 저장할 DTO 생성 및 반환
        Photobook photobook = new Photobook();
        photobook.setFilename(filename);
        photobook.setTitle(title);
        photobook.setColor(color);
        return photobook;
    }

    @Override
    protected void onPostExecute(Photobook photobook) {
        MainActivity.dao.insert(photobook);
        progressDialog.dismiss();
        dialog.dismiss();
        Toast.makeText(context, title+" 만들어졌습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        if(progress[0].equals("progress")) {
            progressDialog.setProgress(Integer.parseInt(progress[1]));
            progressDialog.setMessage(progress[2]);
        }
    }
}
