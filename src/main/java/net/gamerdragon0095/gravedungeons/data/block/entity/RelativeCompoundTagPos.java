package net.gamerdragon0095.gravedungeons.data.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Calendar;

public class RelativeCompoundTagPos {
    public static BlockPos getOrReturnPos(CompoundTag relativeCompoundTagPos, BlockPos defaultPos, BlockPos basePos) {
        BlockPos output = defaultPos;
        
        float defaultX = defaultPos.getX();
        float defaultY = defaultPos.getY();
        float defaultZ = defaultPos.getZ();

        float baseX = basePos.getX();
        float baseY = basePos.getY();
        float baseZ = basePos.getZ();
        
        if (!relativeCompoundTagPos.isEmpty()) {
            if (relativeCompoundTagPos.contains("X") && relativeCompoundTagPos.contains("Y") && relativeCompoundTagPos.contains("Z")) {
                float x = relativeCompoundTagPos.getFloatOr("X", defaultX);
                float y = relativeCompoundTagPos.getFloatOr("Y", defaultY);
                float z = relativeCompoundTagPos.getFloatOr("Z", defaultZ);
                
                output = BlockPos.containing(baseX + x, baseY + y, baseZ + z);
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:CompoundTagPos.getOrReturn] Error: The provided 'compoundTagPos' does not contain a proper X Y and Z values, please insure the provided 'compoundTagPos' is a CompoundTag containing a X Y and Z position before calling this function.");
            }
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:CompoundTagPos.getOrReturn] Error: The provided 'compoundTagPos' is empty, please insure the provided 'compoundTagPos' is a CompoundTag containing a X Y and Z position before calling this function.");
        }
        
        return output;
    }

    public static CompoundTag createTag(BlockPos basePos, BlockPos inputPos) {
        CompoundTag output = new CompoundTag();

        float baseX = basePos.getX();
        float baseY = basePos.getY();
        float baseZ = basePos.getZ();

        //System.out.println("Test basePos: " + baseX + ", " + baseY + ", " + baseZ);

        float inputX = inputPos.getX();
        float inputY = inputPos.getY();
        float inputZ = inputPos.getZ();

        //System.out.println("Test input: " + inputX + ", " + inputY + ", " + inputZ);

        float directionX = ((baseX - inputX) == 0) ? 0.0f : ((baseX - inputX) / (int) Math.abs(baseX - inputX));
        float directionY = ((baseY - inputY) == 0) ? 0.0f : ((baseY - inputY) / (int) Math.abs(baseY - inputY));
        float directionZ = ((baseZ - inputZ) == 0) ? 0.0f : ((baseZ - inputZ) / (int) Math.abs(baseZ - inputZ));

        //System.out.println("Test direction: " + directionX + ", " + directionY + ", " + directionZ);

        //float offsetX = (directionX == 0) ? 0.0f : (inputX - ((-1) / directionX));
        //float offsetY = (directionY == 0) ? 0.0f : (inputY - ((-1) / directionY));
        //float offsetZ = (directionZ == 0) ? 0.0f : (inputZ - ((-1) / directionZ));

        //System.out.println("Test offset: " + offsetX + ", " + offsetY + ", " + offsetZ);

        float X = directionX * Math.abs(baseX - inputX);
        float Y = directionY * Math.abs(baseY - inputY);
        float Z = directionZ * Math.abs(baseZ - inputZ);

        //System.out.println("Test output: " + X * (-1) + ", " + Y * (-1) + ", " + Z * (-1));

        output.putFloat("X", X * (-1));
        output.putFloat("Y", Y * (-1));
        output.putFloat("Z", Z * (-1));

        return output;
    }
}
