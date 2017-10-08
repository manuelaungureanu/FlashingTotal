package com.chefless.ela.flashingtotal;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chefless.ela.flashingtotal.data.NumbersDataSource;
import com.chefless.ela.flashingtotal.data.NumbersRepository;
import java.util.List;

/**
 * Created by ela on 03/10/2017.
 */

public class TotalViewModel extends BaseObservable implements Callbacks {

    private final Context mContext;

    private final NumbersRepository mNumbersRepository;

    public final ObservableField<Number> total = new ObservableField<>();

    public final ObservableField<Number[]> numbers = new ObservableField<>();

    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    public TotalViewModel(Context context, NumbersRepository numbersRepository) {
        mContext = context.getApplicationContext(); // Force use of Application Context.
        mNumbersRepository = numbersRepository;
    }

    public void start() {
        loadNumbers();
    }

    protected void loadNumbers() {

        mNumbersRepository.getNumbers(new NumbersDataSource.LoadNumbersCallback() {
            @Override
            public void onNumbersLoaded(List<Number> nums) {
                mIsDataLoadingError.set(false);
                numbers.set(nums.toArray(new Number[nums.size()]));
                total.set(getTotal());
                notifyChange();
            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.set(true);
            }
        });

    }

    public void onUpdateNumbers(String val, int index){

        if(val==null || val.length()==0)
            return;

        double value;
        try {
            value = Double.parseDouble(val);
            Number[] nums = numbers.get();
            nums[index] = value;
            numbers.set(nums);
            //update total
            total.set(getTotal());
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }

    protected Number getTotal(){
        Number[] numbersArray = numbers.get();
        if(numbersArray==null || numbersArray.length==0)
            return 0;

        double total = 0;
        for (Number num:numbersArray) {
            total += num.doubleValue();
        }
        return total;
    }


    @Override
    public void numberChanged(Editable editable, int index) {
        onUpdateNumbers(editable.toString(), index);
    }

    @Override
    public void onClick(View v) {
        EditText et = (EditText)v;
        et.setCursorVisible(true);
        v.setBackground(null);

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            imm.showSoftInput(et,0);
        }
    }

    private Boolean isFlashing = false;
    private ObjectAnimator anim;

    @Override
    public void blinkTotal(View view){
        isFlashing = !isFlashing;

        if(anim==null) {
            initFlashing(view);
        }

        if(!isFlashing && anim.isRunning()) {
            anim.cancel();
        }
        else {
            anim.start();
        }
    }

    private void initFlashing(View view){
        if(anim!=null)
            return;

        TextView tv = (TextView) view;
        anim = ObjectAnimator.ofInt(tv, "textColor", R.color.colorPrimary, Color.WHITE, R.color.colorPrimary);
        anim.setDuration(500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }
}
