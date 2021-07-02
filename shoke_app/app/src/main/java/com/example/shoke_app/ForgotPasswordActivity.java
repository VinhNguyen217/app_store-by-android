package com.example.shoke_app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edt_email_forgot;
    CardView btn_forgot;
    ProgressBar progressBar_forgot;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getInit();
        setEvent();
    }

    public void getInit() {
        edt_email_forgot = findViewById(R.id.edt_email_forgot);
        btn_forgot = findViewById(R.id.btn_forgot);
        progressBar_forgot = findViewById(R.id.progressBar_forgot);
    }

    public void setEvent(){
        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_forgot.setVisibility(View.VISIBLE);
                String email = edt_email_forgot.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(ForgotPasswordActivity.this, "Yêu cầu nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar_forgot.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                dialog.setTitle("Thông báo");
                                dialog.setMessage("Vui lòng kiểm tra email của bạn để lấy lại mật khẩu");
                                dialog.setIcon(R.drawable.notify);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }else{
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                dialog.setTitle("Thông báo");
                                dialog.setMessage(task.getException().getMessage());
                                dialog.setIcon(R.drawable.notify);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}