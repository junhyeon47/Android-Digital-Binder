package team.code.effect.digitalbinder.photobook;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.zip.ZipFile;

import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.BitmapHelper;
import team.code.effect.digitalbinder.common.DeviceHelper;
import team.code.effect.digitalbinder.common.Photobook;
import team.code.effect.digitalbinder.common.ZipCode;

public class ClientThread extends Thread {
    PhotobookListActivity photobookListActivity;
    BluetoothSocket socket;
    ObjectOutputStream oos;

    public ClientThread(PhotobookListActivity photobookListActivity, BluetoothSocket socket) {
        this.photobookListActivity = photobookListActivity;
        this.socket = socket;
    }

    @Override
    public void run() {
        send();
    }

    public void send() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
//            for (int i = 0; i < photobookListActivity.checkedList.size(); ++i) {
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ObjectOutput out = new ObjectOutputStream(baos);
//                out.writeObject(photobookListActivity.checkedList.get(i));
//                byte[] data = baos.toByteArray();
//                oos.write(data);
//                oos.reset();
//                baos.close();
//            }
//            oos.flush();
            while (photobookListActivity.clientFlag) {
                if(photobookListActivity.isSelected) {
                    for (int i = 0; i < photobookListActivity.checkedList.size(); ++i) {
                        //객체를 byte[]로 변환
                        String zipFileName = AppConstans.APP_PATH_DATA + photobookListActivity.checkedList.get(i).getFilename() + AppConstans.EXT_DAT;
                        SerializableZipFile zipFile = new SerializableZipFile(zipFileName);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutput out = new ObjectOutputStream(bos);
                        out.writeObject(zipFile);
                        out.flush();
                        byte[] data = bos.toByteArray();

                        oos.write(data);
                        oos.flush();
                        out.close();
                        bos.close();
                    }
                    oos.writeObject("EOF");
                    oos.flush();
                    oos.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
