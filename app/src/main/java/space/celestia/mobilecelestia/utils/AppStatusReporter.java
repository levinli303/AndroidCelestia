/*
 * AppStatusReporter.java
 *
 * Copyright (C) 2001-2020, Celestia Development Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package space.celestia.mobilecelestia.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import space.celestia.mobilecelestia.core.CelestiaAppCore;

public class AppStatusReporter implements CelestiaAppCore.ProgressWatcher {
    private static AppStatusReporter singleton = null;
    private ArrayList<Listener> listeners = new ArrayList<>();
    private String status = "";

    private Object lock = new Object();

    public enum State {
        EXTERNAL_LOADING_FAILURE(-2),
        LOADING_FAILURE(-1),
        NONE(0),
        EXTERNAL_LOADING(1),
        LOADING(2),
        LOADING_SUCCESS(3),
        FINISHED(4);

        private int value;

        public int getValue() {
            return value;
        }

        private State(int i) {
            this.value = i;
        }
    }

    private State state = State.NONE;

    @NonNull public State getState() {
        synchronized (lock) {
            return state;
        }
    }

    @NonNull public String getStatus() {
        synchronized (lock) {
            return status;
        }
    }

    @NonNull public static AppStatusReporter shared() {
        if (singleton == null)
            singleton = new AppStatusReporter();
        return singleton;
    }

    public interface Listener {
        void celestiaLoadingProgress(@NonNull String status);
        void celestiaLoadingStateChanged(@NonNull State newState);
    }

    public void register(Listener listener) {
        synchronized (lock) {
            if (!listeners.contains(listener))
                listeners.add(listener);
        }
    }

    public void unregister(Listener listener) {
        synchronized (lock) {
            listeners.remove(listener);
        }
    }

    @Override
    public void onCelestiaProgress(@NonNull String progress) {
        updateStatus(String.format(CelestiaAppCore.getLocalizedString("Loading: %s"), progress));
    }

    public void updateState(@NonNull State state) {
        ArrayList<Listener> currentListeners;
        synchronized (lock) {
            this.state = state;
            currentListeners = new ArrayList<>(listeners.size());
            for (Listener listener : listeners) {
                currentListeners.add(listener);
            }
        }
        for (Listener listener : currentListeners) {
            listener.celestiaLoadingStateChanged(state);
        }
    }

    public void updateStatus(@NonNull String status) {
        ArrayList<Listener> currentListeners;
        synchronized (lock) {
            this.status = status;
            currentListeners = new ArrayList<>(listeners.size());
            for (Listener listener : listeners) {
                currentListeners.add(listener);
            }
        }
        for (Listener listener : currentListeners) {
            listener.celestiaLoadingProgress(status);
        }
    }
}
