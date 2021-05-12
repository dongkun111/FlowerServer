package com.example.flowerserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.FloatArrayEvaluator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flowerserver.Common.Common;
import com.example.flowerserver.Interface.ItemClickListener;
import com.example.flowerserver.Model.Category;
import com.example.flowerserver.Model.Flower;
import com.example.flowerserver.ViewHolder.FlowerViewHolder;
import com.example.flowerserver.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlowerList extends AppCompatActivity {


    FloatingActionButton fab;

    RecyclerView flowerRecycler;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    ImageButton btnBack;

    FirebaseDatabase database;
    DatabaseReference flowerList;

    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId="";



    FirebaseRecyclerAdapter<Flower, FlowerViewHolder>adapter;

    MaterialEditText edtName,edtPrice,edtDetail;
    Button btnSelect, btnUpload;
    Flower newFlower;
    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        flowerList = database.getReference("Flower");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        flowerRecycler = (RecyclerView)findViewById(R.id.recycler_flower);
        flowerRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        flowerRecycler.setLayoutManager(layoutManager);

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(FlowerList.this,Home.class);
                startActivity(home);
            }
        });

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFLowerDialog();
            }
        });



        //get intent here
        if(getIntent()!=null)
            categoryId = getIntent().getStringExtra("idCategory");
        if(!categoryId.isEmpty() && categoryId!=null)
        {
            loadListFlower(categoryId);
        }
    }

    private void showAddFLowerDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FlowerList.this);
        alertDialog.setTitle("Tạo sản phẩm mới");
        alertDialog.setMessage("Vui lòng điền đầy đủ thông tin!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_flower_layout = inflater.inflate(R.layout.add_new_flower_layout,null);

        edtName = add_flower_layout.findViewById(R.id.edtName);
        edtDetail = add_flower_layout.findViewById(R.id.edtDetail);
        edtPrice = add_flower_layout.findViewById(R.id.edtPrice);

        btnSelect = add_flower_layout.findViewById(R.id.btnSelect);
        btnUpload = add_flower_layout.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        alertDialog.setView(add_flower_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_playlist_add_24);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                if(newFlower != null)
                {
                    flowerList.push().setValue(newFlower);
                    Snackbar.make(rootLayout, "Sản phẩm mới "+newFlower.getName()+" đã được thêm"
                            ,Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });
        alertDialog.show();
    }
    private void uploadImage() {
        if(saveUri != null)
        {
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Đang tải lên...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("image/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(FlowerList.this, "Đã tải lên !!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newFlower = new Flower();
                            newFlower.setName(edtName.getText().toString());
                            newFlower.setDetail(edtDetail.getText().toString());
                            newFlower.setPrice(Long.parseLong(edtPrice.getText().toString()));
                            newFlower.setIdCategory(Long.parseLong(categoryId));
                            newFlower.setImgUrl(uri.toString());
                        }
                    });
                }

            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FlowerList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Đã tải lên"+progress+"%");
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chọn hình ảnh"), Common.PICK_IMAGE_REQUEST);
    }


    private void loadListFlower(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Flower, FlowerViewHolder>(
                Flower.class,
                R.layout.flower_item,
                FlowerViewHolder.class,
                flowerList.orderByChild("idCategory").equalTo(Long.parseLong(categoryId))
        )
        {
            @Override
            protected void populateViewHolder(FlowerViewHolder flowerViewHolder, Flower model, int position) {
                flowerViewHolder.flower_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImgUrl())
                        .into(flowerViewHolder.flower_image);
                final Flower clickItem = model;
                flowerViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        flowerRecycler.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Đã chọn");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateFlowerDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteFlower(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    private void showUpdateFlowerDialog(String key, Flower item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FlowerList.this);
        alertDialog.setTitle("Chỉnh sửa sản phẩm");
        alertDialog.setMessage("Vui lòng điền đầy đủ thông tin!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_flower_layout = inflater.inflate(R.layout.add_new_flower_layout,null);

        edtName = add_flower_layout.findViewById(R.id.edtName);
        edtDetail = add_flower_layout.findViewById(R.id.edtDetail);
        edtPrice = add_flower_layout.findViewById(R.id.edtPrice);

        edtName.setText(item.getName());
        edtDetail.setText(item.getDetail());
        edtPrice.setText(item.getPrice().toString());

        btnSelect = add_flower_layout.findViewById(R.id.btnSelect);
        btnUpload = add_flower_layout.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_flower_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_playlist_add_24);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();


                    item.setName(edtName.getText().toString());
                    item.setDetail(edtDetail.getText().toString());
                    item.setPrice(Long.parseLong(edtPrice.getText().toString()));

                    flowerList.child(key).setValue(item);
                    Snackbar.make(rootLayout, "Sản phẩm "+item.getName()+" đã được chỉnh sửa"
                            ,Snackbar.LENGTH_SHORT).show();

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });
        alertDialog.show();
    }

    private void changeImage(final Flower item) {
        if(saveUri != null)
        {
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Đang tải lên...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("image/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(FlowerList.this, "Đã tải lên !!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setImgUrl(uri.toString());
                        }
                    });
                }

            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FlowerList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Đã tải lên"+progress+"%");
                        }
                    });
        }
    }

    private void deleteFlower(String key) {
        flowerList.child(key).removeValue();
    }
}
