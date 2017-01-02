package team.code.effect.digitalbinder.photobook;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.Photobook;
import team.code.effect.digitalbinder.main.MainActivity;

public class ServerThread extends Thread{
    private static int BUFFER_SIZE = 1024;
    PhotobookListActivity photobookListActivity;
    BluetoothSocket socket;
    InputStream in;

    ArrayList<Photobook> photobookList = new ArrayList<>();
    ArrayList<File>filesList = new ArrayList<>();

    public ServerThread(PhotobookListActivity photobookListActivity, BluetoothSocket socket) {
        Log.d("ServerThread", "constructor called");
        this.photobookListActivity = photobookListActivity;
        this.socket = socket;
    }

    @Override
    public void run() {
        listen();
    }

    public void listen(){
        Log.d("ServerThread", "listen method called");
        try {
            in = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            boolean isSelected = ois.readBoolean();
            if(!isSelected){
                socket.close();
            }
            Log.d("ServerThread", "isSelected: "+isSelected);
            photobookListActivity.isSelected = isSelected;
            int numOfPhotobooks = ois.readInt();
            Log.d("ServerThread", "numOfPhotobooks: "+numOfPhotobooks);
            for(int i=0; i<numOfPhotobooks; ++i){
                Object object = ois.readObject();
                if(object instanceof Photobook){
                    photobookList.add((Photobook)object);
                }
            }
            Log.d("ServerThread", "객체 받기 완료");
            DataInputStream dis = new DataInputStream(in);
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            for(int i=0; i<numOfPhotobooks; ++i){
                String zipFilename = dis.readUTF();
                Log.d("ServerThread", "zipFilename: "+zipFilename);
                long dataLength = dis.readLong();
                Log.d("ServerThread", "dataLength: "+dataLength);
                File file = new File(AppConstans.APP_PATH_DATA+zipFilename);
                FileOutputStream fos = new FileOutputStream(file);
                while (dataLength > 0) {
                    length = dis.read(buffer, 0, (int)Math.min(dataLength, buffer.length));
                    fos.write(buffer, 0, length);
                    dataLength -= length;
                }
                fos.close();
                Log.d("ServerThread", "zipFilename: "+zipFilename+" 파일 저장 완료");
            }
            ois.close();
            dis.close();
            Log.d("ServerThread", "파일 받기 완료");
            for(int i=0; i<photobookList.size(); ++i){
                MainActivity.dao.insert(photobookList.get(i));
            }
            Log.d("ServerThread", "DB 저장 완료");
            photobookListActivity.isReceived = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
