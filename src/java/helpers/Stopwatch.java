/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

/**
 *
 * @author nazif
 */
public class Stopwatch {

    private long start;

    public Stopwatch() {
        start = System.currentTimeMillis();
    }

    public double elapsedTimeMillisecond() {
        long now = System.currentTimeMillis();
        long elapsed = now - start;
        start = 0;
        return elapsed;
    }

}
