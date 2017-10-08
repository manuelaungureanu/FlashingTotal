/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chefless.ela.flashingtotal.data;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation to load numbers from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class NumbersRepository implements NumbersDataSource {

    private static NumbersRepository INSTANCE = null;

    private final NumbersDataSource mNumbersLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    private ArrayList<Number> mCachedNumbers;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private NumbersRepository(@NonNull NumbersDataSource numbersLocalDataSource) {
        mNumbersLocalDataSource = numbersLocalDataSource;
    }

    public static NumbersRepository getInstance(NumbersDataSource numbersLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NumbersRepository(numbersLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getNumbers(@NonNull final LoadNumbersCallback callback) {

        // Respond immediately with cache if available and not dirty
        if (mCachedNumbers != null && !mCacheIsDirty) {
            callback.onNumbersLoaded(mCachedNumbers);
            return;
        }

        // Query the local storage if available.
        mNumbersLocalDataSource.getNumbers(new LoadNumbersCallback() {
            @Override
            public void onNumbersLoaded(List<Number> numbers) {
                refreshCache(numbers);
                callback.onNumbersLoaded(mCachedNumbers);
            }

            @Override
            public void onDataNotAvailable() {}
        });
    }



    @Override
    public void deleteAllNumbers() {
        mNumbersLocalDataSource.deleteAllNumbers();

        if (mCachedNumbers == null) {
            mCachedNumbers = new ArrayList<>();
        }
        mCachedNumbers.clear();
    }

    private void refreshCache(List<Number> numbers) {
        if (mCachedNumbers == null) {
            mCachedNumbers = new ArrayList<>();
        }
        mCachedNumbers.clear();
        for (Number number : numbers) {
            mCachedNumbers.add(number);
        }
        mCacheIsDirty = false;
    }

    @Override
    public void saveNumbers(@NonNull List<Number> numbers) {
        mNumbersLocalDataSource.saveNumbers(numbers);
        refreshCache(numbers);
    }
}
