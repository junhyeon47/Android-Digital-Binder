package team.code.effect.digitalbinder.photobook;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AlertHelper;


public class DeviceRecyclerAdapter extends RecyclerView.Adapter<DeviceViewHolder>{
    PhotobookListActivity photobookListActivity;

    public DeviceRecyclerAdapter(PhotobookListActivity photobookListActivity) {
        this.photobookListActivity = photobookListActivity;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, int position) {
        final Device device = photobookListActivity.deviceList.get(position);
        new AsyncTask<Integer, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Integer... params) {
                return BitmapFactory.decodeResource(photobookListActivity.getResources(), params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                holder.iv_device.setImageBitmap(bitmap);
                super.onPostExecute(bitmap);
            }
        }.execute(device.getId());
        holder.txt_device_name.setText(device.getName());
        holder.txt_device_address.setText(device.getAddress());

        holder.layout_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = AlertHelper.getAlertDialog(photobookListActivity, "알림", "선택한 기기와 블루투스 연결을 시도합니다. 계속하시겠습니까?");
                builder.setView(R.layout.layout_alert_bluetooth_connect);
                builder.setPositiveButton("연결", null);
                builder.setNegativeButton("취소", null);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialogInterface) {
                        TextView txt_device_name = (TextView)((Dialog)dialogInterface).findViewById(R.id.txt_device_name);
                        TextView txt_device_address = (TextView)((Dialog)dialogInterface).findViewById(R.id.txt_device_address);
                        txt_device_name.setText(device.getName());
                        txt_device_address.setText(device.getAddress());
                        Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                photobookListActivity.connectDevice(device.getAddress());
                                dialogInterface.dismiss();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return photobookListActivity.deviceList.size();
    }
}
