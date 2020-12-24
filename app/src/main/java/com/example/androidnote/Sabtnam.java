package com.example.androidnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Sabtnam extends AppCompatActivity {
    TextView login;
    EditText name;
    EditText pass;
    EditText email;
    Button register;
    DatabaseReference mrootref;
    FirebaseAuth mAuth;
    ProgressDialog ProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sabtnam);

        login = findViewById(R.id.login);
        name = findViewById(R.id.editname);
        pass = findViewById(R.id.editpass);
        email = findViewById(R.id.editemail);
        register = findViewById(R.id.btnsubmmit);

        mrootref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        ProgressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sabtnam.this, login.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtname = name.getText().toString();
                String txtemail = email.getText().toString();
                String txtpass = pass.getText().toString();

                if (TextUtils.isEmpty(txtname) || TextUtils.isEmpty(txtemail) || TextUtils.isEmpty(txtpass)) {
                    Toast.makeText(Sabtnam.this, "please fill all field!!!", Toast.LENGTH_LONG).show();
                } else if (txtpass.length() > 8) {
                    Toast.makeText(Sabtnam.this, "oops! please use long pass", Toast.LENGTH_LONG).show();
                } else registering(txtemail, txtname, txtpass);
            }

            private void registering(final String txtemail, final String txtname, final String txtpass) {
                mAuth.createUserWithEmailAndPassword(txtemail, txtpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("name", name);
                        data.put("email", email);
                        data.put("password", pass);
                        mrootref.child("User").child(mAuth.getCurrentUser().getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override

                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ProgressDialog.dismiss();
                                    Toast.makeText(Sabtnam.this, "Register is successfuly!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Sabtnam.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                });
            }

        });




    }
}
