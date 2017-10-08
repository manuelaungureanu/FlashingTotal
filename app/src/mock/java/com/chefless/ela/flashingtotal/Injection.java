package com.chefless.ela.flashingtotal;

import com.chefless.ela.flashingtotal.data.FakeNumbersLocalDataSource;
import com.chefless.ela.flashingtotal.data.NumbersRepository;

/**
 * Created by ela on 07/10/2017.
 */

public class Injection {

    public static NumbersRepository provideNumbersRepository() {
        return NumbersRepository.getInstance(FakeNumbersLocalDataSource.getInstance());
    }
}
