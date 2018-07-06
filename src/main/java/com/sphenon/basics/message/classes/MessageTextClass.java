package com.sphenon.basics.message.classes;

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
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.tplinst.*;
import com.sphenon.basics.message.*;

import java.util.regex.*;
import java.util.List;

public class MessageTextClass extends MessageText {
    protected String              text;
    protected String              text_cache;
    protected Variative_String_   variative_text;
    protected boolean             processed;
    protected boolean             to_process;
    protected boolean             contains_attachments;

    protected java.util.Hashtable attributes;

    static protected long next_attachment_id = 0;

    static public interface DynamicStringHandler {
        public String process(CallContext context, String text);
    }
    static public DynamicStringHandler dynamic_string_handler;

    synchronized static protected String getNextAttachmentId (CallContext context) {
        return new Long(next_attachment_id++).toString();
    }

    public void setAttribute (String name, Object value) {
        if (this.attributes == null) {
            this.attributes = new java.util.Hashtable();
        }
        if (this.attributes.containsKey(name)) {
            System.err.println("warning: attribute '" + name + "' in MessageTextClass is set twice, second value will overwrite first one");
        }
        this.attributes.put(name, value == null ? "(null)" : value);
        this.to_process = true;
    }

    public String getText (CallContext cc) {
        return this.getVariant_String_(cc);
    }

    public String getText (CallContext cc, AttachmentHandler attachment_handler) {
        return this.getVariant_String_(cc, null, attachment_handler);
    }

    public String getVariant_String_ (CallContext context) {
        return this.getVariant_String_(context, null, null);
    }

    public String getVariant_String_ (CallContext context, VariantSelectors variant_selectors) {
        return this.getVariant_String_(context, variant_selectors, null);
    }


    static public interface Encoder {
        public String encode(CallContext context, String text, String encoding);
    }

    static public Encoder encoder;

    static protected String encode(CallContext context, String text, String encoding) {
        return encoder == null ? text : encoder.encode(context, encoding, text);
    }

    static protected String format(CallContext context, Object value, String format) {
        return format == null || format.isEmpty() ?
                 value.toString()
               : format.startsWith("%%") ?
                   encode(context, value.toString(), format.substring(2))
                 : String.format(format, value);
    }


    static public String convertToString(CallContext context, Object object) {
        return convertToString(context, object, null, null);
    }

    static public String convertToString(CallContext context, Object object, VariantSelectors variant_selectors) {
        return convertToString(context, object, variant_selectors, null);
    }

    static public String convertToString(CallContext context, Object object, VariantSelectors variant_selectors, String format) {
        String value = null;
        if (object == null) {
            value = "(null)";
        } else if (object instanceof String) {
            value = (String) object;
        } else if (object instanceof Variative_String_) {
            value = ((Variative_String_) object).getVariant_String_(context, variant_selectors);
        } else if (object instanceof Throwable) {
            value = com.sphenon.basics.monitoring.ProblemException.dumpThrowable(context, (Throwable) object);
        } else if (object instanceof MessageAware) {
            value = ((MessageAware) object).toMessageText(context).toString(context);
        } else if (object instanceof MessageText) {
            value = ((MessageText) object).toString(context);
        } else if (object instanceof ContextAware) {
            value = ((ContextAware) object).toString(context);
        } else if (object instanceof List) {
            StringBuffer result = new StringBuffer();
            result.append("[");
            boolean first = true;
            for (Object item : (List) object) {
                if (first) { first = false; } else { result.append(", "); }
                result.append(convertToString(context, item, variant_selectors));
            }
            result.append("]");
            value = result.toString();
        } else if (object.getClass().isArray()) {
            StringBuffer result = new StringBuffer();
            result.append("[");
            boolean first = true;
            for (int i=0; i<java.lang.reflect.Array.getLength(object); i++) {
                if (first) { first = false; } else { result.append(", "); }
                result.append(convertToString(context, java.lang.reflect.Array.get(object, i), variant_selectors));
            }
            result.append("]");
            value = result.toString();
        } else {
            value = format(context, object, format);
            format = null;
        }
        return format(context, value, format);
    }

