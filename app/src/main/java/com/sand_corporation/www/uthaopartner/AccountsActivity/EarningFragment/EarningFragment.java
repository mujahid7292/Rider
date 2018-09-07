package com.sand_corporation.www.uthaopartner.AccountsActivity.EarningFragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sand_corporation.www.uthaopartner.AccountsActivity.AccountActivityViewModel;
import com.sand_corporation.www.uthaopartner.FareCalculator.FareCalculator;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarningFragment extends Fragment {

    private String TAG = "EarningFragment";
    private AccountActivityViewModel mViewModel;
    private List<BarEntry> barEntriesForTotalTripCost, barEntriesForTotalCashCollected,
            barEntriesForTotalUthaoCommission, barEntriesForTotalDiscountApplied;
    private List<PieEntry> pieEntries;
    private BarChart barChartForTotalTripCost, barChartForTotalCashCollected,
            barChartForTotalUthaoCommission, barChartForTotalDiscountApplied;
    private PieChart pieChart;


    public EarningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_earning, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Get a reference to the ViewModel for this screen.
        mViewModel = ViewModelProviders.of(this)
                .get(AccountActivityViewModel.class);

        barChartForTotalTripCost = getActivity().findViewById(R.id.barChartForTotalTripCost);
        barChartForTotalTripCost.setDrawBarShadow(false);
        barChartForTotalTripCost.setDrawValueAboveBar(true);
        barChartForTotalTripCost.setHighlightFullBarEnabled(true);
        barChartForTotalTripCost.setHighlightPerTapEnabled(true);
        barChartForTotalTripCost.setMaxVisibleValueCount(8);
        barChartForTotalTripCost.setPinchZoom(false);
        barChartForTotalTripCost.setDrawGridBackground(false);
        barChartForTotalTripCost.setScaleEnabled(false);
        barChartForTotalTripCost.setDoubleTapToZoomEnabled(false);
        barChartForTotalTripCost.animateXY(3000, 3000);
        //If animate(...) (of any kind) is called, no further calling of invalidate()
        // is necessary to refresh the chart.



        barChartForTotalCashCollected = getActivity().findViewById(R.id.barChartForCashCollected);
        barChartForTotalCashCollected.setDrawBarShadow(false);
        barChartForTotalCashCollected.setDrawValueAboveBar(true);
        barChartForTotalCashCollected.setHighlightFullBarEnabled(true);
        barChartForTotalCashCollected.setHighlightPerTapEnabled(true);
        barChartForTotalCashCollected.setMaxVisibleValueCount(8);
        barChartForTotalCashCollected.setPinchZoom(false);
        barChartForTotalCashCollected.setDrawGridBackground(false);
        barChartForTotalCashCollected.setScaleEnabled(false);
        barChartForTotalCashCollected.setDoubleTapToZoomEnabled(false);
        barChartForTotalCashCollected.animateXY(3000, 3000);

        barChartForTotalDiscountApplied = getActivity().findViewById(R.id.barChartForDiscountApplied);
        barChartForTotalDiscountApplied.setDrawBarShadow(false);
        barChartForTotalDiscountApplied.setDrawValueAboveBar(true);
        barChartForTotalDiscountApplied.setHighlightFullBarEnabled(true);
        barChartForTotalDiscountApplied.setHighlightPerTapEnabled(true);
        barChartForTotalDiscountApplied.setMaxVisibleValueCount(8);
        barChartForTotalDiscountApplied.setPinchZoom(false);
        barChartForTotalDiscountApplied.setDrawGridBackground(false);
        barChartForTotalDiscountApplied.setScaleEnabled(false);
        barChartForTotalDiscountApplied.setDoubleTapToZoomEnabled(false);
        barChartForTotalDiscountApplied.animateXY(3000, 3000);


        barChartForTotalUthaoCommission = getActivity().findViewById(R.id.barChartForUthaoCommission);
        barChartForTotalUthaoCommission.setDrawBarShadow(false);
        barChartForTotalUthaoCommission.setDrawValueAboveBar(true);
        barChartForTotalUthaoCommission.setHighlightFullBarEnabled(true);
        barChartForTotalUthaoCommission.setHighlightPerTapEnabled(true);
        barChartForTotalUthaoCommission.setMaxVisibleValueCount(8);
        barChartForTotalUthaoCommission.setPinchZoom(false);
        barChartForTotalUthaoCommission.setDrawGridBackground(false);
        barChartForTotalUthaoCommission.setScaleEnabled(false);
        barChartForTotalUthaoCommission.setDoubleTapToZoomEnabled(false);
        barChartForTotalUthaoCommission.animateXY(3000, 3000);

        Description description = new Description();
        description.setText("");
        barChartForTotalTripCost.setDescription(description);
        barChartForTotalCashCollected.setDescription(description);
        barChartForTotalUthaoCommission.setDescription(description);
        barChartForTotalDiscountApplied.setDescription(description);


        barChartForTotalTripCost.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i(TAG,"Entry: " + e.toString() + " | Highlight: " + h.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart = getActivity().findViewById(R.id.pieChart);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.animateY(3000, Easing.EasingOption.EaseInOutCubic);

        barEntriesForTotalTripCost = new ArrayList<>();
        barEntriesForTotalCashCollected = new ArrayList<>();
        barEntriesForTotalDiscountApplied = new ArrayList<>();
        barEntriesForTotalUthaoCommission = new ArrayList<>();
        pieEntries = new ArrayList<>();


        putObserverInRideHistoryFromRoom();
    }

    private void putObserverInRideHistoryFromRoom() {
        mViewModel.mMoneyReceiptList.observe(this, new Observer<List<MoneyReceipt>>() {
            @Override
            public void onChanged(@Nullable List<MoneyReceipt> moneyReceipts) {
                if (moneyReceipts != null){
                    Log.i(TAG,"MoneyReceipt List Size: " + moneyReceipts.size());
                    barEntriesForTotalTripCost.clear();
                    barEntriesForTotalCashCollected.clear();
                    barEntriesForTotalDiscountApplied.clear();
                    barEntriesForTotalUthaoCommission.clear();
                    pieEntries.clear();
                    //dayOfTheWeek    6     7     1     2     3     4     5
                    //AfterArrange    0     1     2     3     4     5     6
                    String[] day = {"Sat","Sun","Mon","Tue","Wed","Thu","Fri","N/A"};
                    double[] totalFareThatDay = {0.00,0.00,0.00,0.00,0.00,0.00,0.00};
                    double[] totalCashCollectedThatDay = {0.00,0.00,0.00,0.00,0.00,0.00,0.00};
                    double[] totalDiscountAppliedThatDay = {0.00,0.00,0.00,0.00,0.00,0.00,0.00};
                    double[] totalUthaoCommissionAfterDiscountThatDay = {0.00,0.00,0.00,0.00,0.00,0.00,0.00};


                    XAxis barChartForTotalTripCostXAxis = barChartForTotalTripCost.getXAxis();
                    barChartForTotalTripCostXAxis.setValueFormatter(new MyXAxisValueFormatter(day));
                    barChartForTotalTripCostXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChartForTotalTripCostXAxis.setTextColor(Color.WHITE);
                    barChartForTotalTripCostXAxis.setTextSize(16);
                    barChartForTotalTripCostXAxis.setDrawGridLines(false);

                    XAxis barChartForTotalCashCollectedXAxis = barChartForTotalCashCollected.getXAxis();
                    barChartForTotalCashCollectedXAxis.setValueFormatter(new MyXAxisValueFormatter(day));
                    barChartForTotalCashCollectedXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChartForTotalCashCollectedXAxis.setTextColor(Color.WHITE);
                    barChartForTotalCashCollectedXAxis.setTextSize(16);
                    barChartForTotalCashCollectedXAxis.setDrawGridLines(false);

                    XAxis barChartForTotalDiscountAppliedXAxis = barChartForTotalDiscountApplied.getXAxis();
                    barChartForTotalDiscountAppliedXAxis.setValueFormatter(new MyXAxisValueFormatter(day));
                    barChartForTotalDiscountAppliedXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChartForTotalDiscountAppliedXAxis.setTextColor(Color.WHITE);
                    barChartForTotalDiscountAppliedXAxis.setTextSize(16);
                    barChartForTotalDiscountAppliedXAxis.setDrawGridLines(false);

                    XAxis barChartForTotalUthaoCommissionXAxis = barChartForTotalUthaoCommission.getXAxis();
                    barChartForTotalUthaoCommissionXAxis.setValueFormatter(new MyXAxisValueFormatter(day));
                    barChartForTotalUthaoCommissionXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChartForTotalUthaoCommissionXAxis.setTextColor(Color.WHITE);
                    barChartForTotalUthaoCommissionXAxis.setTextSize(16);
                    barChartForTotalUthaoCommissionXAxis.setDrawGridLines(false);

                    YAxis barChartForTotalTripCostYAxisLeft = barChartForTotalTripCost.getAxisLeft();
                    barChartForTotalTripCostYAxisLeft.setDrawAxisLine(true);
                    barChartForTotalTripCostYAxisLeft.setDrawGridLines(false);
                    barChartForTotalTripCostYAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    barChartForTotalTripCostYAxisLeft.setTextColor(Color.WHITE);
                    barChartForTotalTripCostYAxisLeft.setTextSize(16);

                    YAxis barChartForTotalCashCollectedYAxisLeft = barChartForTotalCashCollected.getAxisLeft();
                    barChartForTotalCashCollectedYAxisLeft.setDrawAxisLine(true);
                    barChartForTotalCashCollectedYAxisLeft.setDrawGridLines(false);
                    barChartForTotalCashCollectedYAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    barChartForTotalCashCollectedYAxisLeft.setTextColor(Color.WHITE);
                    barChartForTotalCashCollectedYAxisLeft.setTextSize(16);

                    YAxis barChartForTotalDiscountAppliedYAxisLeft = barChartForTotalDiscountApplied.getAxisLeft();
                    barChartForTotalDiscountAppliedYAxisLeft.setDrawAxisLine(true);
                    barChartForTotalDiscountAppliedYAxisLeft.setDrawGridLines(false);
                    barChartForTotalDiscountAppliedYAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    barChartForTotalDiscountAppliedYAxisLeft.setTextColor(Color.WHITE);
                    barChartForTotalDiscountAppliedYAxisLeft.setTextSize(16);

                    YAxis barChartForTotalUthaoCommissionYAxisLeft = barChartForTotalUthaoCommission.getAxisLeft();
                    barChartForTotalUthaoCommissionYAxisLeft.setDrawAxisLine(true);
                    barChartForTotalUthaoCommissionYAxisLeft.setDrawGridLines(false);
                    barChartForTotalUthaoCommissionYAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    barChartForTotalUthaoCommissionYAxisLeft.setTextColor(Color.WHITE);
                    barChartForTotalUthaoCommissionYAxisLeft.setTextSize(16);

                    YAxis barChartForTotalTripCostYAxisRight = barChartForTotalTripCost.getAxisRight();
                    barChartForTotalTripCostYAxisRight.setDrawAxisLine(false);
                    barChartForTotalTripCostYAxisRight.setDrawGridLines(false);
                    barChartForTotalTripCostYAxisRight.setDrawLabels(false);

                    YAxis barChartForTotalCashCollectedYAxisRight = barChartForTotalCashCollected.getAxisRight();
                    barChartForTotalCashCollectedYAxisRight.setDrawAxisLine(false);
                    barChartForTotalCashCollectedYAxisRight.setDrawGridLines(false);
                    barChartForTotalCashCollectedYAxisRight.setDrawLabels(false);

                    YAxis barChartForTotalDiscountAppliedYAxisRight = barChartForTotalDiscountApplied.getAxisRight();
                    barChartForTotalDiscountAppliedYAxisRight.setDrawAxisLine(false);
                    barChartForTotalDiscountAppliedYAxisRight.setDrawGridLines(false);
                    barChartForTotalDiscountAppliedYAxisRight.setDrawLabels(false);

                    YAxis barChartForTotalUthaoCommissionYAxisRight = barChartForTotalUthaoCommission.getAxisRight();
                    barChartForTotalUthaoCommissionYAxisRight.setDrawAxisLine(false);
                    barChartForTotalUthaoCommissionYAxisRight.setDrawGridLines(false);
                    barChartForTotalUthaoCommissionYAxisRight.setDrawLabels(false);

                    double latestEarningFigure1 = 0.00;
                    double latestEarningFigure2 = 0.00;
                    double latestEarningFigure3 = 0.00;
                    double latestEarningFigure4 = 0.00;
                    double latestEarningFigure5 = 0.00;
                    double latestEarningFigure6 = 0.00;
                    double latestEarningFigure7 = 0.00;

                    double latestCashCollected1 = 0.00;
                    double latestCashCollected2 = 0.00;
                    double latestCashCollected3 = 0.00;
                    double latestCashCollected4 = 0.00;
                    double latestCashCollected5 = 0.00;
                    double latestCashCollected6 = 0.00;
                    double latestCashCollected7 = 0.00;

                    double latestDiscountApplied1 = 0.00;
                    double latestDiscountApplied2 = 0.00;
                    double latestDiscountApplied3 = 0.00;
                    double latestDiscountApplied4 = 0.00;
                    double latestDiscountApplied5 = 0.00;
                    double latestDiscountApplied6 = 0.00;
                    double latestDiscountApplied7 = 0.00;

                    double latestUthaoCommissionAfterDiscount1 = 0.00;
                    double latestUthaoCommissionAfterDiscount2 = 0.00;
                    double latestUthaoCommissionAfterDiscount3 = 0.00;
                    double latestUthaoCommissionAfterDiscount4 = 0.00;
                    double latestUthaoCommissionAfterDiscount5 = 0.00;
                    double latestUthaoCommissionAfterDiscount6 = 0.00;
                    double latestUthaoCommissionAfterDiscount7 = 0.00;

                    FareCalculator calculator = new FareCalculator();


                    int i = 0;
                    for (MoneyReceipt receipt: moneyReceipts){
                        Date date = new Date(receipt.getRideStartTime());
                        int dayOfTheWeek = new DateTime(date).getDayOfWeek();

                        switch (dayOfTheWeek){
                            case 1:
                                double previousEarningOnThatDay1 = totalFareThatDay[0];
                                double totalFare1 = receipt.getTotalPayment();
                                double discountInPercentage1 = receipt.getDiscountInPercentage();
                                double discountAmountUptoTaka1 = receipt.getDiscountAmountUptoTaka();
                                double demandChargeInPercentage1 = receipt.getDemandChargeInPercentage();

                                latestEarningFigure1 = previousEarningOnThatDay1 + totalFare1;
                                totalFareThatDay[0] = latestEarningFigure1;
                                double cashCollected1 = calculator.getCashCollectedInThisTrip(
                                        totalFare1, discountInPercentage1, discountAmountUptoTaka1,
                                                demandChargeInPercentage1
                                );
                                double appliedDiscountInTaka1 = calculator.getAppliedDiscountInTaka(
                                        totalFare1, discountInPercentage1, discountAmountUptoTaka1,
                                        demandChargeInPercentage1
                                );
                                double uthaCommissionAfterDiscount1 = calculator.getUthaoCommissionAfterDiscount(
                                        totalFare1, discountInPercentage1, discountAmountUptoTaka1,
                                        demandChargeInPercentage1
                                );
                                latestCashCollected1 = latestCashCollected1 + cashCollected1;
                                latestDiscountApplied1 = latestDiscountApplied1 + appliedDiscountInTaka1;
                                latestUthaoCommissionAfterDiscount1 = latestUthaoCommissionAfterDiscount1 +
                                        uthaCommissionAfterDiscount1;

                                totalCashCollectedThatDay[0] = latestCashCollected1;
                                totalDiscountAppliedThatDay[0] = latestDiscountApplied1;
                                totalUthaoCommissionAfterDiscountThatDay[0] = latestUthaoCommissionAfterDiscount1;

                                Log.i(TAG,"Date: " + date.toString() + "\n" +
                                        "dayOfTheWeek: " + dayOfTheWeek + " | " + "totalFare1: " +
                                        totalFare1
                                        +" | latestEarningFigure1: " + latestEarningFigure1);
                                Log.i(TAG,"latestCashCollected1: " + latestCashCollected1 +
                                        " | latestUthaoCommissionAfterDiscount1: "+ latestUthaoCommissionAfterDiscount1);
                                break;

                            case 2:
                                double previousEarningOnThatDay2 = totalFareThatDay[1];
                                double totalFare2 = receipt.getTotalPayment();
                                double discountInPercentage2 = receipt.getDiscountInPercentage();
                                double discountAmountUptoTaka2 = receipt.getDiscountAmountUptoTaka();
                                double demandChargeInPercentage2 = receipt.getDemandChargeInPercentage();

                                latestEarningFigure2 = previousEarningOnThatDay2 + totalFare2;
                                totalFareThatDay[1] = latestEarningFigure2;

                                double cashCollected2 = calculator.getCashCollectedInThisTrip(
                                        totalFare2, discountInPercentage2, discountAmountUptoTaka2,
                                        demandChargeInPercentage2
                                );
                                double appliedDiscountInTaka2 = calculator.getAppliedDiscountInTaka(
                                        totalFare2, discountInPercentage2, discountAmountUptoTaka2,
                                        demandChargeInPercentage2
                                );
                                double uthaCommissionAfterDiscount2 = calculator.getUthaoCommissionAfterDiscount(
                                        totalFare2, discountInPercentage2, discountAmountUptoTaka2,
                                        demandChargeInPercentage2
                                );
                                latestCashCollected2 = latestCashCollected2 + cashCollected2;
                                latestDiscountApplied2 = latestDiscountApplied2 + appliedDiscountInTaka2;
                                latestUthaoCommissionAfterDiscount2 = latestUthaoCommissionAfterDiscount2 +
                                        uthaCommissionAfterDiscount2;

                                totalCashCollectedThatDay[1] = latestCashCollected2;
                                totalDiscountAppliedThatDay[1] = latestDiscountApplied2;
                                totalUthaoCommissionAfterDiscountThatDay[1] = latestUthaoCommissionAfterDiscount2;


                                Log.i(TAG,"Date: " + date.toString() + "\n" +
                                        "dayOfTheWeek: " + dayOfTheWeek + " | " + "totalFare2: " +
                                        totalFare2
                                        +" | latestEarningFigure2: " + latestEarningFigure2);
                                Log.i(TAG,"latestCashCollected2: " + latestCashCollected2 +
                                        " | latestUthaoCommissionAfterDiscount2: "+ latestUthaoCommissionAfterDiscount2);

                                break;

                            case 3:
                                double previousEarningOnThatDay3 = totalFareThatDay[2];
                                double totalFare3 = receipt.getTotalPayment();
                                double discountInPercentage3 = receipt.getDiscountInPercentage();
                                double discountAmountUptoTaka3 = receipt.getDiscountAmountUptoTaka();
                                double demandChargeInPercentage3 = receipt.getDemandChargeInPercentage();

                                latestEarningFigure3 = previousEarningOnThatDay3 + totalFare3;
                                totalFareThatDay[2] = latestEarningFigure3;

                                double cashCollected3 = calculator.getCashCollectedInThisTrip(
                                        totalFare3, discountInPercentage3, discountAmountUptoTaka3,
                                        demandChargeInPercentage3
                                );
                                double appliedDiscountInTaka3 = calculator.getAppliedDiscountInTaka(
                                        totalFare3, discountInPercentage3, discountAmountUptoTaka3,
                                        demandChargeInPercentage3
                                );
                                double uthaCommissionAfterDiscount3 = calculator.getUthaoCommissionAfterDiscount(
                                        totalFare3, discountInPercentage3, discountAmountUptoTaka3,
                                        demandChargeInPercentage3
                                );
                                latestCashCollected3 = latestCashCollected3 + cashCollected3;
                                latestDiscountApplied3 = latestDiscountApplied3 + appliedDiscountInTaka3;
                                latestUthaoCommissionAfterDiscount3 = latestUthaoCommissionAfterDiscount3 +
                                        uthaCommissionAfterDiscount3;

                                totalCashCollectedThatDay[2] = latestCashCollected3;
                                totalDiscountAppliedThatDay[2] = latestDiscountApplied3;
                                totalUthaoCommissionAfterDiscountThatDay[2] = latestUthaoCommissionAfterDiscount3;

                                Log.i(TAG,"Date: " + date.toString() + "\n" +
                                        "dayOfTheWeek: " + dayOfTheWeek + " | " + "totalFare3: " +
                                        totalFare3
                                        +" | latestEarningFigure3: " + latestEarningFigure3);
                                Log.i(TAG,"latestCashCollected3: " + latestCashCollected3 +
                                        " | latestUthaoCommissionAfterDiscount3: "+ latestUthaoCommissionAfterDiscount3);

                                break;

                            case 4:
                                double previousEarningOnThatDay4 = totalFareThatDay[3];
                                double totalFare4 = receipt.getTotalPayment();
                                double discountInPercentage4 = receipt.getDiscountInPercentage();
                                double discountAmountUptoTaka4 = receipt.getDiscountAmountUptoTaka();
                                double demandChargeInPercentage4 = receipt.getDemandChargeInPercentage();

                                latestEarningFigure4 = previousEarningOnThatDay4 + totalFare4;
                                totalFareThatDay[3] = latestEarningFigure4;


                                double cashCollected4 = calculator.getCashCollectedInThisTrip(
                                        totalFare4, discountInPercentage4, discountAmountUptoTaka4,
                                        demandChargeInPercentage4
                                );
                                double appliedDiscountInTaka4 = calculator.getAppliedDiscountInTaka(
                                        totalFare4, discountInPercentage4, discountAmountUptoTaka4,
                                        demandChargeInPercentage4
                                );
                                double uthaCommissionAfterDiscount4 = calculator.getUthaoCommissionAfterDiscount(
                                        totalFare4, discountInPercentage4, discountAmountUptoTaka4,
                                        demandChargeInPercentage4
                                );
                                latestCashCollected4 = latestCashCollected4 + cashCollected4;
                                latestDiscountApplied4 = latestDiscountApplied4 + appliedDiscountInTaka4;
                                latestUthaoCommissionAfterDiscount4 = latestUthaoCommissionAfterDiscount4 +
                                        uthaCommissionAfterDiscount4;

                                totalCashCollectedThatDay[3] = latestCashCollected4;
                                totalDiscountAppliedThatDay[3] = latestDiscountApplied4;
                                totalUthaoCommissionAfterDiscountThatDay[3] = latestUthaoCommissionAfterDiscount4;

                                Log.i(TAG,"Date: " + date.toString() + "\n" +
                                        "dayOfTheWeek: " + dayOfTheWeek + " | " + "totalFare4: " +
                                        totalFare4
                                        +" | latestEarningFigure4: " + latestEarningFigure4);
                                Log.i(TAG,"latestCashCollected4: " + latestCashCollected4 +
                                        " | latestUthaoCommissionAfterDiscount4: "+ latestUthaoCommissionAfterDiscount4);

                                break;

                            case 5:
                                double previousEarningOnThatDay5 = totalFareThatDay[4];
                                double totalFare5 = receipt.getTotalPayment();
                                double discountInPercentage5 = receipt.getDiscountInPercentage();
                                double discountAmountUptoTaka5 = receipt.getDiscountAmountUptoTaka();
                                double demandChargeInPercentage5 = receipt.getDemandChargeInPercentage();

                                latestEarningFigure5 = previousEarningOnThatDay5 + totalFare5;
                                totalFareThatDay[4] = latestEarningFigure5;


                                double cashCollected5 = calculator.getCashCollectedInThisTrip(
                                        totalFare5, discountInPercentage5, discountAmountUptoTaka5,
                                        demandChargeInPercentage5
                                );
                                double appliedDiscountInTaka5 = calculator.getAppliedDiscountInTaka(
                                        totalFare5, discountInPercentage5, discountAmountUptoTaka5,
                                        demandChargeInPercentage5
                                );
                                double uthaCommissionAfterDiscount5 = calculator.getUthaoCommissionAfterDiscount(
                                        totalFare5, discountInPercentage5, discountAmountUptoTaka5,
                                        demandChargeInPercentage5
                                );
                                latestCashCollected5 = latestCashCollected5 + cashCollected5;
                                latestDiscountApplied5 = latestDiscountApplied5 + appliedDiscountInTaka5;
                                latestUthaoCommissionAfterDiscount5 = latestUthaoCommissionAfterDiscount5 +
                                        uthaCommissionAfterDiscount5;

                                totalCashCollectedThatDay[4] = latestCashCollected5;
                                totalDiscountAppliedThatDay[4] = latestDiscountApplied5;
                                totalUthaoCommissionAfterDiscountThatDay[4] = latestUthaoCommissionAfterDiscount5;

                                Log.i(TAG,"Date: " + date.toString() + "\n" +
                                        "dayOfTheWeek: " + dayOfTheWeek + " | " + "totalFare5: " +
                                        totalFare5
                                        +" | latestEarningFigure5: " + latestEarningFigure5);
                                Log.i(TAG,"latestCashCollected5: " + latestCashCollected5 +
                                        " | latestUthaoCommissionAfterDiscount5: "+ latestUthaoCommissionAfterDiscount5);

                                break;

                            case 6:
                                double previousEarningOnThatDay6 = totalFareThatDay[5];
                                double totalFare6 = receipt.getTotalPayment();
                                double discountInPercentage6 = receipt.getDiscountInPercentage();
                                double discountAmountUptoTaka6 = receipt.getDiscountAmountUptoTaka();
                                double demandChargeInPercentage6 = receipt.getDemandChargeInPercentage();

                                latestEarningFigure6 = previousEarningOnThatDay6 + totalFare6;
                                totalFareThatDay[5] = latestEarningFigure6;

                                double cashCollected6 = calculator.getCashCollectedInThisTrip(
                                        totalFare6, discountInPercentage6, discountAmountUptoTaka6,
                                        demandChargeInPercentage6
                                );
                                double appliedDiscountInTaka6 = calculator.getAppliedDiscountInTaka(
                                        totalFare6, discountInPercentage6, discountAmountUptoTaka6,
                                        demandChargeInPercentage6
                                );
                                double uthaCommissionAfterDiscount6 = calculator.getUthaoCommissionAfterDiscount(
                                        totalFare6, discountInPercentage6, discountAmountUptoTaka6,
                                        demandChargeInPercentage6
                                );
                                latestCashCollected6 = latestCashCollected6 + cashCollected6;
                                latestDiscountApplied6 = latestDiscountApplied6 + appliedDiscountInTaka6;
                                latestUthaoCommissionAfterDiscount6 = latestUthaoCommissionAfterDiscount6 +
                                        uthaCommissionAfterDiscount6;

                                totalCashCollectedThatDay[5] = latestCashCollected6;
                                totalDiscountAppliedThatDay[5] = latestDiscountApplied6;
                                totalUthaoCommissionAfterDiscountThatDay[5] = latestUthaoCommissionAfterDiscount6;

                                Log.i(TAG,"Date: " + date.toString() + "\n" +
                                        "dayOfTheWeek: " + dayOfTheWeek + " | " + "totalFare6: " +
                                        totalFare6
                                        +" | latestEarningFigure6: " + latestEarningFigure6);
                                Log.i(TAG,"latestCashCollected6: " + latestCashCollected6 +
                                        " | latestUthaoCommissionAfterDiscount6: "+ latestUthaoCommissionAfterDiscount6);

                                break;

                            case 7:
                                double previousEarningOnThatDay7 = totalFareThatDay[6];
                                double totalFare7 = receipt.getTotalPayment();
                                double discountInPercentage7 = receipt.getDiscountInPercentage();
                                double discountAmountUptoTaka7 = receipt.getDiscountAmountUptoTaka();
                                double demandChargeInPercentage7 = receipt.getDemandChargeInPercentage();

                                latestEarningFigure7 = previousEarningOnThatDay7 + totalFare7;
                                totalFareThatDay[6] = latestEarningFigure7;

                                double cashCollected7 = calculator.getCashCollectedInThisTrip(
                                        totalFare7, discountInPercentage7, discountAmountUptoTaka7,
                                        demandChargeInPercentage7
                                );
                                double appliedDiscountInTaka7 = calculator.getAppliedDiscountInTaka(
                                        totalFare7, discountInPercentage7, discountAmountUptoTaka7,
                                        demandChargeInPercentage7
                                );
                                double uthaCommissionAfterDiscount7 = calculator.getUthaoCommissionAfterDiscount(
                                        totalFare7, discountInPercentage7, discountAmountUptoTaka7,
                                        demandChargeInPercentage7
                                );
                                latestCashCollected7 = latestCashCollected7 + cashCollected7;
                                latestDiscountApplied7 = latestDiscountApplied7 + appliedDiscountInTaka7;
                                latestUthaoCommissionAfterDiscount7 = latestUthaoCommissionAfterDiscount7 +
                                        uthaCommissionAfterDiscount7;

                                totalCashCollectedThatDay[6] = latestCashCollected7;
                                totalDiscountAppliedThatDay[6] = latestDiscountApplied7;
                                totalUthaoCommissionAfterDiscountThatDay[6] = latestUthaoCommissionAfterDiscount7;

                                Log.i(TAG,"Date: " + date.toString() + "\n" +
                                        "dayOfTheWeek: " + dayOfTheWeek + " | " + "totalFare7: "
                                        + totalFare7
                                        +" | latestEarningFigure7: " + latestEarningFigure7);
                                Log.i(TAG,"latestCashCollected7: " + latestCashCollected7 +
                                        " | latestUthaoCommissionAfterDiscount7: "+ latestUthaoCommissionAfterDiscount7);

                                break;
                        }


                        i++;
                    }
                    barEntriesForTotalTripCost.add(new BarEntry(2, (float) latestEarningFigure1));
                    barEntriesForTotalTripCost.add(new BarEntry(3, (float) latestEarningFigure2));
                    barEntriesForTotalTripCost.add(new BarEntry(4, (float) latestEarningFigure3));
                    barEntriesForTotalTripCost.add(new BarEntry(5, (float) latestEarningFigure4));
                    barEntriesForTotalTripCost.add(new BarEntry(6, (float) latestEarningFigure5));
                    barEntriesForTotalTripCost.add(new BarEntry(0, (float) latestEarningFigure6));
                    barEntriesForTotalTripCost.add(new BarEntry(1, (float) latestEarningFigure7));


                    barEntriesForTotalCashCollected.add(new BarEntry(2, (float) latestCashCollected1));
                    barEntriesForTotalCashCollected.add(new BarEntry(3, (float) latestCashCollected2));
                    barEntriesForTotalCashCollected.add(new BarEntry(4, (float) latestCashCollected3));
                    barEntriesForTotalCashCollected.add(new BarEntry(5, (float) latestCashCollected4));
                    barEntriesForTotalCashCollected.add(new BarEntry(6, (float) latestCashCollected5));
                    barEntriesForTotalCashCollected.add(new BarEntry(0, (float) latestCashCollected6));
                    barEntriesForTotalCashCollected.add(new BarEntry(1, (float) latestCashCollected7));

                    barEntriesForTotalDiscountApplied.add(new BarEntry(2, (float) latestDiscountApplied1));
                    barEntriesForTotalDiscountApplied.add(new BarEntry(3, (float) latestDiscountApplied2));
                    barEntriesForTotalDiscountApplied.add(new BarEntry(4, (float) latestDiscountApplied3));
                    barEntriesForTotalDiscountApplied.add(new BarEntry(5, (float) latestDiscountApplied4));
                    barEntriesForTotalDiscountApplied.add(new BarEntry(6, (float) latestDiscountApplied5));
                    barEntriesForTotalDiscountApplied.add(new BarEntry(0, (float) latestDiscountApplied6));
                    barEntriesForTotalDiscountApplied.add(new BarEntry(1, (float) latestDiscountApplied7));

                    barEntriesForTotalUthaoCommission.add(new BarEntry(2, (float) latestUthaoCommissionAfterDiscount1));
                    barEntriesForTotalUthaoCommission.add(new BarEntry(3, (float) latestUthaoCommissionAfterDiscount2));
                    barEntriesForTotalUthaoCommission.add(new BarEntry(4, (float) latestUthaoCommissionAfterDiscount3));
                    barEntriesForTotalUthaoCommission.add(new BarEntry(5, (float) latestUthaoCommissionAfterDiscount4));
                    barEntriesForTotalUthaoCommission.add(new BarEntry(6, (float) latestUthaoCommissionAfterDiscount5));
                    barEntriesForTotalUthaoCommission.add(new BarEntry(0, (float) latestUthaoCommissionAfterDiscount6));
                    barEntriesForTotalUthaoCommission.add(new BarEntry(1, (float) latestUthaoCommissionAfterDiscount7));

                    Date today = new Date(System.currentTimeMillis());
                    int todayNumber = new DateTime(today).getDayOfWeek();


                    pieEntries.add(new PieEntry((float) totalCashCollectedThatDay[todayNumber - 1],
                            "Cash"));
                    pieEntries.add(new PieEntry((float) totalUthaoCommissionAfterDiscountThatDay[todayNumber - 1],
                            "Commission"));
                    pieEntries.add(new PieEntry((float) totalDiscountAppliedThatDay[todayNumber - 1],
                            "Discount"));

                    Log.i(TAG,"PieChart Today Earning: " + totalFareThatDay[todayNumber - 1]
                    + " | TodayNumber: " + todayNumber);
                    Log.i(TAG,"PieChart Cash Collected: " + totalCashCollectedThatDay[todayNumber - 1]);
                    Log.i(TAG,"PieChart Uthao Commission: " + totalUthaoCommissionAfterDiscountThatDay[todayNumber - 1]);

                    BarDataSet barDataSetForTotalTripCost = new BarDataSet(barEntriesForTotalTripCost,"Total Fare");
                    barDataSetForTotalTripCost.setColors(ColorTemplate.COLORFUL_COLORS);
                    barDataSetForTotalTripCost.setHighlightEnabled(true);
                    barDataSetForTotalTripCost.setHighLightAlpha(Color.RED);
                    barDataSetForTotalTripCost.setValueTextColor(Color.WHITE);
                    barDataSetForTotalTripCost.setValueTextSize(16);


                    BarDataSet barDataSetForTotalCashCollected = new BarDataSet(barEntriesForTotalCashCollected,"Total Cash Collected");
                    barDataSetForTotalCashCollected.setColors(ColorTemplate.COLORFUL_COLORS);
                    barDataSetForTotalCashCollected.setHighlightEnabled(true);
                    barDataSetForTotalCashCollected.setHighLightAlpha(Color.RED);
                    barDataSetForTotalCashCollected.setValueTextColor(Color.WHITE);
                    barDataSetForTotalCashCollected.setValueTextSize(16);

                    BarDataSet barDataSetForTotalDiscountApplied = new BarDataSet(barEntriesForTotalDiscountApplied,"Total Discount Applied");
                    barDataSetForTotalDiscountApplied.setColors(ColorTemplate.COLORFUL_COLORS);
                    barDataSetForTotalDiscountApplied.setHighlightEnabled(true);
                    barDataSetForTotalDiscountApplied.setHighLightAlpha(Color.RED);
                    barDataSetForTotalDiscountApplied.setValueTextColor(Color.WHITE);
                    barDataSetForTotalDiscountApplied.setValueTextSize(16);

                    BarDataSet barDataSetForTotalUthaoCommission = new BarDataSet(barEntriesForTotalUthaoCommission,"Total Commission (- Discount)");
                    barDataSetForTotalUthaoCommission.setColors(ColorTemplate.COLORFUL_COLORS);
                    barDataSetForTotalUthaoCommission.setHighlightEnabled(true);
                    barDataSetForTotalUthaoCommission.setHighLightAlpha(Color.RED);
                    barDataSetForTotalUthaoCommission.setValueTextColor(Color.WHITE);
                    barDataSetForTotalUthaoCommission.setValueTextSize(16);

                    PieDataSet pieDataSet = new PieDataSet(pieEntries,null);
                    pieDataSet.setSliceSpace(3f);
                    pieDataSet.setSelectionShift(5f);
                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.WHITE);
                    pieDataSet.setValueTextSize(16);


                    Legend barLegendForTotalTripCost = barChartForTotalTripCost.getLegend();
                    barLegendForTotalTripCost.setTextColor(Color.WHITE);
                    barLegendForTotalTripCost.setTextSize(16);
                    barLegendForTotalTripCost.setEnabled(true);

                    Legend barLegendForTotalCashCollected = barChartForTotalCashCollected.getLegend();
                    barLegendForTotalCashCollected.setTextColor(Color.WHITE);
                    barLegendForTotalCashCollected.setTextSize(16);
                    barLegendForTotalCashCollected.setEnabled(true);

                    Legend barLegendForTotalDiscountApplied = barChartForTotalDiscountApplied.getLegend();
                    barLegendForTotalDiscountApplied.setTextColor(Color.WHITE);
                    barLegendForTotalDiscountApplied.setTextSize(16);
                    barLegendForTotalDiscountApplied.setEnabled(true);

                    Legend barLegendForTotalUthaoCommission = barChartForTotalUthaoCommission.getLegend();
                    barLegendForTotalUthaoCommission.setTextColor(Color.WHITE);
                    barLegendForTotalUthaoCommission.setTextSize(16);
                    barLegendForTotalUthaoCommission.setEnabled(true);

                    Legend pieLegend = pieChart.getLegend();
                    pieLegend.setTextColor(Color.WHITE);
                    pieLegend.setTextSize(16);
                    pieLegend.setEnabled(true);

                    BarData barDataForTotalTripCost = new BarData(barDataSetForTotalTripCost);
                    barDataForTotalTripCost.setBarWidth(0.9f);

                    BarData barDataForTotalCashCollected = new BarData(barDataSetForTotalCashCollected);
                    barDataForTotalCashCollected.setBarWidth(0.9f);

                    BarData barDataForTotalDiscountApplied = new BarData(barDataSetForTotalDiscountApplied);
                    barDataForTotalDiscountApplied.setBarWidth(0.9f); // set custom bar width

                    BarData barDataForTotalUthaoCommission = new BarData(barDataSetForTotalUthaoCommission);
                    barDataForTotalUthaoCommission.setBarWidth(0.9f);

                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueTextSize(16f);
                    pieData.setValueTextColor(Color.WHITE);

                    barChartForTotalTripCost.setData(barDataForTotalTripCost);
                    barChartForTotalTripCost.invalidate(); // refresh

                    barChartForTotalCashCollected.setData(barDataForTotalCashCollected);
                    barChartForTotalCashCollected.invalidate(); // refresh

                    barChartForTotalDiscountApplied.setData(barDataForTotalDiscountApplied);
                    barChartForTotalDiscountApplied.invalidate(); // refresh

                    barChartForTotalUthaoCommission.setData(barDataForTotalUthaoCommission);
                    barChartForTotalUthaoCommission.invalidate(); // refresh

                    pieChart.setData(pieData);
                    pieChart.invalidate(); // refresh



                }
            }
        });
    }

    private Long getMidnight(int dayOfMonth, int monthOfTheYear){
        Calendar c = new GregorianCalendar();
        c.set(Calendar.MONTH,(monthOfTheYear - 1));
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND,0);
        Long midnight  = c.getTimeInMillis();
        Log.i(TAG,"DayOfMonth: "+ dayOfMonth + " | " + "monthOfTheYear: "
                + monthOfTheYear + " | " +"Midnight: " + getDate(midnight));
        return midnight;
    }

    private String getDate(Long dateInMilli) {
        //First we will create an object from Calender.class As we are considering
        //Different time zone. This will give us customers local time zone.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(dateInMilli);
        String date = DateFormat.format("dd-MM-yyyy hh:mm a", calendar).toString();
        return date;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter{

        private String[] mValues;
        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
//            Log.i(TAG, "mValues.length: "+mValues.length + " | Current Value: " + value);
            if (mValues.length > value){

            }
            return mValues[(int) value];
        }
    }
}
