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
import com.sphenon.basics.context.classes.*;

/** {@EntitySecurityClass User}

    @doclet {@Category Definition} {@SecurityClass User} {@Maturity Final}

    A Message, classified according to it's meaning, not processing or
    technical internals. Two main subclasses are provided: 
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/SystemMessage">SystemMessages</link>
    and
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/message/ApplicationMessage">ApplicationMessages</link>,
    refer to specific derived classes for details.

    @doclet {@Category Implementation} {@SecurityClass Developer} {@Maturity Final}

    A note on implementation:
    Why abstract classes instead of interfaces? Why no factories?

    There had been several constraints to be considered, including: message
    class creation will be very widespread throughout code, therefore the API
    has to be very simple and straight foreward to read and understand; the
    actual implementation shall be exchangeable, therefore no use of
    <quote>new</quote> operators is advisable; java is only able to handle
    single inheritance, furthermore no default implementations can be placed
    in interfaces.
*/
public class Message implements ContextAware {
    protected MessageText text;

    protected Message (CallContext cc, MessageText text) {
        this.text = text;
    }

    static public Message create (CallContext cc, MessageText text) {
        return new Message(cc, text);
    }

    public String toString() {
        return this.toString(RootContext.getFallbackCallContext(), null);
    }

    public String toString(CallContext cc) {
        return this.toString(cc, null);
    }

    public String toString(CallContext cc, AttachmentHandler attachment_handler) {
        return this.text.getText(cc, attachment_handler);
    }

    public MessageText getMessageText (CallContext cc) {
        return this.text;
    }
}
