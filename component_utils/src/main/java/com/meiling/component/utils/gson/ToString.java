package com.meiling.component.utils.gson;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class ToString implements Serializable {
    @NonNull
    @Override
    public String toString() {
        return Gsons.getInstance().toJson(this);
    }
}
