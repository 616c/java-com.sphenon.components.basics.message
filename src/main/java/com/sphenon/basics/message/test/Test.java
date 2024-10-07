package com.sphenon.basics.message.test;

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
import com.sphenon.basics.message.*;
import com.sphenon.basics.monitoring.*;

public class Test {
    public static void main(String[] args) {
        System.out.println( "main..." );

        Context context = com.sphenon.basics.context.classes.RootContext.getRootContext ();

        System.out.println( "main, creating message 1..." );
        Message msg1 = Message.create(context, MessageText.create(context, "Hallo, guten Tag!"));
        System.out.println( "main, message 1 is: " + msg1);

        System.out.println( "main, creating message 2..." );
        Message msg2 = Message.create(context, MessageText.create(context, "Hallo, guten %(zeiteinheit)!", "zeiteinheit", "Tag"));
        System.out.println( "main, message 1 is: " + msg2);

        System.out.println( "main, creating message 3..." );
        Message msg3 = Message.create(context, MessageText.create(context, MessageTestStringPool.get(context, "0.0.0" /* Test Message */)));
        System.out.println( "main, message 3 is: " + msg3);

        System.out.println( "main, creating message 4..." );
        Message msg4 = Message.create(context, MessageText.create(context, MessageTestStringPool.get(context, "0.0.1" /* Test Message with Parameter: %(gruss), world! */),
                                     "gruss", "hello"));
        System.out.println( "main, message 4 is: " + msg4);

        System.out.println( "main, creating message 5..." );
        Message msg5 = Message.create(context, MessageText.create(context, "6 * 7 = %(answer)", "answer", t.o(42)));
        System.out.println( "main, message 5 is: " + msg5);

        System.out.println( "main, creating message 6.." );
        Message msg6 = SystemStateMessage.create(context, MessageText.create(context, "Oje: %(problem_state)!"), ProblemState.FATAL_ERROR);
        System.out.println( "main, message 6 is: " + msg6);

        System.out.println( "main done." );
    }
}

