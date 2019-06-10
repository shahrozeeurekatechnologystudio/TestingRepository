package com.engagement.utils;

public enum UserActionsModeEnums {
    MODE_REGISTER {
        @Override
        public String toString() {
            return "register";
        }
    },
    MODE_LOGIN {
        @Override
        public String toString() {
            return "login";
        }
    },
    UPDATE {
        @Override
        public String toString() {
            return "update";
        }
    };
}
