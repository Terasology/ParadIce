// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.paradice.trees;

import org.terasology.coreworlds.generator.trees.AbstractTreeGenerator;
import org.terasology.engine.utilities.random.Random;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.chunks.CoreChunk;

public class PalmTreeGenerator extends AbstractTreeGenerator {

    public int smallestHeight;
    public int tallestHeight;
    public String trunkType;
    public String leafType;

    public PalmTreeGenerator() {
        this(8, 12);
    }

    public PalmTreeGenerator(int minHeight, int maxHeight) {
        if (minHeight > maxHeight) minHeight = maxHeight;
        smallestHeight = minHeight;
        tallestHeight = maxHeight;
    }

    public PalmTreeGenerator setTrunkType(String trunk) {
        trunkType = trunk;
        return this;
    }

    public PalmTreeGenerator setLeafType(String leaf) {
        leafType = leaf;
        return this;
    }

    @Override
    public void generate(BlockManager blockManager, CoreChunk view, Random rand, int posX, int posY, int posZ) {
        int height = rand.nextInt(smallestHeight, tallestHeight + 1);
        Block trunk = blockManager.getBlock(trunkType);
        Block leafNorth = blockManager.getBlock(leafType + ".FRONT");
        Block leafSouth = blockManager.getBlock(leafType + ".BACK");
        Block leafEast = blockManager.getBlock(leafType + ".LEFT");
        Block leafWest = blockManager.getBlock(leafType + ".RIGHT");
        if (leafNorth == null || leafSouth == null || leafEast == null || leafWest == null) {
            leafNorth = leafSouth = leafEast = leafWest = blockManager.getBlock(leafType);
        }
        for (int q = 0; q <= height; q++) {
            safelySetBlock(view, posX, posY + q, posZ, trunk);
        }
        safelySetBlock(view, posX, posY + height, posZ - 1, leafNorth);
        safelySetBlock(view, posX, posY + height, posZ - 2, leafNorth);
        safelySetBlock(view, posX, posY + height, posZ + 1, leafSouth);
        safelySetBlock(view, posX, posY + height, posZ + 2, leafSouth);
        safelySetBlock(view, posX - 1, posY + height, posZ, leafEast);
        safelySetBlock(view, posX - 2, posY + height, posZ, leafEast);
        safelySetBlock(view, posX + 1, posY + height, posZ, leafWest);
        safelySetBlock(view, posX + 2, posY + height, posZ, leafWest);
        if (rand.nextBoolean()) {
            safelySetBlock(view, posX, posY + height - 1, posZ - 1, leafNorth);
            safelySetBlock(view, posX, posY + height - 1, posZ + 1, leafSouth);
            safelySetBlock(view, posX - 1, posY + height - 1, posZ, leafEast);
            safelySetBlock(view, posX + 1, posY + height - 1, posZ, leafWest);
        }
        if (rand.nextBoolean()) {
            safelySetBlock(view, posX, posY + height, posZ - 3, leafNorth);
            safelySetBlock(view, posX, posY + height, posZ + 3, leafSouth);
            safelySetBlock(view, posX - 3, posY + height, posZ, leafEast);
            safelySetBlock(view, posX + 3, posY + height, posZ, leafWest);
        }
    }
}
