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

package com.ait.lienzo.client.core.shape.storage;

import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.tools.client.collection.MetaData;
import com.ait.lienzo.tools.client.collection.NFastArrayList;

public interface IStorageEngine<M> extends IStorageEngineIterable<M>
{
    public int size();

    public void clear();

    public boolean isEmpty();

    public void add(M item);

    public void remove(M item);

    public void refresh();

    public void refresh(M item);

    public boolean contains(M item);

    public NFastArrayList<M> getChildren();

    public NFastArrayList<M> getChildren(BoundingBox bounds);

    public boolean isSpatiallyIndexed();

    public MetaData getMetaData();

    public StorageEngineType getStorageEngineType();

    public void moveUp(M item);

    public void moveDown(M item);

    public void moveToTop(M item);

    public void moveToBottom(M item);

    // @FIXME serialisation (mdp)
    //public JSONObject toJSONObject();

    public void migrate(IStorageEngine<M> storage);
}
