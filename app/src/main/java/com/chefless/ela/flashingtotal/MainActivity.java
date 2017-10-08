package com.chefless.ela.flashingtotal;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chefless.ela.flashingtotal.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        TotalViewModel viewModel = new TotalViewModel(getApplicationContext(), Injection.provideNumbersRepository());
        viewModel.start();
        binding.setViewmodel(viewModel);
    }
}
