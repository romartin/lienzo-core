package com.ait.lienzo.client.core.shape.wires.layouts.prova;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.impl.Grid;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.tooling.common.api.java.util.function.Consumer;

public class ProvaLayouts {

    @SuppressWarnings("all")
    public static void main(String[] args) {

        LayoutFactory factory = null;
        CardinalLayoutContainer container = null;
        IPrimitive<?> primitive1 = null;
        final IPrimitive<?> primitive2 = null;

        container.layout(new BoundingBox());

        container.layout(12d,
                         12.4d);

        container
                .add(primitive1,
                     new Consumer<CardinalLayoutEntry>() {
                         @Override
                         public void accept(CardinalLayoutEntry entry) {
                             entry.at(com.ait.lienzo.client.core.shape.wires.layouts.impl.CardinalLayoutContainer.Cardinal.NORTH);
                         }
                     })
                .append(factory
                                .position()
                                .add(primitive1,
                                     new Consumer<StaticLayoutEntry>() {
                                         @Override
                                         public void accept(StaticLayoutEntry entry) {
                                             entry.at(com.ait.lienzo.client.core.shape.wires.layouts.impl.CardinalLayoutContainer.Cardinal.CENTER);
                                         }
                                     }),
                        new Consumer<CardinalLayoutEntry>() {
                            @Override
                            public void accept(CardinalLayoutEntry entry) {
                                entry.at(com.ait.lienzo.client.core.shape.wires.layouts.impl.CardinalLayoutContainer.Cardinal.CENTER);
                            }
                        });

        container
                .consume(primitive1,
                         new Consumer<CardinalLayoutEntry>() {
                             @Override
                             public void accept(CardinalLayoutEntry entry) {
                                 entry.at(com.ait.lienzo.client.core.shape.wires.layouts.impl.CardinalLayoutContainer.Cardinal.WEST);
                             }
                         });

        factory
                .directional()
                .direction(DirectionalLayoutContainer.Direction.HORIZONTAL)
                .add(primitive1,
                     new Consumer<DirectionalLayoutEntry>() {
                         @Override
                         public void accept(DirectionalLayoutEntry entry) {
                             entry.index(5);
                         }
                     });

        factory
                .grid()
                .grid(new Grid(5,
                               5))
                .add(primitive1,
                     new Consumer<GridLayoutEntry>() {
                         @Override
                         public void accept(GridLayoutEntry entry) {
                             entry
                                     .row(5)
                                     .column(1);
                         }
                     });
    }
}