    static protected Pattern pattern1;

    public String getVariant_String_ (CallContext context, VariantSelectors variant_selectors, AttachmentHandler attachment_handler) {
        if (variative_text != null) {
            String cur_text;
            cur_text = variative_text.getVariant_String_(context, variant_selectors);
            if (this.text == null || this.text_cache != cur_text || this.contains_attachments == true) {
                this.text = cur_text;
                this.text_cache = cur_text;
                this.processed = false;
            }
        } else {
            if (this.contains_attachments == true) {
                this.text = this.text_cache;
                this.processed = false;
            }
        }
        if (this.text == null) {
            return "";
        }
        if ((to_process == false || this.processed == true) && this.contains_attachments == false) {
            // nothing to do
        } else {
            try {
                Pattern pattern = Pattern.compile("(?s)%\\((?:([^\\)\\|]*)\\|)?(?:([^\\)\\|]*)\\|)?([^\\)\\|]+)(?:\\|([^\\)\\|]*))?\\)");
                Matcher matcher = pattern.matcher(this.text);
                boolean first_nonempty = true;
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    String pretext1 = matcher.group(1);
                    String pretext2 = matcher.group(2);
                    String name     = matcher.group(3);
                    String posttext = matcher.group(4);
                    String format   = null;
                    int pos = name.indexOf('%');
                    if (pos != -1) {
                        format = name.substring(pos);
                        name = name.substring(0, pos);
                    }
                    String value;
                    char c0 = name.charAt(0); 
                    if (c0 != '{') {
                        boolean attach = (c0 == '@');
                        if (attach) { name = name.substring(1); }
                        Object oval = (this.attributes != null ? this.attributes.get(name) : null);
                        if (oval == null) {
                            value = "[undefined message parameter '" + name + "']";
                            System.err.println("precondition violation in MessageTextClass: undefined message parameter '" + name + "'");
                        } else {
                            value = convertToString(context, oval, variant_selectors, format);
                            format = null;
                            if (attach) {
                                this.contains_attachments = true;
                                String aid = getNextAttachmentId(context) + "_" + name;
                                if (attachment_handler != null) {
                                    attachment_handler.handleAttachment(context, aid, value);
                                    value = "[@AID:" + aid + "@]";
                                } else {
                                    value = "[@AID--LOST--:" + aid + "@]";
                                }
                            }
                        }
                    } else {
                        if (pattern1 == null) {
                            pattern1 = Pattern.compile("\\{([^\\}]*)\\}\\[([^\\]]*)\\]");
                        }
                        Matcher matcher1;
                        if ((matcher1 = pattern1.matcher(name)).matches() == false) {
                            value = "[invalid message parameter syntax: '" + name + "']";
                            System.err.println("precondition violation in MessageTextClass: invalid message parameter syntax: '" + name + "']");
                        } else {
                            name = matcher1.group(2);
                            String selection = matcher1.group(1);
                            Object oval = (this.attributes != null ? this.attributes.get(name) : null);
                            if (oval == null) {
                                value = "[undefined message parameter '" + name + "']";
                                System.err.println("precondition violation in MessageTextClass: undefined message parameter '" + name + "'");
                            } else {
                                try {
                                    int ival = (oval instanceof Integer ? ((Integer) oval).intValue() : (((Boolean) oval).booleanValue() ? 1 : 0));

                                    char deli = selection.charAt(0); 
                                    if (deli != '"' && deli != '\'') {
                                        value = "[selection list starts with invalid delimiter: " + deli + " ]";
                                        System.err.println("precondition violation in MessageTextClass: selection list starts with invalid delimiter: " + deli);
                                    } else {
                                        Pattern pattern2 = Pattern.compile("" + deli + "([^" + deli + "]*)" + deli);
                                        Matcher matcher2 = pattern2.matcher(selection);

                                        boolean found = false;
                                        int idx = 0;
                                        // int pos = 0;
                                        while (! found && matcher2.find()) {
                                            found = (idx == ival);
                                            idx++;
                                            // pos = rem2.getEndIndex();
                                        }
                                        if (found) {
                                            value = matcher2.group(1);
                                        } else {
                                            value = "[parameter '" + name + "' out of bounds, index is " + ival + ", array is '" + selection + "']";
                                            System.err.println("precondition violation in MessageTextClass: parameter '" + name + "' out of bounds, index is " + ival + ", array is '" + selection + "'");
                                        }
                                    }
                                } catch (ClassCastException cce) {
                                    value = "[invalid parameter type '" + name + "', expected 'Integer', got '" + oval.getClass() + "'], value is '" + oval.toString() + "'";
                                    System.err.println("precondition violation in MessageTextClass: invalid parameter type '" + name + "', expected 'Integer', got '" + oval.getClass() + "'], value is '" + oval.toString() + "'");
                                }
                            }
                        }
                    }
                    boolean empty = (value == null || value.length() == 0);
                    matcher.appendReplacement(sb, "");
                    sb.append(    (   (empty || pretext1 == null) ? ""
                                    : (   pretext2 == null ?
                                          pretext1
                                        : (first_nonempty ? pretext1 : pretext2)
                                      )
                                  )
                                + format(context, value, format)
                                + (   (empty || posttext == null) ? ""
                                    : posttext
                                  )
                             );
                    if (empty == false) { first_nonempty = false; }
                }
                try {
                    matcher.appendTail(sb);
                } catch (java.lang.StringIndexOutOfBoundsException sioobe) {
                    throw sioobe;
                }
                this.text = sb.toString();

            } catch (PatternSyntaxException pse) {
                pse.printStackTrace();
                throw new RuntimeException("internal error in MessageTextClass: " + pse.getMessage() + " (while processing: " + this.text + ")");
            } catch (Throwable t) {
                t.printStackTrace();
                throw new RuntimeException("MessageTextClass failed: " + t.getMessage() + " (while processing: " + this.text + ")");
            }
            this.processed = true;
        }

