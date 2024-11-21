package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Service.HttpRequest;
import com.example.myapplication.adapter.DistributorAdapter;
import com.example.myapplication.models.Distributor;
import com.example.myapplication.models.Respone;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    List<Distributor> list;
    String TAG = "DistributorActivity!";
    private RecyclerView recyclerView;
    private DistributorAdapter distributorAdapter;
    private HttpRequest httpRequest;
    private Dialog dialog;
    private FloatingActionButton floatingActionButton;
    private EditText edNameDis , edtSearch;
    private Button btnSaveDiaLog, btnCancelDiaLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSearch = findViewById(R.id.edSearch);
        floatingActionButton = findViewById(R.id.btnAdd);
        httpRequest = new HttpRequest();
        recyclerView = findViewById(R.id.rcvDistributor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        distributorAdapter = new DistributorAdapter();

        distributorAdapter.setOnItemClickListener(new DistributorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                showDialogDelete(id);
            }

            @Override
            public void updateItem(String id, String name) {
                openDialog(id, name);
            }
        });

        onResume();
        floatingActionButton.setOnClickListener(view -> openDialog("", ""));


        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(actionID == EditorInfo.IME_ACTION_SEARCH){
                    String key = edtSearch.getText().toString().trim();
                        httpRequest.callAPI().searchDistributors(key).enqueue(searchListDistributor);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpRequest.callAPI().getDistributors().enqueue(getListDistributor);
    }

    // Callback để lấy danh sách Distributor
    Callback<Respone<ArrayList<Distributor>>> getListDistributor = new Callback<Respone<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Respone<ArrayList<Distributor>>> call, Response<Respone<ArrayList<Distributor>>> response) {
            if (response.body().getStatus() == 200) {
                list = response.body().getData();
                distributorAdapter.setData(list);
                recyclerView.setAdapter(distributorAdapter);
            }
        }

        @Override
        public void onFailure(Call<Respone<ArrayList<Distributor>>> call, Throwable t) {
            Log.i(TAG, "Error: " + t.toString());
        }
    };

    // Callback thêm Distributor
    Callback<Respone<Distributor>> addDistributor = new Callback<Respone<Distributor>>() {
        @Override
        public void onResponse(Call<Respone<Distributor>> call, Response<Respone<Distributor>> response) {
            if (response.isSuccessful() && response.body().getStatus() == 200) {
                Toast.makeText(getApplicationContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                list.add(response.body().getData());
                distributorAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }

        @Override
        public void onFailure(Call<Respone<Distributor>> call, Throwable t) {
            Log.i(TAG, "Error: " + t.toString());
        }
    };

    // Callback cập nhật Distributor
    Callback<Respone<Distributor>> updateDistributor = new Callback<Respone<Distributor>>() {
        @Override
        public void onResponse(Call<Respone<Distributor>> call, Response<Respone<Distributor>> response) {
            if (response.isSuccessful() && response.body().getStatus() == 200) {
                Toast.makeText(getApplicationContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(response.body().getData().getId())) {
                        list.set(i, response.body().getData());
                        break;
                    }
                }
                distributorAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }

        @Override
        public void onFailure(Call<Respone<Distributor>> call, Throwable t) {
            Log.i(TAG, "Error: " + t.toString());
        }
    };

    // Callback xóa Distributor
    Callback<Respone<Distributor>> deleteDistributor = new Callback<Respone<Distributor>>() {
        @Override
        public void onResponse(Call<Respone<Distributor>> call, Response<Respone<Distributor>> response) {
            if (response.isSuccessful() && response.body().getStatus() == 200) {
                Toast.makeText(getApplicationContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(response.body().getData().getId())) {
                        list.remove(i);
                        break;
                    }
                }
                distributorAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<Respone<Distributor>> call, Throwable t) {
            Log.i(TAG, "Error: " + t.toString());
        }
    };


    // Callback tìm kiếm Distributor
    Callback<Respone<ArrayList<Distributor>>> searchListDistributor = new Callback<Respone<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Respone<ArrayList<Distributor>>> call, Response<Respone<ArrayList<Distributor>>> response) {
            if (response.body().getStatus() == 200) {
                list = response.body().getData();
                distributorAdapter.setData(list);
                recyclerView.setAdapter(distributorAdapter);
            }
        }

        @Override
        public void onFailure(Call<Respone<ArrayList<Distributor>>> call, Throwable t) {
            Log.i(TAG, "Error: " + t.toString());
        }
    };

    // Hiển thị Dialog xác nhận xóa
    public void showDialogDelete(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Xóa Distributor");
        builder.setMessage("Bạn có chắc chắn muốn xóa Distributor này?");
        builder.setPositiveButton("Có", (dialogInterface, i) -> httpRequest.callAPI().deleteDistributor(id).enqueue(deleteDistributor));
        builder.setNegativeButton("Không", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    // Mở Dialog thêm/sửa Distributor
    public void openDialog(String id, String name) {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dailog_distributor);
        edNameDis = dialog.findViewById(R.id.edName);
        edNameDis.setText(name);
        btnSaveDiaLog = dialog.findViewById(R.id.btnSave);
        btnCancelDiaLog = dialog.findViewById(R.id.btnCancel);

        btnCancelDiaLog.setOnClickListener(view -> dialog.dismiss());

        btnSaveDiaLog.setOnClickListener(view -> {
            String strName = edNameDis.getText().toString().trim();
            if (strName.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập tên distributor", Toast.LENGTH_SHORT).show();
            } else {
                Distributor distributor = new Distributor();
                distributor.setName(strName);
                if (id.isEmpty()) {
                    httpRequest.callAPI().addDistributor(distributor).enqueue(addDistributor);
                } else {
                    httpRequest.callAPI().updateDistributor(id, distributor).enqueue(updateDistributor);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
