package com.chefless.ela.flashingtotal.data;

import android.support.annotation.NonNull;

import com.chefless.ela.flashingtotal.data.local.NumbersLocalDataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ela on 07/10/2017.
 */

public class FakeNumbersLocalDataSource implements NumbersDataSource{
    private static final FakeNumbersLocalDataSource ourInstance = new FakeNumbersLocalDataSource();

    public static FakeNumbersLocalDataSource getInstance() {
        return ourInstance;
    }

    private static List<Number> mNumbersList;

    private FakeNumbersLocalDataSource() {
        mNumbersList = new ArrayList<>();

        mNumbersList.add(-634324);
        mNumbersList.add(9234543.32);
        mNumbersList.add(13);
        mNumbersList.add(453043);
        mNumbersList.add(1216);
        mNumbersList.add(-777636.3215);
    }

    @Override
    public void getNumbers(@NonNull LoadNumbersCallback callback) {
        if (mNumbersList.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onNumbersLoaded(mNumbersList);
        }

    }

    @Override
    public void deleteAllNumbers() {
        mNumbersList.clear();
    }

    @Override
    public void saveNumbers(@NonNull List<Number> numbers) {
        mNumbersList = numbers;
    }
}
