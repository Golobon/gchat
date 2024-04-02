package com.golobon.gchat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.AndroidUtil;
import com.golobon.gchat.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileFragment extends Fragment {
    ImageView ivProfilePic;
    EditText etUserName, etPhone;
    Button btnUpdateProfile;
    ProgressBar progressBar;
    TextView tvBtnLogOut;
    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(getContext(), selectedImageUri, ivProfilePic);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ivProfilePic = view.findViewById(R.id.iv_profile_pic);
        etUserName = view.findViewById(R.id.et_profile_username);
        etPhone = view.findViewById(R.id.et_profile_phone_number);
        btnUpdateProfile = view.findViewById(R.id.btn_profile_update);
        progressBar = view.findViewById(R.id.pb_profile);
        tvBtnLogOut = view.findViewById(R.id.tv_btn_profile_logout);

        getUserData();

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBtnClick();
            }
        });

        tvBtnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance()
                        .deleteToken()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FireBaseUtil.logOut();
                                            Intent intent = new Intent(getContext(), SplashActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });
            }
        });

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfileFragment.this)
                        .cropSquare()
                        .compress(512)
                        .maxResultSize(512, 512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });

        return view;
    }

    void updateBtnClick() {
        String newUserName = etUserName.getText().toString();
        if (newUserName.isEmpty() || newUserName.length() < 3) {
            etUserName.setError("Имя слишком короткое!");
            return;
        }
        currentUserModel.setUsername(newUserName);
        setInProgress(true);

        if (selectedImageUri != null) {
            FireBaseUtil.getCurrentProfilePicStorageReference()
                    .putFile(selectedImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            updateToFireStore();
                        }
                    });
        } else {
            updateToFireStore();
        }
    }

    void updateToFireStore() {
        FireBaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setInProgress(false);
                        if (task.isSuccessful()) {
                            AndroidUtil.showToast(getContext(), "Обновление успешно");
                        } else {
                            AndroidUtil.showToast(getContext(), "Обновление не прошло");
                        }
                    }
                });
    }

    void getUserData() {
        setInProgress(true);

        FireBaseUtil.getCurrentProfilePicStorageReference()
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri uriProfilePic = task.getResult();
                            AndroidUtil.setProfilePic(getContext(),
                                    uriProfilePic, ivProfilePic);
                        }
                    }
                });

        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                currentUserModel = task.getResult().toObject(UserModel.class);
                etUserName.setText(currentUserModel.getUsername());
                etPhone.setText(currentUserModel.getPhone());
            }
        });
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btnUpdateProfile.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnUpdateProfile.setVisibility(View.VISIBLE);
        }
    }
}