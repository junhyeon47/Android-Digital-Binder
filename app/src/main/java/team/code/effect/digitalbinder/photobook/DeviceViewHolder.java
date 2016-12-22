package team.code.effect.digitalbinder.photobook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import team.code.effect.digitalbinder.R;

public class DeviceViewHolder extends RecyclerView.ViewHolder {
    LinearLayout layout_device;
    ImageView iv_device;
    TextView txt_device_name;
    TextView txt_device_address;
    public DeviceViewHolder(View itemView) {
        super(itemView);
        layout_device = (LinearLayout)itemView.findViewById(R.id.layout_device);
        iv_device = (ImageView)itemView.findViewById(R.id.iv_device);
        txt_device_name = (TextView)itemView.findViewById(R.id.txt_device_name);
        txt_device_address = (TextView)itemView.findViewById(R.id.txt_device_address);
    }
}
