package team.code.effect.digitalbinder.photobook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.LinearLayout;

import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AlertHelper;
import team.code.effect.digitalbinder.common.Photobook;
import team.code.effect.digitalbinder.main.MainActivity;

public class PhotobookListActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    static final int REQUEST_BLUETOOTH_ENABLE = 1;
    static final int REQUEST_DISCOVERABLE = 2;
    static final int BLUETOOTH_SEARCH_TIME = 60*3;
    static final UUID SERVER_UUID = UUID.fromString("de5d6b03-f23b-43e5-a773-ddd20097d4da");
    static final String MESSAGE_KEY = "MESSAGE_KEY";
    static final int MESSAGE_CONNECTED = 1;

    String TAG;
    Toolbar toolbar;
    ArrayList<Photobook> list;
    ArrayList<Photobook> checkedList = new ArrayList<>();
    RecyclerView recycler_view;
    PhotobookListRecyclerAdapter photobookListRecyclerAdapter;

    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket acceptSocket, connectSocket;
    BluetoothServerSocket serverSocket;
    BroadcastReceiver receiver;
    ArrayList<Device> deviceList = new ArrayList<>();
    RecyclerView recycler_view_device;
    DeviceRecyclerAdapter deviceRecyclerAdapter;
    Handler handler;
    Thread acceptThread, connectThread;
    ProgressDialog progressDialog;
    boolean isAccepted = false;

    MenuItem item_add, item_delete, item_send_receive_bluetooth, item_cancel, item_confirm_delete, item_confirm_send, item_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobook_list);
        TAG = getClass().getName();

        initToolbar();

        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                while (true) {
                    MainActivity.db.beginTransaction();
                    list = (ArrayList) MainActivity.dao.selectAll();
                    MainActivity.db.setTransactionSuccessful();
                    MainActivity.db.endTransaction();
                    if(list != null)
                        break;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                photobookListRecyclerAdapter = new PhotobookListRecyclerAdapter(PhotobookListActivity.this);
                recycler_view.setAdapter(photobookListRecyclerAdapter);
                super.onPostExecute(aVoid);
            }
        }.execute();

        recycler_view_device = (RecyclerView)findViewById(R.id.recycler_view_device);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recycler_view_device.setLayoutManager(layoutManager);
        recycler_view_device.setHasFixedSize(true);
        recycler_view_device.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        deviceRecyclerAdapter = new DeviceRecyclerAdapter(this);
        recycler_view_device.setAdapter(deviceRecyclerAdapter);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)){
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Device device;
                    boolean isDuplicated = false;
                    for(int i=0; i<deviceList.size(); ++i){
                        device = deviceList.get(i);
                        if(device.getAddress().equals(bluetoothDevice.getAddress())){
                            isDuplicated = true;
                            break;
                        }
                    }
                    if(isDuplicated)
                        return;
                    else
                        device = new Device();

                    String name = bluetoothDevice.getName();
                    if(name != null) {
                        switch (bluetoothDevice.getBluetoothClass().getMajorDeviceClass()) {
                            case BluetoothClass.Device.Major.COMPUTER:
                                device.setId(R.drawable.ic_laptop_pink_24dp);
                                break;
                            case BluetoothClass.Device.Major.PHONE:
                                    device.setId(R.drawable.ic_phone_android_pink_24dp);
                                break;
                            default:
                                if (name.toLowerCase().contains("macbook"))
                                    device.setId(R.drawable.ic_laptop_mac_pink_24dp);
                                else if (name.toLowerCase().contains("iphone"))
                                    device.setId(R.drawable.ic_phone_iphone_pink_24dp);
                                else
                                    return;
                                break;
                        }
                        device.setName(bluetoothDevice.getName());
                        device.setAddress(bluetoothDevice.getAddress());
                        device.setBluetoothDevice(bluetoothDevice);
                        deviceList.add(device);
                        deviceRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        handler = new Handler(){
            @Override
            public void handleMessage(Message message) {
                switch (message.getData().getInt(MESSAGE_KEY)){
                    case MESSAGE_CONNECTED:
                        showPhotoBooks();
                        break;
                }
            }
        };
    }

    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("포토북");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바에 뒤로가기 버튼 추가.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp); //뒤로가기 버튼 아이콘 변경
        toolbar.setOnMenuItemClickListener(this);
    }

    /*Toolbar menu 생성*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photobook, menu);
        item_add = menu.findItem(R.id.item_add);
        item_delete = menu.findItem(R.id.item_delete);
        item_send_receive_bluetooth = menu.findItem(R.id.item_send_bluetooth);
        item_cancel = menu.findItem(R.id.item_cancel);
        item_confirm_delete = menu.findItem(R.id.item_confirm_delete);
        item_confirm_send = menu.findItem(R.id.item_confirm_send);
        item_refresh = menu.findItem(R.id.item_refresh);

        checkSupportBluetooth();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                clickMenuAdd();
                break;
            case R.id.item_delete:
                clickMenuDelete();
                break;
            case R.id.item_send_bluetooth:
                clickMenuSendReceiveBluetooth();
                //checkActiveBluetooth();
                break;
            case R.id.item_cancel:
                clickMenuCancel();
                break;
            case R.id.item_confirm_delete:
                clickMenuConfirmDelete();
                break;
            case R.id.item_confirm_send:
                clickMenuConfirmSend();
                break;
            case R.id.item_refresh:
                clickMenuRefresh();
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(recycler_view_device.getVisibility() == View.VISIBLE) {
            bluetoothAdapter.cancelDiscovery();
            deviceList.removeAll(deviceList);
            item_add.setVisible(true);
            item_delete.setVisible(true);
            item_send_receive_bluetooth.setVisible(true);
            item_refresh.setVisible(false);
            recycler_view.setVisibility(View.VISIBLE);
            recycler_view_device.setVisibility(View.GONE);
            getSupportActionBar().setTitle("포토북");
        }else if(recycler_view.getVisibility() == View.VISIBLE){
            finish();
        }
    }

    public void showCheckBox() {
        photobookListRecyclerAdapter.isDeleteMemuClicked = true;
        photobookListRecyclerAdapter.notifyDataSetChanged();
    }

    public void hideCheckBox(){
        photobookListRecyclerAdapter.isDeleteMemuClicked = false;
        photobookListRecyclerAdapter.notifyDataSetChanged();
    }

    public void clickMenuDelete(){
        if(list.size() == 0){
            Toast.makeText(this, "포토북이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        item_add.setVisible(false);
        item_delete.setVisible(false);
        item_send_receive_bluetooth.setVisible(false);
        item_cancel.setVisible(true);
        item_confirm_delete.setVisible(true);
        item_confirm_send.setVisible(false);
        showCheckBox();
    }

    public void clickMenuCancel(){
        item_add.setVisible(true);
        item_delete.setVisible(true);
        item_send_receive_bluetooth.setVisible(true);
        item_cancel.setVisible(false);
        item_confirm_delete.setVisible(false);
        item_confirm_send.setVisible(false);
        hideCheckBox();
    }

    public void clickMenuSendReceiveBluetooth(){
        if(list.size() == 0){
            Toast.makeText(this, "포토북이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = AlertHelper.getAlertDialog(this, "알림", "블루투스로 포토북을 보내기 또는 받기를 진행합니다. 계속하시겠습니까?.");
        builder.setPositiveButton("보내기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recycler_view.setVisibility(View.GONE);
                recycler_view_device.setVisibility(View.VISIBLE);
                item_add.setVisible(false);
                item_delete.setVisible(false);
                item_send_receive_bluetooth.setVisible(false);
                item_refresh.setVisible(true);
                checkActiveBluetooth();
            }
        });
        builder.setNegativeButton("받기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestDiscoverable();
            }
        });
        builder.setNeutralButton("취소", null);
        builder.setCancelable(false);
        builder.show();
    }
    private void clickMenuAdd() {
        Toast.makeText(this, "더하기 버튼 클릭", Toast.LENGTH_SHORT).show();
    }

    private void clickMenuConfirmDelete() {
        if(checkedList.size() == 0) {
            Toast.makeText(this, "선택한 포토북이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = AlertHelper.getAlertDialog(this, "알림", "선택한 포토북을 삭제합니다.");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int arg) {
                for(int i=0; i<checkedList.size(); ++i){
                    MainActivity.db.beginTransaction();
                    MainActivity.dao.delete(checkedList.get(i).getPhotobook_id());
                    MainActivity.db.setTransactionSuccessful();
                    MainActivity.db.endTransaction();
                }
                MainActivity.db.beginTransaction();
                list = (ArrayList)MainActivity.dao.selectAll();
                MainActivity.db.setTransactionSuccessful();
                MainActivity.db.endTransaction();
                photobookListRecyclerAdapter.notifyDataSetChanged();
                clickMenuCancel();
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }

    private void clickMenuConfirmSend() {
        Toast.makeText(this, "보내기 버튼 클릭", Toast.LENGTH_SHORT).show();
    }

    private void clickMenuRefresh(){
        deviceList.removeAll(deviceList);
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_BLUETOOTH_ENABLE:
                if(resultCode == Activity.RESULT_OK)
                    scanBluetoothDevice();
                else
                    Toast.makeText(this, "블루투스를 활성화 시키지 않으면 보내기를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_DISCOVERABLE:
                if(resultCode != Activity.RESULT_CANCELED){
                    acceptDevice();
                    receivePhotobooks();
                }else
                    Toast.makeText(this, "블루투스를 찾을수 있게 하지 않으면 받기를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    //블루투스를 지원하는지 체크
    public void checkSupportBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null)
            item_send_receive_bluetooth.setEnabled(false);
        else {
            item_send_receive_bluetooth.setEnabled(true);
        }
    }

    //블루투스가 활성화 되어 있는지 체크
    public void checkActiveBluetooth(){
        if(bluetoothAdapter.isEnabled()){
            scanBluetoothDevice();
        }else{
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
        }
    }

    //주변의 블루투스가 활성화된 기기들을 찾아 리스트로 뿌려준다.
    public void scanBluetoothDevice(){
        getSupportActionBar().setTitle("기기 검색");
        recycler_view.setVisibility(View.GONE);
        recycler_view_device.setVisibility(View.VISIBLE);
        item_add.setVisible(false);
        item_delete.setVisible(false);
        item_send_receive_bluetooth.setVisible(false);
        item_refresh.setVisible(true);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        bluetoothAdapter.startDiscovery();
    }

    public void connectDevice(String address){
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        if(bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            unregisterReceiver(receiver);
        }
        try {
            connectSocket = bluetoothDevice.createRfcommSocketToServiceRecord(SERVER_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectThread = new Thread(){
            @Override
            public void run() {
                try {
                    connectSocket.connect();
                    Log.d(TAG, "클라이언트 접속 성공");
                    Bundle bundle = new Bundle();
                    bundle.putInt(MESSAGE_KEY, MESSAGE_CONNECTED);
                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        connectThread.start();
    }

    //클라이언트가 서버인 나를 발견할 수 있도록 검색허용 옵션을 지정하는 메서드.
    public void requestDiscoverable(){
        Intent intent = new Intent();
        intent.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BLUETOOTH_SEARCH_TIME);
        startActivityForResult(intent, REQUEST_DISCOVERABLE);
    }

    public void acceptDevice(){
        try {
            serverSocket =bluetoothAdapter.listenUsingRfcommWithServiceRecord(getPackageName(), SERVER_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        acceptThread = new Thread(){
            @Override
            public void run() {
                try {
                    acceptSocket = serverSocket.accept();
                    isAccepted = true;
                    Log.d(TAG, "접속자 감지");

                    acceptSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        acceptThread.start();
    }

    public void receivePhotobooks(){
        new AsyncTask<Void, Integer, Void>(){

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(PhotobookListActivity.this);
                progressDialog.setMessage("사용자 접속 대기 중...");
                progressDialog.setMax(BLUETOOTH_SEARCH_TIME);
                progressDialog.setCancelable(false);
                progressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                int second = 0;
                while (true){
                    if(isAccepted || second > BLUETOOTH_SEARCH_TIME)
                        break;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    second++;
                    publishProgress(second);
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                progressDialog.dismiss();
                super.onPostExecute(aVoid);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressDialog.setProgress(values[0]);
                super.onProgressUpdate(values);
            }
        }.execute();
    }
    public void showPhotoBooks(){
        getSupportActionBar().setTitle("포토북");
        recycler_view.setVisibility(View.VISIBLE);
        recycler_view_device.setVisibility(View.GONE);
        item_refresh.setVisible(false);
        item_confirm_send.setVisible(true);
        item_cancel.setVisible(true);
        showCheckBox();
    }
}
