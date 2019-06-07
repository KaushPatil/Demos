package com.syslogyx.bluetoothdemo.database.listener;

/**
 * Manager that can clear his data.
 *
 */
public interface OnClearListener extends BaseManagerInterface {

    /**
     * Clear all local data.
     *
     * WILL BE CALLED FROM BACKGROUND THREAD. DON'T CHANGE OR ACCESS
     * APPLICATION'S DATA HERE!
     */
    void onClear();

}
