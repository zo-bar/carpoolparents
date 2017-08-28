package com.carpoolparents.auth;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.carpoolparents.R;
import com.carpoolparents.fragments.OnFragmentInteractionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity
        implements OnFragmentInteractionListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            drawPage();
        }else {
            mUserId = mFirebaseUser.getUid();
        }
    }

    private void drawPage() {
        ImageView subscribeImageView = (ImageView) findViewById(R.id.image_subscribe);
        ImageView emailLoginImageView = (ImageView) findViewById(R.id.image_email_login);
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.auth_table_layout);

        subscribeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableLayout.setVisibility(View.GONE);
                Fragment fragment = new SubscribeFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.auth_fragment_contaner, fragment);
                transaction.commit();
            }
        });

        emailLoginImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableLayout.setVisibility(View.GONE);
                Fragment fragment = new EmailLoginFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.auth_fragment_contaner, fragment);
                transaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Bundle args) {
        //TODO
    }
}
