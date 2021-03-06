package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.service.FirebaseCloudMessagingService;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.service.HTTPService;

public class VerifyPhoneActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String mVerificationId;
    TextInputEditText codeEditText;
    String phoneNumber;
    Integer id;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    SQLiteRepository sqLiteRepository;
    HTTPService httpService;

    FirebaseFirestore database;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();
        codeEditText = findViewById(R.id.phone_verify_text_input);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        id = getIntent().getIntExtra("id", -1);

        sqLiteRepository = DBHelper.getInstance(this);
        httpService = HTTPService.getInstance(this);

        database = FirebaseFirestore.getInstance();

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            // This callback will be invoked when the incoming verification SMS can be
            //      automatically detected and perform verification without user action
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if(code != null) {
                    verifyCode(code);
                }
            }

            // This callback will be invoked when an invalid request for verification is made
            //      (ex. phone number format not correct)
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            // This callback will be invoked when the SMS verification code has been
            //      sent to the provided phone number
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mVerificationId = verificationId;
            }
        };

        startPhoneNumberVerification(phoneNumber);

        // This is needed only when auto detection doesn't work
        findViewById(R.id.phone_verify_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = codeEditText.getText().toString().trim();
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    User user = new User(id);
                    user.setMobileNumber(phoneNumber);
                    user.setVerifiedUser(true);
                    sqLiteRepository.updateUserDetails(user);

                    // add a record in users table
                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String currentUserUid = currentUser.getUid();
                    String FCMToken = FirebaseCloudMessagingService.getToken(VerifyPhoneActivity.this);
                    Map<String, String> deviceDetails = new HashMap<>();
                    deviceDetails.put("fcmToken", FCMToken);
                    deviceDetails.put("userUUID", currentUserUid);

                    httpService.postFCMToken("generate-deviceId", deviceDetails,
                            id.toString(), currentUserUid);




//                    database.collection("Users").document(currentUserUid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                sqLiteRepository.updateUserDetails(id, currentUserUid, phoneNumber,
//                                        true, true, FCMToken);
//                                Toast.makeText(VerifyPhoneActivity.this, "User Added Successfully", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(VerifyPhoneActivity.this, "User not Added Successfully", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });

                    Intent nextActivity = new Intent(VerifyPhoneActivity.this, HomeActivity.class);
                    // will clear everything from the stack and start a new activity by closing every other activity
                    nextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(nextActivity);
                } else {
                    Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(VerifyPhoneActivity.this)
                .setCallbacks(mCallBacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}