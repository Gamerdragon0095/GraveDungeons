package net.gamerdragon0095.gravedungeons.data.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.Calendar;

public class CompoundTagPos {
    public static BlockPos getOrReturnPos(CompoundTag compoundTagPos, BlockPos defaultPos) {
        BlockPos output = defaultPos;
        
        float defaultX = defaultPos.getX();
        float defaultY = defaultPos.getY();
        float defaultZ = defaultPos.getZ();
        
        if (!compoundTagPos.isEmpty()) {
            if (compoundTagPos.contains("X") && compoundTagPos.contains("Y") && compoundTagPos.contains("Z")) {
                float x = compoundTagPos.getFloatOr("X", defaultX);
                float y = compoundTagPos.getFloatOr("Y", defaultY);
                float z = compoundTagPos.getFloatOr("Z", defaultZ);
                
                output = BlockPos.containing(x, y, z);
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:CompoundTagPos.getOrReturn] Error: The provided 'compoundTagPos' does not contain a proper X Y and Z values, please insure the provided 'compoundTagPos' is a CompoundTag containing a X Y and Z position before calling this function.");
            }
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:CompoundTagPos.getOrReturn] Error: The provided 'compoundTagPos' is empty, please insure the provided 'compoundTagPos' is a CompoundTag containing a X Y and Z position before calling this function.");
        }
        
        return output;
    }
}
