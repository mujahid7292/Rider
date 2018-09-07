package com.sand_corporation.www.uthaopartner.FareCalculator;

import android.util.Log;

/**
 * Created by HP on 2/26/2018.
 */

public class FareCalculator {

    //********************************Fair Calculation***********************************
    //************************************Starts Here************************************
    private Double premiumCarBaseFair = 80.00;
    private Double premiumCarPerKmFair = 19.00;
    private Double premiumCarWaitingTimePerMin = 5.00;

    private Double economyCarBaseFair = 40.00;
    private Double economyCarPerKmFair = 17.00;
    private Double economyCarWaitingTimePerMin = 4.00;

    private Double premiumBikeBaseFair = 30.00;
    private Double premiumBikePerKmFair = 9.00;
    private Double premiumBikeWaitingTimePerMin = 2.50;

    private Double economyBikeBaseFair = 25.00;
    private Double economyBikePerKmFair = 8.00;
    private Double economyBikeWaitingTimePerMin = 2.00;

    //********************************Fair Calculation***********************************
    //************************************Ends Here************************************

    private double uthaoCommissionInPercentage = 0.20;
    private static final String TAG = "FareCalculator";

    public double getCalculatedTotalFare(String driverOrBiker, String serviceType,
                                          double travelDistance, long totalRideTime,
                                         double demandChargeInPercentage){
        double totalFair = 0;
        double totalDistanceFair = 0;
        double totalWaitingTimeFair = 0;
        if (driverOrBiker.equals("Driver")){
            if (serviceType.equals("Premium")){
                totalDistanceFair = travelDistance * premiumCarPerKmFair;
                totalWaitingTimeFair = totalRideTime * premiumCarWaitingTimePerMin;
                totalFair = premiumCarBaseFair +
                        totalDistanceFair + totalWaitingTimeFair;
            } else if(serviceType.equals("Economy")){
                totalDistanceFair = travelDistance * economyCarPerKmFair;
                totalWaitingTimeFair = totalRideTime * economyCarWaitingTimePerMin;
                totalFair = economyCarBaseFair +
                        totalDistanceFair + totalWaitingTimeFair;
            }
        } else if (driverOrBiker.equals("Biker")){
            if (serviceType.equals("Premium")){
                totalDistanceFair = travelDistance * premiumBikePerKmFair;
                totalWaitingTimeFair = totalRideTime * premiumBikeWaitingTimePerMin;
                totalFair = premiumBikeBaseFair +
                        totalDistanceFair + totalWaitingTimeFair;
            } else if(serviceType.equals("Economy")){
                totalDistanceFair = travelDistance * economyBikePerKmFair;
                totalWaitingTimeFair = totalRideTime * economyBikeWaitingTimePerMin;
                totalFair = economyBikeBaseFair +
                        totalDistanceFair + totalWaitingTimeFair;
            }
        }

        if (demandChargeInPercentage > 0.00){
            totalFair = totalFair + (totalFair * demandChargeInPercentage);
        }
        return totalFair;
    }

    public double getCalculatedDistanceFare(String driverOrBiker, String serviceType,
                                          double travelDistance){
        double totalDistanceFair = 0;
        if (driverOrBiker.equals("Driver")){
            if (serviceType.equals("Premium")){
                totalDistanceFair = travelDistance * premiumCarPerKmFair;
            } else if(serviceType.equals("Economy")){
                totalDistanceFair = travelDistance * economyCarPerKmFair;
            }
        } else if (driverOrBiker.equals("Biker")){
            if (serviceType.equals("Premium")){
                totalDistanceFair = travelDistance * premiumBikePerKmFair;
            } else if(serviceType.equals("Economy")){
                totalDistanceFair = travelDistance * economyBikePerKmFair;
            }
        }

        return totalDistanceFair;
    }

