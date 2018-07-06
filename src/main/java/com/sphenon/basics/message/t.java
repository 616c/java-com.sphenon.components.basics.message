package com.sphenon.basics.message;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sphenon.basics.context.*;

import com.sphenon.basics.message.classes.*;

public class t {
    public static Object o (boolean b) { return new Boolean(b); }
    public static Object o (byte b)    { return new Byte(b); }
    public static Object o (char c)    { return new Character(c); }
    public static Object o (short s)   { return new Short(s); }
    public static Object o (int i)     { return new Integer(i); }
    public static Object o (long l)    { return new Long(l); }
    public static Object o (float f)   { return new Float(f); }
    public static Object o (double d)  { return new Double(d); }
    public static Object o (Object o)  { return o; }

    public static String s (boolean b) { return (new Boolean(b)).toString(); }
    public static String s (byte b)    { return (new Byte(b)).toString(); }
    public static String s (char c)    { return (new Character(c)).toString(); }
    public static String s (short s)   { return (new Short(s)).toString(); }
    public static String s (int i)     { return (new Integer(i)).toString(); }
    public static String s (long l)    { return (new Long(l)).toString(); }
    public static String s (float f)   { return (new Float(f)).toString(); }
    public static String s (double d)  { return (new Double(d)).toString(); }
    public static String s (String s)  { return s; }

    public static String s (CallContext context, boolean b) { return (new Boolean(b)).toString(); }
    public static String s (CallContext context, byte b)    { return (new Byte(b)).toString(); }
    public static String s (CallContext context, char c)    { return (new Character(c)).toString(); }
    public static String s (CallContext context, short s)   { return (new Short(s)).toString(); }
    public static String s (CallContext context, int i)     { return (new Integer(i)).toString(); }
    public static String s (CallContext context, long l)    { return (new Long(l)).toString(); }
    public static String s (CallContext context, float f)   { return (new Float(f)).toString(); }
    public static String s (CallContext context, double d)  { return (new Double(d)).toString(); }
    public static String s (CallContext context, Object o)  { return MessageTextClass.convertToString(context, o); }

    public static boolean b (String s) { return Boolean.parseBoolean(s); }
    public static byte    y (String s) { return Byte.parseByte(s); }
    public static char    c (String s) { return s.charAt(0); }
    public static short   h (String s) { return Short.parseShort(s); }
    public static int     i (String s) { return Integer.parseInt(s); }
    public static long    l (String s) { return Long.parseLong(s); }
    public static float   f (String s) { return Float.parseFloat(s); }
    public static double  d (String s) { return Double.parseDouble(s); }

    public static String  toString (Object object) {
        return (object == null ? "<null>" : (object.getClass().getName().replaceFirst("^.*\\.","") + "#" + Integer.toHexString(System.identityHashCode(object)).toUpperCase()));
    }
}
