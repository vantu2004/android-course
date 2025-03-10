package com.vantu.bio;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.vantu.bio.data.Bio;
import com.vantu.bio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String hobbies = "";
    private Bio bio = new Bio();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // old
        // setContentView(R.layout.activity_main);

        // new
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        old
//        editText_hobbies = findViewById(R.id.editText_hobbies);
//        textView_hobbies = findViewById(R.id.textView_hobbies);

//        force show keyboard
//        editText_hobbies.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(editText_hobbies, InputMethodManager.SHOW_IMPLICIT);

        bio.setName(getString(R.string.full_name));
        binding.setBio(bio);
    }

    public void addHobbies(View view) {
        hobbies += String.format("- %s\n", binding.editTextHobbies.getText().toString().trim());
        //binding.textViewHobbies.setText(hobbies);
        bio.setHobbies(hobbies);
        binding.setBio(bio);

        binding.textViewHobbies.setVisibility(View.VISIBLE);

        binding.editTextHobbies.setText("");

//        hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}