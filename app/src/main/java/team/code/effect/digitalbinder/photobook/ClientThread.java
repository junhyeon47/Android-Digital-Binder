package team.code.effect.digitalbinder.photobook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.ZipFile;

import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.BitmapHelper;
import team.code.effect.digitalbinder.common.DeviceHelper;
import team.code.effect.digitalbinder.common.Photobook;
import team.code.effect.digitalbinder.common.ZipCode;

public class ClientThread extends Thread {
    PhotobookListActivity photobookListActivity;
    ObjectOutputStream oos;
    BufferedInputStream bis;
    BufferedOutputStream bos;

    public ClientThread(PhotobookListActivity photobookListActivity) {
        this.photobookListActivity = photobookListActivity;
    }

    @Override
    public void run() {
        send();
    }

    public void send() {
        try {
            oos = new ObjectOutputStream(photobookListActivity.connectSocket.getOutputStream());
            for (int i = 0; i < photobookListActivity.checkedList.size(); ++i) {
                oos.writeObject(photobookListActivity.checkedList.get(i));
            }
            oos.flush();
            for(int i=0; i<photobookListActivity.checkedList.size(); ++i){
                String zipFileName = AppConstans.APP_PATH_DATA+photobookListActivity.checkedList.get(i).getFilename()+AppConstans.EXT_DAT;
                ZipCode.unzip(zipFileName);
                File[] files = new File(AppConstans.APP_PATH_PHOTOBOOK).listFiles();
                for(int j=0; j<files.length; ++j) {
                    SerializableBitmap bitmap = new SerializableBitmap(files[j].getAbsolutePath());
                    bitmap.writeObject(oos);
                    if(files[j].delete()){
                        Log.d("ClientThread", "파일 삭제: "+files[j].getName());
                    }
                }
                //unzip한 파일 삭제.
            }
            oos.writeObject("EOF");
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