    public double getCalculatedWaitingTimeFair(String driverOrBiker, String serviceType,
                                                long totalRideTime){

        double totalWaitingTimeFair = 0;
        if (driverOrBiker.equals("Driver")){
            if (serviceType.equals("Premium")){
                totalWaitingTimeFair = totalRideTime * premiumCarWaitingTimePerMin;
            } else if(serviceType.equals("Economy")){
                totalWaitingTimeFair = totalRideTime * economyCarWaitingTimePerMin;
            }
        } else if (driverOrBiker.equals("Biker")){
            if (serviceType.equals("Premium")){
                totalWaitingTimeFair = totalRideTime * premiumBikeWaitingTimePerMin;
            } else if(serviceType.equals("Economy")){
                totalWaitingTimeFair = totalRideTime * economyBikeWaitingTimePerMin;
            }
        }

        return totalWaitingTimeFair;

    }
    public double getCashCollectedInThisTrip(double TotalPayment,
                                             double discountInPercentage,
                                             double discountAmountUpToTaka,
                                             double demandChargeInPercentage){
        double totalDemandCharge = TotalPayment  * demandChargeInPercentage;
        double totalDiscountAmountInThisRide = (TotalPayment + totalDemandCharge)
                * discountInPercentage;
        if (totalDiscountAmountInThisRide > discountAmountUpToTaka){
            totalDiscountAmountInThisRide = discountAmountUpToTaka;
        }
        double cashCollected = TotalPayment + totalDemandCharge - totalDiscountAmountInThisRide;
        Log.i(TAG,"******************Cash Collected*********************");
        Log.i(TAG,"totalDemandCharge = TotalPayment  * demandChargeInPercentage\n" +
                totalDemandCharge + " = " + TotalPayment + " * " + demandChargeInPercentage);

        Log.i(TAG,"discountAmountUpToTaka: " + discountAmountUpToTaka);
        Log.i(TAG,"totalDiscountAmountInThisRide = (TotalPayment + totalDemandCharge) " +
                "                * discountInPercentage\n" +
                totalDiscountAmountInThisRide + " = " + "(" + TotalPayment + " + " +totalDemandCharge
        +") * " + discountInPercentage);
        Log.i(TAG,"cashCollected = TotalPayment + totalDemandCharge - totalDiscountAmountInThisRide\n" +
        cashCollected + " = " + TotalPayment + " + " + totalDemandCharge + " - " + totalDiscountAmountInThisRide
                + "\n\n\n");
        return cashCollected;

    }

    public double getAppliedDiscountInTaka(double TotalPayment,
                                                  double discountInPercentage,
                                                  double discountAmountUpToTaka,
                                                  double demandChargeInPercentage){
        double totalDemandCharge = TotalPayment  * demandChargeInPercentage;
        double totalDiscountAmountInThisRide = (TotalPayment + totalDemandCharge)
                * discountInPercentage;
        if (totalDiscountAmountInThisRide > discountAmountUpToTaka){
            totalDiscountAmountInThisRide = discountAmountUpToTaka;
        }

        return totalDiscountAmountInThisRide;

    }

    public double getUthaoCommissionAfterDiscount(double TotalPayment,
                                             double discountInPercentage,
                                             double discountAmountUpToTaka,
                                             double demandChargeInPercentage){
        double totalDemandCharge = TotalPayment  * demandChargeInPercentage;
        double totalDiscountAmountInThisRide = (TotalPayment + totalDemandCharge)
                * discountInPercentage;
        if (totalDiscountAmountInThisRide > discountAmountUpToTaka){
            totalDiscountAmountInThisRide = discountAmountUpToTaka;
        }
        //               100                        500             50                   550 * 20% = 110                  10
        double UthaoCommissionAfterDiscount = ((TotalPayment + totalDemandCharge) * uthaoCommissionInPercentage) - totalDiscountAmountInThisRide;
        Log.i(TAG,"******************Uthao Commission After Discount*********************");
        Log.i(TAG,"UthaoCommissionAfterDiscount = ((TotalPayment + totalDemandCharge) * uthaoCommissionInPercentage) - totalDiscountAmountInThisRide\n" +
                UthaoCommissionAfterDiscount + " = ((" + TotalPayment + " + " + totalDemandCharge + ") * " + uthaoCommissionInPercentage + ") - " +
        totalDiscountAmountInThisRide);
        return UthaoCommissionAfterDiscount;

    }
}
