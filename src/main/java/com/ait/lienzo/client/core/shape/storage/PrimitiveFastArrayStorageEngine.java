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

import com.ait.lienzo.client.core.config.LienzoCore;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.json.IFactory;
import com.ait.lienzo.client.core.shape.json.IJSONSerializable;
import com.ait.lienzo.client.core.shape.json.validators.ValidationContext;
import com.ait.lienzo.client.core.shape.json.validators.ValidationException;

public class PrimitiveFastArrayStorageEngine extends AbstractFastArrayStorageEngine<IPrimitive<?>> implements IJSONSerializable<PrimitiveFastArrayStorageEngine>
{
    public PrimitiveFastArrayStorageEngine()
    {
        super(StorageEngineType.PRIMITIVE_FAST_ARRAY_STORAGE_ENGINE);
    }

    protected PrimitiveFastArrayStorageEngine(final Object node, final ValidationContext ctx) throws ValidationException
    {
        super(StorageEngineType.PRIMITIVE_FAST_ARRAY_STORAGE_ENGINE, node, ctx);
    }

    @Override
    public IFactory<?> getFactory()
    {
        return LienzoCore.get().getFactory(getStorageEngineType());
    }

    public static class PrimitiveFastArrayStorageEngineFactory extends FastArrayStorageEngineFactory<PrimitiveFastArrayStorageEngine>
    {
        public PrimitiveFastArrayStorageEngineFactory()
        {
            super(StorageEngineType.PRIMITIVE_FAST_ARRAY_STORAGE_ENGINE);
        }

        @Override
        public PrimitiveFastArrayStorageEngine create(final Object node, final ValidationContext ctx) throws ValidationException
        {
            return new PrimitiveFastArrayStorageEngine(node, ctx);
        }
    }
}
