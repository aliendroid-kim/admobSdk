package com.aliendroid.alienads;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;


public class AlienGDPR {
    public static ConsentInformation consentInformation;

    public static void loadGdpr(Activity activity, String selectAds, boolean childDirected) {
                ConsentRequestParameters params = new ConsentRequestParameters
                        .Builder()
                        .setTagForUnderAgeOfConsent(childDirected)
                        .build();

                consentInformation = UserMessagingPlatform.getConsentInformation(activity);
                consentInformation.requestConsentInfoUpdate(
                        activity,
                        params,
                        new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
                            @Override
                            public void onConsentInfoUpdateSuccess() {
                                if (consentInformation.isConsentFormAvailable()) {
                                    loadForm(activity);
                                }

                            }
                        },
                        new ConsentInformation.OnConsentInfoUpdateFailureListener() {
                            @Override
                            public void onConsentInfoUpdateFailure(FormError formError) {
                                // Handle the error.
                            }
                        });
    }

    public static void loadForm(Activity activity) {
        UserMessagingPlatform.loadConsentForm(
                activity,
                consentForm -> {
                    if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                        consentForm.show(
                                activity,
                                new ConsentForm.OnConsentFormDismissedListener() {
                                    @Override
                                    public void onConsentFormDismissed(@Nullable FormError formError) {
                                        // Handle dismissal by reloading form.
                                        loadForm(activity);
                                    }
                                });

                    }

                },
                formError -> {
                    /// Handle Error.
                }
        );
    }

}
