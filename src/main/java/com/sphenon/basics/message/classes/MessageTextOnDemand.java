package com.sphenon.basics.message.classes;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

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

abstract public class MessageTextOnDemand extends MessageText {

    abstract protected MessageText getMessageText(CallContext context);

    protected java.util.Hashtable attributes;

    public void setAttribute (String name, Object value) {
        if (attributes == null) {
            attributes = new java.util.Hashtable();
        }
        attributes.put(name, value);
    }

    protected MessageText getMessageTextOnDemand(CallContext context) {
        MessageText mt = getMessageText(context);
        if (attributes != null) {
            for (java.util.Iterator iterator = attributes.entrySet().iterator();
                 iterator.hasNext();
                ) {
                java.util.Map.Entry me = (java.util.Map.Entry) iterator.next();
                mt.setAttribute((String) me.getKey(), me.getValue());
            }
        }
        return mt;
    }

    public String getText (CallContext context) {
        return getMessageTextOnDemand(context).getText(context);
    }

    public String getText (CallContext context, AttachmentHandler attachment_handler) {
        return getMessageTextOnDemand(context).getText(context, attachment_handler);
    }

    public String getVariant_String_ (CallContext context) {
        return getMessageTextOnDemand(context).getVariant_String_(context);
    }

    public String getVariant_String_ (CallContext context, VariantSelectors variant_selectors) {
        return getMessageTextOnDemand(context).getVariant_String_(context, variant_selectors);
    }

    public String getVariant (CallContext context) {
        return getVariant_String_(context);
    }

    public String getVariant (CallContext context, VariantSelectors variant_selectors) {
        return getVariant_String_(context, variant_selectors);
    }
}
