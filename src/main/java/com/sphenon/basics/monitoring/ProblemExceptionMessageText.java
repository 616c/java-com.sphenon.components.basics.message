package com.sphenon.basics.monitoring;

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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.message.*;

import java.io.PrintStream;

import java.util.Vector;

/**
   A {@link Problem} whose cause is an exception.
*/
public class ProblemExceptionMessageText extends ProblemException {

    public ProblemExceptionMessageText(CallContext context, Throwable exception, MessageText message_text) {
        super(context, exception, null);
        this.message_text = message_text;
    }

    protected MessageText message_text;

    public String getMessage (CallContext context) {
        return this.message_text.getText(context);
    }
}
