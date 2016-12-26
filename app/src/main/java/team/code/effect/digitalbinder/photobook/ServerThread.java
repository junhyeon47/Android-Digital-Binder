package team.code.effect.digitalbinder.photobook;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import team.code.effect.digitalbinder.common.AppConstans;
import team.code.effect.digitalbinder.common.Photobook;
import team.code.effect.digitalbinder.common.ZipCode;
import team.code.effect.digitalbinder.main.MainActivity;

public class ServerThread extends Thread{
    PhotobookListActivity photobookListActivity;
    BluetoothSocket socket;
    ObjectInputStream ois;
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
            ois = new ObjectInputStream(socket.getInputStream());
            while (photobookListActivity.serverFlag){
                int bufferSize = ois.available();
                Log.d("ServerThread", "bufferSize size: "+bufferSize);
                byte[] buffer = new byte[bufferSize];
                int length;

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
                while ((length = ois.read(buffer)) != -1){
                    out.write(buffer, 0, length);
                    Log.d("ServerThread", "length: "+length);
                }
                out.flush();
                byte[] data = bos.toByteArray();

                Log.d("ServerThread", "data size: "+data.length);
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInput in = new ObjectInputStream(bis);
                Object object = in.readObject();

                if(object instanceof Photobook) {
                    ((Photobook) object).setBookmark(0);
                    photobookList.add((Photobook)object);
                    Log.d("ServerThread", "photobook file name: " + ((Photobook)object).getFilename());
                    Log.d("ServerThread", "photobook title: " + ((Photobook)object).getTitle());
                }else if(object instanceof SerializableZipFile){
                    Log.d("ServerThread", "else if ZipFile");
//                    File dir = new File(AppConstans.APP_PATH_PHOTOBOOK);
//                    String fileName = Long.toString(System.currentTimeMillis());
//                    FileOutputStream fos = new FileOutputStream(dir+fileName+AppConstans.EXT_IMAGE);
//                    ((Bitmap)object).compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.close();
//                    filesList.add(new File(dir+fileName+AppConstans.EXT_IMAGE));
                }else if(object instanceof String){
                    photobookListActivity.isSelected = true;
                    break;
                }else {
                    Log.d("ServerThread", "else");
                }
                out.close();
                bos.close();

                in.close();
                bis.close();
            }
            ois.close();
//            //파일로 저장하고
//            int index = 0;
//            for(int i=0; i<photobookList.size(); ++i){
//                File[] files = new File[photobookList.get(i).getNumber()];
//                for(int j=0; j<photobookList.get(i).getNumber(); ++j){
//                    files[j] = filesList.get(index+j);
//                }
//                //zip 만들고
//                String fileName = Long.toString(System.currentTimeMillis());
//
//                ZipCode.zip(files, fileName);
//                //임시파일 삭제하고
//                for(int j=0; j<files.length; ++j){
//                    if(files[i].delete()){
//                        Log.d("ServerThread", "파일 삭제: "+files[i].getName());
//                    }
//                }
//                //DB 등록
//                photobookList.get(i).setFilename(fileName);
//                MainActivity.dao.insert(photobookList.get(i));
//                //리스트 index 맞추기.
//                index += photobookList.get(i).getNumber();
//            }
//            //모든 자원 반납.
            photobookListActivity.isReceived = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
