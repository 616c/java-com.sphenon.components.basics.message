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
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.tplinst.*;

public class MessageTextSequence extends MessageText {

    protected MessageText[] message_texts;
    
    public MessageTextSequence (CallContext context, MessageText... message_texts) {
        this.message_texts = message_texts;
    }

    static public MessageTextSequence createSequence (CallContext context, MessageText... message_texts) {
        return new MessageTextSequence (context, message_texts);
    }

    public String getText (CallContext context) {
        StringBuilder sb = new StringBuilder();
        for (MessageText message_text : this.message_texts) {
            sb.append(message_text.getText(context));
        }
        return sb.toString();
    }

    public String getText (CallContext context, AttachmentHandler attachment_handler) {
        StringBuilder sb = new StringBuilder();
        for (MessageText message_text : this.message_texts) {
            sb.append(message_text.getText(context, attachment_handler));
        }
        return sb.toString();
    } 

    public void setAttribute (String name, Object value) {
        for (MessageText message_text : this.message_texts) {
            message_text.setAttribute (name, value);
        }
    }

    public String toString(CallContext context) {
        return this.getText(context);
    }

    public String getVariant_String_ (CallContext context) {
        StringBuilder sb = new StringBuilder();
        for (MessageText message_text : this.message_texts) {
            sb.append(message_text.getVariant_String_(context));
        }
        return sb.toString();
    }

    public String getVariant_String_ (CallContext context, VariantSelectors variant_selectors) {
        StringBuilder sb = new StringBuilder();
        for (MessageText message_text : this.message_texts) {
            sb.append(message_text.getVariant_String_(context, variant_selectors));
        }
        return sb.toString();
    }
}
