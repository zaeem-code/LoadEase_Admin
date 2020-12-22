package com.loadease.uberclone.adminpanels.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

import com.loadease.uberclone.adminpanels.Common.Common;

import java.util.List;
import java.util.Locale;

public class LocationUtils {


    public static String getAddressFromLocation(Context context)
    {
        StringBuilder result=new StringBuilder();
        Geocoder geocoder=new Geocoder(context, Locale.getDefault());
        List<Address> addressList;

        try {

            addressList=geocoder.getFromLocation(Common.currentLat,Common.currentLng,1);
            if (addressList!=null && addressList.size()>0)
            {

                if (addressList.get(0).getLocality()!=null &&  !TextUtils.isEmpty(addressList.get(0).getLocality()))

                {
                    result.append(addressList.get(0).getLocality());
                }
               else if (addressList.get(0).getSubAdminArea()!=null &&  !TextUtils.isEmpty(addressList.get(0).getSubAdminArea()))

                {
                    result.append(addressList.get(0).getSubAdminArea());
                }

                else if (addressList.get(0).getAdminArea()!=null &&  !TextUtils.isEmpty(addressList.get(0).getAdminArea()))

                {
                    result.append(addressList.get(0).getAdminArea());
                }


                else
                {
                    result.append(addressList.get(0).getCountryName());
                }

                result.append("_").append(addressList.get(0).getCountryCode());

            }

            return result.toString();

        }
        catch (Exception e)
        {
            return result.toString();

        }

    }

}
