// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.paradice.trees;

import org.terasology.coreworlds.generator.rasterizers.TreeRasterizer;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.world.block.BlockManager;

public class PalmTreeRasterizer extends TreeRasterizer {

    private BlockManager blockManager;

    @Override
    public void initialize() {
        super.initialize();
        blockManager = CoreRegistry.get(BlockManager.class);
        //TODO: Remove these lines when lazy block registration is fixed
        //Currently they are required to ensure that the blocks are all registered before worldgen
        blockManager.getBlock("PlantPack:AcaiPalmLeaf");
        blockManager.getBlock("PlantPack:AcaiPalmTrunk");
    }
}
