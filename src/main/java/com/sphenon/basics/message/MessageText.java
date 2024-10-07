package com.sphenon.basics.message;

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
import com.sphenon.basics.message.classes.*;
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.tplinst.*;

public abstract class MessageText implements Variative_String_, ContextAware {
    abstract public String getText (CallContext cc);

    abstract public String getText (CallContext cc, AttachmentHandler attachment_handler);

    abstract public void setAttribute (String name, Object value);

    public String toString(CallContext context) {
        return this.getText(context);
    }

    static public MessageText create (CallContext cc, String text) {
        return MessageTextClass.createMessageTextClass (cc, text);
    }

    static public MessageText create (CallContext cc, String text, Object... attributes) {
        return MessageTextClass.createMessageTextClass (cc, text, attributes);
    }

    static public MessageText create (CallContext cc, String text, Object[][] attributes) {
        return MessageTextClass.createMessageTextClass (cc, text, attributes);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1, String an2, Object av2) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1, String an2, Object av2, String an3, Object av3) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6, String an7, Object av7) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6, an7, av7);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6, String an7, Object av7, String an8, Object av8) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6, an7, av7, an8, av8);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6, String an7, Object av7, String an8, Object av8, String an9, Object av9) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6, an7, av7, an8, av8, an9, av9);
    }

    static public MessageText create (CallContext cc, String text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6, String an7, Object av7, String an8, Object av8, String an9, Object av9, String an10, Object av10) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6, an7, av7, an8, av8, an9, av9, an10, av10);
    }

    static public MessageText create (CallContext cc, Variative_String_ text) {
        return MessageTextClass.createMessageTextClass (cc, text);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, Object... attributes) {
        return MessageTextClass.createMessageTextClass (cc, text, attributes);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, Object[][] attributes) {
        return MessageTextClass.createMessageTextClass (cc, text, attributes);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1, String an2, Object av2) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1, String an2, Object av2, String an3, Object av3) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6, String an7, Object av7) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6, an7, av7);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6, String an7, Object av7, String an8, Object av8) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6, an7, av7, an8, av8);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6, String an7, Object av7, String an8, Object av8, String an9, Object av9) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6, an7, av7, an8, av8, an9, av9);
    }

    static public MessageText create (CallContext cc, Variative_String_ text, String an1, Object av1, String an2, Object av2, String an3, Object av3, String an4, Object av4, String an5, Object av5, String an6, Object av6, String an7, Object av7, String an8, Object av8, String an9, Object av9, String an10, Object av10) {
        return MessageTextClass.createMessageTextClass (cc, text, an1, av1, an2, av2, an3, av3, an4, av4, an5, av5, an6, av6, an7, av7, an8, av8, an9, av9, an10, av10);
    }
}
