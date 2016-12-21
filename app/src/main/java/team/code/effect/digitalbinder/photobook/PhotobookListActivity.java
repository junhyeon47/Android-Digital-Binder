package team.code.effect.digitalbinder.photobook;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.LinearLayout;

import android.widget.Toast;

import java.util.ArrayList;

import team.code.effect.digitalbinder.R;
import team.code.effect.digitalbinder.common.AlertHelper;
import team.code.effect.digitalbinder.common.Photobook;
import team.code.effect.digitalbinder.main.MainActivity;

public class PhotobookListActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    static final int REQUEST_BLUETOOTH_ENABLE = 1;
    static final int REQUEST_ACCESS_PERMISSION = 2;
    String TAG;
    Toolbar toolbar;
    ArrayList<Photobook> list;
    ArrayList<Photobook> checkedList = new ArrayList<>();
    RecyclerView recycler_view;
    PhotobookListRecyclerAdapter photobookListRecyclerAdapter;

    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket socket;
    MenuItem item_add, item_delete, item_send_bluetooth, item_cancel, item_confirm_delete, item_confirm_send;

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
                list = (ArrayList) MainActivity.dao.selectAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                photobookListRecyclerAdapter = new PhotobookListRecyclerAdapter(PhotobookListActivity.this);
                recycler_view.setAdapter(photobookListRecyclerAdapter);
                super.onPostExecute(aVoid);
            }
        }.execute();
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
        item_send_bluetooth = menu.findItem(R.id.item_send_bluetooth);
        item_cancel = menu.findItem(R.id.item_cancel);
        item_confirm_delete = menu.findItem(R.id.item_confirm_delete);
        item_confirm_send = menu.findItem(R.id.item_confirm_send);
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
                clickMenuSendBluetooth();
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
        item_send_bluetooth.setVisible(false);
        item_cancel.setVisible(true);
        item_confirm_delete.setVisible(true);
        item_confirm_send.setVisible(false);
        showCheckBox();
    }

    public void clickMenuCancel(){
        item_add.setVisible(true);
        item_delete.setVisible(true);
        item_send_bluetooth.setVisible(true);
        item_cancel.setVisible(false);
        item_confirm_delete.setVisible(false);
        item_confirm_send.setVisible(false);
        hideCheckBox();
    }

    public void clickMenuSendBluetooth(){
        if(list.size() == 0){
            Toast.makeText(this, "포토북이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        item_add.setVisible(false);
        item_delete.setVisible(false);
        item_send_bluetooth.setVisible(false);
        item_cancel.setVisible(true);
        item_confirm_delete.setVisible(false);
        item_confirm_send.setVisible(true);
        showCheckBox();
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
                    MainActivity.dao.delete(checkedList.get(i).getPhotobook_id());
                }
                list = (ArrayList)MainActivity.dao.selectAll();
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
}
