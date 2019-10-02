package com.example.batchprocessing;

import java.util.Arrays;

public class IPRange {
    private final String[] addresses;

    public IPRange(String address) {
        this.addresses = new String[] {address};
    }

    public IPRange(String[] addresses) {
        this.addresses = addresses;
    }

    public boolean isInRange(String ip) {
        for (String address: addresses         ) {
            boolean eq = address.equals(ip);
            if (eq) {
                return eq;
            }
        }
        return false;
    }

    public String[] getAddresses() {
        return addresses;
    }

    @Override
    public String toString() {
        return "IPRange{" +
                "addresses=" + Arrays.toString(addresses) +
                '}';
    }
}
