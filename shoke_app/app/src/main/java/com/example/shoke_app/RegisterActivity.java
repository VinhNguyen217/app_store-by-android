package com.example.shoke_app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_email,edt_password,edt_confirm;
    Button btn_return;
    CardView btn_register;
    ProgressBar progressBar_register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        getInit();
        setEvent();
    }

    public void getInit(){
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        edt_email = findViewById(R.id.edt_email_register);
        edt_password = findViewById(R.id.edt_password_register);
        edt_confirm = findViewById(R.id.edt_confirm_register);
        btn_register = findViewById(R.id.btn_register1);
        btn_return = findViewById(R.id.btn_return);
        progressBar_register = findViewById(R.id.progressBar_register);
    }

    public void setEvent() {

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register(){
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String confirmPassword = edt_confirm.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Y??u c???u nh???p ?????y ????? th??ng tin", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Y??u c???u nh???p ?????y ????? th??ng tin", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "Y??u c???u nh???p ?????y ????? th??ng tin", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword)){
            Toast.makeText(this, "X??c nh???n m???t kh???u kh??ng ch??nh x??c", Toast.LENGTH_SHORT).show();
        }else{
            progressBar_register.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar_register.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                    dialog.setTitle("Th??ng b??o");
                                    dialog.setMessage("????ng k?? t??i kho???n th??nh c??ng.\nVui l??ng ki???m tra email c???a b???n ????? x??c minh");
                                    dialog.setIcon(R.drawable.notify);
                                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                    edt_email.setText("");
                                    edt_password.setText("");
                                    edt_confirm.setText("");
                                }else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                    dialog.setTitle("Th??ng b??o");
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
                    }else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setTitle("Th??ng b??o");
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
}