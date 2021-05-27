package com.meiling.component.utils.gson;

import com.google.gson.Gson;


public class Gsons {

    private static class Holder {
        private static final Gson INSTANCE = new Gson();
    }

    public static Gson getInstance() {
        return Holder.INSTANCE;
    }
}
