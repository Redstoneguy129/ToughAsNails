/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.temperature;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.stat.capability.IThirst;

public class TemperatureHelper 
{
    private static Map<String, ITemperatureModifier> modifiers = Maps.newHashMap();

    public static boolean registerTemperatureModifier(ITemperatureModifier modifier)
    {
        if (modifiers.containsKey(modifier.getId()))
        {
            return false;
        }

        modifiers.put(modifier.getId(), modifier);
        return true;
    }

    public static ImmutableMap<String, ITemperatureModifier> getTemperatureModifiers()
    {
        return ImmutableMap.copyOf(modifiers);
    }

    public static ITemperature getTemperatureData(EntityPlayer player)
    {
        return player.getCapability(TANCapabilities.TEMPERATURE, null);
    }
    
    public static List<ITemperatureRegulator> getTemperatureRegulators(World world)
    {
        List<ITemperatureRegulator> list = Lists.newArrayList();
        
        for (TileEntity tileEntity : world.tickableTileEntities)
        {
            if (tileEntity instanceof ITemperatureRegulator) list.add((ITemperatureRegulator)tileEntity);
        }
        
        return list;
    }
    
    public static boolean isPosClimatisedForTemp(World world, BlockPos pos, Temperature temperature)
    {
        for (ITemperatureRegulator regulator : getTemperatureRegulators(world))
        {
            if (regulator.getRegulatedTemperature().getRawValue() >= temperature.getRawValue() && regulator.isPosRegulated(pos))
            {
                return true;
            }
        }
        
        return false;
    }
}
