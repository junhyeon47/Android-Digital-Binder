package team.code.effect.digitalbinder.photobook;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import team.code.effect.digitalbinder.common.AppConstans;

public class ClientThread extends Thread {
    private static int BUFFER_SIZE = 1024;
    PhotobookListActivity photobookListActivity;
    BluetoothSocket socket;
    OutputStream out;
    InputStream in;

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
            out = socket.getOutputStream();
            in = socket.getInputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            while (photobookListActivity.clientFlag) {
                if(photobookListActivity.isSelected) {
                    oos.writeBoolean(true);
                    oos.writeInt(photobookListActivity.checkedList.size());
                    for (int i = 0; i < photobookListActivity.checkedList.size(); ++i) {
                        oos.writeObject(photobookListActivity.checkedList.get(i));
                        oos.flush();
                    }

                    DataOutputStream dos = new DataOutputStream(out);
                    for(int i=0; i<photobookListActivity.checkedList.size(); ++i){
                        String zipFileName = AppConstans.APP_PATH_DATA + photobookListActivity.checkedList.get(i).getFilename() + AppConstans.EXT_DAT;
                        File file = new File(zipFileName);
                        Log.d("ClientThread", "file length: "+file.length());
                        FileInputStream fis = new FileInputStream(zipFileName);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int length;
                        long dataLength = file.length();
                        dos.writeUTF(photobookListActivity.checkedList.get(i).getFilename() + AppConstans.EXT_DAT);
                        dos.writeLong(dataLength);
                        while (dataLength > 0){
                            length = fis.read(buffer);
                            dos.write(buffer, 0, length);
                            dataLength -= length;
                        }
                        dos.flush();
                    }
                    ObjectInputStream ois = new ObjectInputStream(in);
                    boolean isComplete = ois.readBoolean();
                    if(isComplete) {
                        in.close();
                        out.close();
                    }
                    photobookListActivity.clientFlag = false;
                    photobookListActivity.isReceived = true;
                } else if(photobookListActivity.isCanceled){
                    oos.writeBoolean(false);
                    oos.close();
                    photobookListActivity.clientFlag = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
