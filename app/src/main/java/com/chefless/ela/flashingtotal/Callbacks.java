package com.chefless.ela.flashingtotal;

import android.text.Editable;
import android.view.View;

/**
 * Created by ela on 03/10/2017.
 */

public interface Callbacks {
    void numberChanged(Editable editable, int index);

    void onClick(View v);

    void blinkTotal(View et);
}
