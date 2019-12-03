package org.esourcer.orders.common;

public enum Currency {

    EUR(2),
    USD(2),
    DKK(2),
    HUF(0);

    private int scale;

    Currency(final int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

}
