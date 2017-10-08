package com.chefless.ela.flashingtotal;

import android.content.Context;
import android.content.res.Resources;

import com.chefless.ela.flashingtotal.data.NumbersDataSource;
import com.chefless.ela.flashingtotal.data.NumbersRepository;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TotalViewModelTest {

    private static Number[] NUMBERS;

    @Mock
    private NumbersRepository mNumbersRepository;

    @Mock
    private Context mContext;

    @Mock
    private MainActivity mNumbersNavigator;

    @Captor
    private ArgumentCaptor<NumbersDataSource.LoadNumbersCallback> mLoadNumbersCallbackCaptor;

    private TotalViewModel mTotalViewModel;


    @Before
    public void setupTasksViewModel() {
        MockitoAnnotations.initMocks(this);
        setupContext();

        // Get a reference to the class under test
        mTotalViewModel = new TotalViewModel(mContext, mNumbersRepository);

        // We initialise the numbers
        NUMBERS = new Number[]{1, -20, 333, -4000, 5.53535, 600600};
        mTotalViewModel.numbers.set(NUMBERS);
    }
    private void setupContext() {
        when(mContext.getApplicationContext()).thenReturn(mContext);
        when(mContext.getResources()).thenReturn(mock(Resources.class));
    }

    @Test
    public void loadAllNumbersFromRepository_dataLoaded() {
        // Given an initialized TotalViewModel with initialized numbers
        mTotalViewModel.loadNumbers();

        // Callback is captured and invoked with stubbed tasks
        verify(mNumbersRepository).getNumbers(mLoadNumbersCallbackCaptor.capture());
        mLoadNumbersCallbackCaptor.getValue().onNumbersLoaded(Arrays.asList(NUMBERS));

        // And data loaded
        assertFalse(mTotalViewModel.numbers.get()==null || mTotalViewModel.numbers.get().length==0);
        assertTrue(mTotalViewModel.numbers.get().length == 6);
    }

    @Test
    public void total_isCorrect() throws Exception {
        Number total = mTotalViewModel.getTotal();
        assertEquals(total, 596919.53535);
    }

    @Test
    public void onUpdateNumbers_isCorect(){

        int position = 2;
        String numberAsString = "94532.37";

        mTotalViewModel.onUpdateNumbers(numberAsString, position);
        Number total = mTotalViewModel.getTotal();
        assertEquals(total, 691118.90535);
        assertEquals(mTotalViewModel.numbers.get()[position].toString(), numberAsString);
    }
}