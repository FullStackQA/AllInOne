package com.utilities;

import static com.utilities.PropertyReader.getPropertyValue;

public class Timeout {
    public static int element_timeout_in_seconds = Integer.parseInt(getPropertyValue("timeout"));
}