//package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.signup;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.arch.core.executor.TaskExecutor;
//import androidx.fragment.app.Fragment;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.TaskExecutors;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.FirebaseException;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthOptions;
//import com.google.firebase.auth.PhoneAuthProvider;
//
//import java.util.concurrent.TimeUnit;
//
//import lk.ac.mrt.cse.cs4472.social_distance_reminder.LauncherActivity;
//import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
//import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.HomeActivity;
//import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.NavigationHost;
//import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.SignupActivity;
//
//public class VerifyPhoneFragment extends Fragment {
//
//    private String verificationId;
//    private FirebaseAuth mAuth;
//    private TextInputEditText codeEditText;
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mAuth = FirebaseAuth.getInstance();
//
//        String phone_number = getArguments().getString("phoneNumber");
//        sendVerificationCode(phone_number);
//
//        View view = inflater.inflate(R.layout.sdc_verify_phone_fragment, container, false);
//        Log.d("phone", phone_number);
//        codeEditText = view.findViewById(R.id.edittext_phone_verify);
//
//        // only when auto detection doesn't work
//        view.findViewById(R.id.phone_verify_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String code = codeEditText.getText().toString().trim();
//                verifyCode(code);
//            }
//        });
//
//        return view;
//    }
//
//    private void verifyCode(String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//        signInWithCredential(credential);
//    }
//
//    private void signInWithCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()) {
//                    Intent nextActivity = new Intent((Context) getActivity(), HomeActivity.class);
//                    // will clear everything from the stack and start a new activity by closing every other activity
//                    nextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(nextActivity);
//                } else {
//                    Toast.makeText((Context) getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    private void sendVerificationCode(String phoneNumber) {
//        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
//                .setPhoneNumber(phoneNumber)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(getActivity())
//                .setCallbacks(mCallBack)
//                .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        // called when the code is sent
//        // s has the verification id that is sent by the sms
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            verificationId = s;
//
//        }
//
//        // in here can get the code that is sent by the sms automatically
//        // if this succeeds user do not need to enter the code manually
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            String code = phoneAuthCredential.getSmsCode();
//            if(code != null) {
//                verifyCode(code);
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText((Context) getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    };
//}
