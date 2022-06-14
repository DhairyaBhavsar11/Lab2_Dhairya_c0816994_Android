package com.example.labtest2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    TextView datalist;
    TextView datalist_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper=new DatabaseHelper(MainActivity.this);
        Button delete=findViewById(R.id.delete_data);
        Button insert=findViewById(R.id.insert_data);
        Button update=findViewById(R.id.update_data);
        Button read=findViewById(R.id.refresh_data);
        ImageButton search=findViewById(R.id.imageButton);
        datalist=findViewById(R.id.all_data_list);
        datalist_count=findViewById(R.id.data_list_count);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();

            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInputDialog();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateIdDialog();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {showsearchDialog();}
        });

    }

    private void refreshData() {
        datalist_count.setText("ALL DATA COUNT : "+databaseHelper.getTotalCount());

        List<ProductModel> productModelList =databaseHelper.getAllProducts();
        datalist.setText("");
        for(ProductModel productModel : productModelList){
            datalist.append("ID : "+ productModel.getId()+"\n" + "| product_ID : "+ productModel.getproduct_id()+"\n" + "| Product_Name : "+ productModel.getProduct_name()+"\n" + "| Product_Price : "+ productModel.getProduct_price()+"\n" + "| Product_description : "+ productModel.getProduct_description()+" \n\n");
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder al=new AlertDialog.Builder(MainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.delete_dialog,null);
        al.setView(view);
        final EditText id_input=view.findViewById(R.id.id_input);
        Button delete_btn=view.findViewById(R.id.delete_btn);
        final AlertDialog alertDialog=al.show();

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteProduct(id_input.getText().toString());
                alertDialog.dismiss();
                refreshData();

            }
        });


    }

    private void showUpdateIdDialog() {
        AlertDialog.Builder al=new AlertDialog.Builder(MainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.update_id_dialog,null);
        al.setView(view);
        final EditText id_input=view.findViewById(R.id.id_input);
        Button fetch_btn=view.findViewById(R.id.update_id_btn);
        final AlertDialog alertDialog=al.show();
        fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataDialog(id_input.getText().toString());
                alertDialog.dismiss();
                refreshData();
            }
        });

    }

    private void showsearchDialog() {
        AlertDialog.Builder al=new AlertDialog.Builder(MainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.search_dialog,null);
        al.setView(view);
        final EditText name_input=view.findViewById(R.id.name_input);
        Button fetch_btn=view.findViewById(R.id.search_id_btn);
        final AlertDialog alertDialog=al.show();
        fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showsrchDataDialog(name_input.getText().toString());
                alertDialog.dismiss();
                refreshData();
            }
        });

    }

    private void showDataDialog(final String id) {
        ProductModel productModel =databaseHelper.getProduct(Integer.parseInt(id));
        AlertDialog.Builder al=new AlertDialog.Builder(MainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.update_dialog,null);
        final EditText productID=view.findViewById(R.id.productid);
        final EditText productName=view.findViewById(R.id.productname);
        final EditText productDescription=view.findViewById(R.id.productdescription);
        final EditText productPrice=view.findViewById(R.id.price);
        Button update_btn=view.findViewById(R.id.update_btn);
        al.setView(view);

        productID.setText(productModel.getproduct_id());
        productName.setText(productModel.getProduct_name());
        productDescription.setText(productModel.getProduct_description());
        productPrice.setText(productModel.getProduct_price());

        final AlertDialog alertDialog=al.show();
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel productModel =new ProductModel();
                productModel.setproduct_id(productID.getText().toString());
                productModel.setId(id);
                productModel.setProduct_name(productName.getText().toString());
                productModel.setProduct_description(productDescription.getText().toString());
                productModel.setProduct_price(productPrice.getText().toString());
                databaseHelper.updateProduct(productModel);
                alertDialog.dismiss();
                refreshData();
            }
        });
    }



    private void ShowInputDialog() {
        AlertDialog.Builder al=new AlertDialog.Builder(MainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.insert_dialog,null);
        final EditText productID=view.findViewById(R.id.productid);
        final EditText productName=view.findViewById(R.id.productname);
        final EditText productDescription=view.findViewById(R.id.productdescription);
        final EditText productPrice=view.findViewById(R.id.price);
        Button insertBtn=view.findViewById(R.id.insert_btn);
        al.setView(view);

        final AlertDialog alertDialog=al.show();

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel productModel =new ProductModel();
                productModel.setproduct_id(productID.getText().toString());
                productModel.setProduct_name(productName.getText().toString());
                productModel.setProduct_description(productDescription.getText().toString());
                productModel.setProduct_price(productPrice.getText().toString());
                Date date=new Date();
                productModel.setCreated_at(""+date.getTime());
                databaseHelper.AddProduct(productModel);
                alertDialog.dismiss();
                refreshData();
            }
        });
    }

    private void showsrchDataDialog(final String name) {
        ProductModel productModel =databaseHelper.getsrchProduct(name);
        if (productModel == null){

        }
        AlertDialog.Builder al=new AlertDialog.Builder(MainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.update_dialog,null);
        final EditText productID=view.findViewById(R.id.productid);
        final EditText productName=view.findViewById(R.id.productname);
        final EditText productDescription=view.findViewById(R.id.productdescription);
        final EditText productPrice=view.findViewById(R.id.price);
        Button update_btn=view.findViewById(R.id.update_btn);
        al.setView(view);

        productID.setText(productModel.getproduct_id());
        productName.setText(productModel.getProduct_name());
        productDescription.setText(productModel.getProduct_description());
        productPrice.setText(productModel.getProduct_price());

        final AlertDialog alertDialog=al.show();
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel productModel =new ProductModel();
                productModel.setproduct_id(productID.getText().toString());
                productModel.setId(name);
                productModel.setProduct_name(productName.getText().toString());
                productModel.setProduct_description(productDescription.getText().toString());
                productModel.setProduct_price(productPrice.getText().toString());
                databaseHelper.updateProduct(productModel);
                alertDialog.dismiss();
                refreshData();
            }
        });
    }
}