        // dynamic string post processing
        if (this.text.isEmpty() == false && this.text.charAt(0) == '!') {
            this.text = this.text.substring(1);
            if (this.dynamic_string_handler == null) {
                this.text = "[NO EVALUATOR REGISTERED]" +this.text;
            } else {
                this.text = this.dynamic_string_handler.process(context, this.text);
            }
        }

        return this.text;
    }

    protected MessageTextClass (CallContext cc, String text, Variative_String_ variative_text, Object[][] old_attributes, Object... attributes) {
        if (old_attributes != null) {
            attributes = new Object[old_attributes.length * 2];
            int i=0;
            for (Object[] old_attribute : old_attributes) {
                attributes[i++] = old_attribute[0];
                attributes[i++] = old_attribute[1];
            }
        }

        this.text = text;
        this.variative_text = variative_text;
        this.text_cache = (this.variative_text == null ? this.text : null);
        this.processed = false;
        this.to_process = (attributes.length == 0 ? false : true);
        this.contains_attachments = false;

        if ((attributes.length % 2) != 0) {
            System.err.println("precondition violation in MessageTextClass: attribute list contains not an even number of entries (name/value pairs)");
        }
        for (int i=0; i<attributes.length; i+=2) {
            Object oname = attributes[i];
            Object value = attributes[i+1];
            try {
                String name = (String) oname;
                setAttribute(name, value);
            } catch (ClassCastException cce) {
                System.err.println("precondition violation in MessageTextClass: attribute list entry 'name' is not a String, but a " + oname.getClass());
            }
        }
    }

    static public MessageTextClass createMessageTextClass (CallContext cc, String text, Object... attributes) {
        return new MessageTextClass(cc, text, null, null, attributes);
    }

    static public MessageTextClass createMessageTextClass (CallContext cc, Variative_String_ text, Object... attributes) {
        return new MessageTextClass(cc, null, text, null, attributes);
    }

     static public MessageTextClass createMessageTextClass (CallContext cc, String text, Object[][] attributes) {
         return new MessageTextClass(cc, text, null, attributes);
     }

     static public MessageTextClass createMessageTextClass (CallContext cc, Variative_String_ text, Object[][] attributes) {
         return new MessageTextClass(cc, null, text, attributes);
     }
}
