package org.irrad;

class Timer {
    final long start;

    public Timer() {
        start = System.currentTimeMillis();
    }

    /**
     * 
     * @return seconds elpased
     */
    public double elapsed() {
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        double ret = (double) timeElapsed;
        return ret / 1000;
    }
}