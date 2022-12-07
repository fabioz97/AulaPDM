package pdm.di.ubi.pt.aulapdm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "Tag";
    EditText et_nome, et_email, et_tel, et_pass, et_pass2;
    Button btn_registar;
    TextView btnlogin;
    ProgressBar progessBar;

    FirebaseFirestore fstore;
    String userID;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_nome = findViewById(R.id.ipNome);
        et_email = findViewById(R.id.ipemaillogin);
        et_tel  = findViewById(R.id.iptlv);
        et_pass = findViewById(R.id.ippasswordlogin);
        et_pass2 = findViewById(R.id.ippassword2);
        btn_registar = findViewById(R.id.btnregistar);
        btnlogin = findViewById(R.id.btnregistar);
        progessBar = findViewById(R.id.progressBar);

        fAuth=FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

       /* if(fAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }*/
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        btn_registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = et_email.getText().toString().trim();
                String password = et_pass.getText().toString().trim();
                final String fullName = et_nome.getText().toString().trim();
                final String phone = et_tel.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    et_email.setError("É necessário inserir um Email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    et_pass.setError("É necessário inserir uma Password");
                    return;
                }
                if(password.length() < 6){
                    et_pass.setError("Password demasiado pequena, >= 6");
                    return;
                }
                progessBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            //Enter User Data into the Firebase Realtime Database.
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(email,fullName,phone,password);
                            //Extracting User reference from Database for "Registered Users"
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                            referenceProfile.child(fuser.getUid()).setValue(writeUserDetails);

                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(),"Register com Sucesso",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "On failure: Email Not Sent" + e.getMessage());
                                }
                            });

                            Toast.makeText(getApplicationContext(),"User Criado",Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("user").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"onsucess: User profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onfailure:"+ e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                        }
                        else
                        {
                            Toast.makeText(Register.this, "Error" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progessBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

}