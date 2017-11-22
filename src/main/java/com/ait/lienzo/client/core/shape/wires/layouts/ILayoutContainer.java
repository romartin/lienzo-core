/*
   Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.client.core.shape.wires.layouts;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.Point2D;

public interface ILayoutContainer<L>
{
    ILayoutContainer setOffset(Point2D offset);

    ILayoutContainer setSize(double width, double height);

    ILayoutContainer add(IPrimitive<?> child);

    ILayoutContainer remove(IPrimitive<?> child);

    ILayoutContainer execute();

    ILayoutContainer refresh();

    Group getGroup();

    L getLayout();

    void destroy();
}
