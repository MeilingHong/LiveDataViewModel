package com.meiling.component.utils.network.signal_strength;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

import com.meiling.component.utils.log.Mlog;

/**
 * @Author marisareimu
 * @time 2021-05-26 09:20
 */
public class PhoneSignalStrengthListener extends PhoneStateListener {
    private PhoneSignalStrengthCallback phoneSignalStrengthCallback;
    private Context mApplicationContext;

    public PhoneSignalStrengthListener(Context context, PhoneSignalStrengthCallback phoneSignalStrengthCallback) {
        this.mApplicationContext = context.getApplicationContext();
        this.phoneSignalStrengthCallback = phoneSignalStrengthCallback;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {//Android 6
            int level = signalStrength.getLevel();
            Mlog.d("level====>" + level);
        }
        int cdmaDbm = signalStrength.getCdmaDbm();
        int evdoDbm = signalStrength.getEvdoDbm();
        Mlog.d("cdmaDbm=====>" + cdmaDbm);
        Mlog.d("evdoDbm=====>" + evdoDbm);
        int gsmSignalStrength = signalStrength.getGsmSignalStrength();
        Mlog.d("gsmSignalStrength===========>" + gsmSignalStrength);
        int dbm = -113 + 2 * gsmSignalStrength;
        Mlog.d("dbm===========>" + dbm);
        /*
        0 —— (-55)dbm 满格(4格)信号
        (-55) —— (-70)dbm 3格信号
        (-70) —— (-85)dbm　2格信号
        (-85) —— (-100)dbm 1格信号
         */
        if (dbm > -55) {
            doCallbackResult(SignalStrengthEnum.EXCELLENT);
            return;
        }
        if (dbm > -70) {
            doCallbackResult(SignalStrengthEnum.GOOD);
            return;
        }
        if (dbm > -85) {
            doCallbackResult(SignalStrengthEnum.WELL);
            return;
        }
        if (dbm > -100) {
            doCallbackResult(SignalStrengthEnum.NORMAL);
            return;
        }
        doCallbackResult(SignalStrengthEnum.POORLY);
    }

    private void doCallbackResult(SignalStrengthEnum signalStrengthEnum) {
        if (phoneSignalStrengthCallback != null) {
            phoneSignalStrengthCallback.getPhoneSignalStrength(signalStrengthEnum);
        }
    }

    @Override
    public void onSignalStrengthChanged(int asu) {
        super.onSignalStrengthChanged(asu);
    }
}
