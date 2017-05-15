/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.library;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Comparison {
    private Logger objComparisonLogger = LogManager.getLogger(Comparison.class.getName());

    public boolean compareValues(String strValueOne, String strOperation, String strValueTwo) {
        String strClassNameValueOne = strValueOne.getClass().getSimpleName();
        strOperation = strOperation.toUpperCase();
        if (strClassNameValueOne.contains("String")) {
            return this.compareStrings(strValueOne, strOperation, strValueTwo);
        }
        if (strClassNameValueOne.contains("Integer")) {
            return this.compareIntegers(strValueOne, strOperation, strValueTwo);
        }
        if (strClassNameValueOne.contains("Boolean")) {
            return this.compareBoolean(strValueOne, strOperation, strValueTwo);
        }
        this.objComparisonLogger.error("Either \"" + strValueOne + '\"' + " or " + '\"' + " strValueTwo " + '\"' + " is not of a supported data type");
        return false;
    }

    public boolean compareIntegers(String strIntegerOne, String strOperation, String strIntegerTwo) {
        int intValueOne = Integer.valueOf(strIntegerOne);
        int intValueTwo = Integer.valueOf(strIntegerTwo);
        if (strOperation.equalsIgnoreCase("EQUAL") ? intValueOne == intValueTwo : strOperation.equalsIgnoreCase("NOTEQUAL") && intValueOne != intValueTwo) {
            return true;
        }
        return false;
    }

    public boolean compareBoolean(String strBooleanOne, String strOperation, String strBolleanTwo) {
        boolean blnValueOne = Boolean.valueOf(strBooleanOne);
        boolean blnValueTwo = Boolean.valueOf(strBolleanTwo);
        if (strOperation.equalsIgnoreCase("EQUAL") ? blnValueOne == blnValueTwo : strOperation.equalsIgnoreCase("NOTEQUAL") && blnValueOne != blnValueTwo) {
            return true;
        }
        return false;
    }

    public boolean compareStrings(String strStringOne, String strOperation, String strStringTwo) {
        strStringOne = String.valueOf(strStringOne).trim();
        strStringTwo = String.valueOf(strStringTwo).trim();
        if (strOperation.equalsIgnoreCase("EQUAL") ? strStringOne.equalsIgnoreCase(strStringTwo) : (strOperation.equalsIgnoreCase("NOTEQUAL") ? !strStringOne.equalsIgnoreCase(strStringTwo) : strOperation.equalsIgnoreCase("CONTAINS") && strStringOne.toLowerCase().contains(strStringTwo.toLowerCase()))) {
            return true;
        }
        return false;
    }
}

