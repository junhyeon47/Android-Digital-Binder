package team.code.effect.digitalbinder.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class PreviewThread extends Thread {
    Preview preview;
    ImageView imageView;
    Handler handler;

    public PreviewThread(final Preview preview, final ImageView imageView) {
        this.preview = preview;
        this.imageView = imageView;

        handler = new Handler(){
            @Override
            public void handleMessage(Message message) {
                byte[] bytes = message.getData().getByteArray("bytes");
                int orientation = message.getData().getInt("orientation");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inTempStorage = new byte[16 * 1024];
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, 1080, bitmapHeight/(bitmapWidth/1080), false);
                Matrix matrix = new Matrix();

                switch (orientation){
                    case 0:
                        matrix.preRotate(180f);
                        break;
                    case 1:
                        matrix.preRotate(90f);
                        break;
                    case 2:
                        matrix.preRotate(0f);
                        break;
                    case 3:
                        matrix.preRotate(-90f);
                        break;
                }

                Bitmap rotate = Bitmap.createBitmap(resize, 0, 0, 1080, bitmapHeight/(bitmapWidth/1080), matrix,false);
                imageView.setImageBitmap(rotate);
            }
        };
    }

    @Override
    public void run() {
        Bundle bundle = new Bundle();
        bundle.putByteArray("bytes", preview.getBytes());
        bundle.putInt("orientation", preview.getOrientation());
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }
}