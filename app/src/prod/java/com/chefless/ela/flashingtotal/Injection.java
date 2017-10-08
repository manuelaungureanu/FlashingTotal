package com.chefless.ela.flashingtotal;

import com.chefless.ela.flashingtotal.data.NumbersRepository;
import com.chefless.ela.flashingtotal.data.local.NumbersLocalDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ela on 07/10/2017.
 */

public class Injection {

    public static NumbersRepository provideNumbersRepository() {
        return NumbersRepository.getInstance(NumbersLocalDataSource.getInstance(getInitialData()));
    }

    private static List<Number> getInitialData(){
        List<Number> initialData = new ArrayList<Number>();
        initialData.add(-634324);
        initialData.add(9234543.32);
        initialData.add(13);
        initialData.add(453043);
        initialData.add(1216);
        initialData.add(-777636.3215);

        return initialData;
    }
}
