package com.devup.qcm.sdk.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by istat on 05/07/17.
 */

public interface JSONable {
    JSONObject toJson() throws JSONException;
}
