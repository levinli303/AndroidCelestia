/*
 * CelestiaDestination.java
 *
 * Copyright (C) 2001-2020, Celestia Development Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package space.celestia.mobilecelestia.core;

import java.io.Serializable;

public class CelestiaDestination implements Serializable {
    public final String name;
    public final String target;
    public final double distance;
    public final String description;

    private CelestiaDestination(String name, String target, double distance, String description) {
        this.name = name;
        this.target = target;
        this.distance = distance;
        this.description = description;
    }
}
