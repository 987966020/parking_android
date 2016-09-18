package hbie.vip.parking;

import android.content.Intent;

/**
 * Created by mac on 16/5/9.
 */
public interface ReceiverInterface {
    /**
     * @author zhuchongqian
     * @param actions
     * @exception Registratioin
     *                of radio
     *
     */
    void regiserRadio(String[] actions);

    /**
     * @author zhuchongqian
     * @exception Cancellatioin
     *                of radio
     */
    void destroyRadio();

    /**
     * @author zhuchongqian
     * @param intent
     * @exception To
     *                deal with radio
     */
    void dealWithRadio(Intent intent);
}
