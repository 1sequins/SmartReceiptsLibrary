package co.smartreceipts.android.identity.apis.login;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public class LoginResponse implements Serializable {

    private String token;

    @Nullable
    public String getToken() {
        return token;
    }

}
