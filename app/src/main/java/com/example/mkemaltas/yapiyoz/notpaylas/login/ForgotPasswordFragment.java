package com.example.mkemaltas.yapiyoz.notpaylas.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mkemaltas.yapiyoz.R;
import com.example.mkemaltas.yapiyoz.firebase.FirebaseAuthentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by mkemaltas on 25.08.2016.
 */

public class ForgotPasswordFragment extends Fragment {


    private EditText emailEditText;

    private Button resetPasswordButton;

    private ProgressBar progressBar;

    private TextView backTextView;

    private static ForgotPasswordFragment instance;

    public static ForgotPasswordFragment getInstance() {
        if (instance == null) {
            instance = new ForgotPasswordFragment();
            return instance;
        } else
            return instance;
    }

    public ForgotPasswordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        emailEditText = (EditText) view.findViewById(R.id.forgot_password_fragment_email_et);

        resetPasswordButton = (Button) view.findViewById(R.id.forgot_password_fragment_reset_button);

        progressBar = (ProgressBar) view.findViewById(R.id.forgot_password_fragment_progressBar);

        backTextView = (TextView) view.findViewById(R.id.forgot_password_fragment_back_tv);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String email = emailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Snackbar snackbar = Snackbar.make(v, "Lütfen e-mail adresinizi girin.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                FirebaseAuthentication.getInstance().getAuth().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Snackbar snackbar = Snackbar.make(v, "Bir hata oluştu. Lütfen e-mail adresinizi kontrol edip tekrar deneyin.", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                } else {
                                    Snackbar snackbar = Snackbar.make(v, "Şifre sıfırlama işlemleri mailinize gönderilmiştir.", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });

            }
        });

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = LoginFragment.getInstance();
                //loginFragment.setArguments(getActivity().getIntent().getExtras());

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);


                transaction.replace(R.id.home_fragment_container, loginFragment);
                transaction.commit();
            }
        });
    }

    public void goBackToPreviousFragment() {
        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            getChildFragmentManager().popBackStack();
        }
    }
}
