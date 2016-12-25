package team.code.effect.digitalbinder.photobook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SerializableBitmap implements Serializable {
    public Bitmap bitmap;
    public SerializableBitmap(String fileName){
        bitmap = BitmapFactory.decodeFile(fileName);
    }
    // Converts the Bitmap into a byte array for serialization
    public void writeObject(java.io.ObjectOutputStream oos) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
        byte bitmapBytes[] = byteStream.toByteArray();
        oos.write(bitmapBytes, 0, bitmapBytes.length);
    }

    // Deserializes a byte array representing the Bitmap and decodes it
    public void readObject(java.io.ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int length;
        while((length = ois.read()) != -1)
            byteStream.write(length);
        byte bitmapBytes[] = byteStream.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }
}
